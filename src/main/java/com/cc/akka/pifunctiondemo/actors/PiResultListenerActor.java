package com.cc.akka.pifunctiondemo.actors;

import com.cc.akka.pifunctiondemo.messages.PiResultMessage;

import akka.actor.UntypedActor;

/**
 * *** Note that shutting down the actor system should be done by that part of
 * the application which can safely determine that everything has been said and
 * done. In this case, it is the Listener actor, but in other scenarios it might
 * be the main thread or some other external service.
 * 
 * @author cclaudiu
 */
public class PiResultListenerActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		
		if (message instanceof PiResultMessage) {
			PiResultMessage resultMessage = (PiResultMessage) message;
			System.out.println("Pi Result Message Computed: " + resultMessage.getPi());

			// shutdown system ***
			getContext().system().shutdown();
			
		} else {
			unhandled(message);
		}
	}
}