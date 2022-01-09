package com.h5190059.iett.model;

public class KutuphaneDosyalarModel {
    String dosyaAdi,dosyaBaslik,dosyaUrl,dosyaAdet;

    public KutuphaneDosyalarModel(){

    }

    public KutuphaneDosyalarModel(String dosyaAdi, String dosyaAdet, String dosyaUrl, String dosyaBaslik) {
        this.dosyaAdi = dosyaAdi;
        this.dosyaAdet = dosyaAdet;
        this.dosyaUrl = dosyaUrl;
        this.dosyaBaslik = dosyaBaslik;


    }

    public String getDosyaAdi() {
        return dosyaAdi;
    }

    public void setDosyaAdi(String dosyaAdi) {
        this.dosyaAdi = dosyaAdi;
    }

    public String getDosyaAdet() {
        return dosyaAdet;
    }

    public void setDosyaAdet(String dosyaAdet) {
        this.dosyaAdet = dosyaAdet;
    }

    public String getDosyaUrl() {
        return dosyaUrl;
    }

    public void setDosyaUrl(String dosyaUrl) {
        this.dosyaUrl = dosyaUrl;
    }

    public String getDosyaBaslik() {
        return dosyaBaslik;
    }

    public void setDosyaBaslik(String dosyaBaslik) {
        this.dosyaBaslik = dosyaBaslik;
    }
}
