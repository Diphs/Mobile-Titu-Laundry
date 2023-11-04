package com.example.titulaundry.ModelMySQL;

import com.google.gson.annotations.SerializedName;

public class DataPesananSemua {

	@SerializedName("jenis_jasa")
	private String jenisJasa;

	@SerializedName("total_berat")
	private String totalBerat;

	@SerializedName("total_harga")
	private String totalHarga;

	@SerializedName("status_pesanan")
	private String statusPesanan;

	@SerializedName("waktu_antar")
	private String waktuAntar;

	@SerializedName("image")
	private String image;

	@SerializedName("waktu_penjemputan")
	private String waktu_penjemputan;

	public String getWaktu_penjemputan() {
		return waktu_penjemputan;
	}

	public void setWaktu_penjemputan(String waktu_penjemputan) {
		this.waktu_penjemputan = waktu_penjemputan;
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

	public void setTotalHarga(String totalHarga){
		this.totalHarga = totalHarga;
	}

	public String getTotalHarga(){
		return totalHarga;
	}

	public void setStatusPesanan(String statusPesanan){
		this.statusPesanan = statusPesanan;
	}

	public String getStatusPesanan(){
		return statusPesanan;
	}

	public void setWaktuAntar(String waktuAntar){
		this.waktuAntar = waktuAntar;
	}

	public String getWaktuAntar(){
		return waktuAntar;
	}
}