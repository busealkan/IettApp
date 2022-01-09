package com.h5190059.iett.model;

public class AnadoluBolgesiDeparHatlariModel {
    String deparHatKod,deparHatKodBolgeleri,deparHatAdet;

    public AnadoluBolgesiDeparHatlariModel(){

    }

    public AnadoluBolgesiDeparHatlariModel(String deparHatKod, String deparHatKodBolgeleri,String deparHatAdet) {
        this.deparHatKod = deparHatKod;
        this.deparHatKodBolgeleri = deparHatKodBolgeleri;
        this.deparHatAdet = deparHatAdet;
    }

    public String getDeparHatKod() {
        return deparHatKod;
    }

    public void setDeparHatKod(String deparHatKod) {
        this.deparHatKod = deparHatKod;
    }

    public String getDeparHatKodBolgeleri() {
        return deparHatKodBolgeleri;
    }

    public void setDeparHatKodBolgeleri(String deparHatKodBolgeleri) {
        this.deparHatKodBolgeleri = deparHatKodBolgeleri;
    }

    public String getDeparHatAdet() {
        return deparHatAdet;
    }

    public void setDeparHatAdet(String deparHatAdet) {
        this.deparHatAdet = deparHatAdet;
    }
}
