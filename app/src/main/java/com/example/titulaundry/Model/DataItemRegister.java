package com.example.titulaundry.Model;

import com.google.gson.annotations.SerializedName;

public class DataItemRegister {

	@SerializedName("id_user")
	private int idUser;

	public void setIdUser(int idUser){
		this.idUser = idUser;
	}

	public int getIdUser(){
		return idUser;
	}
}