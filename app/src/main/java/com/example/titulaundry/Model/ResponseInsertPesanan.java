package com.example.titulaundry.Model;

import com.example.titulaundry.ModelMySQL.DataInsert;
import com.google.gson.annotations.SerializedName;

public class ResponseInsertPesanan{

	@SerializedName("kode")
	private int kode;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private DataInsert data;

	public DataInsert getData() {
		return data;
	}

	public void setData(DataInsert data) {
		this.data = data;
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