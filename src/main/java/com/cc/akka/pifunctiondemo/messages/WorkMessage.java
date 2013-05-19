package com.cc.akka.pifunctiondemo.messages;

public class WorkMessage {

	private final int start;
	private final int nrOfElements;
	
	public WorkMessage(int start, int nrOfElements) {
		this.start = start;
		this.nrOfElements = nrOfElements;
	}

	public int getStart() {
		return start;
	}

	public int getNrOfElements() {
		return nrOfElements;
	}
}