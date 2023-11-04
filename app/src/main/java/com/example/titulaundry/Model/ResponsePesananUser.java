package com.example.titulaundry.Model;

import com.google.gson.annotations.SerializedName;

public class ResponsePesananUser{

	@SerializedName("data")
	private DataPesananUser data;

	@SerializedName("kode")
	private int kode;

	@SerializedName("message")
	private String message;

	public void setData(DataPesananUser data){
		this.data = data;
	}

	public DataPesananUser getData(){
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