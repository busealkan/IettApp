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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.model.RaporModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RaporOlusturActivity extends AppCompatActivity {

    Button btnRaporOlusturBaslikIcon, btnRaporOlusturBaslik, btnRaporOlustur;
    TextView txtRaporOlusturSicil, txtRaporOlusturRaporBaslik, txtRaporOlusturRaporAciklama;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private int year, month, dayOfMonth;
    private Calendar calendar;
    private ArrayList<Users> users;
    private boolean sicilVarMi=false;
    public int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapor_olustur);
        init();
    }

    private void init() {
        btnRaporOlusturBaslikIcon = findViewById(R.id.btnRaporOlusturBaslikIcon);
        btnRaporOlusturBaslik = findViewById(R.id.btnRaporOlusturBaslik);
        btnRaporOlustur = findViewById(R.id.btnRaporOlustur);
        txtRaporOlusturSicil = findViewById(R.id.txtRaporOlusturSicil);
        txtRaporOlusturRaporBaslik = findViewById(R.id.txtRaporOlusturRaporBaslik);
        txtRaporOlusturRaporAciklama = findViewById(R.id.txtRaporOlusturRaporAciklama);

        mAuth = FirebaseAuth.getInstance();
        i=0;

        btnRaporOlusturBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(RaporOlusturActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnRaporOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raporKayitOlustur();
            }
        });
        users = new ArrayList<>();


    }

    private void raporKayitOlustur() {
        String raporYazilanSicil = txtRaporOlusturSicil.getText().toString().trim();
        String raporBaslik = txtRaporOlusturRaporBaslik.getText().toString().trim();
        String raporAciklama = txtRaporOlusturRaporAciklama.getText().toString().trim();

        if(raporYazilanSicil.equals(Constants.BOS_KONTROL) || raporBaslik.equals(Constants.BOS_KONTROL) || raporAciklama.equals(Constants.BOS_KONTROL)){
            Toast.makeText(getApplicationContext(),R.string.toastZorunluBosAlan,Toast.LENGTH_LONG).show();
        }
        else{
            sicilVarMi(raporYazilanSicil,raporBaslik,raporAciklama);
        }
    }

    private void sicilVarMi(String raporYazilanSicil, String raporBaslik, String raporAciklama) {
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
                            String raporYazanUserId = mAuth.getCurrentUser().getUid();
                            String raporYazanSicil = dataSnapshot.child(raporYazanUserId).child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS);
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                        String raporYazilanUserId = postSnapshot.getKey().toString();
                                        if(dataSnapshot.child(raporYazilanUserId).child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString().equals(raporYazilanSicil)){
                                            sicilVarMi=true;
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR).child(raporYazilanSicil);
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    int raporAdet = 1;
                                                    ///Arizalar/CUbfJ1tbwMPxqElicEQbKWK7TQp2/ariza1

                                                    if (!(dataSnapshot.hasChild(Constants.FIREBASE_TUTANAK_CHILD + raporAdet))) {
                                                        raporEkle(raporYazanSicil,raporYazilanSicil, raporBaslik, raporAciklama, raporAdet,raporYazanUserId,raporYazilanUserId);
                                                    } else {
                                                        while ((dataSnapshot.hasChild(Constants.FIREBASE_TUTANAK_CHILD + raporAdet))) {
                                                            raporAdet = raporAdet + 1;
                                                        }
                                                        raporEkle(raporYazanSicil,raporYazilanSicil, raporBaslik, raporAciklama, raporAdet,raporYazanUserId,raporYazilanUserId);

                                                    }

                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                            break;
                                        }
                                        else{
                                            if (i == users.size()) {
                                                Toast.makeText(RaporOlusturActivity.this, R.string.toastSicilNumarasinaAitCalisanBulunmamaktadir, Toast.LENGTH_SHORT).show();
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

    private void raporEkle(String raporYazanSicil,String raporYazilanSicil, String raporBaslik, String raporAciklama, int raporAdet,String raporYazanUserId,String raporYazilanUserId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR).child(raporYazilanSicil).child(Constants.FIREBASE_TUTANAK+raporAdet);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String,String> raporOlusturMap = new HashMap<>();
                raporOlusturMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZAN_SICIL,raporYazanSicil);
                raporOlusturMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZILAN_SICIL,raporYazilanSicil);
                raporOlusturMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_BASLIK,raporBaslik);
                raporOlusturMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_ACIKLAMA,raporAciklama);

                raporOlusturMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZAN_USER_ID,raporYazanUserId);
                raporOlusturMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_YAZILAN_USER_ID,raporYazilanUserId);


                SimpleDateFormat tarihFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_ZAMANLI);
                Calendar takvim = Calendar.getInstance();
                takvim.setTime(new Date());
                takvim.add(Calendar.HOUR_OF_DAY, Constants.CALENDAR_HOUR_OF_DAY);
                String raporTarih = tarihFormat.format(takvim.getTime());
                raporOlusturMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_TARIH,raporTarih);
                raporOlusturMap.put(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM,Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM_ONAY_BEKLENIYOR);

                mDatabase.setValue(raporOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),R.string.toastRaporOlusturmaBasarili,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.toastRaporOlusturmaBasarisiz,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}