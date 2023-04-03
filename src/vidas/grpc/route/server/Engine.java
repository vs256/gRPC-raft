package vidas.grpc.route.server;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

import route.RouteServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import vidas.grpc.route.server.DebugTools.DebugHelper;
import vidas.grpc.route.server.StateMachine.ServerStateMachine;
import vidas.grpc.route.server.Types.Link;
import vidas.grpc.route.server.Types.Work;

/**
 * Core components to process work; shared with/across all sessions.
 * 
 * @author vidas
 *
 */
public class Engine {

	protected static Logger logger = LoggerFactory.getLogger("server");
	private static Engine instance;
	private static Properties conf;

	public String serverName;
	public Long serverID;
	public Integer serverPort;
	private Long nextMessageID;

	public Long serverTerm;

	/* workQueue/containers */
	protected LinkedBlockingDeque<Work> workQueue, mgmtQueue;

	/* worker threads */
	protected ArrayList<Worker> workers;

	public Election election;

	public DebugHelper debugHelper;

	/* connectivity */
	public ArrayList<Link> links;
	
	protected ArrayList<RouteServiceGrpc.RouteServiceStub> asyncStubs;



	public ServerStateMachine serverStateMachine;

	public static Properties getConf() {
		return conf;
	}

	public static void configure(Properties conf) {
		if (Engine.conf == null) {
			Engine.conf = conf;
			instance = new Engine();
			instance.init();
		}
	}

	public static Engine getInstance() {
		if (instance == null)
			throw new RuntimeException("Engine not initialized");

		return instance;
	}

	private Engine() {
	}

	private synchronized void init() {
		if (conf == null) {
			Engine.logger.error("server is not configured!");
			throw new RuntimeException("server not configured!");
		}

		// if (manager != null) {
		// Engine.logger.error("trying to re-init() logistics!");
		// return;
		// }

		// extract settings. Here we are using basic properties which, requires
		// type checking and should also include range checking as well.

		String tmp = conf.getProperty("server.id");
		if (tmp == null)
			throw new RuntimeException("missing server ID");
		serverID = Long.parseLong(tmp);

		tmp = conf.getProperty("server.port");
		if (tmp == null)
			throw new RuntimeException("missing server port");
		serverPort = Integer.parseInt(tmp);
		if (serverPort <= 1024)
			throw new RuntimeException("server port must be above 1024");

		// monotonically increasing number
		nextMessageID = 0L;

		// our list of connections to other servers
		links = new ArrayList<Link>();
		// get the server connect link and add it to the arrayList
		String[] serverConnectionsPorts = conf.getProperty("server.connections.ports").split(",");
		String[] serverConnectionsNames = conf.getProperty("server.connections.names").split(",");
		String[] serverConnectionsIDs = conf.getProperty("server.connections.ids").split(",");
		for (int i = 0; i < serverConnectionsPorts.length; i++) {
			String serverConnectName = serverConnectionsNames[i];
			Integer serverConnectID = Integer.parseInt(serverConnectionsIDs[i]);
			String serverConnectIP = "127.0.0.1";
			Integer serverConnectPort = Integer.parseInt(serverConnectionsPorts[i]);
			Link link = new Link(serverConnectName, serverConnectID, serverConnectIP, serverConnectPort);
			links.add(link);
			logger.info("Adding server link | name: " + link.getServerName() + " | id: " + link.getServerID());

		}
		// links

		serverName = conf.getProperty("server.name"); // set server name

		Engine.logger.info("Starting Queues");
		workQueue = new LinkedBlockingDeque<Work>();
		//mgmtQueue = new LinkedBlockingDeque<Work>();

		Engine.logger.info("Starting Workers");
		workers = new ArrayList<Worker>();
		var w = new Worker();
		workers.add(w);
		w.start();


		// Engine.logger.info("Starting Election");
		// election = new Election();
		// election.start();

		serverStateMachine = new ServerStateMachine(); // server state machine
		serverTerm = 0L;

		// DebugHelper
		debugHelper = new DebugHelper(this);
		//


		// create an asyncStub for Client
		Engine.logger.info("Starting asyncStub client connection");
		int clientPort = Integer.parseInt(conf.getProperty("server.client.port"));
		asyncStubs = new ArrayList<RouteServiceGrpc.RouteServiceStub>();
		ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", clientPort).usePlaintext()
				.build();
		asyncStubs.add(RouteServiceGrpc.newStub(ch));
		

		Engine.logger.info("initializaton complete");
	}

	public synchronized void shutdown(boolean hard) {
		Engine.logger.info("server shutting down.");

		if (!hard && workQueue.size() > 0) {
			try {
				while (workQueue.size() > 0) {
					Thread.sleep(2000);
				}
			} catch (InterruptedException e) {
				Engine.logger.error("Waiting for work queue to empty interrupted, shutdown hard", e);
			}
		}
		for (var w : workers) {
			w.shutdown();
		}

		// manager.shutdown();
	}

	public synchronized void increaseWorkers() {
		var w = new Worker();
		workers.add(0, w);
		w.start();
	}

	public synchronized void decreaseWorkers() {
		if (workers.size() > 0) {
			var w = workers.remove(0);
			w.shutdown();
		}
	}

	public String getServerName() {
		return serverName;
	}

	public Long getServerID() {
		return serverID;
	}

	public synchronized Long getNextMessageID() {
		// TODO this should be a hash value (but we want to retain the implicit
		// ordering effect of an increasing number)
		if (nextMessageID == Long.MAX_VALUE)
			nextMessageID = 0l;

		return ++nextMessageID;
	}

	public Integer getServerPort() {
		return serverPort;
	}
}
