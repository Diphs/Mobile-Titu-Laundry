package com.example.titulaundry.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBanner{

	@SerializedName("Kode")
	private int kode;

	@SerializedName("Data")
	private List<DataItemBanner> data;

	@SerializedName("Pesan")
	private String pesan;

	public void setKode(int kode){
		this.kode = kode;
	}

	public int getKode(){
		return kode;
	}

	public void setData(List<DataItemBanner> data){
		this.data = data;
	}

	public List<DataItemBanner> getData(){
		return data;
	}

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}
}