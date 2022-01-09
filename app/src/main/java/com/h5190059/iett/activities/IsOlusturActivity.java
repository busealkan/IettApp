package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class IsOlusturActivity extends AppCompatActivity {

    private Button btnIsOlusturBaslikIcon, btnIsOlusturBaslik, btnIsOlustur, btnIsOlusturTarih;
    private EditText txtIsOlusturSicil, txtIsOlusturBolge, txtIsOlusturSaatBaslangici, txtIsOlusturSaatBitisi;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private ConstraintLayout constraintLayout;
    private ArrayList<Users> users;
    private boolean sicilVarMi;
    private String isTarihi;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_olustur);
        init();
    }

    private void init() {
        btnIsOlusturBaslikIcon = findViewById(R.id.btnIsOlusturBaslikIcon);
        btnIsOlusturBaslik = findViewById(R.id.btnIsOlusturBaslik);
        btnIsOlustur = findViewById(R.id.btnIsOlustur);
        txtIsOlusturSicil = findViewById(R.id.txtIsOlusturSicil);
        txtIsOlusturBolge = findViewById(R.id.txtIsOlusturBolge);
        btnIsOlusturTarih = findViewById(R.id.btnIsOlusturTarih);
        txtIsOlusturSaatBaslangici = findViewById(R.id.txtIsOlusturSaatBaslangici);
        txtIsOlusturSaatBitisi = findViewById(R.id.txtIsOlusturSaatBitisi);
        constraintLayout = findViewById(R.id.isOlusturLayout);

        i = 0;
        sicilVarMi = false;
        users = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();



        btnIsOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt();
            }
        });

        btnIsOlusturBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isOlusturVeyaDuzenleIntent = new Intent(IsOlusturActivity.this, IsIslemleriActivity.class);
                startActivity(isOlusturVeyaDuzenleIntent);
                finish();
            }
        });

        btnIsOlusturTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(IsOlusturActivity.this, R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String isTarihi = btnIsOlusturTarih.getText().toString();
                                if (isTarihi.equals(R.string.isTarihiSec)) {
                                    Toast.makeText(IsOlusturActivity.this, R.string.isTarihiSec, Toast.LENGTH_SHORT).show();
                                }
                                btnIsOlusturTarih.setText(day + "/" + (month + 1) + "/" + year);

                                String btnIsTarih = btnIsOlusturTarih.getText().toString().trim();
                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date isTarih = tarihFormat.parse(btnIsTarih);
                                    btnIsOlusturTarih.setText(tarihFormat.format(isTarih));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    private void bosAlanVarMiKontrolEt() {
        String sicil = txtIsOlusturSicil.getText().toString().trim();
        String tarih = btnIsOlusturTarih.getText().toString().trim();
        String bolge = txtIsOlusturBolge.getText().toString().trim();
        String saatBaslangic = txtIsOlusturSaatBaslangici.getText().toString().trim();
        String saatBitis = txtIsOlusturSaatBitisi.getText().toString().trim();


        if (sicil.equals(Constants.BOS_KONTROL) || tarih.equals(getResources().getString(R.string.isTarihiSec)) || bolge.equals(Constants.BOS_KONTROL) || saatBaslangic.equals(Constants.BOS_KONTROL) || saatBitis.equals(Constants.BOS_KONTROL)) {
            Toast.makeText(getApplicationContext(), R.string.toastZorunluBosAlan, Toast.LENGTH_LONG).show();
        } else {
            sicilVarMi(sicil, bolge, tarih, saatBaslangic, saatBitis);
        }
    }

    private void sicilVarMi(String sicil, String bolge, String tarih, String saatBaslangic, String saatBitis) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Users user = postSnapshot.getValue(Users.class);
                    users.add(
                            new Users(
                                    //String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad
                                    user.getAd(),
                                    user.getDogumTarihi(),
                                    user.getDurumu(),
                                    user.getEmail(),
                                    user.getGorevi(),
                                    user.getIlkGiris(),
                                    user.getKanGrubu(),
                                    user.getResim(),
                                    user.getSicil(),
                                    user.getSifre(),
                                    user.getSoyad()
                            )
                    );
                }
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String userId = postSnapshot.getKey();
                            if (dataSnapshot.child(userId).child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString().equals(sicil)) {
                                if(bolge.contains(Constants.FIREBASE_IS_LISTESI_HAT_KOD)){
                                    hatKoduVarMi(sicil, bolge, tarih, saatBaslangic, saatBitis);
                                    break;
                                }else{
                                    izinVarMi(sicil, bolge, tarih, saatBaslangic, saatBitis);
                                    break;
                                }

                            } else {
                                if (i == users.size() - 1) {
                                    Toast.makeText(IsOlusturActivity.this, R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir, Toast.LENGTH_SHORT).show();
                                }
                            }
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void hatKoduVarMi(String sicil, String bolge, String tarih, String saatBaslangic, String saatBitis) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // /Araclar/C-1907
                int hatKodIndex = bolge.indexOf("-", 0);
                String hatKod = bolge.substring(hatKodIndex-1);

                if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD + hatKod)) {
                    aractaArizaVarMi(sicil, bolge, tarih, saatBaslangic, saatBitis,hatKod);

                } else {
                    Toast.makeText(IsOlusturActivity.this, R.string.toastHatKodunaSahipAracBulunamadi, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void aractaArizaVarMi(String sicil, String bolge, String tarih, String saatBaslangic, String saatBitis, String hatKod) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String aracArizaDurumu = dataSnapshot.child(Constants.FIREBASE_ARACLAR_ARIZA_DURUMU).getValue().toString();

                if(aracArizaDurumu.equals(Constants.FIREBASE_ARIZA_DURUMU_FALSE)){
                    izinVarMi(sicil, bolge, tarih, saatBaslangic, saatBitis);
                }
                else{
                    Toast.makeText(IsOlusturActivity.this, getResources().getString(R.string.toastHatKodunaAitAractArizaBulunmaktadır), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void izinVarMi(String sicil, String bolge, String tarih, String saatBaslangic, String saatBitis) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isTarihi = tarih;
                if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD + getIsTarihi(tarih).replace('/', '_'))) {
                    Snackbar snackbar = Snackbar.make(constraintLayout, getIsTarihi(isTarihi) + getResources().getString(R.string.snackbarTarihineAitIzinBulunmaktadir), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(getResources().getColor(R.color.logoRengi));
                    snackbar.show();
                } else {
                    isVarMi(sicil, bolge, tarih, saatBaslangic, saatBitis);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isVarMi(String sicil, String bolge, String tarih, String saatBaslangic, String saatBitis) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isTarihi = tarih;
                if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD + getIsTarihi(tarih).replace('/', '_'))) {
                    Snackbar snackbar = Snackbar.make(constraintLayout, getIsTarihi(isTarihi) + getResources().getString(R.string.snackbarTarihineAitIsListesiBulunmaktadir), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(getResources().getColor(R.color.logoRengi));
                    snackbar.setAction(R.string.isDuzenle, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            Intent isDuzenleIntent = new Intent(IsOlusturActivity.this, IsDuzenleActivity.class);
                            isDuzenleIntent.putExtra(Constants.FIREBASE_KULLANICI_SICIL, sicil);
                            isDuzenleIntent.putExtra(Constants.FIREBASE_IS_LISTESI_IS_TARIH, tarih.replace('/', '_'));
                            startActivity(isDuzenleIntent);
                            finish();
                        }
                    });
                    snackbar.show();
                } else {
                    isOlustur(sicil, bolge, tarih, saatBaslangic, saatBitis);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isOlustur(String sicil, String bolge, String tarih, String saatBaslangic, String saatBitis) {
        String isTarihChild = tarih.replace(Constants.FIREBASE_CHILD, Constants.FIREBASE_CHILD_ALT_CIZGI);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, String> isOlusturMap = new HashMap<>();
                isOlusturMap.put(Constants.FIREBASE_KULLANICI_SICIL, sicil);
                isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI, saatBaslangic);
                isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI, saatBitis);
                isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BOLGE, bolge);
                isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_IS_TARIH, tarih);

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(isTarihChild);
                mDatabase.setValue(isOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(IsOlusturActivity.this, R.string.toastIsOlusturmaBasarili, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(IsOlusturActivity.this, R.string.toastIsOlusturmaBasarisiz, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getIsTarihi(String isTarihi) {
        return isTarihi;
    }

    public void setIsTarihi(String isTarih) {
        this.isTarihi = isTarih;
    }
}