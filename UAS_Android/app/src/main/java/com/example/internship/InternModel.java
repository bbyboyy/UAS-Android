package com.example.internship;

public class InternModel {
    public String nama;
    public String divisi;

    public InternModel(String nama, String divisi){
        this.nama = nama;
        this.divisi = divisi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }
}
