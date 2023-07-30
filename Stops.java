package com.busfare.calculate;

public enum Stops {

	STOP1("STOP1"),
	STOP2("STOP2"),
	STOP3("STOP2");

	private final String stop;

	private Stops(String stop) {
		this.stop = stop;
	}

	public String getStop() {
		return stop;
	}
}
