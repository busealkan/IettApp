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

public class ArizaDetayActivity extends AppCompatActivity {

    private Button btnArizaDetayBaslikIcon, btnArizaDetayBaslik, btnArizaDetaySicilBaslik, btnArizaDetayAdSoyadBaslik, btnArizaDetayHatKoduBaslik, btnArizaDetayHatIstikametBaslik, btnArizaDetayTarihBaslik, btnArizaDetayBaslikBaslik, btnArizaDetayAciklamaBaslik, btnArizaDetayDurumuBaslik, btnArizaSonlandir, btnArizaIslemeAlindi;
    private TextView txtArizaDetaySicil, txtArizaDetayAdSoyad, txtArizaDetayHatKodu, txtArizaDetayHatIstikamet, txtArizaDetayTarih, txtArizaDetayBaslik, txtArizaDetayAciklama, txtArizaDetayDurumu;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabaseArac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariza_detay);
        init();
    }

    private void init() {
        btnArizaDetayBaslikIcon = findViewById(R.id.btnArizaDetayBaslikIcon);
        btnArizaDetayBaslik = findViewById(R.id.btnArizaDetayBaslik);
        btnArizaDetaySicilBaslik = findViewById(R.id.btnBordroAd);
        btnArizaDetayAdSoyadBaslik = findViewById(R.id.btnArizaDetayAdSoyadBaslik);
        btnArizaDetayHatKoduBaslik = findViewById(R.id.btnArizaDetayHatKoduBaslik);
        btnArizaDetayHatIstikametBaslik = findViewById(R.id.btnArizaDetayHatIstikametBaslik);
        btnArizaDetayTarihBaslik = findViewById(R.id.btnArizaDetayTarihBaslik);
        btnArizaDetayBaslikBaslik = findViewById(R.id.btnArizaDetayBaslikBaslik);
        btnArizaDetayAciklamaBaslik = findViewById(R.id.btnArizaDetayAciklamaBaslik);
        btnArizaDetayDurumuBaslik = findViewById(R.id.btnArizaDetayDurumuBaslik);
        btnArizaSonlandir = findViewById(R.id.btnArizaSonlandir);
        btnArizaIslemeAlindi = findViewById(R.id.btnArizaIslemeAlindi);
        txtArizaDetaySicil = findViewById(R.id.txtBordroAd);
        txtArizaDetayAdSoyad = findViewById(R.id.txtArizaDetayAdSoyad);
        txtArizaDetayHatKodu = findViewById(R.id.txtArizaDetayHatKodu);
        txtArizaDetayHatIstikamet = findViewById(R.id.txtArizaDetayHatIstikamet);
        txtArizaDetayTarih = findViewById(R.id.txtArizaDetayTarih);
        txtArizaDetayBaslik = findViewById(R.id.txtArizaDetayBaslik);
        txtArizaDetayAciklama = findViewById(R.id.txtArizaDetayAciklama);
        txtArizaDetayDurumu = findViewById(R.id.txtArizaDetayDurumu);

        mAuth = FirebaseAuth.getInstance();

        String intentArizaSayi = getIntent().getExtras().getString(Constants.FIREBASE_ARIZALAR_TIKLANAN_ARIZA_SAYI);
        String intentUserId = getIntent().getExtras().getString(Constants.FIREBASE_ARIZALAR_TIKLANAN_ARIZA_USER_ID);

        btnArizaDetayBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent arizaListeleIntent = new Intent(ArizaDetayActivity.this, ArizaListeleActivity.class);
                startActivity(arizaListeleIntent);
                finish();
            }
        });

        btnArizaSonlandir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arizaSonlandir(intentArizaSayi, intentUserId);
            }
        });

        btnArizaIslemeAlindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arizaIslemeAlindi(intentArizaSayi, intentUserId);
            }
        });


        gorevKontrol(intentArizaSayi, intentUserId);
    }

    private void gorevKontrol(String intentArizaSayi, String intentUserId) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                if (gorevi.toLowerCase().equals(Constants.GOREV_SOFOR.toLowerCase())) {
                    btnArizaSonlandir.setVisibility(View.INVISIBLE);
                    btnArizaIslemeAlindi.setVisibility(View.INVISIBLE);
                    soforArizaDetayBilgileriniGetir(txtArizaDetaySicil, txtArizaDetayAdSoyad, txtArizaDetayHatKodu, txtArizaDetayHatIstikamet, txtArizaDetayTarih, txtArizaDetayBaslik, txtArizaDetayAciklama, txtArizaDetayDurumu, intentArizaSayi, intentUserId);
                } else {
                    btnArizaDetayBaslik.setText(R.string.arizalar);
                    bolgeYoneticisiArizaDetayBilgileriniGetir(txtArizaDetaySicil, txtArizaDetayAdSoyad, txtArizaDetayHatKodu, txtArizaDetayHatIstikamet, txtArizaDetayTarih, txtArizaDetayBaslik, txtArizaDetayAciklama, txtArizaDetayDurumu, intentArizaSayi, intentUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bolgeYoneticisiArizaDetayBilgileriniGetir(TextView txtArizaDetaySicil, TextView txtArizaDetayAdSoyad, TextView txtArizaDetayHatKodu, TextView txtArizaDetayHatIstikamet, TextView txtArizaDetayTarih, TextView txtArizaDetayBaslik, TextView txtArizaDetayAciklama, TextView txtArizaDetayDurumu, String intentArizaSayi, String intentUserId) {
        String arizaSayi = getIntent().getExtras().getString(Constants.FIREBASE_ARIZALAR_TIKLANAN_ARIZA_SAYI);
        String userId = getIntent().getExtras().getString(Constants.FIREBASE_ARIZALAR_TIKLANAN_ARIZA_USER_ID);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                txtArizaDetayAdSoyad.setText(ad + " " + soyad);
                txtArizaDetaySicil.setText(sicil);

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil).child(Constants.FIREBASE_ARIZA + arizaSayi);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String arizaAciklama = snapshot.child(Constants.FIREBASE_ARIZA_ACIKLAMA).getValue().toString();
                        String arizaBaslik = snapshot.child(Constants.FIREBASE_ARIZA_BASLIK).getValue().toString();
                        String arizaDurum = snapshot.child(Constants.FIREBASE_ARIZA_DURUMU).getValue().toString();
                        String arizaTarih = snapshot.child(Constants.FIREBASE_ARIZA_TARIH).getValue().toString();
                        String hatIstikamet = snapshot.child(Constants.FIREBASE_ARIZA_HAT_ISTIKAMET).getValue().toString();
                        String hatKodu = snapshot.child(Constants.FIREBASE_ARIZA_HAT_KODU).getValue().toString();

                        txtArizaDetayHatKodu.setText(hatKodu);
                        txtArizaDetayHatIstikamet.setText(hatIstikamet);
                        txtArizaDetayTarih.setText(arizaTarih);
                        txtArizaDetayBaslik.setText(arizaBaslik);
                        txtArizaDetayAciklama.setText(arizaAciklama);
                        if (arizaDurum.equals(Constants.FIREBASE_ARIZA_DURUMU_TRUE)) {
                            txtArizaDetayDurumu.setText(R.string.arizaDevamEdiyor);
                        } else if (arizaDurum.equals(Constants.FIREBASE_ARIZA_DURUMU_FALSE)) {
                            txtArizaDetayDurumu.setText(R.string.arizaSonlandirildi);
                        } else {
                            txtArizaDetayDurumu.setText(R.string.arizaOnayBekleniyor);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void soforArizaDetayBilgileriniGetir(TextView txtArizaDetaySicil, TextView txtArizaDetayAdSoyad, TextView txtArizaDetayHatKodu, TextView txtArizaDetayHatIstikamet, TextView txtArizaDetayTarih, TextView txtArizaDetayBaslik, TextView txtArizaDetayAciklama, TextView txtArizaDetayDurumu, String intentArizaSayi, String intentUserId) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                txtArizaDetayAdSoyad.setText(ad + " " + soyad);

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil).child(Constants.FIREBASE_ARIZA + intentArizaSayi);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        String arizaAciklama = snapshot.child(Constants.FIREBASE_ARIZA_ACIKLAMA).getValue().toString();
                        String arizaBaslik = snapshot.child(Constants.FIREBASE_ARIZA_BASLIK).getValue().toString();
                        String arizaDurum = snapshot.child(Constants.FIREBASE_ARIZA_DURUMU).getValue().toString();
                        String arizaTarih = snapshot.child(Constants.FIREBASE_ARIZA_TARIH).getValue().toString();
                        String hatIstikamet = snapshot.child(Constants.FIREBASE_ARIZA_HAT_ISTIKAMET).getValue().toString();
                        String hatKodu = snapshot.child(Constants.FIREBASE_ARIZA_HAT_KODU).getValue().toString();

                        txtArizaDetaySicil.setText(sicil);
                        txtArizaDetayHatKodu.setText(hatKodu);
                        txtArizaDetayHatIstikamet.setText(hatIstikamet);
                        txtArizaDetayTarih.setText(arizaTarih);
                        txtArizaDetayBaslik.setText(arizaBaslik);
                        txtArizaDetayAciklama.setText(arizaAciklama);
                        if (arizaDurum.equals(Constants.FIREBASE_ARIZA_DURUMU_TRUE)) {
                            txtArizaDetayDurumu.setText(R.string.arizaDevamEdiyor);
                        } else if (arizaDurum.equals(Constants.FIREBASE_ARIZA_DURUMU_FALSE)) {
                            txtArizaDetayDurumu.setText(R.string.arizaSonlandirildi);
                        } else {
                            txtArizaDetayDurumu.setText(R.string.arizaOnayBekleniyor);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void arizaIslemeAlindi(String intentArizaSayi, String intentUserId) {
        Map aracArizaDurumUpdate = new HashMap();
        aracArizaDurumUpdate.put(Constants.FIREBASE_ARIZA_DURUMU, Constants.FIREBASE_ARIZA_DURUMU_TRUE);
        String hatKod = txtArizaDetayHatKodu.getText().toString().trim();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(intentUserId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                mDatabaseArac = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
                mDatabaseArac.updateChildren(aracArizaDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {


                            Map soforArizaDurumUpdate = new HashMap();
                            soforArizaDurumUpdate.put(Constants.FIREBASE_ARIZA_DURUMU, Constants.FIREBASE_ARIZA_DURUMU_TRUE);


                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil).child(Constants.FIREBASE_ARIZA + intentArizaSayi);
                            mDatabase.updateChildren(soforArizaDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void arizaSonlandir(String intentArizaSayi, String intentUserId) {
        Map aracArizaDurumUpdate = new HashMap();
        aracArizaDurumUpdate.put(Constants.FIREBASE_ARIZA_DURUMU, Constants.FIREBASE_ARIZA_DURUMU_FALSE);
        String hatKod = txtArizaDetayHatKodu.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(intentUserId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                mDatabaseArac = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
                mDatabaseArac.updateChildren(aracArizaDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {


                            Map soforArizaDurumUpdate = new HashMap();
                            soforArizaDurumUpdate.put(Constants.FIREBASE_ARIZA_DURUMU, Constants.FIREBASE_ARIZA_DURUMU_FALSE);


                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil).child(Constants.FIREBASE_ARIZA + intentArizaSayi);
                            mDatabase.updateChildren(soforArizaDurumUpdate).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}