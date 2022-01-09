package com.h5190059.iett.model;

public class KazaModel {
    String  kazaTarihi, kazaTuru, kazaHasar,kazaSicil,kazaAdet;

    public KazaModel() {

    }

    public KazaModel(String kazaTarihi, String kazaTuru, String kazaHasar, String kazaSicil, String kazaAdet) {
        this.kazaTarihi = kazaTarihi;
        this.kazaTuru = kazaTuru;
        this.kazaHasar = kazaHasar;
        this.kazaSicil = kazaSicil;
        this.kazaAdet = kazaAdet;
    }

    public String getKazaTarihi() {
        return kazaTarihi;
    }

    public void setKazaTarihi(String kazaTarihi) {
        this.kazaTarihi = kazaTarihi;
    }

    public String getKazaTuru() {
        return kazaTuru;
    }

    public void setKazaTuru(String kazaTuru) {
        this.kazaTuru = kazaTuru;
    }

    public String getKazaSicil() {
        return kazaSicil;
    }

    public void setKazaSicil(String kazaSicil) {
        this.kazaSicil = kazaSicil;
    }

    public String getKazaAdet() {
        return kazaAdet;
    }

    public void setKazaAdet(String kazaAdet) {
        this.kazaAdet = kazaAdet;
    }

    public String getKazaHasar() {
        return kazaHasar;
    }

    public void setKazaHasar(String kazaHasar) {
        this.kazaHasar = kazaHasar;
    }
}
