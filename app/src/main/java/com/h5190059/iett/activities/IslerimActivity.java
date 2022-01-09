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
import com.h5190059.iett.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IslerimActivity extends AppCompatActivity {

    Button btnIsListeSaatBaslik, btnIsListeBaslikIcon, btnIsListeBaslik, btnIsListeSicilBaslik, btnIsListeAdSoyadBaslik, btnIsListeFiiliGoreviBaslik, btnIsListeFiiliGorevYeriBaslik, btnTarihFiltre, btnIsListeTarihBaslik;
    TextView txtIsListeSaat, txtIsListeSicil, txtIsListeAdSoyad, txtIsListeFiiliGorevi, txtIsListeGorevYeri, txtIsListeTarih;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, uDatabase;
    private FirebaseUser mUser;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islerim);
        init();
    }

    private void init() {
        btnIsListeBaslikIcon = findViewById(R.id.btnIsListeBaslikIcon);
        btnIsListeBaslik = findViewById(R.id.btnIsListeBaslik);
        btnIsListeSicilBaslik = findViewById(R.id.btnIsSorgulaListeSicilBaslik);
        btnIsListeAdSoyadBaslik = findViewById(R.id.btnIsSorgulaAdSoyadBaslik);
        btnTarihFiltre = findViewById(R.id.btnTarihFiltre);
        btnIsListeFiiliGoreviBaslik = findViewById(R.id.btnIsSorgulaFiiliGoreviBaslik);
        btnIsListeFiiliGorevYeriBaslik = findViewById(R.id.btnIsSorgulaFiiliGorevYeriBaslik);
        btnIsListeTarihBaslik = findViewById(R.id.btnIsSorgulaTarihBaslik);
        btnIsListeSaatBaslik = findViewById(R.id.btnIsSorgulaSaatBaslik);

        txtIsListeSicil = findViewById(R.id.txtIsSorgulaSicil);
        txtIsListeAdSoyad = findViewById(R.id.txtIsSorgulaAdSoyad);
        txtIsListeFiiliGorevi = findViewById(R.id.txtIsSorgulaFiiliGorevi);
        txtIsListeGorevYeri = findViewById(R.id.txtIsSorgulaGorevYeri);
        txtIsListeTarih = findViewById(R.id.txtIsSorgulaTarih);
        txtIsListeSaat = findViewById(R.id.txtIsSorgulaSaat);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        bilgileriGizle();

        isListeDetayGoruntule();

        btnIsListeBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(IslerimActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnTarihFiltre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IslerimActivity.this, R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String isTarihi = btnTarihFiltre.getText().toString();
                                if (isTarihi.equals(R.string.isTarihiSec)) {
                                    Toast.makeText(IslerimActivity.this, R.string.isTarihiSec, Toast.LENGTH_SHORT).show();
                                }
                                btnTarihFiltre.setText(day + "/" + (month + 1) + "/" + year);
                                String tarihFiltre = btnTarihFiltre.getText().toString();


                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date isTarih = tarihFormat.parse(tarihFiltre);
                                    btnTarihFiltre.setText(tarihFormat.format(isTarih));
                                    String btnIsTarih = btnTarihFiltre.getText().toString().replace('/', '_');
                                    isListeFiltreliDetayGoruntule(btnIsTarih);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    private void bilgileriGizle() {
        btnIsListeSicilBaslik.setVisibility(View.INVISIBLE);
        btnIsListeAdSoyadBaslik.setVisibility(View.INVISIBLE);
        btnIsListeFiiliGoreviBaslik.setVisibility(View.INVISIBLE);
        btnIsListeFiiliGorevYeriBaslik.setVisibility(View.INVISIBLE);
        btnIsListeTarihBaslik.setVisibility(View.INVISIBLE);
        btnIsListeSaatBaslik.setVisibility(View.INVISIBLE);
        txtIsListeSicil.setVisibility(View.INVISIBLE);
        txtIsListeAdSoyad.setVisibility(View.INVISIBLE);
        txtIsListeSaat.setVisibility(View.INVISIBLE);
        txtIsListeFiiliGorevi.setVisibility(View.INVISIBLE);
        txtIsListeGorevYeri.setVisibility(View.INVISIBLE);
        txtIsListeTarih.setVisibility(View.INVISIBLE);
    }

    private void bilgileriGoster() {
        btnIsListeSicilBaslik.setVisibility(View.VISIBLE);
        btnIsListeAdSoyadBaslik.setVisibility(View.VISIBLE);
        btnIsListeFiiliGoreviBaslik.setVisibility(View.VISIBLE);
        btnIsListeFiiliGorevYeriBaslik.setVisibility(View.VISIBLE);
        btnIsListeTarihBaslik.setVisibility(View.VISIBLE);
        btnIsListeSaatBaslik.setVisibility(View.VISIBLE);
        txtIsListeSicil.setVisibility(View.VISIBLE);
        txtIsListeAdSoyad.setVisibility(View.VISIBLE);
        txtIsListeSaat.setVisibility(View.VISIBLE);
        txtIsListeFiiliGorevi.setVisibility(View.VISIBLE);
        txtIsListeGorevYeri.setVisibility(View.VISIBLE);
        txtIsListeTarih.setVisibility(View.VISIBLE);
    }

    private void isListeFiltreliDetayGoruntule(String tarihFiltre) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                txtIsListeAdSoyad.setText(ad + " " + soyad);
                txtIsListeFiiliGorevi.setText(gorevi);

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("/" + sicil + "/" + tarihFiltre)) {
                            String tarihChild = (tarihFiltre.replace('/', '_'));
                            String gorevYeri = snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IS_LISTESI_BOLGE).getValue().toString();
                            String isTarih = snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IS_LISTESI_IS_TARIH).getValue().toString();
                            String baslangicSaati = snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI).getValue().toString();
                            String bitisSaati = snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI).getValue().toString();

                            txtIsListeGorevYeri.setText(gorevYeri);
                            txtIsListeSicil.setText(sicil);
                            txtIsListeTarih.setText(isTarih);
                            txtIsListeSaat.setText(baslangicSaati + " - " + bitisSaati);
                            btnTarihFiltre.setText(isTarih);

                            bilgileriGoster();
                        } else {
                            Toast.makeText(IslerimActivity.this, R.string.toastIsListesiBulunmamaktadir, Toast.LENGTH_SHORT).show();
                            bilgileriGizle();
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

    private void isListeDetayGoruntule() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                txtIsListeAdSoyad.setText(ad + " " + soyad);
                txtIsListeFiiliGorevi.setText(gorevi);

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SimpleDateFormat bicim = new SimpleDateFormat("dd/MM/yyyy");
                        Date tarih = new Date();
                        String bugunTarih = bicim.format(tarih).replace('/', '_');
                        if (snapshot.hasChild("/" + sicil + "/" + bugunTarih)) {
                            String tarihChild = (bugunTarih.replace('/', '_'));
                            String gorevYeri = snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IS_LISTESI_BOLGE).getValue().toString();
                            String isTarih = snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IS_LISTESI_IS_TARIH).getValue().toString();
                            String baslangicSaati = snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI).getValue().toString();
                            String bitisSaati = snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI).getValue().toString();

                            txtIsListeGorevYeri.setText(gorevYeri);
                            txtIsListeSicil.setText(sicil);
                            txtIsListeTarih.setText(isTarih);
                            txtIsListeSaat.setText(baslangicSaati + " - " + bitisSaati);

                            bilgileriGoster();
                        } else {
                            bilgileriGizle();
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