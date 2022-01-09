package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IsSilActivity extends AppCompatActivity {

    private Button btnIsAra,btnIsSilTarih, btnIsSilBaslikIcon, btnIsSilBaslik, btnIsSilListeSicilBaslik, btnIsSilAdSoyadBaslik, btnIsSilFiiliGoreviBaslik, btnIsSilFiiliGorevYeriBaslik, btnIsSilTarihBaslik, btnIsSilSaatBaslik, btnIsSil;
    private TextView txtIsSilSicilArama, txtIsSilSicil, txtIsSilAdSoyad, txtIsSilFiiliGorevi, txtIsSilGorevYeri, txtIsSilTarih, txtIsSilSaat;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, isDatabase, izinDatabase, izinSilDatabase, isSilDatabase;
    private FirebaseUser mUser;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private ArrayList<Users> users;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_sil);
        init();
    }

    private void init() {
        btnIsSilBaslikIcon = findViewById(R.id.btnIsSilBaslikIcon);
        btnIsSilBaslik = findViewById(R.id.btnIsSilBaslik);
        btnIsSilTarih = findViewById(R.id.btnIsSilTarih);
        btnIsSilListeSicilBaslik = findViewById(R.id.btnIsSilListeSicilBaslik);
        btnIsSilAdSoyadBaslik = findViewById(R.id.btnIsSilAdSoyadBaslik);
        btnIsSilFiiliGoreviBaslik = findViewById(R.id.btnIsSilFiiliGoreviBaslik);
        btnIsSilFiiliGorevYeriBaslik = findViewById(R.id.btnIsSilFiiliGorevYeriBaslik);
        btnIsSilTarihBaslik = findViewById(R.id.btnIsSilTarihBaslik);
        btnIsSilSaatBaslik = findViewById(R.id.btnIsSilSaatBaslik);
        btnIsSil = findViewById(R.id.btnIsSil);
        btnIsAra = findViewById(R.id.btnIsSilAra);
        txtIsSilSicilArama = findViewById(R.id.txtIsSilSicilArama);
        txtIsSilSicil = findViewById(R.id.txtIsSilSicil);
        txtIsSilAdSoyad = findViewById(R.id.txtIsSilAdSoyad);
        txtIsSilFiiliGorevi = findViewById(R.id.txtIsSilFiiliGorevi);
        txtIsSilGorevYeri = findViewById(R.id.txtIsSilGorevYeri);
        txtIsSilTarih = findViewById(R.id.txtIsSilTarih);
        txtIsSilSaat = findViewById(R.id.txtIsSilSaat);

        mAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        i=0;

        btnIsSilBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anaSayfaIntent = new Intent(IsSilActivity.this, AnaSayfaActivity.class);
                startActivity(anaSayfaIntent);
                finish();
            }
        });

        btnIsSilTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IsSilActivity.this, R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinlerimTarih = btnIsSilTarih.getText().toString().trim();

                                if (izinlerimTarih.equals(R.string.isTarihiSec)) {
                                    Toast.makeText(IsSilActivity.this, R.string.izinBaslangicTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIsSilTarih.setText(day + "/" + (month + 1) + "/" + year);


                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String btnIsTarih = btnIsSilTarih.getText().toString().trim();

                                try {
                                    Date isTarih = tarihFormat.parse(btnIsTarih);
                                    btnIsSilTarih.setText(tarihFormat.format(isTarih));
                                    bilgileriGizle();

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }


        });

        btnIsSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSil();
            }
        });

        btnIsAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isAraTarih = btnIsSilTarih.getText().toString().trim();
                bosAlanVarMiKontrolEt(isAraTarih);
            }
        });


        bilgileriGizle();
    }

    private void bosAlanVarMiKontrolEt(String silinecekIsTarih) {
        String sicil = txtIsSilSicilArama.getText().toString().trim();
        String tarih = btnIsSilTarih.getText().toString().trim();
        if (sicil.equals(getResources().getString(R.string.izinSilinecekSicilTools)) || sicil.equals(Constants.BOS_KONTROL) || tarih.equals(getResources().getString(R.string.isSilinecekTarihTools))) {
            Toast.makeText(IsSilActivity.this, R.string.toastBosAlan, Toast.LENGTH_SHORT).show();
        } else {
            sicilVarMi(sicil, silinecekIsTarih);
        }
    }

    private void sicilVarMi(String sicil, String silinecekIsTarih) {
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
                                    islerim(sicil, silinecekIsTarih, userId);
                                    break;
                                } else {
                                    if (i == users.size()-1) {
                                        Toast.makeText(IsSilActivity.this, getResources().getString(R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir), Toast.LENGTH_SHORT).show();
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
        btnIsSilListeSicilBaslik.setVisibility(View.INVISIBLE);
        btnIsSilAdSoyadBaslik.setVisibility(View.INVISIBLE);
        btnIsSilFiiliGoreviBaslik.setVisibility(View.INVISIBLE);
        btnIsSilFiiliGorevYeriBaslik.setVisibility(View.INVISIBLE);
        btnIsSilTarihBaslik.setVisibility(View.INVISIBLE);
        btnIsSilSaatBaslik.setVisibility(View.INVISIBLE);
        btnIsSilSaatBaslik.setVisibility(View.INVISIBLE);
        btnIsSil.setVisibility(View.INVISIBLE);


        txtIsSilSicil.setVisibility(View.INVISIBLE);
        txtIsSilAdSoyad.setVisibility(View.INVISIBLE);
        txtIsSilFiiliGorevi.setVisibility(View.INVISIBLE);
        txtIsSilGorevYeri.setVisibility(View.INVISIBLE);
        txtIsSilTarih.setVisibility(View.INVISIBLE);
        txtIsSilSaat.setVisibility(View.INVISIBLE);

    }

    private void bilgileriGoster() {
        btnIsSilListeSicilBaslik.setVisibility(View.VISIBLE);
        btnIsSilAdSoyadBaslik.setVisibility(View.VISIBLE);
        btnIsSilFiiliGoreviBaslik.setVisibility(View.VISIBLE);
        btnIsSilFiiliGorevYeriBaslik.setVisibility(View.VISIBLE);
        btnIsSilTarihBaslik.setVisibility(View.VISIBLE);
        btnIsSilSaatBaslik.setVisibility(View.VISIBLE);
        btnIsSil.setVisibility(View.VISIBLE);

        txtIsSilSicil.setVisibility(View.VISIBLE);
        txtIsSilAdSoyad.setVisibility(View.VISIBLE);
        txtIsSilFiiliGorevi.setVisibility(View.VISIBLE);
        txtIsSilGorevYeri.setVisibility(View.VISIBLE);
        txtIsSilTarih.setVisibility(View.VISIBLE);
        txtIsSilSaat.setVisibility(View.VISIBLE);
    }

    private void islerim(String sicil, String isTarih, String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adSoyad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString() + " " + snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String gorev = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                txtIsSilAdSoyad.setText(adSoyad);
                txtIsSilFiiliGorevi.setText(gorev);


                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(Constants.FIREBASE_CHILD + isTarih.replace('/', '_'))) {
                            izinDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
                            izinDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String dbBaslangicSaati = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI).getValue().toString();
                                    String dbBitisSaati = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI).getValue().toString();
                                    String dbBolge = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_BOLGE).getValue().toString();
                                    String dbIsTarih = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_IS_TARIH).getValue().toString();
                                    String dbSicil = snapshot.child(isTarih.replace('/', '_')).child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                                    txtIsSilSaat.setText(dbBaslangicSaati + " " + dbBitisSaati);
                                    txtIsSilGorevYeri.setText(dbBolge);
                                    txtIsSilTarih.setText(dbIsTarih);
                                    txtIsSilSicil.setText(dbSicil);

                                    bilgileriGoster();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            bilgileriGizle();
                            Toast.makeText(IsSilActivity.this, isTarih+getResources().getString(R.string.toastTariheAitIsBulunmamaktadir), Toast.LENGTH_SHORT).show();
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

    private void isSil() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        builder.setTitle(getResources().getString(R.string.alertIsTitle));
        builder.setMessage(getResources().getString(R.string.alertIsMessage));
        builder.setNegativeButton(getResources().getString(R.string.alertIsNegative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sicil = txtIsSilSicil.getText().toString().trim();
                String tarih = txtIsSilTarih.getText().toString().trim();

                isSilDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(tarih.replace('/', '_'));
                isSilDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(IsSilActivity.this, getResources().getString(R.string.toastIsSilmeBasarili), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(IsSilActivity.this, getResources().getString(R.string.toastIsSilmeBasarisiz), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.alertIzinPositive), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                });
        builder.show();


    }
}