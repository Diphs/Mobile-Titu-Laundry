package com.example.titulaundry.Model;

import java.util.List;

import com.example.titulaundry.ModelMySQL.DataPesananSemua;
import com.google.gson.annotations.SerializedName;

public class SemuaPesanan{

	@SerializedName("data")
	private List<DataPesananSemua> data;

	@SerializedName("kode")
	private int kode;

	@SerializedName("message")
	private String message;

	public void setData(List<DataPesananSemua> data){
		this.data = data;
	}

	public List<DataPesananSemua> getData(){
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