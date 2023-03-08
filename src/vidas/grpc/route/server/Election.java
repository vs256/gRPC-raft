package vidas.grpc.route.server;

import java.util.*;

import com.google.protobuf.ByteString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Election extends Thread {
	protected static Logger logger = LoggerFactory.getLogger("election");
	private boolean forever = true;

	private TimerTask electionTask;

	public Election() {
		long leftLimit = 0L;
		long rightLimit = 10L;
		long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
		electionTimerTask(generatedLong * 1000L);
	}

	public void electionTimerTask(long delay) {

		//
		if (electionTask != null) {
			electionTask.cancel();
			System.out.println("cancelling election timer task");
		}
		electionTask = new TimerTask() {

			int seconds = 15;
			int i = 0;

			@Override
			public void run() {

				i++;
				if (i % seconds == 0) {
					System.out.println("Timer action!");
					electionTimerTask(delay); // reset election timer for itself

					// System.out.println("task is running so happy");
					Engine engine = Engine.getInstance();
					if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Follower) {

						engine.serverStateMachine.state = engine.serverStateMachine.state.nextState(); // upgrade
																										// follower
																										// to candidate

						engine.serverTerm++; // increment term

						// DEBUG PRINT
						// System.out.println(" ** " + "Term: " + engine.serverTerm + " || State: "
						// + engine.serverStateMachine.state.toString() + " || votedFor: "
						// + engine.serverStateMachine.votedFor + " || nominationVotes: "
						// + engine.serverStateMachine.nominationVotes + " || Type:
						// electionTimerTask()|| Origin: "
						// + engine.getServerPort() + " || Destination: " + engine.getServerPort() + "
						// || Path: "
						// + "" + " || " + " || Reason: "
						// + " election timer over, upgrading to candidate & term incremented to "
						// + engine.serverTerm + " | ==> sending nominate requests " + " ** \n");
						String str = " ** " + "Term: " + engine.serverTerm + " || State: "
								+ engine.serverStateMachine.state.toString() + " || votedFor: "
								+ engine.serverStateMachine.votedFor + " || nominationVotes: "
								+ engine.serverStateMachine.nominationVotes + " || Type: electionTimerTask()|| Origin: "
								+ engine.getServerPort() + " || Destination: " + engine.getServerPort() + " || Path: "
								+ "" + " || " + " || Reason: "
								+ " election timer over, upgrading to candidate & term incremented to "
								+ engine.serverTerm + " | ==> sending nominate requests " + " ** \n";
						engine.gui.setLabel(str);
						// DEBUG PRINT

						engine.serverStateMachine.votedFor = "";
						engine.serverStateMachine.state.sendNominateRequest();

					} else if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Candidate) {
						// did not get back majority nominate requests, trying again as a candidate
						// after increment

						engine.serverTerm++;
						engine.serverStateMachine.votedFor = "";
						engine.serverStateMachine.state.sendNominateRequest(); // vote for me request
						// electionTimerTask(); // reset election timer after sending out requests
					} else if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Leader) {
						engine.serverStateMachine.state.sendLeaderHeartbeat();
						System.out.println("sending leader heartbeat " + engine.getNextMessageID());
					}

				} else {
					System.out.println("Time left:" + (seconds - (i % seconds)));
					Engine.getInstance().gui.setTimer(Integer.toString(seconds - (i % seconds)));
				}

			}
		};
		Timer timer = new Timer("Timer");
		// long delay = 20000L;
		timer.schedule(electionTask, delay, 1000);
		//
	}

	public void shutdown() {
		logger.info("shutting down worker " + this.getId());

		forever = true;
	}

	@Override
	public void run() {

		// TODO not a good idea to spin on work --> wastes CPU cycles
		while (forever) {
			try {
				if (logger.isDebugEnabled())
					logger.debug("run() work qsize = " + Engine.getInstance().workQueue.size());

				// Thread.sleep(10000);
				// if (Engine.getInstance().resetTimer == false) {
				// if (state == ServerState.Follower) {

				// state = state.nextState();
				// Engine.getInstance().serverTerm++;
				// System.out.println("* election timer over, upgrading to candidate & term
				// incremented to " + Engine.getInstance().serverTerm + " | ==> sending nominate
				// requests");
				// state.sendNominateRequest();
				// }
				// }
				// else {
				// System.out.println("reset timer triggered, resetting thread sleep");
				// Engine.getInstance().resetTimer = false;
				// }

				// if (resetTimer == false) {
				// Engine.getInstance().serverTerm++; // timer ended so incrementing server term
				// // System.out.println("test");
				// var w = Engine.getInstance().workQueue.poll();
				// doElection(w);
				// // if(w == null) startNominations();
				// } else {
				// resetTimer = false;
				// }

			} catch (Exception e) {
				logger.error("worker failure", e);
			}
		}

		// TODO any shutdown steps
	}

	// private void doElection(Work w) {

	// if (w == null)
	// return;

	// Engine engine = Engine.getInstance();

	// if (engine.getServerRole() == "follower") {
	// if (w.request.getPath().contains("/election")) // follower is elected
	// {
	// // TODO: take care of if its for the correct server

	// // become candidate
	// engine.serverRole = "candidate";
	// engine.serverTerm++; // increment term by 1

	// System.out
	// .println(
	// " ** electionTimer is over | elections are off | promoted to candidate | term
	// gets incremented to "
	// + engine.serverTerm);

	// // sending out nominate vote request to all other followers
	// for (Link l : engine.links) {
	// long referenceID = engine.getNextMessageID();
	// String path = "/nominate/" + engine.serverTerm + "/" +
	// engine.getServerPort();
	// Heartbeat.sendHeartbeat(l.getPort(), referenceID, l.getServerID(),
	// engine.serverID, path,
	// ByteString.copyFromUtf8("term: " + engine.serverTerm));
	// System.out.println(
	// " ** sending nominate vote request to " + l.getServerID() + " | refID: " +
	// referenceID
	// + " | path: " + path + " |");
	// //
	// }

	// }
	// if (w.request.getPath().contains("/nominate")) // follower receives a vote
	// request
	// {
	// long requestServerTerm = Long.parseLong(w.request.getPath().split("/")[2]);
	// System.out.println(
	// "reaches follower /nominate | comparing " + requestServerTerm + " == " +
	// engine.serverTerm);
	// if (requestServerTerm == engine.serverTerm) // if request term matches this
	// server term
	// {

	// // vote /accept if it has not voted on current term
	// long referenceID = engine.getNextMessageID();
	// String path = w.request.getPath() + "/accept";
	// int serverPort = Integer.parseInt(w.request.getPath().split("/")[3]);
	// Heartbeat.sendHeartbeat(serverPort, referenceID, w.request.getOrigin(),
	// engine.serverID, path,
	// ByteString.copyFromUtf8("term: " + engine.serverTerm));
	// System.out.println(" ** sending /accept to " + w.request.getOrigin() + " |
	// refID: " + referenceID
	// + " | path: " + path + " |");

	// } else {// then /reject if it has voted on current term already

	// }
	// }
	// } else if (engine.getServerRole() == "candidate") {
	// if (w.request.getPath().contains("/nominate")) // candidated received
	// response of vote request, either
	// // /accept or /reject
	// {
	// long requestServerTerm = Long.parseLong(w.request.getPath().split("/")[2]);
	// System.out.println(
	// "reaches candidate /nominate | comparing " + requestServerTerm + " == " +
	// engine.serverTerm);
	// if (requestServerTerm == engine.serverTerm) // if request term matches this
	// server term
	// {
	// String voteRequestResponse = w.request.getPath().split("/")[4]; // accept or
	// reject
	// if (voteRequestResponse.contains("accept")) {
	// System.out.println("I reach accept");
	// } else // reject
	// {

	// }
	// }
	// }
	// }

	// // // System.out.println(" *** work is not null *** ");

	// // if (w.request.getDestination() == engine.serverID) {
	// // try {

	// // logger.info(
	// // "**** do work " + w + " ****");

	// // //needs to increment its term

	// // //then vote /accept or /reject

	// // // simulates latency
	// // Thread.sleep(2000);

	// // } catch (InterruptedException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// // } else {

	// // // logger.info("Worker | no direct destination exists | server.id =" +
	// // // Engine.getInstance().serverID + " | server destination = " +
	// // // w.request.getDestination());

	// // // forward the message to Link instances
	// // for (var link : Engine.getInstance().links) {
	// // if (link.getServerID() == w.request.getDestination()) {
	// // logger.info("TODO: send message");

	// // }

	// // String path = "unsure"; // should be /accept or /reject

	// // Heartbeat.sendHeartbeat(link.getPort(), w.request.getId(),
	// // w.request.getDestination(), w.request.getOrigin(), path,
	// // w.request.getPayload());

	// // }

	// // // if no direct destination exists, forward to all
	// // // links or the next link?
	// // }

	// }

}
