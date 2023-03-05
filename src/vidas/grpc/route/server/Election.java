package vidas.grpc.route.server;

import java.util.*;

import com.google.protobuf.ByteString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Election extends Thread {
	protected static Logger logger = LoggerFactory.getLogger("worker");
	private boolean forever = true;

	public boolean resetTimer = false;

	public Election() {

	}

	public void shutdown() {
		logger.info("shutting down worker " + this.getId());

		forever = true;
	}

	private void startNominations() {
		Engine engine = Engine.getInstance();

		if (engine.serverRole == "follower") // follower
		{

			engine.serverRole = "candidate"; // becomes candidate role
			engine.serverTerm++; // increment term by 1

			System.out
					.println(
							" ** electionTimer is over | elections are off | promoted to candidate | term gets incremented to "
									+ engine.serverTerm);

			// sending out nominate vote request to all other followers
			sendNominateVoteRequests();

		} else if (engine.serverRole == "candidate") {
			sendNominateVoteRequests();
		}
	}

	private void sendNominateVoteRequests() {
		Engine engine = Engine.getInstance();
		for (Link l : engine.links) {
			long referenceID = engine.getNextMessageID();
			String path = "/nominate/" + engine.getServerName();

			Heartbeat.sendHeartbeat(l.getPort(), referenceID, l.getServerID(), engine.serverID, path,
					ByteString.copyFromUtf8("heartbeat"));

			System.out.println(" ** sending heartbeat " + " to " + l.getServerID() + " | refID: " + referenceID
					+ " | path: " + path + " |");
		}
	}

	@Override
	public void run() {
		// TODO not a good idea to spin on work --> wastes CPU cycles
		while (forever) {
			try {
				if (logger.isDebugEnabled())
					logger.debug("run() work qsize = " + Engine.getInstance().workQueue.size());

				// System.out.println("woiuld this print forever");

				// System.out.println("else is printing");
				// Random random = new Random();
				// Thread.sleep(random.nextInt(20000 - 2000 + 1) + 2000);

				Thread.sleep(10000);
				if (resetTimer == false) {
					Engine.getInstance().serverTerm++; //timer ended so incrementing server term
					// System.out.println("test");
					var w = Engine.getInstance().workQueue.poll();
					doElection(w);
					// if(w == null) startNominations();
				}
				else { resetTimer = true;}

			} catch (Exception e) {
				logger.error("worker failure", e);
			}
		}

		// TODO any shutdown steps
	}

	private void doElection(Work w) {

		if (w == null)
			return;

		Engine engine = Engine.getInstance();

		if (engine.getServerRole() == "follower") {
			if (w.request.getPath().contains("/election")) // follower is elected
			{
				// TODO: take care of if its for the correct server

				// become candidate
				engine.serverRole = "candidate";
				engine.serverTerm++; // increment term by 1

				System.out
						.println(
								" ** electionTimer is over | elections are off | promoted to candidate | term gets incremented to "
										+ engine.serverTerm);

				// sending out nominate vote request to all other followers
				for (Link l : engine.links) {
					long referenceID = engine.getNextMessageID();
					String path = "/nominate/" + engine.serverTerm + "/" + engine.getServerPort();
					Heartbeat.sendHeartbeat(l.getPort(), referenceID, l.getServerID(), engine.serverID, path,
							ByteString.copyFromUtf8("term: " + engine.serverTerm));
					System.out.println(
							" ** sending nominate vote request to " + l.getServerID() + " | refID: " + referenceID
									+ " | path: " + path + " |");
					//
				}

			}
			if (w.request.getPath().contains("/nominate")) // follower receives a vote request
			{
				long requestServerTerm = Long.parseLong(w.request.getPath().split("/")[2]);
				System.out.println(
						"reaches follower /nominate | comparing " + requestServerTerm + " == " + engine.serverTerm);
				if (requestServerTerm == engine.serverTerm) // if request term matches this server term
				{
					
					// vote /accept if it has not voted on current term
					long referenceID = engine.getNextMessageID();
					String path = w.request.getPath() + "/accept";
					int serverPort = Integer.parseInt(w.request.getPath().split("/")[3]);
					Heartbeat.sendHeartbeat(serverPort, referenceID, w.request.getOrigin(), engine.serverID, path,
							ByteString.copyFromUtf8("term: " + engine.serverTerm));
					System.out.println(" ** sending /accept to " + w.request.getOrigin() + " | refID: " + referenceID
							+ " | path: " + path + " |");

				} else {// then /reject if it has voted on current term already

				}
			}
		} else if (engine.getServerRole() == "candidate") {
			if (w.request.getPath().contains("/nominate")) // candidated received response of vote request, either
															// /accept or /reject
			{
				long requestServerTerm = Long.parseLong(w.request.getPath().split("/")[2]);
				System.out.println(
						"reaches candidate /nominate | comparing " + requestServerTerm + " == " + engine.serverTerm);
				if (requestServerTerm == engine.serverTerm) // if request term matches this server term
				{
					String voteRequestResponse = w.request.getPath().split("/")[4]; // accept or reject
					if (voteRequestResponse.contains("accept")) {
						System.out.println("I reach accept");
					} else // reject
					{

					}
				}
			}
		}

		// // System.out.println(" *** work is not null *** ");

		// if (w.request.getDestination() == engine.serverID) {
		// try {

		// logger.info(
		// "**** do work " + w + " ****");

		// //needs to increment its term

		// //then vote /accept or /reject

		// // simulates latency
		// Thread.sleep(2000);

		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } else {

		// // logger.info("Worker | no direct destination exists | server.id =" +
		// // Engine.getInstance().serverID + " | server destination = " +
		// // w.request.getDestination());

		// // forward the message to Link instances
		// for (var link : Engine.getInstance().links) {
		// if (link.getServerID() == w.request.getDestination()) {
		// logger.info("TODO: send message");

		// }

		// String path = "unsure"; // should be /accept or /reject

		// Heartbeat.sendHeartbeat(link.getPort(), w.request.getId(),
		// w.request.getDestination(), w.request.getOrigin(), path,
		// w.request.getPayload());

		// }

		// // if no direct destination exists, forward to all
		// // links or the next link?
		// }

	}

}
