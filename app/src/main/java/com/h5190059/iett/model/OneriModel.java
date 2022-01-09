package com.h5190059.iett.model;

public class OneriModel {
    private String oneriBaslik, oneriAciklama, oneriDurum, oneriTarih, sicil, userId, oneriAdet;

    public OneriModel(){

    }

    public OneriModel(String oneriBaslik, String oneriAciklama, String oneriDurum, String oneriTarih, String sicil, String userId, String oneriAdet) {
        this.oneriBaslik = oneriBaslik;
        this.oneriAciklama = oneriAciklama;
        this.oneriDurum = oneriDurum;
        this.oneriTarih = oneriTarih;
        this.sicil = sicil;
        this.userId = userId;
        this.oneriAdet = oneriAdet;
    }

    public String getOneriBaslik() {
        return oneriBaslik;
    }

    public void setOneriBaslik(String oneriBaslik) {
        this.oneriBaslik = oneriBaslik;
    }

    public String getOneriAciklama() {
        return oneriAciklama;
    }

    public void setOneriAciklama(String oneriAciklama) {
        this.oneriAciklama = oneriAciklama;
    }

    public String getOneriDurum() {
        return oneriDurum;
    }

    public void setOneriDurum(String oneriDurum) {
        this.oneriDurum = oneriDurum;
    }

    public String getOneriTarih() {
        return oneriTarih;
    }

    public void setOneriTarih(String oneriTarih) {
        this.oneriTarih = oneriTarih;
    }

    public String getSicil() {
        return sicil;
    }

    public void setSicil(String sicil) {
        this.sicil = sicil;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOneriAdet() {
        return oneriAdet;
    }

    public void setOneriAdet(String oneriAdet) {
        this.oneriAdet = oneriAdet;
    }
}
