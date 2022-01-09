package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class IzinDuzenleActivity extends AppCompatActivity {

    Button btnIzinDuzenleBaslikIcon, btnIzinDuzenleBaslik, btnIzinDuzenleTur, btnIzinDuzenle, btnIzinDuzenleBaslangicTarih, btnIzinDuzenleBitisTarih;
    EditText txtIzinDuzenleSicil;
    TextView txtIzinDuzenleGun;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, uDatabase, isDatabase;
    private FirebaseUser mUser;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    ConstraintLayout constraintLayout;
    private String izinBaslangicTarihi,izinBitisTarihi;
    private ArrayList<Users> users;
    private int i;
    private boolean izinVarMi;
    private String baslangicTarihChild,bitisTarihChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_duzenle);
        init();
    }

    private void init() {
        btnIzinDuzenleBaslikIcon = findViewById(R.id.btnIzinDuzenleBaslikIcon);
        btnIzinDuzenleBaslik = findViewById(R.id.btnIzinDuzenleBaslik);
        btnIzinDuzenleTur = findViewById(R.id.btnIzinDuzenleTur);
        btnIzinDuzenle = findViewById(R.id.btnIzinDuzenle);
        btnIzinDuzenleBaslangicTarih = findViewById(R.id.btnIzinDuzenleBaslangicTarih);
        btnIzinDuzenleBitisTarih = findViewById(R.id.btnIzinDuzenleBitisTarih);
        txtIzinDuzenleSicil = findViewById(R.id.txtIzinDuzenleSicil);
        txtIzinDuzenleGun = findViewById(R.id.txtIzinDuzenleGun);
        constraintLayout = findViewById(R.id.izinDuzenleLayout);


        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        i=0;
        users = new ArrayList<>();
        izinVarMi = false;





        String intentSicil = getIntent().getExtras().getString(Constants.FIREBASE_KULLANICI_SICIL);
        String intentUserId = getIntent().getExtras().getString(Constants.FIREBASE_KULLANICI_USER_ID);

        String intentIzinTarih = getIntent().getExtras().getString(Constants.FIREBASE_IZINLER_IZIN_TARIH).replace('/','_');


        if (!(intentSicil.equals(Constants.BOS_KONTROL)) && !(intentIzinTarih.equals(Constants.BOS_KONTROL)) && !(intentUserId.equals(Constants.BOS_KONTROL))) {
            txtIzinDuzenleSicil.setText(intentSicil);
            sicilIzinBilgileriGetir(intentSicil,intentIzinTarih);
        } else {
            txtIzinDuzenleSicil.setHint(R.string.sicilButon);
            btnIzinDuzenleBitisTarih.setEnabled(false);

        }


        btnIzinDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt(intentSicil, intentIzinTarih,intentUserId);
            }
        });

        btnIzinDuzenleBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent izinOlusturVeyaDuzenleIntent = new Intent(IzinDuzenleActivity.this, IzinIslemleriActivity.class);
                startActivity(izinOlusturVeyaDuzenleIntent);
                finish();
            }
        });

        btnIzinDuzenleBaslangicTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IzinDuzenleActivity.this, R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinBaslangicTarihi = btnIzinDuzenleBaslangicTarih.getText().toString();
                                if (izinBaslangicTarihi.equals(R.string.isTarihiSec)) {
                                    Toast.makeText(IzinDuzenleActivity.this, R.string.izinBaslangicTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIzinDuzenleBaslangicTarih.setText(day + "/" + (month + 1) + "/" + year);


                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String izinBaslangicTarih = btnIzinDuzenleBaslangicTarih.getText().toString().trim();

                                try {
                                    Date izinBaslangic = tarihFormat.parse(izinBaslangicTarih);
                                    btnIzinDuzenleBaslangicTarih.setText(tarihFormat.format(izinBaslangic));
                                    btnIzinDuzenleBitisTarih.setEnabled(true);
                                    String izinBitisTarih = btnIzinDuzenleBitisTarih.getText().toString().trim();

                                    Date izinBitis = tarihFormat.parse(izinBitisTarih);



                                    if(!btnIzinDuzenleBitisTarih.getText().toString().trim().equals(getResources().getString(R.string.izinBitisTarihiSec))){
                                        long tarihFark = izinBitis.getTime() - izinBaslangic.getTime();
                                        long difference_In_Days = TimeUnit.MILLISECONDS.toDays(tarihFark) % 365;

                                        String gunFark = Long.toString(difference_In_Days);
                                        if (Integer.parseInt(gunFark) < 1) {
                                            Toast.makeText(IzinDuzenleActivity.this, R.string.toastYanlisIzinTarihiSectiniz, Toast.LENGTH_SHORT).show();
                                            btnIzinDuzenle.setEnabled(false);
                                        } else {
                                            txtIzinDuzenleGun.setText(gunFark);
                                            btnIzinDuzenle.setEnabled(true);

                                        }
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        btnIzinDuzenleBitisTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IzinDuzenleActivity.this, R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinBitisTarihi = btnIzinDuzenleBitisTarih.getText().toString();
                                if (izinBitisTarihi.equals(R.string.isTarihiSec)) {
                                    Toast.makeText(IzinDuzenleActivity.this, R.string.izinBitisTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIzinDuzenleBitisTarih.setText(day + "/" + (month + 1) + "/" + year);
                                String izinBitisTarih = btnIzinDuzenleBitisTarih.getText().toString().trim();
                                String izinBaslangicTarih = btnIzinDuzenleBaslangicTarih.getText().toString().trim();

                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date izinBaslangic = tarihFormat.parse(izinBaslangicTarih);
                                    Date izinBitis = tarihFormat.parse(izinBitisTarih);

                                    btnIzinDuzenleBitisTarih.setText(tarihFormat.format(izinBitis));

                                    long tarihFark = izinBitis.getTime() - izinBaslangic.getTime();
                                    long difference_In_Days = TimeUnit.MILLISECONDS.toDays(tarihFark) % 365;

                                    String gunFark = Long.toString(difference_In_Days);
                                    if (Integer.parseInt(gunFark) < 1) {
                                        Toast.makeText(IzinDuzenleActivity.this, R.string.toastYanlisIzinTarihiSectiniz, Toast.LENGTH_SHORT).show();
                                        btnIzinDuzenle.setEnabled(false);
                                    } else {
                                        txtIzinDuzenleGun.setText(gunFark);
                                        btnIzinDuzenle.setEnabled(true);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();

            }
        });

        btnIzinDuzenleTur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(IzinDuzenleActivity.this, btnIzinDuzenleTur);
                popup.getMenuInflater().inflate(R.menu.menu_izin_tur, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        btnIzinDuzenleTur.setText(item.getTitle().toString());
                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    private void sicilIzinBilgileriGetir(String intentSicil, String intentIzinTarih) {
        String izinTarihi = intentIzinTarih.replace('/', '_');

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(intentSicil).child(izinTarihi);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                btnIzinDuzenleBaslangicTarih.setText(snapshot.child(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI).getValue().toString());
                btnIzinDuzenleBitisTarih.setText(snapshot.child(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI).getValue().toString());
                txtIzinDuzenleGun.setText(snapshot.child(Constants.FIREBASE_IZINLER_IZIN_GUN).getValue().toString());
                btnIzinDuzenleTur.setText(snapshot.child(Constants.FIREBASE_IZINLER_IZIN_TUR).getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bosAlanVarMiKontrolEt(String intentSicil, String intentIzinTarih,String intentUserId) {
        String sicil = txtIzinDuzenleSicil.getText().toString().trim();
        String izinBaslangicTarih = btnIzinDuzenleBaslangicTarih.getText().toString().trim();
        String izinBitisTarih = btnIzinDuzenleBitisTarih.getText().toString().trim();
        String izinTur = btnIzinDuzenleTur.getText().toString().trim();
        String izinGun= txtIzinDuzenleGun.getText().toString().trim();


        if(sicil.equals(Constants.BOS_KONTROL) || izinBaslangicTarih.equals(getResources().getString(R.string.izinBaslangicTarihiSec)) || izinBitisTarih.equals(getResources().getString(R.string.izinBitisTarihiSec)) || izinTur.equals(getResources().getString(R.string.izinTuruSeciniz))){
            Toast.makeText(IzinDuzenleActivity.this,R.string.toastBosAlan,Toast.LENGTH_LONG).show();
        } else {
            if(izinGun.equals(getResources().getString(R.string.izinGunSayisi)) || izinGun.equals(Constants.BOS_KONTROL)){
                Toast.makeText(IzinDuzenleActivity.this, R.string.toastYanlisIzinTarihiSectiniz, Toast.LENGTH_SHORT).show();
            }
            else{
                if(intentIzinTarih.replace('_','/').equals(izinBaslangicTarih) && intentSicil.equals(sicil) ){
                    bilgilerDegistiMi(sicil,izinBaslangicTarih,izinBitisTarih,izinTur,izinGun,intentSicil,intentIzinTarih,intentUserId);
                }else{
                    sicilVarMi(sicil,izinBaslangicTarih,izinBitisTarih,izinTur,izinGun,intentSicil,intentIzinTarih);
                }
            }
        }
    }

    private void sicilVarMi(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur, String izinGun, String intentSicil, String intentIzinTarih) {
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
                                tarihlerArasindaIzinVarMi(sicil,izinBaslangicTarih,izinBitisTarih,izinTur,userId);
                                break;
                            }else {
                                if (i == users.size()-1) {
                                    Toast.makeText(IzinDuzenleActivity.this, getResources().getString(R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir), Toast.LENGTH_SHORT).show();
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

    private void bilgilerDegistiMi(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur, String izinGun, String intentSicil, String intentIzinTarih,String intentUserId) {
        String izinTarihi = intentIzinTarih.replace('/', '_');

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(izinTarihi);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dbIzinBaslangic = snapshot.child(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI).getValue().toString();
                String dbIzinBitis = snapshot.child(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI).getValue().toString();
                String dbIzinGun = snapshot.child(Constants.FIREBASE_IZINLER_IZIN_GUN).getValue().toString();
                String dbIzinTur = snapshot.child(Constants.FIREBASE_IZINLER_IZIN_TUR).getValue().toString();

                //tarihler ve tür değiştiyse
                if (!(izinBaslangicTarih.equals(dbIzinBaslangic)) || !(izinBitisTarih.equals(dbIzinBitis)) || !(izinTur.equals(dbIzinTur)) || !(dbIzinGun.equals(dbIzinGun))) {
                    //tarihler değişti ise
                    if (!(izinBaslangicTarih.equals(dbIzinBaslangic)) || !(izinBitisTarih.equals(dbIzinBitis))) {
                        tarihlerArasindaIzinVarMi(sicil, izinBaslangicTarih, izinBitisTarih, izinTur,intentUserId);
                    } else {//yalnız tür değişti ise
                        izinGuncelle(sicil,izinBaslangicTarih,izinBitisTarih,izinTur);
                    }
                }

                //değişmediyse güncelleme olmayacak o yüzden else yok
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void tarihlerArasindaIzinVarMi(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur,String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                izinBaslangicTarihi = izinBaslangicTarih;
                String tatilGun = txtIzinDuzenleGun.getText().toString();
                int gun = Integer.parseInt(tatilGun);
                for(int i=1;i<=gun;i++){
                    //izin var ise
                    if(dataSnapshot.hasChild(Constants.FIREBASE_CHILD + getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_'))) {
                        baslangicTarihChild = dataSnapshot.child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_')).child(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI).getValue().toString();
                        bitisTarihChild = dataSnapshot.child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_')).child(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI).getValue().toString();
                        String tarihGun =dataSnapshot.child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_')).child(Constants.FIREBASE_IZINLER_IZIN_GUN).getValue().toString();
                        //izin başlangıç  ve bitiş tarihi girilen tarihlerle tam uyuşuyor ise
                        if(izinBaslangicTarih.equals(baslangicTarihChild) && izinBitisTarih.equals(bitisTarihChild)){
                            izinGuncelle(sicil,izinBaslangicTarih,izinBitisTarih,izinTur);
                        }
                        else{
                            izinBitisTarihi = bitisTarihChild;

                            if((izinBaslangicTarih.equals(baslangicTarihChild)) && !(izinBitisTarih.equals(bitisTarihChild))){
                                tarihlerArasindaIsVarMi(sicil, izinBaslangicTarih, izinBitisTarih, izinTur);
                            }
                            else if(!(izinBaslangicTarih.equals(baslangicTarihChild)) && (izinBitisTarih.equals(bitisTarihChild))){
                                tarihlerArasindaIsVarMi(sicil, izinBaslangicTarih, izinBitisTarih, izinTur);

                            }
                            else if(!(izinBaslangicTarih.equals(baslangicTarihChild)) && (izinBitisTarih.equals(bitisTarihChild))){
                                tarihlerArasindaIsVarMi(sicil, izinBaslangicTarih, izinBitisTarih, izinTur);

                            }
                            else if(dataSnapshot.hasChild(Constants.FIREBASE_CHILD + getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_'))){
                                Snackbar snackbar = Snackbar.make(constraintLayout, getIzinBaslangicTarihi(izinBaslangicTarihi) +" "+ getResources().getString(R.string.snackbarTarihineAitIzinBulunmaktadir), Snackbar.LENGTH_LONG);
                                snackbar.setAction(R.string.izinDuzenle, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                        Intent izinDuzenleIntent = new Intent(IzinDuzenleActivity.this, IzinDuzenleActivity.class);
                                        izinDuzenleIntent.putExtra(Constants.FIREBASE_KULLANICI_SICIL, sicil);
                                        izinDuzenleIntent.putExtra(Constants.FIREBASE_IZINLER_IZIN_TARIH, getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_'));
                                        startActivity(izinDuzenleIntent);
                                        finish();
                                    }
                                });
                                snackbar.show();
                            }
                            else if(dataSnapshot.hasChild(Constants.FIREBASE_CHILD + getIzinBitisTarihi(izinBitisTarihi).replace('/','_'))){
                                Snackbar snackbar = Snackbar.make(constraintLayout, getIzinBitisTarihi(izinBitisTarihi) +" "+ getResources().getString(R.string.snackbarTarihineAitIzinBulunmaktadir), Snackbar.LENGTH_LONG);
                                snackbar.setAction(R.string.izinDuzenle, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                        Intent izinDuzenleIntent = new Intent(IzinDuzenleActivity.this, IzinDuzenleActivity.class);
                                        izinDuzenleIntent.putExtra(Constants.FIREBASE_KULLANICI_SICIL, sicil);
                                        izinDuzenleIntent.putExtra(Constants.FIREBASE_IZINLER_IZIN_TARIH, getIzinBitisTarihi(izinBitisTarihi).replace('/','_'));
                                        startActivity(izinDuzenleIntent);
                                        finish();
                                    }
                                });
                                snackbar.show();
                            }
                            else{
                            }
                        }
                    }
                    else{ //izin yok ise
                        try{
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar c = Calendar.getInstance();
                            c.setTime(sdf.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                            c.add(Calendar.DATE, 1);
                            Date afterDay = c.getTime();
                            String strDate = sdf.format(afterDay);
                            setIzinBaslangicTarihi(strDate);
                            if(i==gun){


                                    Snackbar snackbar = Snackbar.make(constraintLayout, getResources().getString(R.string.snackbarIzinBulunmamaktadir), Snackbar.LENGTH_LONG);
                                    snackbar.setActionTextColor(getResources().getColor(R.color.logoRengi));
                                    snackbar.setAction(R.string.izinOlustur, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                            Intent izinOlusturIntent = new Intent(IzinDuzenleActivity.this, IzinOlusturActivity.class);
                                            startActivity(izinOlusturIntent);
                                            finish();
                                        }
                                    });
                                    snackbar.show();




                            }
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void tarihlerArasindaIsVarMi(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                izinBaslangicTarihi = izinBaslangicTarih;

                String tatilGun = txtIzinDuzenleGun.getText().toString();
                int gun = Integer.parseInt(tatilGun);

                for (int i = 1; i <= gun; i++) {
                    //is child var ise
                    if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD+ getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'))) {
                        String is = dataSnapshot.child(Constants.FIREBASE_CHILD + getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_IS_TARIH).getValue().toString();
                        //iş var
                        if (((!is.contains(Constants.FIREBASE_IZINLER_IZIN))) && ((!is.contains(Constants.FIREBASE_IZINLER_IZNI)))) {
                            String isTarihChild = getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_');
                            Snackbar snackbar = Snackbar.make(constraintLayout, getIzinBaslangicTarihi(izinBaslangicTarihi) +" "+ getResources().getString(R.string.snackbarTarihineAitIsListesiBulunmaktadir), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else { //iş yok izin var
                            try {
                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar takvim = Calendar.getInstance();
                                takvim.setTime(tarihFormat.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                                takvim.add(Calendar.DATE, 1);
                                Date birSonrakiGun = takvim.getTime();
                                String tarih = tarihFormat.format(birSonrakiGun);
                                setIzinBaslangicTarihi(tarih);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                    else { //is child yok ise
                        try {
                            SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar takvim = Calendar.getInstance();
                            takvim.setTime(tarihFormat.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                            takvim.add(Calendar.DATE, 1);
                            Date birSonrakiGun = takvim.getTime();
                            String tarih = tarihFormat.format(birSonrakiGun);
                            setIzinBaslangicTarihi(tarih);
                            if(i==gun){
                                isListesiOlustur(sicil, izinBaslangicTarih, izinBitisTarih, izinTur);

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isListesiOlustur(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur) {
        izinBaslangicTarihi = izinBaslangicTarih;
        String tatilGun = txtIzinDuzenleGun.getText().toString();
        int gun = Integer.parseInt(tatilGun);

        for (int i = 1; i <= gun; i++) {
            HashMap<String, String> isOlusturMap = new HashMap<>();
            isOlusturMap.put(Constants.FIREBASE_KULLANICI_SICIL, sicil);
            isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI, izinTur + " " + izinBaslangicTarih + "_" + izinBitisTarih);
            isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI, izinTur + " " + izinBaslangicTarih + "_" + izinBitisTarih);

            isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BOLGE, izinTur + " " + izinBaslangicTarih + "_" + izinBitisTarih);
            isOlusturMap.put(Constants.FIREBASE_IS_LISTESI_IS_TARIH, izinTur + " " + izinBaslangicTarih + "_" + izinBitisTarih);

            if(gun==1){
                isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(izinBaslangicTarih.replace('/', '_'));
                isDatabase.setValue(isOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        izinOlustur(sicil, izinBaslangicTarih, izinBitisTarih, izinTur);
                    }
                });
            }
            else{
                if (i == 1) {
                    isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(izinBaslangicTarih.replace('/', '_'));
                    isDatabase.setValue(isOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                        c.add(Calendar.DATE, 1);
                        Date afterDay = c.getTime();
                        String strDate = sdf.format(afterDay);
                        setIzinBaslangicTarihi(strDate);

                        isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                        isDatabase.setValue(isOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        if (i == gun) {
                            izinOlustur(sicil, izinBaslangicTarih, izinBitisTarih, izinTur);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    private void izinOlustur(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur) {
        izinBaslangicTarihi = izinBaslangicTarih;

        String tatilGun = txtIzinDuzenleGun.getText().toString();
        int gun = Integer.parseInt(tatilGun);
        for (int i = 1; i <= gun; i++) {
            HashMap<String, String> izinOlusturMap = new HashMap<>();
            izinOlusturMap.put(Constants.FIREBASE_KULLANICI_SICIL, sicil);
            izinOlusturMap.put(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI, izinBaslangicTarih);
            izinOlusturMap.put(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI, izinBitisTarih);
            izinOlusturMap.put(Constants.FIREBASE_IZINLER_IZIN_TUR, izinTur);
            izinOlusturMap.put(Constants.FIREBASE_IZINLER_IZIN_GUN, txtIzinDuzenleGun.getText().toString().trim());

            if(gun==1){
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                mDatabase.setValue(izinOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(IzinDuzenleActivity.this, R.string.toastIzinDuzenlemeBasarili, Toast.LENGTH_SHORT).show();

                    }
                });
            }
            else{
                if (i == 1) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                    mDatabase.setValue(izinOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                } else {
                    try {
                        SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(tarihFormat.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                        calendar.add(Calendar.DATE, 1);
                        Date birSonrakiTarih = calendar.getTime();
                        String arttirilmisTarih = tarihFormat.format(birSonrakiTarih);
                        setIzinBaslangicTarihi(arttirilmisTarih);

                        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                        mDatabase.setValue(izinOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        if (i == gun) {
                            Toast.makeText(IzinDuzenleActivity.this, R.string.toastIzinDuzenlemeBasarili, Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    private void izinGuncelle(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur) {
        izinBaslangicTarihi = izinBaslangicTarih;

        String tatilGun = txtIzinDuzenleGun.getText().toString();
        int gun = Integer.parseInt(tatilGun);
        for (int i = 1; i <= gun; i++) {
            Map izinDuzenleMap = new HashMap();
            izinDuzenleMap.put(Constants.FIREBASE_KULLANICI_SICIL, sicil);
            izinDuzenleMap.put(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI, izinBaslangicTarih);
            izinDuzenleMap.put(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI, izinBitisTarih);
            izinDuzenleMap.put(Constants.FIREBASE_IZINLER_IZIN_TUR, izinTur);
            izinDuzenleMap.put(Constants.FIREBASE_IZINLER_IZIN_GUN, txtIzinDuzenleGun.getText().toString().trim());

            Map isDuzenleMap = new HashMap();
            isDuzenleMap.put(Constants.FIREBASE_KULLANICI_SICIL, sicil);
            isDuzenleMap.put(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI, izinTur + " " + izinBaslangicTarih + "_" + izinBitisTarih);
            isDuzenleMap.put(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI, izinTur + " " + izinBaslangicTarih + "_" + izinBitisTarih);
            isDuzenleMap.put(Constants.FIREBASE_IS_LISTESI_BOLGE, izinTur + " " + izinBaslangicTarih + "_" + izinBitisTarih);
            isDuzenleMap.put(Constants.FIREBASE_IS_LISTESI_IS_TARIH, izinTur + " " + izinBaslangicTarih + "_" + izinBitisTarih);

            if(gun==1){
                uDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                uDatabase.updateChildren(izinDuzenleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                        isDatabase.updateChildren(isDuzenleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), R.string.toastIzinGuncellemeBasarili, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });


            }
            else{
                if (i == 1) {
                    uDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                    uDatabase.updateChildren(izinDuzenleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });

                    isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                    isDatabase.updateChildren(isDuzenleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                } else {
                    try {
                        SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(tarihFormat.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                        calendar.add(Calendar.DATE, 1);
                        Date birSonrakiTarih = calendar.getTime();
                        String arttirilmisTarih = tarihFormat.format(birSonrakiTarih);
                        setIzinBaslangicTarihi(arttirilmisTarih);

                        uDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                        uDatabase.updateChildren(izinDuzenleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                        isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                        isDatabase.updateChildren(isDuzenleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        if (i == gun) {
                            Toast.makeText(getApplicationContext(), R.string.toastIzinGuncellemeBasarili, Toast.LENGTH_SHORT).show();

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }



        }
    }

    public String getIzinBaslangicTarihi(String izinBaslangicTarihi) {
        return izinBaslangicTarihi;
    }

    public void setIzinBaslangicTarihi(String izinBaslangicTarih) {
        this.izinBaslangicTarihi = izinBaslangicTarih;
    }

    public String getIzinBitisTarihi(String izinBitisTarihi) {
        return izinBitisTarihi;
    }

    public void setIzinBitisTarihi(String izinBitisTarihi) {
        this.izinBitisTarihi = izinBitisTarihi;
    }

}