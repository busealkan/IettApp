package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class OneriDetayActivity extends AppCompatActivity {

    Button btnOneriDetayBaslikIcon,btnOneriDetayBaslik,btnOneriDetaySicilBaslik,btnOneriDetayAdSoyadBaslik,btnOneriDetayBaslikBaslik,btnOneriDetayTarihBaslik, btnOneriDetayTalepBaslik,btnOneriDetayDurumuBaslik,btnOneriRed,btnOneriOnay;
    TextView txtOneriDetaySicil,txtOneriDetayAdSoyad,txtOneriDetayBaslik,txtOneriDetayTarih,txtOneriDetayOneri,txtOneriDetayDurumu;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneri_detay);
        init();
    }

    private void init() {
        btnOneriDetayBaslikIcon = findViewById(R.id.btnOneriDetayBaslikIcon);
        btnOneriDetayBaslik = findViewById(R.id.btnOneriDetayBaslik);
        btnOneriDetaySicilBaslik = findViewById(R.id.btnOneriDetaySicilBaslik);
        btnOneriDetayAdSoyadBaslik = findViewById(R.id.btnOneriDetayAdSoyadBaslik);
        btnOneriDetayBaslikBaslik = findViewById(R.id.btnOneriDetayBaslikBaslik);
        btnOneriDetayTarihBaslik = findViewById(R.id.btnOneriDetayTarihBaslik);
        btnOneriDetayTalepBaslik = findViewById(R.id.btnOneriDetayTalepBaslik);
        btnOneriDetayDurumuBaslik = findViewById(R.id.btnOneriDetayDurumuBaslik);
        btnOneriRed = findViewById(R.id.btnOneriRed);
        btnOneriOnay = findViewById(R.id.btnOneriOnay);
        txtOneriDetaySicil = findViewById(R.id.txtOneriDetaySicil);
        txtOneriDetayAdSoyad = findViewById(R.id.txtOneriDetayAdSoyad);
        txtOneriDetayBaslik = findViewById(R.id.txtOneriDetayBaslik);
        txtOneriDetayTarih = findViewById(R.id.txtOneriDetayTarih);
        txtOneriDetayOneri = findViewById(R.id.txtOneriDetayOneri);
        txtOneriDetayDurumu = findViewById(R.id.txtOneriDetayDurumu);

        mAuth = FirebaseAuth.getInstance();


        btnOneriDetayBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detayEkranindakiKullanici();
            }
        });

        btnOneriRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneriRed();
            }
        });

        btnOneriOnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneriOnay();
            }
        });


        gorevKontrol();
    }

    private void detayEkranindakiKullanici() {
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                if(!txtOneriDetaySicil.getText().toString().trim().equals(sicil)){
                    Intent oneriListeleIntent = new Intent(OneriDetayActivity.this, OneriListeleActivity.class);
                    startActivity(oneriListeleIntent);
                    finish();
                }
                else{
                    Intent oneriListeleIntent = new Intent(OneriDetayActivity.this, OnerilerimActivity.class);
                    startActivity(oneriListeleIntent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void gorevKontrol() {

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                if(!(gorevi.toLowerCase().equals(Constants.GOREV_SEF))){
                    btnOneriRed.setVisibility(View.INVISIBLE);
                    btnOneriOnay.setVisibility(View.INVISIBLE);
                    sefOneriDetayBilgileriniGetir(txtOneriDetaySicil,btnOneriDetayAdSoyadBaslik,txtOneriDetayTarih,txtOneriDetayBaslik,txtOneriDetayOneri,txtOneriDetayDurumu,userId);

                }
                else{
                    sefOneriDetayBilgileriniGetir(txtOneriDetaySicil,btnOneriDetayAdSoyadBaslik,txtOneriDetayTarih,txtOneriDetayBaslik,txtOneriDetayOneri,txtOneriDetayDurumu,userId);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sefOneriDetayBilgileriniGetir(TextView txtOneriDetaySicil, Button btnOneriDetayAdSoyadBaslik, TextView txtOneriDetayTarih, TextView txtOneriDetayBaslik, TextView txtOneriDetayOneri, TextView txtOneriDetayDurumu,String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();

                txtOneriDetayAdSoyad.setText(ad+" "+soyad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String tiklananOneriAdetChild = getIntent().getStringExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_SAYI);
        String tiklananOneriSicil=getIntent().getStringExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_SICIL);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ONERILER).child(tiklananOneriSicil).child(Constants.FIREBASE_ONERI+tiklananOneriAdetChild);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String oneriAciklama = snapshot.child(Constants.FIREBASE_ONERI_ACIKLAMA).getValue().toString();
                String oneriBaslik = snapshot.child(Constants.FIREBASE_ONERI_BASLIK).getValue().toString();
                String oneriDurum = snapshot.child(Constants.FIREBASE_ONERI_DURUM).getValue().toString();
                String oneriTarih = snapshot.child(Constants.FIREBASE_ONERI_TARIH).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();



                txtOneriDetaySicil.setText(sicil);
                txtOneriDetayTarih.setText(oneriTarih);
                txtOneriDetayBaslik.setText(oneriBaslik);
                txtOneriDetayOneri.setText(oneriAciklama);
                if(oneriDurum.equals(Constants.FIREBASE_ONERI_DURUMU_TRUE)){
                    txtOneriDetayDurumu.setText(getResources().getString(R.string.oneriOnaylandi));
                }
                else if(oneriDurum.equals(Constants.FIREBASE_ONERI_DURUM_ONAY_BEKLENIYOR)){
                    txtOneriDetayDurumu.setText(getResources().getString(R.string.onayBekleniyor));

                }
                else{
                    txtOneriDetayDurumu.setText(getResources().getString(R.string.oneriReddedildi));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void oneriRed() {
        String tiklananOneriAdetChild = getIntent().getStringExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_SAYI);
        String tiklananOneriSicil=getIntent().getStringExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_SICIL);

        Map oneriDurumUpdate = new HashMap();
        oneriDurumUpdate.put(Constants.FIREBASE_ONERI_DURUM,Constants.FIREBASE_ONERI_DURUMU_FALSE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ONERILER).child(tiklananOneriSicil).child(Constants.FIREBASE_ONERI+tiklananOneriAdetChild);
        mDatabase.updateChildren(oneriDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    txtOneriDetayDurumu.setText(getResources().getString(R.string.oneriReddedildi));

                }
            }
        });
    }

    private void oneriOnay() {
        String tiklananOneriAdetChild = getIntent().getStringExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_SAYI);
        String tiklananOneriSicil=getIntent().getStringExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_SICIL);

        Map oneriDurumUpdate = new HashMap();
        oneriDurumUpdate.put(Constants.FIREBASE_ONERI_DURUM,Constants.FIREBASE_ONERI_DURUMU_TRUE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ONERILER).child(tiklananOneriSicil).child(Constants.FIREBASE_ONERI+tiklananOneriAdetChild);
        mDatabase.updateChildren(oneriDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    txtOneriDetayDurumu.setText(getResources().getString(R.string.oneriOnaylandi));

                }
            }
        });
    }

}