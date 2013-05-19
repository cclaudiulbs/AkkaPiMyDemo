package com.cc.akka.pifunctiondemo.actors;

import com.cc.akka.pifunctiondemo.messages.WorkMessage;
import com.cc.akka.pifunctiondemo.messages.WorkerResultMessage;
import com.cc.akka.pifunctiondemo.util.CalculatePiAlgorithm;

import akka.actor.UntypedActor;

/**
 * Since each Worker/Actor extends from an {@link UntypedActor} Create a handler
 * {@code onReceive()} for the Actor/Worker, check the message, calculate the
 * Pi, by retrieving the start, and nrOfElements, and {@code getSender()} and
 * tell the Master Actor in our case the result wrapped in an
 * {@link WorkerResultMessage} class
 * 
 * In Akka the sender reference is implicitly passed along with the message so
 * that the receiver can always reply or store away the sender reference for
 * future use.
 * 
 * @author cclaudiu
 */
public class PiWorkerActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WorkMessage) {
			WorkMessage workMessage = (WorkMessage) message;
			double result = CalculatePiAlgorithm.compute(workMessage.getStart(),
					workMessage.getNrOfElements());

			// getMaster && tell him back a message wrapped by a Response Object
			getSender().tell(new WorkerResultMessage(result));
		}
	}
}