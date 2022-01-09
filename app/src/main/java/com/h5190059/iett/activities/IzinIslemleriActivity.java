package com.h5190059.iett.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

public class IzinIslemleriActivity extends AppCompatActivity {

    Button btnIzinSilme, btnIzinOlusturVeyaDuzenleLeftIcon, btnIzinSorgulama, btnIzinOlusturVeyaDuzenle, btnIzinOlusturma, btnIzinDuzenleme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_islemleri);
        init();

    }

    private void init() {
        btnIzinOlusturVeyaDuzenleLeftIcon = findViewById(R.id.btnIzinOlusturVeyaDuzenleLeftIcon);
        btnIzinOlusturVeyaDuzenle = findViewById(R.id.btnIzinOlusturVeyaDuzenle);
        btnIzinOlusturma = findViewById(R.id.btnIzinOlusturma);
        btnIzinSorgulama = findViewById(R.id.btnIzinSorgulama);
        btnIzinDuzenleme = findViewById(R.id.btnIzinDuzenleme);
        btnIzinSilme = findViewById(R.id.btnIzinSilme);

        btnIzinOlusturVeyaDuzenleLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(IzinIslemleriActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnIzinOlusturma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent izinOlusturIntent = new Intent(IzinIslemleriActivity.this, IzinOlusturActivity.class);
                startActivity(izinOlusturIntent);
                finish();
            }
        });

        btnIzinDuzenleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent izinDuzenlemeIntent = new Intent(IzinIslemleriActivity.this, IzinDuzenleActivity.class);
                izinDuzenlemeIntent.putExtra(Constants.FIREBASE_KULLANICI_SICIL, Constants.BOS_KONTROL);
                izinDuzenlemeIntent.putExtra(Constants.FIREBASE_IZINLER_IZIN_TARIH, Constants.BOS_KONTROL);

                startActivity(izinDuzenlemeIntent);
                finish();
            }
        });


        btnIzinSilme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent izinSilmeIntent = new Intent(IzinIslemleriActivity.this, IzinSilActivity.class);
                startActivity(izinSilmeIntent);
                finish();
            }
        });


        btnIzinSorgulama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent izinSorgulamaIntent = new Intent(IzinIslemleriActivity.this, IzinSorgulamaActivity.class);
                startActivity(izinSorgulamaIntent);
                finish();
            }
        });

    }
}