package com.h5190059.iett.model;

public class AcilTelefonlarModel {
    String acilTelefonBaslik,acilTelefonNumara;

    public  AcilTelefonlarModel(){

    }

    public AcilTelefonlarModel(String acilTelefonBaslik, String acilTelefonNumara) {
        this.acilTelefonBaslik = acilTelefonBaslik;
        this.acilTelefonNumara = acilTelefonNumara;
    }

    public String getAcilTelefonBaslik() {
        return acilTelefonBaslik;
    }

    public void setAcilTelefonBaslik(String acilTelefonBaslik) {
        this.acilTelefonBaslik = acilTelefonBaslik;
    }

    public String getAcilTelefonNumara() {
        return acilTelefonNumara;
    }

    public void setAcilTelefonNumara(String acilTelefonNumara) {
        this.acilTelefonNumara = acilTelefonNumara;
    }
}
