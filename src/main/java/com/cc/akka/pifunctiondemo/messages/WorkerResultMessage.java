package com.cc.akka.pifunctiondemo.messages;

public class WorkerResultMessage {

	private final double value;
	
	public WorkerResultMessage(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}
}