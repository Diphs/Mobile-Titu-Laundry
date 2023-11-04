package com.example.titulaundry.Model;

import java.util.List;

import com.example.titulaundry.ModelMySQL.DataPesanan;
import com.google.gson.annotations.SerializedName;

public class ResponsePesanan{

	@SerializedName("Kode")
	private int kode;

	@SerializedName("Data")
	private List<DataPesanan> data;

	@SerializedName("Pesan")
	private String pesan;

	public void setKode(int kode){
		this.kode = kode;
	}

	public int getKode(){
		return kode;
	}

	public void setData(List<DataPesanan> data){
		this.data = data;
	}

	public List<DataPesanan> getData(){
		return data;
	}

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}
}