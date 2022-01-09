package com.h5190059.iett.model;

public class TalepModel {
    private String talepBaslik, talepAciklama, talepDurum, talepTarih, sicil, userId, talepAdet;

    public TalepModel(){

    }

    public TalepModel(String talepBaslik, String talepAciklama, String talepDurum, String talepTarih, String sicil, String userId, String talepAdet) {
        this.talepBaslik = talepBaslik;
        this.talepAciklama = talepAciklama;
        this.talepDurum = talepDurum;
        this.talepTarih = talepTarih;
        this.sicil = sicil;
        this.userId = userId;
        this.talepAdet = talepAdet;
    }

    public String getTalepBaslik() {
        return talepBaslik;
    }

    public void setTalepBaslik(String talepBaslik) {
        this.talepBaslik = talepBaslik;
    }

    public String getTalepAciklama() {
        return talepAciklama;
    }

    public void setTalepAciklama(String talepAciklama) {
        this.talepAciklama = talepAciklama;
    }

    public String getTalepDurum() {
        return talepDurum;
    }

    public void setTalepDurum(String talepDurum) {
        this.talepDurum = talepDurum;
    }

    public String getTalepTarih() {
        return talepTarih;
    }

    public void setTalepTarih(String talepTarih) {
        this.talepTarih = talepTarih;
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

    public String getTalepAdet() {
        return talepAdet;
    }

    public void setTalepAdet(String talepAdet) {
        this.talepAdet = talepAdet;
    }
}
