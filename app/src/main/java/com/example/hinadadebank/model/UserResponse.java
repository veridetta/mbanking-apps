package com.example.hinadadebank.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse{

	@SerializedName("data")
	private UserData userData;

	@SerializedName("messages")
	private String messages;

	@SerializedName("status")
	private String status;

	public void setData(UserData userData){
		this.userData = userData;
	}

	public UserData getData(){
		return userData;
	}

	public void setMessages(String messages){
		this.messages = messages;
	}

	public String getMessages(){
		return messages;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}