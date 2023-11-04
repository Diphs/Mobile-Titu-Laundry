package com.example.titulaundry.ModelMySQL;

public class DataBarang {
    private int id_jasa,harga;
    private String jenis_jasa,deskripsi,durasi,image;

    public int getId_jasa() {
        return id_jasa;
    }

    public void setId_jasa(int id_jasa) {
        this.id_jasa = id_jasa;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getJenis_jasa() {
        return jenis_jasa;
    }

    public void setJenis_jasa(String jenis_jasa) {
        this.jenis_jasa = jenis_jasa;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDurrasi() {
        return durasi;
    }

    public void setDurrasi(String durrasi) {
        this.durasi = durrasi;
    }
}
