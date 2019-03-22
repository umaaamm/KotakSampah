package com.example.umaaamm.kotaksampah;

import java.io.Serializable;



public class Request implements Serializable {
    private String nama;
    private String email;
    private String username;
    private String password;
    private String alamat;
    private String uid;

    public Request(String nama, String email,String alamat,String uid) {
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public String toString(){
        return " "+nama+"\n "+
                " "+email+"\n"+
                " "+uid+"\n"+
                " "+alamat;
    }
}
