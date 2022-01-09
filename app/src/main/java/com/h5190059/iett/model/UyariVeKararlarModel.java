package com.h5190059.iett.model;

public class UyariVeKararlarModel {
    String  uyariVeKararTarihi, uyariVeKararAciklama, uyariVeKararDisiplin,uyariVeKararAdet,uyariVeKararSicil;

    public UyariVeKararlarModel() {

    }

    public UyariVeKararlarModel(String uyariVeKararTarihi, String uyariVeKararAciklama, String uyariVeKararDisiplin, String uyariVeKararAdet, String uyariVeKararSicil) {
        this.uyariVeKararTarihi = uyariVeKararTarihi;
        this.uyariVeKararAciklama = uyariVeKararAciklama;
        this.uyariVeKararDisiplin = uyariVeKararDisiplin;
        this.uyariVeKararAdet = uyariVeKararAdet;
        this.uyariVeKararSicil = uyariVeKararSicil;
    }

    public String getUyariVeKararTarihi() {
        return uyariVeKararTarihi;
    }

    public void setUyariVeKararTarihi(String uyariVeKararTarihi) {
        this.uyariVeKararTarihi = uyariVeKararTarihi;
    }

    public String getUyariVeKararAciklama() {
        return uyariVeKararAciklama;
    }

    public void setUyariVeKararAciklama(String uyariVeKararAciklama) {
        this.uyariVeKararAciklama = uyariVeKararAciklama;
    }

    public String getUyariVeKararDisiplin() {
        return uyariVeKararDisiplin;
    }

    public void setUyariVeKararDisiplin(String uyariVeKararDisiplin) {
        this.uyariVeKararDisiplin = uyariVeKararDisiplin;
    }

    public String getUyariVeKararAdet() {
        return uyariVeKararAdet;
    }

    public void setUyariVeKararAdet(String uyariVeKararAdet) {
        this.uyariVeKararAdet = uyariVeKararAdet;
    }

    public String getUyariVeKararSicil() {
        return uyariVeKararSicil;
    }

    public void setUyariVeKararSicil(String uyariVeKararSicil) {
        this.uyariVeKararSicil = uyariVeKararSicil;
    }
}
