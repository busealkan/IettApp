package com.h5190059.iett.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

public class IsIslemleriActivity extends AppCompatActivity {

    Button btnIsListeIslemlerLeftIcon, btnIsListeIslemler,btnIsOlusturma,btnIsSorgulama,btnIsDuzenleme,btnIsSilme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_islemleri);
        init();
    }

    private void init() {
        btnIsListeIslemlerLeftIcon = findViewById(R.id.btnIsListeIslemlerLeftIcon);
        btnIsListeIslemler = findViewById(R.id.btnIsListeIslemler);
        btnIsOlusturma = findViewById(R.id.btnIsOlusturma);
        btnIsSorgulama = findViewById(R.id.btnIsSorgulama);
        btnIsDuzenleme = findViewById(R.id.btnIsDuzenleme);
        btnIsSilme = findViewById(R.id.btnIsSilme);

        btnIsListeIslemlerLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(IsIslemleriActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnIsOlusturma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isOlusturIntent = new Intent(IsIslemleriActivity.this, IsOlusturActivity.class);
                startActivity(isOlusturIntent);
                finish();
            }
        });

        btnIsSorgulama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isSorgulaIntent = new Intent(IsIslemleriActivity.this, IsSorgulamaActivity.class);
                startActivity(isSorgulaIntent);
                finish();
            }
        });


        btnIsDuzenleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isDuzenlemeIntent = new Intent(IsIslemleriActivity.this, IsDuzenleActivity.class);
                isDuzenlemeIntent.putExtra(Constants.FIREBASE_KULLANICI_SICIL, Constants.BOS_KONTROL);
                isDuzenlemeIntent.putExtra(Constants.FIREBASE_IS_LISTESI_IS_TARIH, Constants.BOS_KONTROL);
                startActivity(isDuzenlemeIntent);
                finish();
            }
        });


        btnIsSilme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isSilIntent = new Intent(IsIslemleriActivity.this, IsSilActivity.class);
                startActivity(isSilIntent);
                finish();
            }
        });

    }
}