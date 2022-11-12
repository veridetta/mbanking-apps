package com.example.hinadadebank.model;

import com.google.gson.annotations.SerializedName;

public class MutasiData {

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("users_id")
	private int usersId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("from")
	private long from;

	@SerializedName("saldo")
	private String saldo;

	@SerializedName("debit")
	private long debit;

	@SerializedName("credit")
	private long credit;

	@SerializedName("dest")
	private long dest;

	@SerializedName("desc")
	private String desc;

	public MutasiData(String updatedAt, int usersId, String createdAt, long from, String saldo,
					  long debit, long credit, long dest, String desc){
		this.updatedAt =updatedAt;
		this.usersId = usersId;
		this.createdAt = createdAt;
		this.from = from;
		this.saldo = saldo;
		this.debit = debit;
		this.credit = credit;
		this.dest = dest;
		this.desc = desc;
	}
	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUsersId(int usersId){
		this.usersId = usersId;
	}

	public int getUsersId(){
		return usersId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setFrom(long from){
		this.from = from;
	}

	public long getFrom(){
		return from;
	}

	public void setSaldo(String saldo){
		this.saldo = saldo;
	}

	public String getSaldo(){
		return saldo;
	}

	public void setDebit(long debit){
		this.debit = debit;
	}

	public long getDebit(){
		return debit;
	}

	public void setCredit(long credit){
		this.credit = credit;
	}

	public long getCredit(){
		return credit;
	}

	public void setDest(long dest){
		this.dest = dest;
	}

	public long getDest(){
		return dest;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}
}