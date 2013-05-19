package com.cc.akka.pifunctiondemo.messages;

public class PiResultMessage {

	private final double pi;
	
	public PiResultMessage(double pi) {
		this.pi = pi;
	}

	public double getPi() {
		return pi;
	}
}