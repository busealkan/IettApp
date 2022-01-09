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

public class TalepDetayActivity extends AppCompatActivity {

    Button btnTalepDetayBaslikIcon,btnTalepDetayBaslik,btnTalepDetaySicilBaslik,btnTalepDetayAdSoyadBaslik,btnTalepDetayBaslikBaslik,btnTalepDetayTarihBaslik, btnTalepDetayTalepBaslik,btnTalepDetayDurumBaslik,btnTalepRed,btnTalepOnay;
    TextView txtTalepDetaySicil,txtTalepDetayAdSoyad,txtTalepDetayBaslik,txtTalepDetayTarih,txtTalepDetayTalep,txtTalepDetayDurumu;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_detay);
        init();
    }

    private void init() {
        btnTalepDetayBaslikIcon = findViewById(R.id.btnTalepDetayBaslikIcon);
        btnTalepDetayBaslik = findViewById(R.id.btnTalepDetayBaslik);
        btnTalepDetaySicilBaslik = findViewById(R.id.btnTalepDetaySicilBaslik);
        btnTalepDetayAdSoyadBaslik = findViewById(R.id.btnTalepDetayAdSoyadBaslik);
        btnTalepDetayBaslikBaslik = findViewById(R.id.btnTalepDetayBaslikBaslik);
        btnTalepDetayTarihBaslik = findViewById(R.id.btnTalepDetayTarihBaslik);
        btnTalepDetayTalepBaslik = findViewById(R.id.btnTalepDetayTalepBaslik);
        btnTalepDetayDurumBaslik = findViewById(R.id.btnTalepDetayDurumBaslik);
        btnTalepRed = findViewById(R.id.btnTalepRed);
        btnTalepOnay = findViewById(R.id.btnTalepOnay);
        txtTalepDetaySicil = findViewById(R.id.txtTalepDetaySicil);
        txtTalepDetayAdSoyad = findViewById(R.id.txtTalepDetayAdSoyad);
        txtTalepDetayBaslik = findViewById(R.id.txtTalepDetayBaslik);
        txtTalepDetayTarih = findViewById(R.id.txtTalepDetayTarih);
        txtTalepDetayTalep = findViewById(R.id.txtTalepDetayTalep);
        txtTalepDetayDurumu = findViewById(R.id.txtTalepDetayDurumu);

        mAuth = FirebaseAuth.getInstance();

        btnTalepDetayBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detayEkranindakiKullanici();

            }
        });

        btnTalepRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talepRed();
            }
        });

        btnTalepOnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talepOnay();
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
                if(!txtTalepDetaySicil.getText().toString().trim().equals(sicil)){
                    Intent oneriListeleIntent = new Intent(TalepDetayActivity.this, TalepListeleActivity.class);
                    startActivity(oneriListeleIntent);
                    finish();
                }
                else{
                    Intent oneriListeleIntent = new Intent(TalepDetayActivity.this, TaleplerimActivity.class);
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
                    btnTalepRed.setVisibility(View.INVISIBLE);
                    btnTalepOnay.setVisibility(View.INVISIBLE);
                    sefTalepDetayBilgileriniGetir(txtTalepDetaySicil,txtTalepDetayAdSoyad,txtTalepDetayTarih,txtTalepDetayBaslik,txtTalepDetayTalep,txtTalepDetayDurumu,userId);

                }
                else{
                    sefTalepDetayBilgileriniGetir(txtTalepDetaySicil,txtTalepDetayAdSoyad,txtTalepDetayTarih,txtTalepDetayBaslik,txtTalepDetayTalep,txtTalepDetayDurumu,userId);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void sefTalepDetayBilgileriniGetir(TextView txtTalepDetaySicil, TextView txtTalepDetayAdSoyad, TextView txtTalepDetayTarih, TextView txtTalepDetayBaslik, TextView txtTalepDetayTalep, TextView txtTalepDetayDurumu,String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();

                txtTalepDetayAdSoyad.setText(ad+" "+soyad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String tiklananTalepAdetChild = getIntent().getStringExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_SAYI);
        String tiklananTalepSicil=getIntent().getStringExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_SICIL);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TALEPLER).child(tiklananTalepSicil).child(Constants.FIREBASE_TALEP+tiklananTalepAdetChild);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String talepAciklama = snapshot.child(Constants.FIREBASE_TALEP_ACIKLAMA).getValue().toString();
                String talepBaslik = snapshot.child(Constants.FIREBASE_TALEP_BASLIK).getValue().toString();
                String talepDurum = snapshot.child(Constants.FIREBASE_TALEP_DURUM).getValue().toString();
                String talepTarih = snapshot.child(Constants.FIREBASE_TALEP_TARIH).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();



                txtTalepDetaySicil.setText(sicil);
                txtTalepDetayTarih.setText(talepTarih);
                txtTalepDetayBaslik.setText(talepBaslik);
                txtTalepDetayTalep.setText(talepAciklama);
                if(talepDurum.equals(Constants.FIREBASE_TALEP_DURUMU_TRUE)){
                    txtTalepDetayDurumu.setText(getResources().getString(R.string.talepOnaylandi));
                }
                else if(talepDurum.equals(Constants.FIREBASE_TALEP_DURUM_ONAY_BEKLENIYOR)){
                    txtTalepDetayDurumu.setText(getResources().getString(R.string.onayBekleniyor));

                }
                else{
                    txtTalepDetayDurumu.setText(getResources().getString(R.string.talepReddedildi));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void talepRed() {
        String tiklananTalepAdetChild = getIntent().getStringExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_SAYI);
        String tiklananTalepSicil=getIntent().getStringExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_SICIL);

        Map talepDurumUpdate = new HashMap();
        talepDurumUpdate.put(Constants.FIREBASE_TALEP_DURUM,Constants.FIREBASE_TALEP_DURUMU_FALSE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TALEPLER).child(tiklananTalepSicil).child(Constants.FIREBASE_TALEP+tiklananTalepAdetChild);
        mDatabase.updateChildren(talepDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    txtTalepDetayDurumu.setText(getResources().getString(R.string.talepReddedildi));

                }
            }
        });
    }


    private void talepOnay() {

        String tiklananTalepAdetChild = getIntent().getStringExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_SAYI);
        String tiklananTalepSicil=getIntent().getStringExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_SICIL);

        Map talepDurumUpdate = new HashMap();
        talepDurumUpdate.put(Constants.FIREBASE_TALEP_DURUM,Constants.FIREBASE_TALEP_DURUMU_TRUE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TALEPLER).child(tiklananTalepSicil).child(Constants.FIREBASE_TALEP+tiklananTalepAdetChild);
        mDatabase.updateChildren(talepDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    txtTalepDetayDurumu.setText(getResources().getString(R.string.talepOnaylandi));
                }
            }
        });
    }

}