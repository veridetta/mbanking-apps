package com.example.hinadadebank.model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse{

	@SerializedName("data")
	private UploadData data;

	@SerializedName("messages")
	private String messages;

	@SerializedName("status")
	private String status;

	public void setData(UploadData data){
		this.data = data;
	}

	public UploadData getData(){
		return data;
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