package com.ag.nanshi.other.rottomak.vo;

import java.util.List;

import com.ag.nanshi.other.rottomak.RotToMakUser;

public class RotToMakResult {
	
	private String event;
	
	private List<RotToMakUser> rotToMakUserList;
	
	private long onWheatTime;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public List<RotToMakUser> getRotToMakUserList() {
		return rotToMakUserList;
	}

	public void setRotToMakUserList(List<RotToMakUser> rotToMakUserList) {
		this.rotToMakUserList = rotToMakUserList;
	}

	public RotToMakResult(){}

	public RotToMakResult(String event, List<RotToMakUser> rotToMakUserList) {
		super();
		this.event = event;
		this.rotToMakUserList = rotToMakUserList;
	}

	public long getOnWheatTime() {
		return onWheatTime;
	}

	public void setOnWheatTime(long onWheatTime) {
		this.onWheatTime = onWheatTime;
	}

	public RotToMakResult(String event, List<RotToMakUser> rotToMakUserList, long onWheatTime) {
		super();
		this.event = event;
		this.rotToMakUserList = rotToMakUserList;
		this.onWheatTime = onWheatTime;
	}

}
