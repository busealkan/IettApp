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

public class IsSorgulamaActivity extends AppCompatActivity {

    private Button btnIsSorgulaBaslikIcon,btnIsSorgulaBaslik,btnIsSorgulaTarih,btnIsSorgulaAra,btnIsSorgulaListeSicilBaslik,btnIsSorgulaAdSoyadBaslik,
            btnIsSorgulaFiiliGoreviBaslik,btnIsSorgulaFiiliGorevYeriBaslik,btnIsSorgulaTarihBaslik,btnIsSorgulaSaatBaslik;
    private TextView txtIsSorgulaSicilArama,txtIsSorgulaSicil,txtIsSorgulaAdSoyad,txtIsSorgulaFiiliGorevi,txtIsSorgulaGorevYeri,txtIsSorgulaTarih,txtIsSorgulaSaat;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, isDatabase;
    private FirebaseUser mUser;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private ArrayList<Users> users;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_sorgulama);
        init();
    }

    private void init() {
        btnIsSorgulaBaslikIcon = findViewById(R.id.btnIsSorgulaBaslikIcon);
        btnIsSorgulaBaslik = findViewById(R.id.btnIsSorgulaBaslik);
        btnIsSorgulaTarih = findViewById(R.id.btnIsSorgulaTarih);
        btnIsSorgulaAra = findViewById(R.id.btnIsSorgulaAra);
        btnIsSorgulaListeSicilBaslik = findViewById(R.id.btnIsSorgulaListeSicilBaslik);
        btnIsSorgulaAdSoyadBaslik = findViewById(R.id.btnIsSorgulaAdSoyadBaslik);
        btnIsSorgulaFiiliGoreviBaslik = findViewById(R.id.btnIsSorgulaFiiliGoreviBaslik);
        btnIsSorgulaFiiliGorevYeriBaslik = findViewById(R.id.btnIsSorgulaFiiliGorevYeriBaslik);
        btnIsSorgulaTarihBaslik = findViewById(R.id.btnIsSorgulaTarihBaslik);
        btnIsSorgulaSaatBaslik = findViewById(R.id.btnIsSorgulaSaatBaslik);

        txtIsSorgulaSicilArama = findViewById(R.id.txtIsSorgulaSicilArama);
        txtIsSorgulaSicil = findViewById(R.id.txtIsSorgulaSicil);
        txtIsSorgulaAdSoyad = findViewById(R.id.txtIsSorgulaAdSoyad);
        txtIsSorgulaFiiliGorevi = findViewById(R.id.txtIsSorgulaFiiliGorevi);
        txtIsSorgulaGorevYeri = findViewById(R.id.txtIsSorgulaGorevYeri);
        txtIsSorgulaTarih = findViewById(R.id.txtIsSorgulaTarih);
        txtIsSorgulaSaat= findViewById(R.id.txtIsSorgulaSaat);

        mAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        i=0;

        btnIsSorgulaBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anaSayfaIntent = new Intent(IsSorgulamaActivity.this, AnaSayfaActivity.class);
                startActivity(anaSayfaIntent);
                finish();
            }
        });

        btnIsSorgulaTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IsSorgulamaActivity.this, R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinlerimTarih = btnIsSorgulaTarih.getText().toString().trim();

                                if (izinlerimTarih.equals(R.string.isTarihiSec)) {
                                    Toast.makeText(IsSorgulamaActivity.this, R.string.izinBaslangicTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIsSorgulaTarih.setText(day + "/" + (month + 1) + "/" + year);


                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String btnIsTarih = btnIsSorgulaTarih.getText().toString().trim();

                                try {
                                    Date isTarih = tarihFormat.parse(btnIsTarih);
                                    btnIsSorgulaTarih.setText(tarihFormat.format(isTarih));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }


        });


        btnIsSorgulaAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isAraTarih = btnIsSorgulaTarih.getText().toString().trim();
                bosAlanVarMiKontrolEt(isAraTarih);
            }
        });


        bilgileriGizle();
    }

    private void bosAlanVarMiKontrolEt(String izinAraTarih) {
        String sicil = txtIsSorgulaSicilArama.getText().toString().trim();
        String tarih = btnIsSorgulaTarih.getText().toString().trim();
        if (sicil.equals(getResources().getString(R.string.izinSilinecekSicilTools)) || sicil.equals(Constants.BOS_KONTROL) || tarih.equals(getResources().getString(R.string.toolsSorgulanacakIsTarihiniSeciniz))) {
            Toast.makeText(IsSorgulamaActivity.this, R.string.toastBosAlan, Toast.LENGTH_SHORT).show();
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
                                islerim(sicil, izinAraTarih, userId);
                                break;
                            } else {
                                if (i == users.size()-1) {
                                    Toast.makeText(IsSorgulamaActivity.this, getResources().getString(R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir), Toast.LENGTH_SHORT).show();
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

    private void bilgileriGizle() {
        btnIsSorgulaListeSicilBaslik.setVisibility(View.INVISIBLE);
        btnIsSorgulaAdSoyadBaslik.setVisibility(View.INVISIBLE);
        btnIsSorgulaFiiliGoreviBaslik.setVisibility(View.INVISIBLE);
        btnIsSorgulaFiiliGorevYeriBaslik.setVisibility(View.INVISIBLE);
        btnIsSorgulaTarihBaslik.setVisibility(View.INVISIBLE);
        btnIsSorgulaSaatBaslik.setVisibility(View.INVISIBLE);

        txtIsSorgulaSicil.setVisibility(View.INVISIBLE);
        txtIsSorgulaAdSoyad.setVisibility(View.INVISIBLE);
        txtIsSorgulaFiiliGorevi.setVisibility(View.INVISIBLE);
        txtIsSorgulaGorevYeri.setVisibility(View.INVISIBLE);
        txtIsSorgulaTarih.setVisibility(View.INVISIBLE);
        txtIsSorgulaSaat.setVisibility(View.INVISIBLE);
    }

    private void bilgileriGoster() {
        btnIsSorgulaListeSicilBaslik.setVisibility(View.VISIBLE);
        btnIsSorgulaAdSoyadBaslik.setVisibility(View.VISIBLE);
        btnIsSorgulaFiiliGoreviBaslik.setVisibility(View.VISIBLE);
        btnIsSorgulaFiiliGorevYeriBaslik.setVisibility(View.VISIBLE);

        btnIsSorgulaTarihBaslik.setVisibility(View.VISIBLE);
        btnIsSorgulaSaatBaslik.setVisibility(View.VISIBLE);
        txtIsSorgulaSicil.setVisibility(View.VISIBLE);
        txtIsSorgulaAdSoyad.setVisibility(View.VISIBLE);
        txtIsSorgulaFiiliGorevi.setVisibility(View.VISIBLE);
        txtIsSorgulaGorevYeri.setVisibility(View.VISIBLE);
        txtIsSorgulaTarih.setVisibility(View.VISIBLE);
        txtIsSorgulaSaat.setVisibility(View.VISIBLE);
    }

    private void islerim(String sicil, String isTarih, String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adSoyad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString() + " " + snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String gorev = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                txtIsSorgulaAdSoyad.setText(adSoyad);
                txtIsSorgulaFiiliGorevi.setText(gorev);


                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(Constants.FIREBASE_CHILD + isTarih.replace('/', '_'))) {
                            isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
                            isDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String dbBaslangicSaati = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI).getValue().toString();
                                    String dbBitisSaati = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI).getValue().toString();
                                    String dbBolge = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_BOLGE).getValue().toString();
                                    String dbIsTarih = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_IS_TARIH).getValue().toString();
                                    String dbSicil = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                                    txtIsSorgulaSaat.setText(dbBaslangicSaati + " " + dbBitisSaati);
                                    txtIsSorgulaGorevYeri.setText(dbBolge);
                                    txtIsSorgulaTarih.setText(dbIsTarih);
                                    txtIsSorgulaSicil.setText(dbSicil);

                                    bilgileriGoster();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            bilgileriGizle();
                            Toast.makeText(IsSorgulamaActivity.this, isTarih+getResources().getString(R.string.toastTariheAitIsBulunmamaktadir), Toast.LENGTH_SHORT).show();
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