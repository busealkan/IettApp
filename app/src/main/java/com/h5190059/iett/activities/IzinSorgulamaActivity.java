package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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

public class IzinSorgulamaActivity extends AppCompatActivity {

    Button btnIzinSorgulaBaslikIcon,btnIzinSorgulaBaslik,btnIzinSorgulaTarih,btnIzinSorgulaAra,btnIzinSorgulaSicilBaslik,btnIzinSorgulaAdSoyadBaslik, btnIzinSorgulaIzinTurBaslik,btnIzinSorgulaIzinBaslangicTarihiBaslik,btnIzinSorgulaIzinBitisTarihiBaslik,btnIzinSorgulaIzinGunBaslik;
    TextView txtIzinSorgulaSicilArama,txtIzinSorgulaSicil,txtIzinSorgulaAdSoyad,txtIzinSorgulaIzinTuru,txtIzinSorgulaIzinBaslangicTarihi, txtIzinSorgulaIzinBitisTarihi,txtIzinSorgulaIzinGunSayisi;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, izinDatabase;
    private FirebaseUser mUser;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private ArrayList<Users> users;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_sorgulama);
        init();
    }

    private void init() {
        btnIzinSorgulaBaslikIcon = findViewById(R.id.btnIzinSorgulaBaslikIcon);
        btnIzinSorgulaBaslik = findViewById(R.id.btnIzinSorgulaBaslik);
        btnIzinSorgulaTarih = findViewById(R.id.btnIzinSorgulaTarih);
        btnIzinSorgulaAra = findViewById(R.id.btnIzinSorgulaAra);
        btnIzinSorgulaSicilBaslik = findViewById(R.id.btnIzinSorgulaSicilBaslik);
        btnIzinSorgulaAdSoyadBaslik = findViewById(R.id.btnIzinSorgulaAdSoyadBaslik);
        btnIzinSorgulaIzinTurBaslik = findViewById(R.id.btnIzinSorgulaIzinTurBaslik);
        btnIzinSorgulaIzinBaslangicTarihiBaslik = findViewById(R.id.btnIzinSorgulaIzinBaslangicTarihiBaslik);
        btnIzinSorgulaIzinBitisTarihiBaslik = findViewById(R.id.btnIzinSorgulaIzinBitisTarihiBaslik);
        btnIzinSorgulaIzinGunBaslik = findViewById(R.id.btnIzinSorgulaIzinGunBaslik);

        txtIzinSorgulaSicilArama = findViewById(R.id.txtIzinSorgulaSicilArama);
        txtIzinSorgulaSicil = findViewById(R.id.txtIzinSorgulaSicil);
        txtIzinSorgulaAdSoyad = findViewById(R.id.txtIzinSorgulaAdSoyad);
        txtIzinSorgulaIzinTuru = findViewById(R.id.txtIzinSorgulaIzinTuru);
        txtIzinSorgulaIzinBaslangicTarihi = findViewById(R.id.txtIzinSorgulaIzinBaslangicTarihi);
        txtIzinSorgulaIzinBitisTarihi = findViewById(R.id.txtIzinSorgulaIzinBitisTarihi);
        txtIzinSorgulaIzinGunSayisi= findViewById(R.id.txtIzinSorgulaIzinGunSayisi);

        mAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        i=0;

        btnIzinSorgulaBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anaSayfaIntent = new Intent(IzinSorgulamaActivity.this, AnaSayfaActivity.class);
                startActivity(anaSayfaIntent);
                finish();
            }
        });

        btnIzinSorgulaTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IzinSorgulamaActivity.this, R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinlerimTarih = btnIzinSorgulaTarih.getText().toString().trim();

                                if (izinlerimTarih.equals(R.string.isTarihiSec)) {
                                    Toast.makeText(IzinSorgulamaActivity.this, R.string.izinBaslangicTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIzinSorgulaTarih.setText(day + "/" + (month + 1) + "/" + year);


                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String btnIsTarih = btnIzinSorgulaTarih.getText().toString().trim();

                                try {
                                    Date isTarih = tarihFormat.parse(btnIsTarih);
                                    btnIzinSorgulaTarih.setText(tarihFormat.format(isTarih));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }


        });


        btnIzinSorgulaAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String izinAraTarih = btnIzinSorgulaTarih.getText().toString().trim();
                bosAlanVarMiKontrolEt(izinAraTarih);
            }
        });


        bilgileriGizle();
    }

    private void bilgileriGizle() {

        btnIzinSorgulaSicilBaslik.setVisibility(View.INVISIBLE);
        btnIzinSorgulaAdSoyadBaslik.setVisibility(View.INVISIBLE);
        btnIzinSorgulaIzinTurBaslik.setVisibility(View.INVISIBLE);
        btnIzinSorgulaIzinBaslangicTarihiBaslik.setVisibility(View.INVISIBLE);
        btnIzinSorgulaIzinBitisTarihiBaslik.setVisibility(View.INVISIBLE);
        btnIzinSorgulaIzinGunBaslik.setVisibility(View.INVISIBLE);

        txtIzinSorgulaSicil.setVisibility(View.INVISIBLE);
        txtIzinSorgulaAdSoyad.setVisibility(View.INVISIBLE);
        txtIzinSorgulaIzinTuru.setVisibility(View.INVISIBLE);
        txtIzinSorgulaIzinBaslangicTarihi.setVisibility(View.INVISIBLE);
        txtIzinSorgulaIzinBitisTarihi.setVisibility(View.INVISIBLE);
        txtIzinSorgulaIzinGunSayisi.setVisibility(View.INVISIBLE);
    }

    private void bilgileriGoster() {

        btnIzinSorgulaAra.setVisibility(View.VISIBLE);
        btnIzinSorgulaSicilBaslik.setVisibility(View.VISIBLE);
        btnIzinSorgulaAdSoyadBaslik.setVisibility(View.VISIBLE);
        btnIzinSorgulaIzinTurBaslik.setVisibility(View.VISIBLE);
        btnIzinSorgulaIzinBaslangicTarihiBaslik.setVisibility(View.VISIBLE);
        btnIzinSorgulaIzinBitisTarihiBaslik.setVisibility(View.VISIBLE);
        btnIzinSorgulaIzinGunBaslik.setVisibility(View.VISIBLE);

        txtIzinSorgulaSicil.setVisibility(View.VISIBLE);
        txtIzinSorgulaAdSoyad.setVisibility(View.VISIBLE);
        txtIzinSorgulaIzinTuru.setVisibility(View.VISIBLE);
        txtIzinSorgulaIzinBaslangicTarihi.setVisibility(View.VISIBLE);
        txtIzinSorgulaIzinBitisTarihi.setVisibility(View.VISIBLE);
        txtIzinSorgulaIzinGunSayisi.setVisibility(View.VISIBLE);
    }

    private void bosAlanVarMiKontrolEt(String izinAraTarih) {
        String sicil = txtIzinSorgulaSicilArama.getText().toString().trim();
        String izinTarih = btnIzinSorgulaTarih.getText().toString().trim();
        if (sicil.equals(getResources().getString(R.string.izinSilinecekSicilTools)) || sicil.equals(Constants.BOS_KONTROL) || izinTarih.equals(getResources().getString(R.string.toolsSorgulanacakIzinTarihiniSeciniz))) {
            Toast.makeText(IzinSorgulamaActivity.this, R.string.toastBosAlan, Toast.LENGTH_SHORT).show();
        } else {
            sicilVarMi(sicil, izinAraTarih);
        }
    }

    private void sicilVarMi(String sicil, String izinAraTarih) {
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
                                izinlerim(sicil, izinAraTarih, userId);
                                break;
                            } else {
                                if (i == users.size()-1) {
                                    Toast.makeText(IzinSorgulamaActivity.this, getResources().getString(R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir), Toast.LENGTH_SHORT).show();
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

    private void izinlerim(String sicil, String izinAraTarih, String userId) {
        String izintarih = izinAraTarih.replace('/','_');
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adSoyad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString() + " " + snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                txtIzinSorgulaAdSoyad.setText(adSoyad);
                txtIzinSorgulaSicil.setText(sicil);


                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(Constants.FIREBASE_CHILD + izintarih)) {
                            izinDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil);
                            izinDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String dbIzinTur =  snapshot.child(izintarih).child(Constants.FIREBASE_IZINLER_IZIN_TUR).getValue().toString();
                                    String dbIzinBaslangicTarihi =  snapshot.child(izintarih).child(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI).getValue().toString();
                                    String dbIzinBitisTarihi =  snapshot.child(izintarih).child(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI).getValue().toString();
                                    String dbIzinGun =  snapshot.child(izintarih).child(Constants.FIREBASE_IZINLER_IZIN_GUN).getValue().toString();

                                    txtIzinSorgulaIzinBaslangicTarihi.setText(dbIzinBaslangicTarihi);
                                    txtIzinSorgulaIzinBitisTarihi.setText(dbIzinBitisTarihi);
                                    txtIzinSorgulaIzinTuru.setText(dbIzinTur);
                                    txtIzinSorgulaIzinGunSayisi.setText(dbIzinGun);

                                    bilgileriGoster();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            bilgileriGizle();
                            Toast.makeText(IzinSorgulamaActivity.this, izinAraTarih+getResources().getString(R.string.toastTariheAitIzinBulunmamaktadir), Toast.LENGTH_SHORT).show();
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


}