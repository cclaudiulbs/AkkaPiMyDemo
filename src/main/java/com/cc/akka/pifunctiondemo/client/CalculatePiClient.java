package com.cc.akka.pifunctiondemo.client;

import com.cc.akka.pifunctiondemo.actors.PiMasterActor;
import com.cc.akka.pifunctiondemo.actors.PiResultListenerActor;
import com.cc.akka.pifunctiondemo.messages.CalculateMessage;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

/**
 * Calculating the value of PI using akka's actors The design we are aiming for
 * is to have one Master actor initiating the computation, creating a set of
 * Worker actors. Then it splits up the work into discrete chunks, and sends
 * these chunks to the different workers in a round-robin fashion. The master
 * waits until all the workers have completed their work and sent back results
 * for aggregation. When computation is completed the master sends the result to
 * the Listener, which prints out the result.
 * <p>
 * Note; this can be accomplished with the java's 7 ForkJoin framework, however
 * we have the freedom to coordinate the actors, for a more complex logic, than
 * map/reduce framework.
 * 
 * <p>
 * First time when you're doing an akka application, calculate the number of
 * messages-type the application needs.
 * <p>
 * For this program we need 4:
 * <ul>
 * Calculate: message sent to the Master Actor to start computation
 * </ul>
 * <ul>
 * Worker: message sent from the Master to each Worker(delegates the task from
 * master->each-worker)
 * </ul>
 * <ul>
 * Result: message sent from each Worker to the Master to aggregate the results
 * </ul>
 * <ul>
 * PiApproximation: final message result, sent from the Master to the Listener
 * Actor to display the results
 * </ul>
 * 
 * <p>
 * Messages sent to the Actors should be immutable!!! passed as Value Objects,
 * to avoid shared mutable data.
 * 
 * <p>
 * After creating the messages, in their corresponding package, as Value
 * Objects(which should be as we know immutable) we start creating the
 * Workers/Actors in their corresponding package.
 * 
 * @author cclaudiu
 */
public class CalculatePiClient {
	
	public static void main(String[] args) {
		new CalculatePiClient().compute(3, 10000, 10000);
	}

	private void compute(final int nrOfWorkers, final int nrOfElements,
			final int nrOfMessages) {

		// 1. Create an Akka System
		final ActorSystem akkaSystem = ActorSystem.create("PiSystem");

		// 2. Create the Last-Actor, which is the Listener(in this case) print
		// result of computation
		// and shutdown the system
		final ActorRef listenerActor = akkaSystem.actorOf(new Props(
				PiResultListenerActor.class), "ListenerActor");

		// 3. Create the Master Actor, which will control and aggregate the
		// results coming from Worker Actors
		final ActorRef masterActor = akkaSystem.actorOf(new Props(
				new UntypedActorFactory() {

					@Override
					public UntypedActor create() {
						return new PiMasterActor(nrOfWorkers, nrOfMessages,
								nrOfElements, listenerActor);
					}
				}), "MasterActor");

		// 4. Send a message from main-thread to Master to start computation
		masterActor.tell(new CalculateMessage());
	}
}