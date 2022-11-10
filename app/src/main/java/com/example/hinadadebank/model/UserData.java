package com.example.hinadadebank.model;

import com.google.gson.annotations.SerializedName;

public class UserData {

	@SerializedName("nik")
	private long nik;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("tel")
	private long tel;

	@SerializedName("id")
	private int id;

	@SerializedName("saldo")
	private int saldo;

	@SerializedName("card")
	private long card;

	@SerializedName("email")
	private String email;

	@SerializedName("username")
	private String username;

	@SerializedName("status")
	private String status;

	public void setNik(int nik){
		this.nik = nik;
	}

	public long getNik(){
		return nik;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTel(int tel){
		this.tel = tel;
	}

	public long getTel(){
		return tel;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSaldo(int saldo){
		this.saldo = saldo;
	}

	public int getSaldo(){
		return saldo;
	}

	public void setCard(long card){
		this.card = card;
	}

	public long getCard(){
		return card;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}