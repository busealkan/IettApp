package com.h5190059.iett.model;

public class CezaModel {
    String cezaYeri, ceza, cezaTarihi, cezaSicil,cezaAdet;

    public CezaModel() {

    }

    public CezaModel(String cezaYeri, String ceza, String cezaTarihi, String cezaSicil,String cezaAdet) {
        this.cezaYeri = cezaYeri;
        this.ceza = ceza;
        this.cezaTarihi = cezaTarihi;
        this.cezaSicil = cezaSicil;
        this.cezaAdet = cezaAdet;
    }

    public String getCezaYeri() {
        return cezaYeri;
    }

    public void setCezaYeri(String cezaYeri) {
        this.cezaYeri = cezaYeri;
    }

    public String getCeza() {
        return ceza;
    }

    public void setCeza(String ceza) {
        this.ceza = ceza;
    }

    public String getCezaTarihi() {
        return cezaTarihi;
    }

    public void setCezaTarihi(String cezaTarihi) {
        this.cezaTarihi = cezaTarihi;
    }

    public String getCezaSicil() {
        return cezaSicil;
    }

    public void setCezaSicil(String cezaSicil) {
        this.cezaSicil = cezaSicil;
    }

    public String getCezaAdet() {
        return cezaAdet;
    }

    public void setCezaAdet(String cezaAdet) {
        this.cezaAdet = cezaAdet;
    }
}
