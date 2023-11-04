package com.example.titulaundry.ModelMySQL;

import com.google.gson.annotations.SerializedName;

public class DataInsert {

    @SerializedName("id_pesanan")
    private String id_pesanan;

    public String getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(String id_pesanan) {
        this.id_pesanan = id_pesanan;
    }
}
