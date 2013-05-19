package com.cc.akka.pifunctiondemo.util;

public class CalculatePiAlgorithm {
	
	private CalculatePiAlgorithm() { }
	
	public static double compute(int start, int nrOfElements) {
		double acc = 0.0;
		for(int idx = start * nrOfElements; idx <= (start + 1) * nrOfElements - 1; idx++) {
			acc += 4.0 * (1 - (idx % 2) * 2) / (2 * idx + 1);
		}
		return acc;
	}
}