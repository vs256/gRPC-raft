package vidas.grpc.route.util;


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

import route.RouteServiceGrpc.RouteServiceImplBase;
import vidas.grpc.route.client.UploadClient;
import vidas.grpc.route.server.Engine;
import route.FileUploadRequest;
import route.FileUploadResponse;
import route.RouteServiceGrpc;
import route.MetaData;
import route.Route;
import route.FileContent;
import route.FileStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

public class FileStorage {



    protected static Logger logger = LoggerFactory.getLogger("storage");

	private static final Route constructMessage(long msgID, long toID, long origin, String path, ByteString payload) {
		Route.Builder bld = Route.newBuilder();
		bld.setId(msgID);
		bld.setDestination(toID);
		bld.setOrigin(origin);
		bld.setPath(path);

		// byte[] hello = payload.getBytes();
		// bld.setPayload(ByteString.copyFrom(hello));
		bld.setPayload(payload);

		return bld.build();
	}

	public static void sendNonBlockingRequest(int serverPort, long referenceID, long destinationID, long origin,
			String path,
			ByteString payload, String header) {

		ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", serverPort).usePlaintext().build();
		RouteServiceGrpc.RouteServiceStub stub = RouteServiceGrpc.newStub(ch);

        uploadRequest(stub, payload, header);
	}



    
    public static void uploadRequest(RouteServiceGrpc.RouteServiceStub asyncStub, ByteString payload, String header) {
        logger.info("** Server make upload request **");

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
        uploadContents(requestObserver, payload, header);
    }

    public static void uploadContents(StreamObserver<FileUploadRequest> requestObserver,  ByteString payload, String header) {
        long t1 = System.currentTimeMillis();
        final CountDownLatch finishLatch = new CountDownLatch(1);
        logger.info("-------- Server send  chunks -------");

        // build metadata
        FileUploadRequest metadata = FileUploadRequest.newBuilder()
        .setMetadata(MetaData.newBuilder()
                .setHeader(header)
                .build())
        .build();
        requestObserver.onNext(metadata);
        logger.info("** Server send mentadata **");

        FileUploadRequest uploadRequest = FileUploadRequest.newBuilder()
        .setFile(FileContent.newBuilder()
                .setContent(payload)
                .build())
        .build();
    requestObserver.onNext(uploadRequest);  

        requestObserver.onCompleted();

        long t2 = System.currentTimeMillis();
        System.out.println("File sent | Time: " + (t2 - t1) + "ms");

        // Receiving happens asynchronously
        try {
            finishLatch.await(15, TimeUnit.MINUTES);
            logger.info("*** Server asynchronously timer out ***");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    
    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        FileStorage.logger = logger;
    }


}
