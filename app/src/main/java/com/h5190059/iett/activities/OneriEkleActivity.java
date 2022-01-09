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
import com.h5190059.iett.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class OneriEkleActivity extends AppCompatActivity {

    Button btnOneriEkleBaslikIcon,btnOneriEkleBaslik,btnOneriGonder;
    TextView txtOneriBaslik,txtOneriAciklama;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,mDatabaseSicil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneri_ekle);
        init();
    }

    private void init() {
        btnOneriEkleBaslikIcon = findViewById(R.id.btnOneriEkleBaslikIcon);
        btnOneriEkleBaslik = findViewById(R.id.btnOneriEkleBaslik);
        btnOneriGonder = findViewById(R.id.btnOneriGonder);
        txtOneriBaslik = findViewById(R.id.txtOneriBaslik);
        txtOneriAciklama = findViewById(R.id.txtOneriAciklama);

        mAuth = FirebaseAuth.getInstance();

        btnOneriEkleBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepVeOneriIntent = new Intent(OneriEkleActivity.this, TalepVeOneriActivity.class);
                startActivity(talepVeOneriIntent);
                finish();
            }
        });

        btnOneriGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneriOlustur();
            }
        });
    }

    private void oneriOlustur() {
        String oneriBaslik = txtOneriBaslik.getText().toString().trim().toUpperCase();
        String oneriAciklama = txtOneriAciklama.getText().toString().trim();


        if(oneriBaslik.equals(Constants.BOS_KONTROL) || oneriAciklama.equals(Constants.BOS_KONTROL)){
            Toast.makeText(getApplicationContext(),R.string.toastZorunluBosAlan,Toast.LENGTH_LONG).show();
        }
        else{
            oneriAdetKontrol(oneriBaslik,oneriAciklama);

        }
    }

    private void oneriAdetKontrol(String oneriBaslik, String oneriAciklama) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabaseSicil = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabaseSicil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sicil = dataSnapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ONERILER).child(sicil);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int oneriAdet = 1;
                        ///Arizalar/CUbfJ1tbwMPxqElicEQbKWK7TQp2/ariza1

                        if (!(dataSnapshot.hasChild(Constants.FIREBASE_ONERI_CHILD + oneriAdet))) {
                            oneriEkle(sicil,oneriBaslik,oneriAciklama,oneriAdet,userId);

                        } else {
                            while ((dataSnapshot.hasChild(Constants.FIREBASE_ONERI_CHILD + oneriAdet))) {
                                oneriAdet = oneriAdet + 1;
                            }
                            oneriEkle(sicil,oneriBaslik,oneriAciklama,oneriAdet,userId);
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

    private void oneriEkle(String sicil, String oneriBaslik, String oneriAciklama, int oneriAdet, String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ONERILER).child(sicil).child(Constants.FIREBASE_ONERI+oneriAdet);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String,String> oneriOlusturMap = new HashMap<>();
                oneriOlusturMap.put(Constants.FIREBASE_KULLANICI_SICIL,sicil);
                oneriOlusturMap.put(Constants.FIREBASE_KULLANICI_USER_ID,userId);
                oneriOlusturMap.put(Constants.FIREBASE_ONERI_BASLIK,oneriBaslik);
                oneriOlusturMap.put(Constants.FIREBASE_ONERI_ACIKLAMA,oneriAciklama);
                SimpleDateFormat tarihFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_ZAMANLI);
                Calendar takvim = Calendar.getInstance();
                takvim.setTime(new Date());
                String oneriTarih = tarihFormat.format(takvim.getTime());
                oneriOlusturMap.put(Constants.FIREBASE_ONERI_TARIH,oneriTarih.toString());
                oneriOlusturMap.put(Constants.FIREBASE_ONERI_DURUM,Constants.FIREBASE_ONERI_DURUM_ONAY_BEKLENIYOR);

                mDatabase.setValue(oneriOlusturMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),R.string.toastOneriOlusturmaBasarili,Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.toastOneriOlusturmaBasarisiz,Toast.LENGTH_LONG).show();
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