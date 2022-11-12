package com.example.hinadadebank.model;

import com.google.gson.annotations.SerializedName;

public class TransResponse{

	@SerializedName("data")
	private TransData transData;

	@SerializedName("messages")
	private String messages;

	@SerializedName("status")
	private String status;

	public void setData(TransData transData){
		this.transData = transData;
	}

	public TransData getData(){
		return transData;
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