package com.busfare.calculate;

import java.io.Serializable;
import java.util.Date;

public class TripResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date startDate; 
	private Date endDate; 
	private Long duration;
	private String fromStopId;
	private String endStopId;
	private Double tripFare;
	private String companyId;
	private String busId;
	private String panNo;
	private String status;
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getFromStopId() {
		return fromStopId;
	}
	public void setFromStopId(String fromStopId) {
		this.fromStopId = fromStopId;
	}
	public String getEndStopId() {
		return endStopId;
	}
	public void setEndStopId(String endStopId) {
		this.endStopId = endStopId;
	}
	public Double getTripFare() {
		return tripFare;
	}
	public void setTripFare(Double tripFare) {
		this.tripFare = tripFare;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusID(String busId) {
		this.busId = busId;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
