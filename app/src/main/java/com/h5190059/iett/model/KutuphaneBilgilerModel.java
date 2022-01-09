package com.h5190059.iett.model;

public class KutuphaneBilgilerModel {
    String bilgilerBaslik;

    public KutuphaneBilgilerModel(){

    }

    public KutuphaneBilgilerModel(String bilgilerBaslik) {
        this.bilgilerBaslik = bilgilerBaslik;
    }

    public String getBilgilerBaslik() {
        return bilgilerBaslik;
    }

    public void setBilgilerBaslik(String bilgilerBaslik) {
        this.bilgilerBaslik = bilgilerBaslik;
    }
}
