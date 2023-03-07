package vidas.grpc.route.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

public class Worker extends Thread {
	protected static Logger logger = LoggerFactory.getLogger("worker");
	private boolean forever = true;

	private CountDownLatch electionTimerLatch;
	private int electionTimer = 5;

	LinkedBlockingDeque<Boolean> electionTimerQueue;

	public Worker() {

	}

	public void shutdown() {
		logger.info("shutting down manager");
		forever = true;
	}

	private void processWorkRequest(Work w) {

		if (w == null)
			return;

		if (w.request.getPath().contains("/nominate")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Engine engine = Engine.getInstance();
			if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Follower) {

				engine.serverTerm = Long.parseLong(w.request.getPath().split("/")[2]);
				// DEBUG PRINT
				// System.out.println(" ** " + "Term: " + engine.serverTerm + " || State: "
				// + engine.serverStateMachine.state.toString() + " || votedFor: "
				// + engine.serverStateMachine.votedFor + " || nominationVotes: "
				// + engine.serverStateMachine.nominationVotes + " || Type:
				// processWorkRequest()|| Origin: "
				// + w.request.getOrigin() + " || Destination: " + w.request.getDestination() +
				// " || Path: "
				// + w.request.getPath() + " || " +
				// " || Reason: follower received request that contains /nominate path |
				// resetting election timer task | setting term to request term: "
				// + engine.serverTerm + " ** \n");
				String str = " ** " + "Term: " + engine.serverTerm + " || State: "
						+ engine.serverStateMachine.state.toString() + " || votedFor: "
						+ engine.serverStateMachine.votedFor + " || nominationVotes: "
						+ engine.serverStateMachine.nominationVotes + " || Type: processWorkRequest()|| Origin: "
						+ w.request.getOrigin() + " || Destination: " + w.request.getDestination() + " || Path: "
						+ w.request.getPath() + " || " +
						" || Reason: follower received request that contains /nominate path | resetting election timer task | setting term to request term: "
						+ engine.serverTerm + " ** \n";
				engine.gui.setLabel(str);
				// DEBUG PRINT
				//Engine.getInstance().election.electionTimerTask();
			}
		} else if (w.request.getPath().contains("/heartbeat")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Engine engine = Engine.getInstance();
			if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Follower) {

				engine.serverStateMachine.nominationVotes = 0;
				//engine.election.electionTimerTask(); // reset election timer

				// DEBUG PRINT
				// System.out.println(" ** " + "Term: " + engine.serverTerm + " || State: "
				// + engine.serverStateMachine.state.toString() + " || votedFor: "
				// + engine.serverStateMachine.votedFor + " || nominationVotes: "
				// + engine.serverStateMachine.nominationVotes + " || Type:
				// processWorkRequest()|| Origin: "
				// + w.request.getOrigin() + " || Destination: " + w.request.getDestination() +
				// " || Path: "
				// + w.request.getPath() + " || " +
				// " || Reason: follower received leader request that contains /heartbeat path |
				// resetting election timer task | resetting nomination votes "
				// + engine.serverTerm + " ** \n");
				String str = " ** " + "Term: " + engine.serverTerm + " || State: "
						+ engine.serverStateMachine.state.toString() + " || votedFor: "
						+ engine.serverStateMachine.votedFor + " || nominationVotes: "
						+ engine.serverStateMachine.nominationVotes + " || Type: processWorkRequest()|| Origin: "
						+ w.request.getOrigin() + " || Destination: " + w.request.getDestination() + " || Path: "
						+ w.request.getPath() + " || " +
						" || Reason:  follower received leader request that contains /heartbeat path | resetting election timer task | resetting nomination votes "
						+ engine.serverStateMachine.nominationVotes + " ** \n";
				engine.gui.setLabel(str);
				// DEBUG PRINT
				
			} else if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Candidate) {
				
				// DEBUG PRINT
				// System.out.println(" ** " + "Term: " + engine.serverTerm + " || State: "
				// + engine.serverStateMachine.state.toString() + " || votedFor: "
				// + engine.serverStateMachine.votedFor + " || nominationVotes: "
				// + engine.serverStateMachine.nominationVotes + " || Type:
				// processWorkRequest()|| Origin: "
				// + w.request.getOrigin() + " || Destination: " + w.request.getDestination() +
				// " || Path: "
				// + w.request.getPath() + " || " +
				// " || Reason: candidate received leader request that contains /heartbeat path
				// | resetting election timer task | demoting to follower | resetting nomination
				// votes"
				// + " ** \n");
				String str = " ** " + "Term: " + engine.serverTerm + " || State: "
						+ engine.serverStateMachine.state.toString() + " || votedFor: "
						+ engine.serverStateMachine.votedFor + " || nominationVotes: "
						+ engine.serverStateMachine.nominationVotes + " || Type: processWorkRequest()|| Origin: "
						+ w.request.getOrigin() + " || Destination: " + w.request.getDestination() + " || Path: "
						+ w.request.getPath() + " || " +
						" || Reason: candidate received leader request that contains /heartbeat path | resetting election timer task | demoting to follower | resetting nomination votes"
						+ " ** \n";
				engine.gui.setLabel(str);
				// DEBUG PRINT
				engine.serverStateMachine.state = engine.serverStateMachine.state.previousState();
				engine.serverStateMachine.nominationVotes = 0;
				//engine.election.electionTimerTask(); // reset election timer

			}
		}

	}

	@Override
	public void run() {
		// TODO not a good idea to spin on work --> wastes CPU cycles
		while (forever) {
			try {

				// System.out.println("any reach");
				var w = Engine.getInstance().workQueue.poll(); // check if any work is to be
																					// processed
				processWorkRequest(w);

			} catch (Exception e) {
				logger.error("worker failure", e);
			}
		}
	}
}
