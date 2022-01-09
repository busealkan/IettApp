package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

public class IzinSilActivity extends AppCompatActivity {

    private Button btnIzinSilAra,btnIzinSilIzinDurumu,btnIzinSil,btnIzinSilBaslikIcon, btnIzinSilBaslik, btnIzinSilTarih,btnIzinSilAdSoyadBaslik,btnIzinSilIzinTurBaslik,btnIzinSilIzinBaslangicTarihiBaslik,btnIzinSilIzinBitisTarihiBaslik,btnIzinSilIzinGunBaslik;
    private TextView txtIzinSilIzinDurumu,txtIzinSilSicil, txtIzinSilAdSoyad, txtIzinSilIzinTuru, txtIzinSilIzinBaslangicTarihi,txtIzinSilIzinBitisTarihi,txtIzinSilIzinGunSayisi;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, isDatabase,izinDatabase,izinSilDatabase,isSilDatabase;
    private FirebaseUser mUser;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private String izinBaslangicTarihi;
    private ArrayList<Users> users;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_sil);
        init();
    }

    private void init() {
        btnIzinSilBaslikIcon = findViewById(R.id.btnIzinSilBaslikIcon);
        btnIzinSilBaslik = findViewById(R.id.btnIzinSilBaslik);
        btnIzinSilTarih = findViewById(R.id.btnIzinSilTarih);


        btnIzinSilAdSoyadBaslik = findViewById(R.id.btnIzinSilAdSoyadBaslik);
        btnIzinSilIzinTurBaslik = findViewById(R.id.btnIzinSilIzinTurBaslik);
        btnIzinSilIzinBaslangicTarihiBaslik = findViewById(R.id.btnIzinSilIzinBaslangicTarihiBaslik);
        btnIzinSilIzinBitisTarihiBaslik = findViewById(R.id.btnIzinSilIzinBitisTarihiBaslik);
        btnIzinSilIzinGunBaslik = findViewById(R.id.btnIzinSilIzinGunBaslik);
        btnIzinSil= findViewById(R.id.btnIzinSil);
        btnIzinSilIzinDurumu= findViewById(R.id.btnIzinSilIzinDurumu);
        btnIzinSilAra = findViewById(R.id.btnIzinSilAra);

        txtIzinSilSicil = findViewById(R.id.txtIzinSilSicil);
        txtIzinSilIzinDurumu = findViewById(R.id.txtIzinSilIzinDurumu);
        txtIzinSilAdSoyad = findViewById(R.id.txtIzinSilAdSoyad);
        txtIzinSilIzinTuru = findViewById(R.id.txtIzinSilIzinTuru);
        txtIzinSilIzinBaslangicTarihi = findViewById(R.id.txtIzinSilIzinBaslangicTarihi);
        txtIzinSilIzinBitisTarihi = findViewById(R.id.txtIzinSilIzinBitisTarihi);
        txtIzinSilIzinGunSayisi = findViewById(R.id.txtIzinSilIzinGunSayisi);

        users = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        i=0;


        btnIzinSilBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anaSayfaIntent = new Intent(IzinSilActivity.this, AnaSayfaActivity.class);
                startActivity(anaSayfaIntent);
                finish();
            }
        });

        btnIzinSilTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(IzinSilActivity.this,R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String izinlerimTarih = btnIzinSilTarih.getText().toString().trim();

                                if(izinlerimTarih.equals(R.string.isTarihiSec)){
                                    Toast.makeText(IzinSilActivity.this, R.string.izinBaslangicTarihi, Toast.LENGTH_SHORT).show();
                                }
                                btnIzinSilTarih.setText(day + "/" + (month+1) + "/" + year);


                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String izinTarih = btnIzinSilTarih.getText().toString().trim();

                                try {
                                    Date izinBaslangic = tarihFormat.parse(izinTarih);
                                    btnIzinSilTarih.setText(tarihFormat.format(izinBaslangic));
                                    bilgileriGizle();
                                    String aramaIzinTarih = btnIzinSilTarih.getText().toString().trim();

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }


        });

        btnIzinSilAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String silIzinTarih = btnIzinSilTarih.getText().toString().trim();
                bosAlanVarMiKontrolEt(silIzinTarih);
            }
        });

        btnIzinSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSil();
            }
        });

        bilgileriGizle();

    }

    private void bilgileriGizle() {
        btnIzinSilAdSoyadBaslik.setVisibility(View.INVISIBLE);
        btnIzinSilIzinTurBaslik.setVisibility(View.INVISIBLE);
        btnIzinSilIzinBaslangicTarihiBaslik.setVisibility(View.INVISIBLE);
        btnIzinSilIzinBitisTarihiBaslik.setVisibility(View.INVISIBLE);
        btnIzinSilIzinGunBaslik.setVisibility(View.INVISIBLE);
        btnIzinSilIzinDurumu.setVisibility(View.INVISIBLE);
        btnIzinSil.setVisibility(View.INVISIBLE);

        txtIzinSilIzinDurumu.setVisibility(View.INVISIBLE);
        txtIzinSilAdSoyad.setVisibility(View.INVISIBLE);
        txtIzinSilIzinTuru.setVisibility(View.INVISIBLE);
        txtIzinSilIzinBaslangicTarihi.setVisibility(View.INVISIBLE);
        txtIzinSilIzinBitisTarihi.setVisibility(View.INVISIBLE);
        txtIzinSilIzinGunSayisi.setVisibility(View.INVISIBLE);
    }

    private void bilgileriGoster() {
        btnIzinSilAdSoyadBaslik.setVisibility(View.VISIBLE);
        btnIzinSilIzinTurBaslik.setVisibility(View.VISIBLE);
        btnIzinSilIzinBaslangicTarihiBaslik.setVisibility(View.VISIBLE);
        btnIzinSilIzinBitisTarihiBaslik.setVisibility(View.VISIBLE);
        btnIzinSilIzinGunBaslik.setVisibility(View.VISIBLE);
        btnIzinSilIzinDurumu.setVisibility(View.VISIBLE);
        btnIzinSil.setVisibility(View.VISIBLE);
        txtIzinSilIzinDurumu.setVisibility(View.VISIBLE);
        txtIzinSilAdSoyad.setVisibility(View.VISIBLE);
        txtIzinSilIzinTuru.setVisibility(View.VISIBLE);
        txtIzinSilIzinBaslangicTarihi.setVisibility(View.VISIBLE);
        txtIzinSilIzinBitisTarihi.setVisibility(View.VISIBLE);
        txtIzinSilIzinGunSayisi.setVisibility(View.VISIBLE);
    }

    private void bosAlanVarMiKontrolEt(String silinecekIzinTarih) {
        String sicil = txtIzinSilSicil.getText().toString().trim();
        String tarih = btnIzinSilTarih.getText().toString().trim();
        if (sicil.equals(getResources().getString(R.string.izinSilinecekSicilTools)) || sicil.equals(Constants.BOS_KONTROL) || tarih.equals(getResources().getString(R.string.izinSilinecekTarihTools))) {
            Toast.makeText(IzinSilActivity.this, R.string.toastBosAlan, Toast.LENGTH_SHORT).show();
        } else {
            sicilVarMi(sicil, silinecekIzinTarih);
        }
    }

    private void sicilVarMi(String sicil, String silinecekIzinTarih) {
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
                                izinlerim(sicil,silinecekIzinTarih,userId);
                                break;
                            } else {
                                if (i == users.size() - 1) {
                                    Toast.makeText(IzinSilActivity.this, R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir, Toast.LENGTH_SHORT).show();
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

    private void izinlerim(String sicil, String izinTarih, String userId) {
        String izinTarihChild= izinTarih.replace('/','_');
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adSoyad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString() + " " + snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                txtIzinSilAdSoyad.setText(adSoyad);
                txtIzinSilSicil.setText(sicil);


                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(Constants.FIREBASE_CHILD + izinTarihChild)) {
                            izinDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(izinTarihChild);
                            izinDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String dbIzinTur =  snapshot.child(Constants.FIREBASE_IZINLER_IZIN_TUR).getValue().toString();
                                    String dbIzinBaslangicTarihi =  snapshot.child(Constants.FIREBASE_IZINLER_IZIN_BASLANGIC_TARIHI).getValue().toString();
                                    String dbIzinBitisTarihi =  snapshot.child(Constants.FIREBASE_IZINLER_IZIN_BITIS_TARIHI).getValue().toString();
                                    String dbIzinGun =  snapshot.child(Constants.FIREBASE_IZINLER_IZIN_GUN).getValue().toString();

                                    txtIzinSilIzinBaslangicTarihi.setText(dbIzinBaslangicTarihi);
                                    txtIzinSilIzinBitisTarihi.setText(dbIzinBitisTarihi);
                                    txtIzinSilIzinTuru.setText(dbIzinTur);
                                    txtIzinSilIzinGunSayisi.setText(dbIzinGun);

                                    bilgileriGoster();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            bilgileriGizle();
                            Toast.makeText(IzinSilActivity.this, izinTarih+getResources().getString(R.string.toastTariheAitIzinBulunmamaktadir), Toast.LENGTH_SHORT).show();
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
        builder.setTitle(getResources().getString(R.string.alertIzinTitle));
        builder.setMessage(getResources().getString(R.string.alertIzinMessage));
        builder.setNegativeButton(getResources().getString(R.string.alertIzinNegative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tatilGun = txtIzinSilIzinGunSayisi.getText().toString();
                String sicil = txtIzinSilSicil.getText().toString();
                String silinecekIs = txtIzinSilIzinBaslangicTarihi.getText().toString();
                izinBaslangicTarihi = silinecekIs;

                Integer gun = Integer.parseInt(tatilGun);

                for(int i=1;i<=gun;i++){
                    if(gun==1){
                        isSilDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(silinecekIs.replace('/', '_'));
                        isSilDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    izinSil(sicil,silinecekIs,gun);
                                }
                            }
                        });
                    }
                    else{
                        if (i == 1) {
                            isSilDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(silinecekIs.replace('/', '_'));
                            isSilDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }
                            });

                        }
                        else{
                            try {
                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar takvim = Calendar.getInstance();
                                takvim.setTime(tarihFormat.parse(getIzinBaslangicTarihi(izinBaslangicTarihi)));
                                takvim.add(Calendar.DATE, 1);
                                Date birSonrakiGun = takvim.getTime();
                                String isGun = tarihFormat.format(birSonrakiGun);
                                setIzinBaslangicTarihi(isGun);


                                isSilDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/', '_'));
                                isSilDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                        }
                                    }
                                });
                                if(i==gun){
                                    izinSil(sicil,silinecekIs,gun);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
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

    private void izinSil(String sicil,String silinecekIzin,int gun) {
        izinBaslangicTarihi = silinecekIzin;


        for(int i=1;i<=gun;i++){
            if(gun==1){
                izinSilDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(silinecekIzin.replace('/','_'));
                izinSilDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(IzinSilActivity.this, R.string.toastIzinSilmeBasarili, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                if(i==1){
                    izinSilDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(silinecekIzin.replace('/','_'));
                    izinSilDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            }
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



                        izinSilDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IZINLER).child(sicil).child(getIzinBaslangicTarihi(izinBaslangicTarihi).replace('/','_'));
                        izinSilDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });
                        if(i==gun){
                            Toast.makeText(IzinSilActivity.this, R.string.toastIzinSilmeBasarili, Toast.LENGTH_SHORT).show();

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