package com.example.hinadadebank.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MutasiResponse {

	@SerializedName("data")
	private ArrayList<MutasiData> MutasiData;

	@SerializedName("messages")
	private String messages;

	@SerializedName("status")
	private String status;

	public void setData(ArrayList<MutasiData> mutasiData){
		this.MutasiData = mutasiData;
	}

	public ArrayList<com.example.hinadadebank.model.MutasiData> getData(){
		return MutasiData;
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