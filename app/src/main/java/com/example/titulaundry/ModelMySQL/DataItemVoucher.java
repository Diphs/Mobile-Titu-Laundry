package com.example.titulaundry.ModelMySQL;

import com.google.gson.annotations.SerializedName;

public class DataItemVoucher {

	@SerializedName("slot_voucher")
	private String slotVoucher;

	@SerializedName("id_voucher")
	private String idVoucher;

	@SerializedName("potongan_harga")
	private String potonganHarga;

	@SerializedName("nama")
	private String namaVoucher;


	public String getNamaVoucher() {
		return namaVoucher;
	}

	public void setNamaVoucher(String namaVoucher) {
		this.namaVoucher = namaVoucher;
	}

	public void setSlotVoucher(String slotVoucher){
		this.slotVoucher = slotVoucher;
	}

	public String getSlotVoucher(){
		return slotVoucher;
	}

	public void setIdVoucher(String idVoucher){
		this.idVoucher = idVoucher;
	}

	public String getIdVoucher(){
		return idVoucher;
	}

	public void setPotonganHarga(String potonganHarga){
		this.potonganHarga = potonganHarga;
	}

	public String getPotonganHarga(){
		return potonganHarga;
	}
}