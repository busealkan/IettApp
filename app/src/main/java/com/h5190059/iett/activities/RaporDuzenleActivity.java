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
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RaporDuzenleActivity extends AppCompatActivity {

    private Button btnRaporDuzenleBaslikIcon,btnRaporDuzenleBaslik,btnRaporDuzenle;
    private TextView txtRaporDuzenleSicil,txtRaporDuzenleRaporBaslik,txtRaporDuzenleRaporAciklama;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<Users> users;
    private boolean sicilVarMi=false;
    public int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapor_duzenle);
        init();
    }

    private void init() {
        btnRaporDuzenleBaslikIcon = findViewById(R.id.btnRaporDuzenleBaslikIcon);
        btnRaporDuzenleBaslik = findViewById(R.id.btnRaporDuzenleBaslik);
        btnRaporDuzenle = findViewById(R.id.btnRaporDuzenle);
        txtRaporDuzenleSicil = findViewById(R.id.txtRaporDuzenleSicil);
        txtRaporDuzenleRaporBaslik = findViewById(R.id.txtRaporDuzenleRaporBaslik);
        txtRaporDuzenleRaporAciklama = findViewById(R.id.txtRaporDuzenleRaporAciklama);

        mAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();

        btnRaporDuzenleBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent raporListeleIntent = new Intent(RaporDuzenleActivity.this, RaporListeleActivity.class);
                startActivity(raporListeleIntent);
                finish();
            }
        });

        btnRaporDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raporYazanSicilBilgisiGetir();


            }
        });
        raporBilgisiGetir();
    }

    private void raporBilgisiGetir() {

        String sicil = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SICIL);
        String raporSayi = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SAYI);
        String userId = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_USER_ID);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR).child(sicil).child(Constants.FIREBASE_TUTANAK+raporSayi);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String raporAciklama = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_ACIKLAMA).getValue().toString();
                String raporBaslik = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_BASLIK).getValue().toString();
                String raporYazilanSicil = snapshot.child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZILAN_SICIL).getValue().toString();

                txtRaporDuzenleSicil.setText(raporYazilanSicil);
                txtRaporDuzenleRaporBaslik.setText(raporBaslik);
                txtRaporDuzenleRaporAciklama.setText(raporAciklama);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void raporYazanSicilBilgisiGetir() {
        String raporYazanUserId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(raporYazanUserId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String raporYazanSicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                bosAlanVarMiKontrolEt(raporYazanUserId,raporYazanSicil);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void bosAlanVarMiKontrolEt(String raporYazanUserId,String raporYazanSicil ) {
        String raporYazilanSicil = txtRaporDuzenleSicil.getText().toString().trim();
        String raporBaslik = txtRaporDuzenleRaporBaslik.getText().toString().trim();
        String raporAciklama = txtRaporDuzenleRaporAciklama.getText().toString().trim();
        String raporAdet = getIntent().getExtras().getString(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SAYI);


        if(raporYazilanSicil.equals(Constants.BOS_KONTROL) || raporBaslik.equals(Constants.BOS_KONTROL) || raporAciklama.equals(Constants.BOS_KONTROL)){
            Toast.makeText(getApplicationContext(),R.string.toastZorunluBosAlan,Toast.LENGTH_LONG).show();
        }
        else{
            raporDuzenle(raporYazanSicil,raporYazilanSicil, raporBaslik, raporAciklama, raporAdet,raporYazanUserId);
        }
    }

    private void raporDuzenle(String raporYazanSicil,String raporYazilanSicil, String raporBaslik, String raporAciklama, String raporAdet, String raporYazanUserId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR).child(raporYazilanSicil).child(Constants.FIREBASE_TUTANAK+raporAdet);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map raporDuzenleMap = new HashMap();
                raporDuzenleMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_BASLIK,raporBaslik);
                raporDuzenleMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_ACIKLAMA,raporAciklama);
                raporDuzenleMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZAN_USER_ID,raporYazanUserId);
                raporDuzenleMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZAN_SICIL,raporYazanSicil);

                SimpleDateFormat tarihFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_ZAMANLI);
                Calendar takvim = Calendar.getInstance();
                takvim.setTime(new Date());
                takvim.add(Calendar.HOUR_OF_DAY, Constants.CALENDAR_HOUR_OF_DAY);
                String raporTarih = tarihFormat.format(takvim.getTime());
                raporDuzenleMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_TARIH,raporTarih);
                raporDuzenleMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM,Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM_ONAY_BEKLENIYOR);

                mDatabase.updateChildren(raporDuzenleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),R.string.toastRaporGuncellemeBasarili,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.toastRaporGuncellemeBasarisiz,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}