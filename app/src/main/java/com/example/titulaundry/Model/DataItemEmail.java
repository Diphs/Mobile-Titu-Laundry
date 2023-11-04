package com.example.titulaundry.Model;

import com.google.gson.annotations.SerializedName;

public class DataItemEmail {

	@SerializedName("kode_verifikasi")
	private String kodeVerifikasi;

	@SerializedName("verify_status")
	private String verifyStatus;

	@SerializedName("id_user")
	private String idUser;

	public void setKodeVerifikasi(String kodeVerifikasi){
		this.kodeVerifikasi = kodeVerifikasi;
	}

	public String getKodeVerifikasi(){
		return kodeVerifikasi;
	}

	public void setVerifyStatus(String verifyStatus){
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyStatus(){
		return verifyStatus;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}
}