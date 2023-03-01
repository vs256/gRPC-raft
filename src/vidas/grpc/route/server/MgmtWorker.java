package vidas.grpc.route.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

public class MgmtWorker extends Thread {
	protected static Logger logger = LoggerFactory.getLogger("manager");
	private boolean forever = true;

	private CountDownLatch electionTimerLatch;
	private int electionTimer = 5;

	LinkedBlockingDeque<Boolean> electionTimerQueue;

	// paths that are processed by this worker
	private static String[] paths = { "/manage/", "/election/" };

	public static boolean isPriority(route.Route request) {
		var rtn = false;

		if (request != null) {
			// TODO how do you speed this testing up?

			System.out.println("\n" + request.getPath());
			// for (String t : paths) {
			// 	if (request.getPath().toLowerCase().startsWith(t)) {
				if(request.getPath().contains("election")){
					rtn = true;
					//break;
				}
			}
		

		return rtn;
	}

	public MgmtWorker() {
		electionTimerLatch = new CountDownLatch(1);
		electionTimerQueue = new LinkedBlockingDeque<Boolean>();
		electionTimerQueue.add(true);
		try {
			electionTimerQueue.poll(electionTimer, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void shutdown() {
		logger.info("shutting down manager");
		forever = true;
	}

	private void startElections() {
		Engine engine = Engine.getInstance();
		if (engine.serverRole == "candidate") {

		} else if (engine.serverRole == "follower") // follower
		{

			engine.serverRole = "candidate"; // becomes candidate role
			engine.serverTerm++; // increment term by 1

			System.out
					.println(
							" ** electionTimer is over | elections are off | promoted to candidate | term gets incremented to "
									+ engine.serverTerm);

			

			// sending out nominate vote request to all other followers
			for (Link l : engine.links) {
				long referenceID = engine.getNextMessageID();
				String path = "/nominate/" + engine.getServerName();

				Heartbeat.sendHeartbeat(l.getPort(), referenceID, l.getServerID(), engine.serverID, path,
						ByteString.copyFromUtf8("heartbeat"));

				System.out.println(" ** sending heartbeat " + " to " + l.getServerID() + " | refID: " + referenceID
						+ " | path: " + path + " |");
			}

		}
	}

	private void doWork(Work w) {

		if (w != null) {
			if (w.request.getDestination() == Engine.getInstance().serverID) {
				try {

					logger.info("*** election candidate is being run here ** manager is working() " + w + " ***");
					startElections();
					//electionTimerQueue.add(true);
					// simulates latency
					Thread.sleep(electionTimer);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

				// logger.info("Manager | no direct destination exists | server.id =" +
				// Engine.getInstance().serverID+ " | server destination = " +
				// w.request.getDestination());

				// logger.info(
				// "Forwarding message to link instances | request destination: " +
				// w.request.getDestination());
				// forward the message to Link instances
				for (var link : Engine.getInstance().links) {
					if (link.getServerID() == w.request.getDestination()) {
						logger.info("TODO: send message");
						// RouteClient.sendMessage?

					}

					// ForwardMessage.forwardMessage(link.getPort(), (int) w.request.getId(),
					// (int) w.request.getDestination(), (int) w.request.getOrigin(),
					// w.request.getPayload() );

				}

				// if no direct destination exists, forward to all
				// links or the next link?
			}
		}
	}

	public boolean electionWaitCondition()
	{
		
		if(electionTimerQueue.isEmpty()) { return true; }

		try
        {
            return electionTimerQueue.poll(electionTimer, TimeUnit.SECONDS);
        }
        catch (final InterruptedException e)
        {
            System.out.println("Someone has disturbed the condition awaiter.");
            return false;
        }
	}

	@Override
	public void run() {
		// TODO not a good idea to spin on work --> wastes CPU cycles
		while (forever) {
			try {
				if (logger.isDebugEnabled())
					logger.debug("run() mgmt qsize = " + Engine.getInstance().mgmtQueue.size());

					//startElections();
					
					// if(electionWaitCondition() == false)
					// {
					// 	System.out.println("starting elections");
					// 	startElections();
					// }
					// else {
					// var w = Engine.getInstance().mgmtQueue.poll(electionTimer, TimeUnit.SECONDS);
					
					//System.out.println("never reaches");
					//if (w != null)
						// doWork(w);

					// }

			} catch (Exception e) {
				logger.error("manager failure", e);
			}
		}
	}

	public boolean waitForElectionTimerToBeOver() {
		try {
			return electionTimerLatch.await(electionTimer, TimeUnit.SECONDS);
		} catch (final InterruptedException e) {
			System.out.println("Someone has disturbed the condition awaiter.");
			return false;
		}

	}

	public void setElectionTimerOver() {
		electionTimerLatch.countDown();
	}
}
