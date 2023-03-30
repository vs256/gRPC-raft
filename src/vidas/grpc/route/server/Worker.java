package vidas.grpc.route.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

import vidas.grpc.route.server.StateMachine.ServerState;
import vidas.grpc.route.server.StateMachine.ServerStateMachine;
import vidas.grpc.route.server.Types.Work;

public class Worker extends Thread {
	protected static Logger logger = LoggerFactory.getLogger("worker");
	private boolean forever = true;

	LinkedBlockingDeque<Boolean> electionTimerQueue;

	public Worker() {

	}

	public void shutdown() {
		logger.info("shutting down worker");
		forever = true;
	}

	@Override
	public void run() {
		// TODO not a good idea to spin on work --> wastes CPU cycles
		while (forever) {
			try {

				var w = Engine.getInstance().workQueue.poll(); // check if any work is to be
																// processed
				processWorkRequest(w);

			} catch (Exception e) {
				logger.error("worker failure", e);
			}
		}
	}

	private void processWorkRequest(Work w) {

		if (w == null) //return nothing if work is null
			return;

		handleClientRequests(w); //handle client requests

		handleElectionRequests(w); //handle election requests


	}


	public void handleClientRequests(Work w)
	{
		if (w.request.getPath().contains("/client")) {
			// byte[] hello = w.request.getPayload().toByteArray();
			logger.info("Server got clients message: " + w.request.getPayload().toStringUtf8());
		}
	}

	public void handleElectionRequests(Work w)
	{
		
		if (w.request.getPath().contains("/nominate")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Engine engine = Engine.getInstance();
			if (engine.serverStateMachine.state.getStateRole() == ServerStateMachine.ServerStateRoles.Follower) {

				// // DEBUG PRINT
				// engine.debugHelper.debugPrint(w.request, "processWorkRequest()",
				// " follower received request that contains /nominate path |<br> resetting
				// election timer task | setting term to request term: "
				// + engine.serverTerm);
				// // DEBUG PRINT
			}
		} else if (w.request.getPath().contains("/heartbeat")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Engine engine = Engine.getInstance();

			if (engine.serverStateMachine.state.getStateRole()== ServerStateMachine.ServerStateRoles.Follower) {

				engine.serverStateMachine.nominationVotes = 0;

				// DEBUG PRINT
				engine.debugHelper.debugPrint(w.request, "processWorkRequest()",
						"follower received leader request that contains /heartbeat path |<br> resetting election timer task | resetting nomination votes "
								+ engine.serverStateMachine.nominationVotes);
				// DEBUG PRINT

			} else if (engine.serverStateMachine.state.getStateRole() == ServerStateMachine.ServerStateRoles.Candidate) {

				engine.serverStateMachine.state.previousState();
				engine.serverStateMachine.nominationVotes = 0;
				engine.election.electionTimerTask(4000L); // reset election timer

				// DEBUG PRINT
				engine.debugHelper.debugPrint(w.request, "processWorkRequest()",
						"candidate received leader request that contains /heartbeat path |<br> resetting election timer task | demoting to follower | resetting nomination votes");
				// DEBUG PRINT

			}

		}
	}




}
