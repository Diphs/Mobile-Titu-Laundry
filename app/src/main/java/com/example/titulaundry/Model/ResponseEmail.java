package com.example.titulaundry.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseEmail{

	@SerializedName("data")
	private DataItemEmail data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setData(DataItemEmail data){
		this.data = data;
	}

	public DataItemEmail getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}