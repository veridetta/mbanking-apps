package com.example.hinadadebank.model;

import com.google.gson.annotations.SerializedName;

public class UploadData {

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("users_id")
	private String usersId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("value")
	private String value;

	@SerializedName("desc")
	private String desc;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUsersId(String usersId){
		this.usersId = usersId;
	}

	public String getUsersId(){
		return usersId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}
}