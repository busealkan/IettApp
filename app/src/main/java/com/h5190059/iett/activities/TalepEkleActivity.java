package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TalepEkleActivity extends AppCompatActivity {

    Button btnTalepEkleBaslikIcon,btnTalepEkleBaslik,btnTalepGonder;
    TextView txtTalepBaslik,txtTalepAciklama;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,mDatabaseSicil;
    private ArrayList<Users> users;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_ekle);
        init();
    }

    private void init() {
        btnTalepEkleBaslikIcon = findViewById(R.id.btnTalepEkleBaslikIcon);
        btnTalepEkleBaslik = findViewById(R.id.btnTalepEkleBaslik);
        btnTalepGonder = findViewById(R.id.btnTalepGonder);
        txtTalepBaslik = findViewById(R.id.txtTalepBaslik);
        txtTalepAciklama = findViewById(R.id.txtTalepAciklama);

        mAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        i=0;


        btnTalepEkleBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepVeOneriIntent = new Intent(TalepEkleActivity.this, TalepVeOneriActivity.class);
                startActivity(talepVeOneriIntent);
                finish();
            }
        });

        btnTalepGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talepOlustur();
            }
        });
    }

    private void talepOlustur() {
        String talepBaslik = txtTalepBaslik.getText().toString().trim().toUpperCase();
        String talepAciklama = txtTalepAciklama.getText().toString().trim();


        if(talepBaslik.equals(Constants.BOS_KONTROL) || talepAciklama.equals(Constants.BOS_KONTROL)){
            Toast.makeText(getApplicationContext(),R.string.toastZorunluBosAlan,Toast.LENGTH_LONG).show();
        }
        else{
            talepAdetKontrol(talepBaslik,talepAciklama);
        }
    }

    private void talepAdetKontrol(String talepBaslik, String talepAciklama) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabaseSicil = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabaseSicil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sicil = dataSnapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TALEPLER).child(sicil);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int talepAdet = 1;
                        ///Arizalar/CUbfJ1tbwMPxqElicEQbKWK7TQp2/ariza1

                        if (!(dataSnapshot.hasChild(Constants.FIREBASE_TALEP_CHILD + talepAdet))) {
                            talepEkle(sicil,talepBaslik,talepAciklama,talepAdet,userId);

                        } else {
                            while ((dataSnapshot.hasChild(Constants.FIREBASE_TALEP_CHILD + talepAdet))) {
                                talepAdet = talepAdet + 1;
                            }
                            talepEkle(sicil,talepBaslik,talepAciklama,talepAdet,userId);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void talepEkle(String sicil, String talepBaslik, String talepAciklama, int talepAdet, String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TALEPLER).child(sicil).child(Constants.FIREBASE_TALEP+talepAdet);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String,String> talepOlusturMap = new HashMap<>();
                talepOlusturMap.put(Constants.FIREBASE_KULLANICI_SICIL,sicil);
                talepOlusturMap.put(Constants.FIREBASE_KULLANICI_USER_ID,userId);
                talepOlusturMap.put(Constants.FIREBASE_TALEP_BASLIK,talepBaslik);
                talepOlusturMap.put(Constants.FIREBASE_TALEP_ACIKLAMA,talepAciklama);

                SimpleDateFormat tarihFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_ZAMANLI);
                Calendar takvim = Calendar.getInstance();
                takvim.setTime(new Date());
                String talepTarih = tarihFormat.format(takvim.getTime());

                talepOlusturMap.put(Constants.FIREBASE_TALEP_TARIH,talepTarih.toString());
                talepOlusturMap.put(Constants.FIREBASE_TALEP_DURUM,Constants.FIREBASE_TALEP_DURUM_ONAY_BEKLENIYOR);

                mDatabase.setValue(talepOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),R.string.toastTalepOlusturmaBasarili,Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.toastTalepOlusturmaBasarisiz,Toast.LENGTH_SHORT).show();
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