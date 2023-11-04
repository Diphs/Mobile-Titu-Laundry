package com.example.titulaundry.Model;

import com.google.gson.annotations.SerializedName;

public class DataPesananUser {

	@SerializedName("image")
	private String image;

	@SerializedName("nama")
	private String nama;

	@SerializedName("harga")
	private String harga;

	@SerializedName("jenis_jasa")
	private String jenisJasa;

	@SerializedName("alamat_penjemputan")
	private String alamatPenjemputan;

	@SerializedName("total_berat")
	private String totalBerat;

	@SerializedName("alamat_pengiriman")
	private String alamatPengiriman;

	@SerializedName("total_harga")
	private String totalHarga;

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("status_pesanan")
	private String statusPesanan;

	@SerializedName("waktu_antar")
	private String waktuAntar;

	@SerializedName("harga_diskon")
	private String harga_diskon;

	public String getHarga_diskon() {
		return harga_diskon;
	}

	public void setHarga_diskon(String harga_diskon) {
		this.harga_diskon = harga_diskon;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setHarga(String harga){
		this.harga = harga;
	}

	public String getHarga(){
		return harga;
	}

	public void setJenisJasa(String jenisJasa){
		this.jenisJasa = jenisJasa;
	}

	public String getJenisJasa(){
		return jenisJasa;
	}

	public void setAlamatPenjemputan(String alamatPenjemputan){
		this.alamatPenjemputan = alamatPenjemputan;
	}

	public String getAlamatPenjemputan(){
		return alamatPenjemputan;
	}

	public void setTotalBerat(String totalBerat){
		this.totalBerat = totalBerat;
	}

	public String getTotalBerat(){
		return totalBerat;
	}

	public void setAlamatPengiriman(String alamatPengiriman){
		this.alamatPengiriman = alamatPengiriman;
	}

	public String getAlamatPengiriman(){
		return alamatPengiriman;
	}

	public void setTotalHarga(String totalHarga){
		this.totalHarga = totalHarga;
	}

	public String getTotalHarga(){
		return totalHarga;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
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