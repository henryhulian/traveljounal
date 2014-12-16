package com.travelover.traveljournal.model;

import java.io.Serializable;

import android.R.integer;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8697268603477352506L;

	private integer id;
	
	private String userName;
	
	private String nickName;
	
	
	public integer getId() {
		return id;
	}
	public void setId(integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
