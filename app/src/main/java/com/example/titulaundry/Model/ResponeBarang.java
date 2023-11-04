package com.example.titulaundry.Model;

import com.example.titulaundry.ModelMySQL.DataBarang;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponeBarang {

    private int Kode;
    private String Pesan;
    private List<DataBarang> Data;

    public int getKode() {
        return Kode;
    }

    public void setKode(int kode) {
        this.Kode = kode;
    }

    public String getPesan() {
        return Pesan;
    }

    public void setPesan(String pesan) {
        this.Pesan = pesan;
    }

    public List<DataBarang> getData() {
        return Data;
    }

    public void setData(List<DataBarang> data) {
        this.Data = data;
    }
}
