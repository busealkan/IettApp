package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class RaporDetayActivity extends AppCompatActivity {

    Button btnRaporYazanSicilBaslik,btnRaporDetayBaslikIcon,btnRaporDetayBaslik,btnRaporDetaySicilBaslik,btnRaporDetayAdSoyadBaslik, btnRaporDetayTarihBaslik,btnRaporDetayBaslikBaslik,btnRaporDetayAciklamaBaslik,btnRaporDetayDurumuBaslik,btnRaporSonlandir,btnRaporIslemeAlindi;
    TextView txtRaporYazanSicil,txtRaporDetaySicil,txtRaporDetayAdSoyad,txtRaporDetayTarih,txtRaporDetayBaslik,txtRaporDetayAciklama,txtRaporDetayDurumu;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabaseArac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapor_detay);
        init();
    }

    private void init() {
        btnRaporDetayBaslikIcon = findViewById(R.id.btnRaporDetayBaslikIcon);
        btnRaporDetayBaslik = findViewById(R.id.btnRaporDetayBaslik);
        btnRaporDetaySicilBaslik = findViewById(R.id.btnRaporDetaySicilBaslik);
        btnRaporDetayAdSoyadBaslik = findViewById(R.id.btnRaporDetayAdSoyadBaslik);
        btnRaporDetayTarihBaslik = findViewById(R.id.btnRaporDetayTarihBaslik);
        btnRaporDetayBaslikBaslik = findViewById(R.id.btnRaporDetayBaslikBaslik);
        btnRaporDetayAciklamaBaslik = findViewById(R.id.btnRaporDetayAciklamaBaslik);
        btnRaporDetayDurumuBaslik = findViewById(R.id.btnRaporDetayDurumuBaslik);
        btnRaporSonlandir = findViewById(R.id.btnRaporSonlandir);
        btnRaporIslemeAlindi = findViewById(R.id.btnRaporIslemeAlindi);
        txtRaporDetaySicil = findViewById(R.id.txtRaporDetaySicil);
        txtRaporDetayAdSoyad = findViewById(R.id.txtRaporDetayAdSoyad);
        txtRaporDetayTarih = findViewById(R.id.txtRaporDetayTarih);
        txtRaporDetayBaslik = findViewById(R.id.txtRaporDetayBaslik);
        txtRaporDetayAciklama = findViewById(R.id.txtRaporDetayAciklama);
        txtRaporDetayDurumu = findViewById(R.id.txtRaporDetayDurumu);
        btnRaporYazanSicilBaslik = findViewById(R.id.btnRaporYazanSicilBaslik);
        txtRaporYazanSicil = findViewById(R.id.txtRaporYazanSicil);

        mAuth = FirebaseAuth.getInstance();




        btnRaporDetayBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent raporListeleIntent = new Intent(RaporDetayActivity.this, RaporListeleActivity.class);
                startActivity(raporListeleIntent);
                finish();
            }
        });

        btnRaporSonlandir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raporSonlandir();
            }
        });

        btnRaporIslemeAlindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               raporIslemeAlindi();
            }
        });

        gorevKontrol();
    }

    private void gorevKontrol() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                if (gorevi.toLowerCase().equals(Constants.GOREV_AMIR.toLowerCase())) {
                    btnRaporSonlandir.setVisibility(View.INVISIBLE);
                    btnRaporIslemeAlindi.setVisibility(View.INVISIBLE);
                    raporDetayBilgisiGetir();
                } else {
                    raporDetayBilgisiGetir();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void raporDetayBilgisiGetir() {

        String sicil = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SICIL);
        String raporSayi = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SAYI);
        String userId = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_USER_ID);


        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();

                txtRaporDetayAdSoyad.setText(ad+" "+soyad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR).child(sicil).child(Constants.FIREBASE_TUTANAK+raporSayi);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String raporAciklama = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_ACIKLAMA).getValue().toString();
                String raporBaslik = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_BASLIK).getValue().toString();
                String raporDurum = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM).getValue().toString();
                String raporTarih = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_TARIH).getValue().toString();
                String raporYazilanSicil = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZILAN_SICIL).getValue().toString();
                String raporYazanSicil = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZAN_SICIL).getValue().toString();



                txtRaporDetaySicil.setText(raporYazilanSicil);
                txtRaporDetayTarih.setText(raporTarih);
                txtRaporDetayBaslik.setText(raporBaslik);
                txtRaporDetayAciklama.setText(raporAciklama);
                txtRaporYazanSicil.setText(raporYazanSicil);

                if(raporDurum.equals(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM_TRUE)){
                    txtRaporDetayDurumu.setText(R.string.raporIslemde);
                }
                else if(raporDurum.equals(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM_ONAY_BEKLENIYOR)){
                    txtRaporDetayDurumu.setText(R.string.raporOnayBekliyor);
                }
                else{
                    txtRaporDetayDurumu.setText(R.string.raporSonlandirildi);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void raporIslemeAlindi() {
        String sicil = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SICIL);
        String raporSayi = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SAYI);

        String tiklananRaporChild = Constants.FIREBASE_TUTANAK+raporSayi;

        Map soforArizaDurumUpdate = new HashMap();
        soforArizaDurumUpdate.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM,Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM_TRUE);


        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR).child(sicil).child(tiklananRaporChild);
        mDatabase.updateChildren(soforArizaDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    txtRaporDetayDurumu.setText(R.string.raporIslemde);
                }
            }
        });
    }



    private void raporSonlandir() {
        String sicil = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SICIL);
        String raporSayi = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SAYI);


        String tiklananRaporChild = Constants.FIREBASE_TUTANAK+raporSayi;

        Map soforArizaDurumUpdate = new HashMap();
        soforArizaDurumUpdate.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM,Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM_FALSE);


        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR).child(sicil).child(tiklananRaporChild);
        mDatabase.updateChildren(soforArizaDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    txtRaporDetayDurumu.setText(R.string.raporSonlandirildi);

                }
            }
        });

    }
}
