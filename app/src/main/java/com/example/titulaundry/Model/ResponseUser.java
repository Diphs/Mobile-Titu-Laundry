package com.example.titulaundry.Model;

import com.example.titulaundry.ModelMySQL.DataItemUser;
import com.google.gson.annotations.SerializedName;

public class ResponseUser{

	@SerializedName("data")
	private DataItemUser data;

	@SerializedName("kode")
	private int kode;

	@SerializedName("message")
	private String message;

	public void setData(DataItemUser data){
		this.data = data;
	}

	public DataItemUser getData(){
		return data;
	}

	public void setKode(int kode){
		this.kode = kode;
	}

	public int getKode(){
		return kode;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}