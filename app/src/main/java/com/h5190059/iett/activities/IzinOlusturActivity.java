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
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class IzinOlusturActivity extends AppCompatActivity {

    Button btnIzinOlusturBaslikIcon, btnIzinOlusturBaslik, btnIzinOlusturTur, btnIzinOlustur, btnIzinOlusturBaslangicTarih, btnIzinOlusturBitisTarih;
    EditText txtIzinOlusturSicil;
    TextView txtIzinOlusturGun;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, isDatabase;
    private FirebaseUser mUser;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    ConstraintLayout constraintLayout;
    private String izinBaslangicTarihi;
    private ArrayList<Users> users;
    private int i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_olustur);
        init();
    }

    private void init() {
        btnIzinOlusturBaslikIcon = findViewById(R.id.btnIzinOlusturBaslikIcon);
        btnIzinOlusturBaslik = findViewById(R.id.btnIzinOlusturBaslik);
        btnIzinOlusturTur = findViewById(R.id.btnIzinOlusturTur);
        btnIzinOlustur = findViewById(R.id.btnIzinOlustur);
        btnIzinOlusturBaslangicTarih = findViewById(R.id.btnIzinOlusturBaslangicTarih);
        btnIzinOlusturBitisTarih = findViewById(R.id.btnIzinOlusturBitisTarih);
        txtIzinOlusturSicil = findViewById(R.id.txtIzinOlusturSicil);
        txtIzinOlusturGun = findViewById(R.id.txtIzinOlusturGun);
        constraintLayout = findViewById(R.id.izinOlusturLayout) ;


        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        users = new ArrayList<>();
        i=0;

        btnIzinOlusturBitisTarih.setEnabled(false);

        btnIzinOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt();
            }
        });

        btnIzinOlusturBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent izinOlusturVeyaDuzenleIntent = new Intent(IzinOlusturActivity.this, IzinIslemleriActivity.class);
                startActivity(izinOlusturVeyaDuzenleIntent);
                finish();
            }
        });

        btnIzinOlusturBaslangicTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IzinOlusturActivity.this,R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinBaslangicTarihi = btnIzinOlusturBaslangicTarih.getText().toString();
                                if(izinBaslangicTarihi.equals(R.string.isTarihiSec)){
                                    Toast.makeText(IzinOlusturActivity.this, R.string.izinBaslangicTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIzinOlusturBaslangicTarih.setText(day + "/" + (month+1) + "/" + year);


                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String izinBaslangicTarih = btnIzinOlusturBaslangicTarih.getText().toString().trim();

                                try {
                                    Date izinBaslangic = tarihFormat.parse(izinBaslangicTarih);
                                    btnIzinOlusturBaslangicTarih.setText(tarihFormat.format(izinBaslangic));
                                    btnIzinOlusturBitisTarih.setEnabled(true);
                                    String izinBitisTarih = btnIzinOlusturBitisTarih.getText().toString().trim();

                                    Date izinBitis = tarihFormat.parse(izinBitisTarih);



                                    if(!btnIzinOlusturBitisTarih.getText().toString().trim().equals(getResources().getString(R.string.izinBitisTarihiSec))){
                                        long tarihFark = izinBitis.getTime() - izinBaslangic.getTime();
                                        long difference_In_Days = TimeUnit.MILLISECONDS.toDays(tarihFark) % 365;

                                        String gunFark = Long.toString(difference_In_Days);
                                        if(Integer.parseInt(gunFark)<1){
                                            Toast.makeText(IzinOlusturActivity.this, R.string.toastYanlisIzinTarihiSectiniz, Toast.LENGTH_SHORT).show();


                                        }
                                        else{
                                            txtIzinOlusturGun.setText(gunFark);
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

        btnIzinOlusturBitisTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IzinOlusturActivity.this,R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinBitisTarihi = btnIzinOlusturBitisTarih.getText().toString();
                                if(izinBitisTarihi.equals(R.string.isTarihiSec)){
                                    Toast.makeText(IzinOlusturActivity.this, R.string.izinBitisTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIzinOlusturBitisTarih.setText(day + "/" + (month+1) + "/" + year);
                                String izinBitisTarih = btnIzinOlusturBitisTarih.getText().toString().trim();
                                String izinBaslangicTarih = btnIzinOlusturBaslangicTarih.getText().toString().trim();

                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date izinBaslangic = tarihFormat.parse(izinBaslangicTarih);
                                    Date izinBitis = tarihFormat.parse(izinBitisTarih);

                                    btnIzinOlusturBitisTarih.setText(tarihFormat.format(izinBitis));

                                    long tarihFark = izinBitis.getTime() - izinBaslangic.getTime();
                                    long difference_In_Days = TimeUnit.MILLISECONDS.toDays(tarihFark) % 365;

                                    String gunFark = Long.toString(difference_In_Days);
                                    if(Integer.parseInt(gunFark)<1){
                                        Toast.makeText(IzinOlusturActivity.this, R.string.toastYanlisIzinTarihiSectiniz, Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        txtIzinOlusturGun.setText(gunFark);
                                    }


                                }
                                catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();

            }
        });

        btnIzinOlusturTur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(IzinOlusturActivity.this, btnIzinOlusturTur);
                popup.getMenuInflater().inflate(R.menu.menu_izin_tur, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        btnIzinOlusturTur.setText(item.getTitle().toString());
                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    private void bosAlanVarMiKontrolEt() {
        String sicil = txtIzinOlusturSicil.getText().toString().trim();
        String izinBaslangicTarih = btnIzinOlusturBaslangicTarih.getText().toString().trim();
        String izinBitisTarih = btnIzinOlusturBitisTarih.getText().toString().trim();
        String izinTur = btnIzinOlusturTur.getText().toString().trim();
        String izinGun= txtIzinOlusturGun.getText().toString().trim();


        if(sicil.equals(Constants.BOS_KONTROL) || izinBaslangicTarih.equals(getResources().getString(R.string.izinBaslangicTarihiSec)) || izinBitisTarih.equals(getResources().getString(R.string.izinBitisTarihiSec)) || izinTur.equals(getResources().getString(R.string.izinTuruSeciniz))){
            Toast.makeText(getApplicationContext(),R.string.toastBosAlan,Toast.LENGTH_LONG).show();
        }
        else{
            if(izinGun.equals(getResources().getString(R.string.izinGunSayisi)) || izinGun.equals(Constants.BOS_KONTROL)){
                Toast.makeText(IzinOlusturActivity.this, R.string.toastYanlisIzinTarihiSectiniz, Toast.LENGTH_SHORT).show();

            }
            else{
                sicilVarMi(sicil,izinBaslangicTarih,izinBitisTarih,izinTur);

            }
        }
    }

    private void sicilVarMi(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur) {
        izinBaslangicTarihi =izinBaslangicTarih;
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
                                tarihlerArasindaIsVarMi(sicil,izinBaslangicTarih,izinBitisTarih,izinTur,userId);
                                break;
                            } else {
                                if (i == users.size() - 1) {
                                    Toast.makeText(IzinOlusturActivity.this, R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir, Toast.LENGTH_SHORT).show();
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

    private void tarihlerArasindaIsVarMi(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur,String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                izinBaslangicTarihi = izinBaslangicTarih;

                String tatilGun = txtIzinOlusturGun.getText().toString();
                int gun = Integer.parseInt(tatilGun);

                for(int i=1;i<=gun;i++){
                    if(dataSnapshot.hasChild(Constants.FIREBASE_CHILD + getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_'))) {
                        String is = dataSnapshot.child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_')).child(Constants.FIREBASE_IS_LISTESI_IS_TARIH).getValue().toString();
                        if(!(is.contains(getResources().getString(R.string.izinTurUcretsiz))) && !(is.contains(getResources().getString(R.string.izinTurUcretli))) && !(is.contains(getResources().getString(R.string.izinTurHafta))) && !(is.contains(getResources().getString(R.string.izinTurHastalik))) && !(is.contains(getResources().getString(R.string.izinTurSosyal))) && !(is.contains(getResources().getString(R.string.izinTurZorunlu))) && !(is.contains(getResources().getString(R.string.izinTurYillik)))){
                            String isTarihChild = getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_');
                            Snackbar snackbar = Snackbar.make(constraintLayout, getIzinBaslangicTarihi(izinBaslangicTarihi)+getResources().getString(R.string.snackbarTarihineAitIsListesiBulunmaktadir), Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(getResources().getColor(R.color.logoRengi));
                            snackbar.show();
                        }
                        else{
                            tarihlerArasindaIzinVarMi(sicil, izinBaslangicTarih, izinBitisTarih, izinTur,userId);

                        }

                    }
                    else{
                        try{
                            SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar takvim = Calendar.getInstance();
                            takvim.setTime(tarihFormat.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                            takvim.add(Calendar.DATE, 1);
                            Date birGunSonra = takvim.getTime();
                            String tarih = tarihFormat.format(birGunSonra);
                            setIzinBaslangicTarihi(tarih);
                            if(i==gun){
                               tarihlerArasindaIzinVarMi(sicil, izinBaslangicTarih, izinBitisTarih, izinTur,userId);
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

    private void tarihlerArasindaIzinVarMi(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur,String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                izinBaslangicTarihi = izinBaslangicTarih;

                String tatilGun = txtIzinOlusturGun.getText().toString();
                int gun = Integer.parseInt(tatilGun);

                for(int i=1;i<=gun;i++){
                    if(dataSnapshot.hasChild("/" + getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_'))) {
                        Snackbar snackbar = Snackbar.make(constraintLayout, getIzinBaslangicTarihi(izinBaslangicTarihi) + getResources().getString(R.string.snackbarTarihineAitIzinBulunmaktadir), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(getResources().getColor(R.color.logoRengi));
                        snackbar.setAction(R.string.izinDuzenle, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                                Intent izinDuzenleIntent = new Intent(IzinOlusturActivity.this, IzinDuzenleActivity.class);
                                izinDuzenleIntent.putExtra(Constants.FIREBASE_KULLANICI_SICIL, sicil);
                                izinDuzenleIntent.putExtra(Constants.FIREBASE_KULLANICI_USER_ID, userId);

                                izinDuzenleIntent.putExtra(Constants.FIREBASE_IZINLER_IZIN_TARIH, getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_'));

                                startActivity(izinDuzenleIntent);
                                finish();
                            }
                        });
                        snackbar.show();
                    }
                    else{
                        try{
                            SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar takvim = Calendar.getInstance();
                            takvim.setTime(tarihFormat.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                            takvim.add(Calendar.DATE, 1);
                            Date birSonrakitarih = takvim.getTime();
                            String tarih = tarihFormat.format(birSonrakitarih);
                            setIzinBaslangicTarihi(tarih);
                            if(i==gun){
                                isOlustur(sicil, izinBaslangicTarih, izinBitisTarih, izinTur);
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

    private void isOlustur(String sicil, String izinBaslangicTarih, String izinBitisTarih, String izinTur) {
        izinBaslangicTarihi = izinBaslangicTarih;
        String tatilGun = txtIzinOlusturGun.getText().toString();
        int gun = Integer.parseInt(tatilGun);

        for(int i=1;i<=gun;i++){
            HashMap<String, String> izinIsiOlusturMap = new HashMap<>();
            izinIsiOlusturMap.put(Constants.FIREBASE_KULLANICI_SICIL, sicil);
            izinIsiOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BASLANGIC_SAATI, izinTur+" "+izinBaslangicTarih+"_"+izinBitisTarih);
            izinIsiOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BITIS_SAATI, izinTur+" "+izinBaslangicTarih+"_"+izinBitisTarih);
            izinIsiOlusturMap.put(Constants.FIREBASE_IS_LISTESI_BOLGE, izinTur+" "+izinBaslangicTarih+"_"+izinBitisTarih);
            izinIsiOlusturMap.put(Constants.FIREBASE_IS_LISTESI_IS_TARIH, izinTur+" "+izinBaslangicTarih+"_"+izinBitisTarih );

            if(gun==1){
                isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(izinBaslangicTarih.replace('/', '_'));
                isDatabase.setValue(izinIsiOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        izinOlustur(sicil,izinBaslangicTarih,izinBitisTarih,izinTur);

                    }
                });
            }
            else{
                if (i == 1) {
                    isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(izinBaslangicTarih.replace('/', '_'));
                    isDatabase.setValue(izinIsiOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                }
                else{
                    try {
                        SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar takvim = Calendar.getInstance();
                        takvim.setTime(tarihFormat.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                        takvim.add(Calendar.DATE, 1);
                        Date birSonrakitarih = takvim.getTime();
                        String tarih = tarihFormat.format(birSonrakitarih);
                        setIzinBaslangicTarihi(tarih);

                        isDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                        isDatabase.setValue(izinIsiOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        if(i==gun){
                            izinOlustur(sicil,izinBaslangicTarih,izinBitisTarih,izinTur);
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

        String tatilGun = txtIzinOlusturGun.getText().toString();
        int gun = Integer.parseInt(tatilGun);
        for(int i=1;i<=gun;i++){
            HashMap<String, String> izinOlusturMap = new HashMap<>();
            izinOlusturMap.put(Constants.FIREBASE_KULLANICI_SICIL, sicil);
            izinOlusturMap.put(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI, izinBaslangicTarih);
            izinOlusturMap.put(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI, izinBitisTarih);
            izinOlusturMap.put(Constants.FIREBASE_IZINLER_IZIN_TUR, izinTur);
            izinOlusturMap.put(Constants.FIREBASE_IZINLER_IZIN_GUN, txtIzinOlusturGun.getText().toString().trim());

            if(gun==1){
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                mDatabase.setValue(izinOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(IzinOlusturActivity.this, R.string.toastIzinOlusturmaBasarili, Toast.LENGTH_SHORT).show();

                    }
                });
            }
            else{
                if(i==1){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                    mDatabase.setValue(izinOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
                else{
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
                        if(i==gun){
                            Toast.makeText(IzinOlusturActivity.this, R.string.toastIzinOlusturmaBasarili, Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }



        }

    }

    public String getIzinBaslangicTarihi(String izinBaslangicTarihi){
        return izinBaslangicTarihi;
    }

    public void setIzinBaslangicTarihi(String izinBaslangicTarih){
        this.izinBaslangicTarihi = izinBaslangicTarih;
    }
}