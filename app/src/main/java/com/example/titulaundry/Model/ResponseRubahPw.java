package com.example.titulaundry.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseRubahPw{

	@SerializedName("kode")
	private int kode;

	@SerializedName("message")
	private String message;

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