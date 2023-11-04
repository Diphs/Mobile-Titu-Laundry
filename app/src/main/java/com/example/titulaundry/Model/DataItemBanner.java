package com.example.titulaundry.Model;

import com.google.gson.annotations.SerializedName;

public class DataItemBanner {

	@SerializedName("image")
	private String namaBanner;

	@SerializedName("id_banner")
	private String idBanner;

	public void setNamaBanner(String namaBanner){
		this.namaBanner = namaBanner;
	}

	public String getNamaBanner(){
		return namaBanner;
	}

	public void setIdBanner(String idBanner){
		this.idBanner = idBanner;
	}

	public String getIdBanner(){
		return idBanner;
	}
}