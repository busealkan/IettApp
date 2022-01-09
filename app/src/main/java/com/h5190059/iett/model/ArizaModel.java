package com.h5190059.iett.model;


public class ArizaModel {
    private String arizaAciklama;
    private String arizaBaslik;
    private String arizaDurumu;
    private String arizaTarih;
    private String hatIstikamet;
    private String hatKodu;
    private String userId;
    private String arizaAdet;

    public ArizaModel(){

    }

    public ArizaModel(String arizaAciklama, String arizaBaslik, String arizaDurumu, String arizaTarih, String hatIstikamet, String hatKodu, String userId, String arizaAdet) {
        this.arizaAciklama = arizaAciklama;
        this.arizaBaslik = arizaBaslik;
        this.arizaDurumu = arizaDurumu;
        this.arizaTarih = arizaTarih;
        this.hatIstikamet = hatIstikamet;
        this.hatKodu = hatKodu;
        this.userId = userId;
        this.arizaAdet = arizaAdet;
    }

    public String getArizaAciklama() {
        return arizaAciklama;
    }

    public void setArizaAciklama(String arizaAciklama) {
        this.arizaAciklama = arizaAciklama;
    }

    public String getArizaBaslik() {
        return arizaBaslik;
    }

    public void setArizaBaslik(String arizaBaslik) {
        this.arizaBaslik = arizaBaslik;
    }

    public String getArizaDurumu() {
        return arizaDurumu;
    }

    public void setArizaDurumu(String arizaDurumu) {
        this.arizaDurumu = arizaDurumu;
    }

    public String getArizaTarih() {
        return arizaTarih;
    }

    public void setArizaTarih(String arizaTarih) {
        this.arizaTarih = arizaTarih;
    }

    public String getHatIstikamet() {
        return hatIstikamet;
    }

    public void setHatIstikamet(String hatIstikamet) {
        this.hatIstikamet = hatIstikamet;
    }

    public String getHatKodu() {
        return hatKodu;
    }

    public void setHatKodu(String hatKodu) {
        this.hatKodu = hatKodu;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArizaAdet() {
        return arizaAdet;
    }

    public void setArizaAdet(String arizaAdet) {
        this.arizaAdet = arizaAdet;
    }
}
