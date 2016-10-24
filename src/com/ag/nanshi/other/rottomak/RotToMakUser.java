package com.ag.nanshi.other.rottomak;

import java.io.Serializable;

public class RotToMakUser implements Serializable{

	private String userId;
	
	private String username;
	
	private String pic;
	
	//麦克风第三方ID
	private String openMicrophoneUserId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getOpenMicrophoneUserId() {
		return openMicrophoneUserId;
	}

	public void setOpenMicrophoneUserId(String openMicrophoneUserId) {
		this.openMicrophoneUserId = openMicrophoneUserId;
	}

	
}
