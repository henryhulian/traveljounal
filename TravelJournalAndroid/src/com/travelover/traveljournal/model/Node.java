package com.travelover.traveljournal.model;

import java.io.Serializable;

/**
 * @author fb421
 *
 */
public class Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5143175984031187895L;
	
	private Double langtitude;
	
	private Double latitude;
	
	private Double altitude;
	
	private Long time;

	public Double getLangtitude() {
		return langtitude;
	}

	public void setLangtitude(Double langtitude) {
		this.langtitude = langtitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	public String toString() {
		return "Node [langtitude=" + langtitude + ", latitude=" + latitude
				+ ", altitude=" + altitude + ", time=" + time + "]";
	}
	
}
