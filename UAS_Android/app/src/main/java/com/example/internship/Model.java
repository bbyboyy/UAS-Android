package com.example.internship;

public class Model {
    Integer id;
    String namaProject, jumlahMember;

    public String getJumlahMember() {
        return jumlahMember;
    }

    public void setJumlahMember(String jumlahMember) {
        this.jumlahMember = jumlahMember;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaProject() {
        return namaProject;
    }

    public void setNamaProject(String namaProject) {
        this.namaProject = namaProject;
    }
}
