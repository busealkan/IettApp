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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class IzinlerimActivity extends AppCompatActivity {

    Button btnIzinlerimBaslikIcon, btnIzinlerimBaslik, btnIzinlerimTarih,btnIzinlerimSicilBaslik,btnIzinlerimAdSoyadBaslik,btnIzinlerimIzinTurBaslik,btnIzinlerimIzinBaslangicTarihiBaslik,btnIzinlerimIzinBitisTarihiBaslik,btnIzinlerimIzinGunBaslik;
    TextView txtIzinlerimSicil, txtIzinlerimAdSoyad, txtIzinlerimIzinTuru, txtIzinlerimIzinBaslangicTarihi,txtIzinlerimIzinBitisTarihi,txtIzinlerimIzinGunSayisi;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, uDatabase,izinDatabase;
    private FirebaseUser mUser;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private String izinBaslangicTarihi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izinlerim);
        init();
    }

    private void init() {
        btnIzinlerimBaslikIcon = findViewById(R.id.btnIzinlerimBaslikIcon);
        btnIzinlerimBaslik = findViewById(R.id.btnIzinlerimBaslik);
        btnIzinlerimTarih = findViewById(R.id.btnIzinlerimTarih);

        btnIzinlerimSicilBaslik = findViewById(R.id.btnIzinlerimSicilBaslik);
        btnIzinlerimAdSoyadBaslik = findViewById(R.id.btnIzinlerimAdSoyadBaslik);
        btnIzinlerimIzinTurBaslik = findViewById(R.id.btnIzinlerimIzinTurBaslik);
        btnIzinlerimIzinBaslangicTarihiBaslik = findViewById(R.id.btnIzinlerimIzinBaslangicTarihiBaslik);
        btnIzinlerimIzinBitisTarihiBaslik = findViewById(R.id.btnIzinlerimIzinBitisTarihiBaslik);
        btnIzinlerimIzinGunBaslik = findViewById(R.id.btnIzinlerimIzinGunBaslik);

        txtIzinlerimSicil = findViewById(R.id.txtIzinlerimSicil);
        txtIzinlerimAdSoyad = findViewById(R.id.txtIzinlerimAdSoyad);
        txtIzinlerimIzinTuru = findViewById(R.id.txtIzinlerimIzinTuru);
        txtIzinlerimIzinBaslangicTarihi = findViewById(R.id.txtIzinlerimIzinBaslangicTarihi);
        txtIzinlerimIzinBitisTarihi = findViewById(R.id.txtIzinlerimIzinBitisTarihi);
        txtIzinlerimIzinGunSayisi = findViewById(R.id.txtIzinlerimIzinGunSayisi);

        bilgileriGizle();

        izinListeDetayGoruntule();

        mAuth = FirebaseAuth.getInstance();

        btnIzinlerimBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anaSayfaIntent = new Intent(IzinlerimActivity.this, AnaSayfaActivity.class);
                startActivity(anaSayfaIntent);
                finish();
            }
        });

        btnIzinlerimTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IzinlerimActivity.this,R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinlerimTarih = btnIzinlerimTarih.getText().toString().trim();

                                if(izinlerimTarih.equals(R.string.isTarihiSec)){
                                    Toast.makeText(IzinlerimActivity.this, R.string.izinBaslangicTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIzinlerimTarih.setText(day + "/" + (month+1) + "/" + year);


                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String izinTarih = btnIzinlerimTarih.getText().toString().trim();

                                try {
                                    Date izinBaslangic = tarihFormat.parse(izinTarih);
                                    btnIzinlerimTarih.setText(tarihFormat.format(izinBaslangic));

                                    String aramaIzinTarih = btnIzinlerimTarih.getText().toString().trim();

                                    izinFiltreliDetayGoruntule(aramaIzinTarih);

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
        btnIzinlerimSicilBaslik.setVisibility(View.INVISIBLE);
        btnIzinlerimAdSoyadBaslik.setVisibility(View.INVISIBLE);
        btnIzinlerimIzinTurBaslik.setVisibility(View.INVISIBLE);
        btnIzinlerimIzinBaslangicTarihiBaslik.setVisibility(View.INVISIBLE);
        btnIzinlerimIzinBitisTarihiBaslik.setVisibility(View.INVISIBLE);
        btnIzinlerimIzinGunBaslik.setVisibility(View.INVISIBLE);

        txtIzinlerimSicil.setVisibility(View.INVISIBLE);
        txtIzinlerimAdSoyad.setVisibility(View.INVISIBLE);
        txtIzinlerimIzinBaslangicTarihi.setVisibility(View.INVISIBLE);
        txtIzinlerimIzinBitisTarihi.setVisibility(View.INVISIBLE);
        txtIzinlerimIzinTuru.setVisibility(View.INVISIBLE);
        txtIzinlerimIzinGunSayisi.setVisibility(View.INVISIBLE);
    }

    private void bilgileriGoster() {
        btnIzinlerimSicilBaslik.setVisibility(View.VISIBLE);
        btnIzinlerimAdSoyadBaslik.setVisibility(View.VISIBLE);
        btnIzinlerimIzinTurBaslik.setVisibility(View.VISIBLE);
        btnIzinlerimIzinBaslangicTarihiBaslik.setVisibility(View.VISIBLE);
        btnIzinlerimIzinBitisTarihiBaslik.setVisibility(View.VISIBLE);
        btnIzinlerimIzinGunBaslik.setVisibility(View.VISIBLE);

        txtIzinlerimSicil.setVisibility(View.VISIBLE);
        txtIzinlerimAdSoyad.setVisibility(View.VISIBLE);
        txtIzinlerimIzinBaslangicTarihi.setVisibility(View.VISIBLE);
        txtIzinlerimIzinBitisTarihi.setVisibility(View.VISIBLE);
        txtIzinlerimIzinTuru.setVisibility(View.VISIBLE);
        txtIzinlerimIzinGunSayisi.setVisibility(View.VISIBLE);
    }

    private void izinFiltreliDetayGoruntule(String izinlerimTarih) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adSoyad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString()+" "+snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                txtIzinlerimAdSoyad.setText(adSoyad);
                txtIzinlerimSicil.setText(sicil);

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("/" + izinlerimTarih.replace('/','_'))){
                            String izinChild = izinlerimTarih.replace('/','_');

                            uDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(izinChild);
                            uDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String dbIzinTur =  snapshot.child(Constants.FIREBASE_IZINLER_IZIN_TUR).getValue().toString();
                                    String dbIzinBaslangicTarihi =  snapshot.child(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI).getValue().toString();
                                    String dbIzinBitisTarihi =  snapshot.child(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI).getValue().toString();
                                    String dbIzinGun =  snapshot.child(Constants.FIREBASE_IZINLER_IZIN_GUN).getValue().toString();

                                    txtIzinlerimIzinBaslangicTarihi.setText(dbIzinBaslangicTarihi);
                                    txtIzinlerimIzinBitisTarihi.setText(dbIzinBitisTarihi);
                                    txtIzinlerimIzinTuru.setText(dbIzinTur);
                                    txtIzinlerimIzinGunSayisi.setText(dbIzinGun);

                                    bilgileriGoster();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{
                            bilgileriGizle();
                            Toast.makeText(IzinlerimActivity.this, R.string.toastIzinBulunmamaktadir, Toast.LENGTH_SHORT).show();
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

    private void izinListeDetayGoruntule() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                txtIzinlerimSicil.setText(sicil);
                txtIzinlerimAdSoyad.setText(ad + " " + soyad);

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SimpleDateFormat bicim = new SimpleDateFormat("dd/MM/yyyy");
                        Date tarih = new Date();
                        String bugunTarih = bicim.format(tarih).replace('/', '_');
                        if (snapshot.hasChild("/" + sicil + "/" + bugunTarih)) {
                            String tarihChild = (bugunTarih.replace('/', '_'));
                            String dbIzinTur =  snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IZINLER_IZIN_TUR).getValue().toString();
                            String dbIzinBaslangicTarihi =  snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI).getValue().toString();
                            String dbIzinBitisTarihi =  snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI).getValue().toString();
                            String dbIzinGun =  snapshot.child(sicil).child(tarihChild).child(Constants.FIREBASE_IZINLER_IZIN_GUN).getValue().toString();

                            txtIzinlerimIzinBaslangicTarihi.setText(dbIzinBaslangicTarihi);
                            txtIzinlerimIzinBitisTarihi.setText(dbIzinBitisTarihi);
                            txtIzinlerimIzinTuru.setText(dbIzinTur);
                            txtIzinlerimIzinGunSayisi.setText(dbIzinGun);

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

    public String getIzinBaslangicTarihi(String izinBaslangicTarihi){
        return izinBaslangicTarihi;
    }

    public void setIzinBaslangicTarihi(String izinBaslangicTarih){
        this.izinBaslangicTarihi = izinBaslangicTarih;
    }
}