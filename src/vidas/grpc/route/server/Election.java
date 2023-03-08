package vidas.grpc.route.server;

import java.util.*;

import com.google.protobuf.ByteString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Election extends Thread {
	protected static Logger logger = LoggerFactory.getLogger("election");

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
			System.out.println("cancelling election timer task meaning the electionTimerTask is triggered with delay: " + delay);
		}
		electionTask = new TimerTask() {

			int seconds = 15;
			int i = 0;

			@Override
			public void run() {

				i++;
				if (i % seconds == 0) {
					System.out.println("Election Timer Over! Performing Election Role Based Work");
					electionTimerTask(0); // reset election timer for itself so it keeps going

					Engine engine = Engine.getInstance();
					if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Follower) {

						engine.serverStateMachine.state = engine.serverStateMachine.state.nextState(); // upgrade
																										// follower
																										// to candidate

						engine.serverTerm++; // increment term

						// DEBUG PRINT
						engine.debugHelper.debugPrint(null, "electionTimerTask()",
								" election timer over, upgrading to candidate & term incremented to ");
						// DEBUG PRINT

						engine.serverStateMachine.votedFor = "";
						engine.serverStateMachine.state.sendNominateRequest();

					} else if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Candidate) {
						// did not get back majority nominate requests, trying again as a candidate
						// after increment

						engine.serverTerm++;
						engine.serverStateMachine.votedFor = "";
						engine.serverStateMachine.state.sendNominateRequest(); // vote for me request

					} else if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Leader) {
						engine.serverStateMachine.state.sendLeaderHeartbeat();
						System.out.println("sending leader heartbeat " + engine.getNextMessageID());
					}

				} else {
					System.out.println("Time left:" + (seconds - (i % seconds)));
					Engine.getInstance().debugHelper.gui.setTimer(Integer.toString(seconds - (i % seconds)));
				}

			}
		};
		Timer timer = new Timer("Timer");
		// long delay = 20000L;
		timer.schedule(electionTask, delay, 1000);
		
	}

	public void shutdown() {
		logger.info("shutting down election " + this.getId());

	}

}
