package com.example.titulaundry.ModelMySQL;

import com.google.gson.annotations.SerializedName;

public class DataPesanan {

	@SerializedName("jenis_jasa")
	private String jenisJasa;

	@SerializedName("total_berat")
	private String totalBerat;

	@SerializedName("total_harga")
	private int totalHarga;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("durasi")
	private String durasi;

	@SerializedName("status_pesanan")
	private String statusPesanan;

	@SerializedName("image")
	private String image;

	@SerializedName("id_pesanan")
	private String id_pesanan;


	public String getId_pesanan() {
		return id_pesanan;
	}

	public void setId_pesanan(String id_pesanan) {
		this.id_pesanan = id_pesanan;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setJenisJasa(String jenisJasa){
		this.jenisJasa = jenisJasa;
	}

	public String getJenisJasa(){
		return jenisJasa;
	}

	public void setTotalBerat(String totalBerat){
		this.totalBerat = totalBerat;
	}

	public String getTotalBerat(){
		return totalBerat;
	}

	public void setTotalHarga(int totalHarga){
		this.totalHarga = totalHarga;
	}

	public int getTotalHarga(){
		return totalHarga;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setDurasi(String durasi){
		this.durasi = durasi;
	}

	public String getDurasi(){
		return durasi;
	}

	public void setStatusPesanan(String statusPesanan){
		this.statusPesanan = statusPesanan;
	}

	public String getStatusPesanan(){
		return statusPesanan;
	}
}