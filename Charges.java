package com.busfare.calculate;

public enum Charges {
	
	STOP1_TO_STOP2("STOP1", "STOP2", "3.25"),
	STOP2_TO_STOP1("STOP2", "STOP1", "3.25"),
	STOP2_TO_STOP3("STOP2", "STOP3", "5.50"),
	STOP3_TO_STOP2("STOP3", "STOP2", "5.50"),
	STOP1_TO_STOP3("STOP1", "STOP3", "7.30"),
	STOP3_TO_STOP1("STOP3", "STOP1", "7.30");
	
	 private final String beginStop;
	 private final String endStop;
	 private final String fare;

	 private Charges(String beginStop, String endStop, String fare) {
		 this.beginStop = beginStop;
	     this.endStop = endStop;
	     this.fare = fare;
	 }

	public String getBeginStop() {
		return beginStop;
	}

	public String getEndStop() {
		return endStop;
	}

	public String getFare() {
		return fare;
	}
	  
	 

}
