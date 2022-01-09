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
import java.util.Map;

public class IsDuzenleActivity extends AppCompatActivity {

    private Button btnIsDuzenleBaslikIcon, btnIsDuzenleBaslik,btnIsDuzenle, btnIsDuzenleTarih ;
    private EditText txtIsDuzenleSicil, txtIsDuzenleBolge,txtIsDuzenleSaatBaslangici,txtIsDuzenleSaatBitisi;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, uDatabase;
    private FirebaseUser mUser;
    private ConstraintLayout constraintLayout;
    private ArrayList<Users> users;
    private int i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_duzenle);
        init();
    }

    private void init() {
        btnIsDuzenleBaslikIcon = findViewById(R.id.btnIsDuzenleBaslikIcon);
        btnIsDuzenleBaslik = findViewById(R.id.btnIsDuzenleBaslik);
        btnIsDuzenle = findViewById(R.id.btnIsDuzenle);
        txtIsDuzenleSicil = findViewById(R.id.txtIsDuzenleSicil);
        txtIsDuzenleBolge = findViewById(R.id.txtIsDuzenleBolge);
        btnIsDuzenleTarih = findViewById(R.id.btnIsDuzenleTarih);
        txtIsDuzenleSaatBaslangici = findViewById(R.id.txtIsDuzenleSaatBaslangici);
        txtIsDuzenleSaatBitisi = findViewById(R.id.txtIsDuzenleSaatBitisi);
        constraintLayout = findViewById(R.id.isDuzenleLayout);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        i=0;

        users = new ArrayList<>();
        String intentSicil = getIntent().getExtras().getString(Constants.FIREBASE_KULLANICI_SICIL);
        String intentIsTarih = getIntent().getExtras().getString(Constants.FIREBASE_IS_LISTESI_IS_TARIH);


        if(!(intentSicil.equals(Constants.BOS_KONTROL)) && !(intentIsTarih.equals(Constants.BOS_KONTROL))){
            txtIsDuzenleSicil.setText(intentSicil);
            btnIsDuzenleTarih.setText(intentIsTarih.replace(Constants.FIREBASE_CHILD_ALT_CIZGI,Constants.FIREBASE_CHILD));
            sicilIsBilgileriGetir(intentSicil);
        }
        else{
            txtIsDuzenleSicil.setHint(R.string.sicilButon);
            btnIsDuzenleTarih.setText(R.string.isTarihiSec);
        }

        btnIsDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt(intentSicil,intentIsTarih);
            }
        });

        btnIsDuzenleBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isOlusturVeyaDuzenleIntent = new Intent(IsDuzenleActivity.this, IsIslemleriActivity.class);
                startActivity(isOlusturVeyaDuzenleIntent);
                finish();
            }
        });

        btnIsDuzenleTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IsDuzenleActivity.this,R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String isTarihi = btnIsDuzenleTarih.getText().toString();
                                if(isTarihi.equals(R.string.isTarihiSec)){
                                    Toast.makeText(IsDuzenleActivity.this, R.string.isTarihiSec, Toast.LENGTH_SHORT).show();
                                }
                                btnIsDuzenleTarih.setText(day + "/" + (month+1) + "/" + year);
                                String btnIsTarih = btnIsDuzenleTarih.getText().toString().trim();
                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date isTarih = tarihFormat.parse(btnIsTarih);
                                    btnIsDuzenleTarih.setText(tarihFormat.format(isTarih));
                                }
                                catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    private void sicilIsBilgileriGetir(String sicil) {
        String isTarihi = getIntent().getExtras().getString(Constants.FIREBASE_IS_LISTESI_IS_TARIH).replace('/','_');
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(isTarihi);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bolge = snapshot.child(Constants.FIREBASE_IS_LISTESI_BOLGE).getValue().toString();
                String baslangicSaati = snapshot.child(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI).getValue().toString();
                String bitisSaati = snapshot.child(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI).getValue().toString();
                txtIsDuzenleBolge.setText(bolge);
                txtIsDuzenleSaatBaslangici.setText(baslangicSaati);
                txtIsDuzenleSaatBitisi.setText(bitisSaati);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bosAlanVarMiKontrolEt(String intentSicil,String intentIsTarih) {
        String sicil = txtIsDuzenleSicil.getText().toString().trim();
        String tarih = btnIsDuzenleTarih.getText().toString().trim();
        String bolge = txtIsDuzenleBolge.getText().toString().trim();
        String baslangicSaat = txtIsDuzenleSaatBaslangici.getText().toString().trim();
        String bitisSaat = txtIsDuzenleSaatBitisi.getText().toString().trim();

        if (sicil.equals(Constants.BOS_KONTROL) || tarih.equals(getResources().getString(R.string.isTarihiSec)) || bolge.equals(Constants.BOS_KONTROL) || baslangicSaat.equals(Constants.BOS_KONTROL) || bitisSaat.equals(Constants.BOS_KONTROL)) {
            Toast.makeText(IsDuzenleActivity.this, R.string.toastBosAlan, Toast.LENGTH_LONG).show();
        } else {
            if(intentIsTarih.replace('_','/').equals(tarih) && intentSicil.equals(sicil)){
                isDuzenle(sicil,tarih,bolge,baslangicSaat,bitisSaat,intentSicil,intentIsTarih);
            }
            else{
                sicilVarMi(sicil,tarih,bolge,baslangicSaat,bitisSaat,intentSicil,intentIsTarih);
            }
        }
    }

    private void sicilVarMi(String sicil, String tarih, String bolge, String baslangicSaat, String bitisSaat,String intentSicil,String intentIsTarih) {
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
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                String userId = postSnapshot.getKey();
                                if(dataSnapshot.child(userId).child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString().equals(sicil)){
                                    if(bolge.contains(Constants.FIREBASE_IS_LISTESI_HAT_KOD)){
                                        hatKoduVarMi(sicil, bolge, tarih, baslangicSaat, bitisSaat,intentSicil,intentIsTarih);
                                        break;
                                    }else{
                                        isVarMi(sicil, bolge, tarih, baslangicSaat, bitisSaat,intentSicil,intentIsTarih);
                                        break;
                                    }
                                }else {
                                    if (i == users.size()-1) {
                                        Toast.makeText(IsDuzenleActivity.this, getResources().getString(R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir), Toast.LENGTH_SHORT).show();
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

    private void hatKoduVarMi(String sicil, String bolge, String tarih, String saatBaslangic, String saatBitis,String intentSicil,String intentIsTarih) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // /Araclar/C-1907
                int hatKodIndex = bolge.indexOf("-", 0);
                String hatKod = bolge.substring(hatKodIndex-1);

                if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD + hatKod)) {
                    aractaArizaVarMi(sicil, bolge, tarih, saatBaslangic, saatBitis,hatKod,intentSicil,intentIsTarih);

                } else {
                    Toast.makeText(IsDuzenleActivity.this, R.string.toastHatKodunaSahipAracBulunamadi, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void aractaArizaVarMi(String sicil, String bolge, String tarih, String saatBaslangic, String saatBitis, String hatKod,String intentSicil,String intentIsTarih) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String aracArizaDurumu = dataSnapshot.child(Constants.FIREBASE_ARACLAR_ARIZA_DURUMU).getValue().toString();

                if(aracArizaDurumu.equals(Constants.FIREBASE_ARIZA_DURUMU_FALSE)){
                    isVarMi(sicil, bolge, tarih, saatBaslangic, saatBitis,intentSicil,intentIsTarih);
                }
                else{
                    Toast.makeText(IsDuzenleActivity.this, getResources().getString(R.string.toastHatKodunaAitAractArizaBulunmaktadır), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void isVarMi(String sicil, String bolge,String tarih, String baslangicSaat, String bitisSaat,String intentSicil,String intentIsTarih) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Constants.FIREBASE_CHILD + tarih.replace('/','_'))) {
                    if(intentSicil.equals(sicil) && intentIsTarih.equals(intentIsTarih)){
                        Snackbar snackbar = Snackbar.make(constraintLayout, tarih + getResources().getString(R.string.snackbarTarihineAitIsListesiBulunmaktadir), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(getResources().getColor(R.color.logoRengi));
                        snackbar.setAction(R.string.isDuzenle, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                                Intent isDuzenleIntent = new Intent(IsDuzenleActivity.this, IsDuzenleActivity.class);
                                isDuzenleIntent.putExtra(Constants.FIREBASE_KULLANICI_SICIL, sicil);
                                isDuzenleIntent.putExtra(Constants.FIREBASE_IS_LISTESI_IS_TARIH, tarih.replace('/','_'));
                                startActivity(isDuzenleIntent);
                                finish();
                            }
                        });
                        snackbar.show();
                    }
                    else{
                        isDuzenle(sicil,bolge,tarih,baslangicSaat,bitisSaat,intentSicil,intentIsTarih);
                    }
                }
                else{
                    if(intentSicil.equals(sicil) && intentIsTarih.equals(intentIsTarih)){
                        isDuzenle(sicil,bolge,tarih,baslangicSaat,bitisSaat,intentSicil,intentIsTarih);
                    }
                    else{
                        Snackbar snackbar = Snackbar.make(constraintLayout, tarih + getResources().getString(R.string.snackbarTarihineAitIsListesiBulunmamaktadir), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(getResources().getColor(R.color.logoRengi));
                        snackbar.setAction(R.string.isOlusturButon, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                                Intent isOlusturIntent = new Intent(IsDuzenleActivity.this, IsOlusturActivity.class);
                                startActivity(isOlusturIntent);
                                finish();
                            }
                        });
                        snackbar.show();
                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isDuzenle(String sicil, String bolge, String tarih, String baslangicSaat, String bitisSaat,String intentSicil,String intentIsTarih) {
        String isTarihChild = tarih.replace('/','_');
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("/" + isTarihChild)) {
                    Map isOlusturMap = new HashMap();
                    isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI, baslangicSaat);
                    isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI, bitisSaat);
                    isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BOLGE, bolge);

                    uDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(isTarihChild);
                    uDatabase.updateChildren(isOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), R.string.toastIsGuncellemeBasarili, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), R.string.toastIsGuncellemeBasarisiz, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}