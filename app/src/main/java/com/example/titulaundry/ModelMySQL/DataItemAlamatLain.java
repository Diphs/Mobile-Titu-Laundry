package com.example.titulaundry.ModelMySQL;

import com.google.gson.annotations.SerializedName;

public class DataItemAlamatLain {

	@SerializedName("id_alamat")
	private String idAlamat;

	@SerializedName("alamat")
	private String alamat;

	public void setIdAlamat(String idAlamat){
		this.idAlamat = idAlamat;
	}

	public String getIdAlamat(){
		return idAlamat;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}
}