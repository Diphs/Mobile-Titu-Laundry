package com.example.titulaundry.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseRegister {

	@SerializedName("data")
	private DataItemRegister data;

	@SerializedName("kode")
	private int kode;

	public void setData(DataItemRegister data){
		this.data = data;
	}

	public DataItemRegister getData(){
		return data;
	}

	public void setKode(int kode){
		this.kode = kode;
	}

	public int getKode(){
		return kode;
	}
}