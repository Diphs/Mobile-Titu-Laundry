package com.example.titulaundry.Model;

import java.util.List;

import com.example.titulaundry.ModelMySQL.DataItemAlamatLain;
import com.google.gson.annotations.SerializedName;

public class ResponseAlamatLain{

	@SerializedName("Kode")
	private int kode;

	@SerializedName("Data")
	private List<DataItemAlamatLain> data;

	@SerializedName("Pesan")
	private String pesan;

	public void setKode(int kode){
		this.kode = kode;
	}

	public int getKode(){
		return kode;
	}

	public void setData(List<DataItemAlamatLain> data){
		this.data = data;
	}

	public List<DataItemAlamatLain> getData(){
		return data;
	}

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}
}