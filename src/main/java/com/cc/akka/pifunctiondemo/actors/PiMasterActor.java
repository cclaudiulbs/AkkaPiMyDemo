package com.cc.akka.pifunctiondemo.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

import com.cc.akka.pifunctiondemo.messages.CalculateMessage;
import com.cc.akka.pifunctiondemo.messages.PiResultMessage;
import com.cc.akka.pifunctiondemo.messages.WorkMessage;
import com.cc.akka.pifunctiondemo.messages.WorkerResultMessage;

/**
 * Master Actor, which controls the Worker-Actors. The last Actor Constructor
 * argument, is used to pass information to outside world. The Master Actor,
 * should be able to respond to 2 different Type of messages:
 * {@link CalculateMessage} which should start the computation,
 * {@link PiResultMessage} which should aggregate the results.
 * 
 * Once a Master Actor is stopped, all of its Supervised-Worker-Children will
 * automatically receive a shutdown message.
 * 
 * @author cclaudiu
 */
public class PiMasterActor extends UntypedActor {

	private final int nrOfMessages;
	private final int nrOfElements;

	private double pi;
	private int nrOfResults;

	private final ActorRef listener;
	private final ActorRef workerRouter;

	public PiMasterActor(final int nrOfWorkers, final int nrOfMessages,
			final int nrOfElements, ActorRef listener) {
		this.nrOfMessages = nrOfMessages;
		this.nrOfElements = nrOfElements;
		this.listener = listener;

		workerRouter = this.getContext().actorOf(
				new Props(PiWorkerActor.class).withRouter(new RoundRobinRouter(nrOfWorkers)));
	}

	@Override
	public void onReceive(Object message) throws Exception {
		// calculate is invoked
		if (message instanceof CalculateMessage) {
			for (int idx = 0; idx < nrOfMessages; idx++) {
				workerRouter.tell(new WorkMessage(idx, nrOfElements), getSelf());
			}

		} else if (message instanceof WorkerResultMessage) {
			WorkerResultMessage resultMessage = (WorkerResultMessage) message;
			pi += resultMessage.getValue();
			nrOfResults += 1;

			if (nrOfResults == nrOfMessages) {
				listener.tell(new PiResultMessage(pi), getSelf());
				getContext().stop(getSelf());
			}
			
		} else {
			unhandled(message);
		}
	}
}