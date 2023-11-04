package com.example.titulaundry.Model;

import java.util.List;

import com.example.titulaundry.ModelMySQL.DataItemVoucher;
import com.google.gson.annotations.SerializedName;

public class ResponseVoucher{

	@SerializedName("Kode")
	private int kode;

	@SerializedName("Data")
	private List<DataItemVoucher> data;

	@SerializedName("Pesan")
	private String pesan;

	public void setKode(int kode){
		this.kode = kode;
	}

	public int getKode(){
		return kode;
	}

	public void setData(List<DataItemVoucher> data){
		this.data = data;
	}

	public List<DataItemVoucher> getData(){
		return data;
	}

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}
}