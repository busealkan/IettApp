package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ArizaOlusturActivity extends AppCompatActivity {

    private Button btnArizaOlusturBaslikIcon, btnArizaOlusturBaslik, btnArizaOlustur;
    private EditText txtArizaOlusturArizaBaslik, txtArizaOlusturArizaAciklama, txtArizaOlusturHatKod, txtArizaOlusturHatIstikamet;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabaseSicil, mDatabaseArac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariza_olustur);
        init();
    }

    private void init() {
        btnArizaOlusturBaslikIcon = findViewById(R.id.btnArizaOlusturBaslikIcon);
        btnArizaOlusturBaslik = findViewById(R.id.btnArizaOlusturBaslik);
        btnArizaOlustur = findViewById(R.id.btnArizaOlustur);
        txtArizaOlusturHatKod = findViewById(R.id.txtArizaOlusturHatKod);
        txtArizaOlusturHatIstikamet = findViewById(R.id.txtArizaOlusturHatIstikamet);
        txtArizaOlusturArizaBaslik = findViewById(R.id.txtArizaOlusturArizaBaslik);
        txtArizaOlusturArizaAciklama = findViewById(R.id.txtArizaOlusturArizaAciklama);

        mAuth = FirebaseAuth.getInstance();

        btnArizaOlusturBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(ArizaOlusturActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnArizaOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt();
            }
        });

    }

    private void bosAlanVarMiKontrolEt() {
        String hatKod = txtArizaOlusturHatKod.getText().toString().trim().toUpperCase();
        String hatIstikamet = txtArizaOlusturHatIstikamet.getText().toString().trim();
        String arizaBaslik = txtArizaOlusturArizaBaslik.getText().toString().trim();
        String arizaAciklama = txtArizaOlusturArizaAciklama.getText().toString().trim();


        if (hatKod.equals(Constants.BOS_KONTROL) || hatIstikamet.equals(Constants.BOS_KONTROL) || arizaBaslik.equals(Constants.BOS_KONTROL) || arizaAciklama.equals(Constants.BOS_KONTROL)) {
            Toast.makeText(getApplicationContext(), R.string.toastZorunluBosAlan, Toast.LENGTH_LONG).show();
        } else {
            hatKoduVarMi(hatKod, hatIstikamet, arizaBaslik, arizaAciklama);
        }
    }

    private void hatKoduVarMi(String hatKod, String hatIstikamet, String arizaBaslik, String arizaAciklama) {
        mDatabaseArac = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR);
        mDatabaseArac.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // /Araclar/C-1907
                if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD + hatKod)) {
                    aracArizaDurumuGuncelle(hatKod, hatIstikamet, arizaBaslik, arizaAciklama);
                } else {
                    Toast.makeText(ArizaOlusturActivity.this, R.string.toastHatKodunaSahipAracBulunamadi, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void aracArizaDurumuGuncelle(String hatKod, String hatIstikamet, String arizaBaslik, String arizaAciklama) {
        Map aracArizaUpdate = new HashMap();
        aracArizaUpdate.put(Constants.FIREBASE_ARIZA_DURUMU, Constants.FIREBASE_ARIZA_DURUMU_TRUE);
        mDatabaseArac = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
        mDatabaseArac.updateChildren(aracArizaUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    arizaAdetKontrol(hatKod, hatIstikamet, arizaBaslik, arizaAciklama);
                }
            }
        });
    }

    private void arizaAdetKontrol(String hatKod, String hatIstikamet, String arizaBaslik, String arizaAciklama) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabaseSicil = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabaseSicil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sicil = dataSnapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int arizaAdet = 1;
                        ///Arizalar/CUbfJ1tbwMPxqElicEQbKWK7TQp2/ariza1

                        if (!(dataSnapshot.hasChild(Constants.FIREBASE_ARIZA_CHILD + arizaAdet))) {
                            arizaEkle(hatKod, hatIstikamet, arizaBaslik, arizaAciklama, arizaAdet, sicil);
                        } else {
                            while ((dataSnapshot.hasChild(Constants.FIREBASE_ARIZA_CHILD + arizaAdet))) {
                                arizaAdet = arizaAdet + 1;
                            }
                            arizaEkle(hatKod, hatIstikamet, arizaBaslik, arizaAciklama, arizaAdet, sicil);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void arizaEkle(String hatKod, String hatIstikamet, String arizaBaslik, String arizaAciklama, int arizaAdet, String sicil) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil).child(Constants.FIREBASE_ARIZA + arizaAdet);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> arizaOlusturMap = new HashMap<>();
                arizaOlusturMap.put(Constants.FIREBASE_KULLANICI_USER_ID, userId);
                arizaOlusturMap.put(Constants.FIREBASE_ARIZA_HAT_KODU, hatKod);
                arizaOlusturMap.put(Constants.FIREBASE_ARIZA_HAT_ISTIKAMET, hatIstikamet);
                arizaOlusturMap.put(Constants.FIREBASE_ARIZA_BASLIK, arizaBaslik);
                arizaOlusturMap.put(Constants.FIREBASE_ARIZA_ACIKLAMA, arizaAciklama);
                arizaOlusturMap.put(Constants.FIREBASE_ARIZA_DURUMU, Constants.FIREBASE_ARIZA_DURUMU_ONAY_BEKLENIYOR);

                SimpleDateFormat tarihFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_ZAMANLI);
                Calendar takvim = Calendar.getInstance();
                takvim.setTime(new Date());
                String arizaTarih = tarihFormat.format(takvim.getTime());

                arizaOlusturMap.put(Constants.FIREBASE_ARIZA_TARIH, arizaTarih);


                mDatabase.setValue(arizaOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.toastArizaOlusturmaBasarili, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.toastArizaOlusturmaBasarisiz, Toast.LENGTH_SHORT).show();
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