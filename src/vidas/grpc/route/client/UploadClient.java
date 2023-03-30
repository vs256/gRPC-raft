package vidas.grpc.route.client;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.FileReader;
import java.io.BufferedReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// import com.opencsv.exceptions.CsvValidationException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import java.util.Properties;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import fileStream.FileServiceGrpc.FileServiceImplBase;
import fileStream.FileUploadRequest;
import fileStream.FileUploadResponse;
import fileStream.FileServiceGrpc;
import fileStream.MetaData;
import fileStream.FileContent;
import fileStream.FileStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

public class UploadClient {

    private static final String filepath = 
    "data-storage/raw-original-files/nyc-parking/parkingviolations-2014/parkingviolations/Parking_Violations_Issued_-_Fiscal_Year_2014.csv";
    // private static final String filepath =
    // "data-storage/raw-original-files/nyc-parking/parkingviolations-2014/parkingviolations/EXAMPLE_SMALL_SUBSET.csv";

    private static final int MAX_LINES = 5000;
    private static Long destID;
    private static Long clientID;
    private static int clientPort;
    private static int linkedPort;
    // private final RouteServiceGrpc.RouteServiceBlockingStub stub;
    private final FileServiceGrpc.FileServiceStub asyncStub;

    protected static Properties conf;
    protected static Logger logger = LoggerFactory.getLogger("client");

    public UploadClient(ManagedChannel ch) {
        // stub = RouteServiceGrpc.newBlockingStub(ch);
        asyncStub = FileServiceGrpc.newStub(ch);
    }

    public static void configure(Properties conf) {
        if (conf == null)
            throw new RuntimeException("client not configured!");

        // extract settings. Here we are using basic properties which, requires
        // type checking and should also include range checking as well.

        String tmp = conf.getProperty("client.id");
        if (tmp == null)
            throw new RuntimeException("missing client ID");
        clientID = Long.parseLong(tmp);

        tmp = conf.getProperty("client.port");
        if (tmp == null)
            throw new RuntimeException("missing client port");
        clientPort = Integer.parseInt(tmp);
        if (clientPort <= 1024)
            throw new RuntimeException("client port must be above 1024");

        tmp = conf.getProperty("client.destID");
        if (tmp == null)
            throw new RuntimeException("missing destination ID");
        destID = Long.parseLong(tmp);

        tmp = conf.getProperty("server.connects.port");
        if (tmp == null)
            throw new RuntimeException("missing linked port");
        linkedPort = Integer.parseInt(tmp);
        if (linkedPort <= 1024)
            throw new RuntimeException("linked port must be above 1024");

    }

    /**
     * Configuration of the server's identity, port, and role
     */
    private static Properties getConfiguration(final File path) throws IOException {
        if (!path.exists())
            throw new IOException("missing file");

        Properties rtn = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            rtn.load(fis);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return rtn;
    }

    public void uploadRequest() {
        logger.info("** Client make upload request **");

        StreamObserver<FileUploadRequest> requestObserver = asyncStub.upload(new StreamObserver<FileUploadResponse>() {
            @Override
            public void onNext(FileUploadResponse fileUploadResponse) {
                System.out.println(
                        "File upload status :: " + fileUploadResponse.getStatus());
            }

            @Override
            public void onError(Throwable t) {
                logger.info("Request Failed: {}", Status.fromThrowable(t));
                // finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                logger.info("Finished Request");
                // finishLatch.countDown();
            }
        });

        // upload contents
        uploadContents(requestObserver);
    }

    public void uploadContents(StreamObserver<FileUploadRequest> requestObserver) {
        long t1 = System.currentTimeMillis();
        final CountDownLatch finishLatch = new CountDownLatch(1);
        // upload file as chunks of specific number of lines
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            // extract header(the first line)
            String header = reader.readLine();
            String[] h = header.split(",");
            System.out.println("Header: " + header + " (size: " + h.length + ")");

            // build metadata
            FileUploadRequest metadata = FileUploadRequest.newBuilder()
                    .setMetadata(MetaData.newBuilder()
                            .setHeader(header)
                            .build())
                    .build();
            requestObserver.onNext(metadata);
            logger.info("** Client send mentadata **");

            List<Byte> chunkArray = new ArrayList<Byte>();
            String line = null;
            int line_cnt = 0;
            int chunk_cnt = 0;

            while ((line = reader.readLine()) != null) {
                byte[] lineArray = (line + "\n").getBytes();
                for (byte b : lineArray) {
                    chunkArray.add(b);
                }
                line_cnt++;

                // send this chunk of data after reading MAX_LINES lines
                if (line_cnt == MAX_LINES) {
                    byte[] byteArray = new byte[chunkArray.size()];
                    for (int i = 0; i < chunkArray.size(); i++) {
                        byteArray[i] = chunkArray.get(i);
                    }
                    FileUploadRequest uploadRequest = FileUploadRequest.newBuilder()
                            .setFile(FileContent.newBuilder()
                                    .setContent(ByteString.copyFrom(byteArray, 0, chunkArray.size()))
                                    .build())
                            .build();
                    requestObserver.onNext(uploadRequest);
                    logger.info("** Send No. " + (chunk_cnt + 1) + " chunk **");

                    chunkArray.clear();
                    line_cnt = 0;
                    chunk_cnt++;
                }
            }

            // send the last chunk of data which has less than MAX_LINES lines
            if (line_cnt > 0) {
                logger.info("** Last Line No. " + line_cnt + " **");
                byte[] byteArray = new byte[chunkArray.size()];
                for (int i = 0; i < chunkArray.size(); i++) {
                    byteArray[i] = chunkArray.get(i);
                }
                FileUploadRequest uploadRequest = FileUploadRequest.newBuilder()
                        .setFile(FileContent.newBuilder()
                                .setContent(ByteString.copyFrom(byteArray, 0, chunkArray.size()))
                                .build())
                        .build();
                requestObserver.onNext(uploadRequest);
                chunk_cnt++;
            }

            logger.info("-------- Client send " + (chunk_cnt + 1) + " chunks -------");
            requestObserver.onCompleted();

            long t2 = System.currentTimeMillis();
            System.out.println("File sent | Time: " + (t2 - t1) + "ms");

            // Receiving happens asynchronously
            try {
                finishLatch.await(15, TimeUnit.MINUTES);
                logger.info("*** Client asynchronously timer out ***");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String filePath = args[0];
        try {
            Properties conf = UploadClient.getConfiguration(new File(filePath));
            UploadClient.configure(conf);
            logger.info("------- starting client -------");
        } catch (IOException error) {
            // TODO better error message
            error.printStackTrace();
        }

        // create the client stub using the newly created channel
        ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", UploadClient.linkedPort).usePlaintext()
                .build();

        UploadClient client = new UploadClient(ch);
        client.uploadRequest();
    }
}
