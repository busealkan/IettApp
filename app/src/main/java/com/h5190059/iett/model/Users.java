package com.h5190059.iett.model;

public class Users {

    private String ad;
    private String dogumTarihi;
    private String durumu;
    private String email;
    private String gorevi;
    private String ilkGiris;
    private String kanGrubu;
    private String resim;
    private String sicil;
    private String sifre;
    private String soyad;

    public Users(){

    }

    public Users(String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad) {
        this.ad = ad;
        this.dogumTarihi = dogumTarihi;
        this.durumu = durumu;
        this.email = email;
        this.gorevi = gorevi;
        this.ilkGiris = ilkGiris;
        this.kanGrubu = kanGrubu;
        this.resim = resim;
        this.sicil = sicil;
        this.sifre = sifre;
        this.soyad = soyad;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public String getDurumu() {
        return durumu;
    }

    public void setDurumu(String durumu) {
        this.durumu = durumu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGorevi() {
        return gorevi;
    }

    public void setGorevi(String gorevi) {
        this.gorevi = gorevi;
    }

    public String getIlkGiris() {
        return ilkGiris;
    }

    public void setIlkGiris(String ilkGiris) {
        this.ilkGiris = ilkGiris;
    }

    public String getKanGrubu() {
        return kanGrubu;
    }

    public void setKanGrubu(String kanGrubu) {
        this.kanGrubu = kanGrubu;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getSicil() {
        return sicil;
    }

    public void setSicil(String sicil) {
        this.sicil = sicil;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }
}