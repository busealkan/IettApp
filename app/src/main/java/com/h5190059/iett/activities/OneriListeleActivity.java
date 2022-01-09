package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.adaptor.OneriAdapter;
import com.h5190059.iett.adaptor.TalepAdapter;
import com.h5190059.iett.model.OneriModel;
import com.h5190059.iett.model.TalepModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class OneriListeleActivity extends AppCompatActivity {

    private Button btnOnerilerLeftIcon, btnOnerilerBaslik, btnOnerilerAddIcon;
    private ListView listViewOneriler;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<Users> users;
    private ArrayList<OneriModel> oneriler;
    private OneriAdapter oneriAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneri_listele);
        init();
    }

    private void init() {
        btnOnerilerLeftIcon = findViewById(R.id.btnOnerilerLeftIcon);
        btnOnerilerBaslik = findViewById(R.id.btnOnerilerBaslik);
        btnOnerilerAddIcon = findViewById(R.id.btnOnerilerAddIcon);
        listViewOneriler = findViewById(R.id.listViewOneriler);

        users = new ArrayList<>();
        oneriler = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        oneriAdapter = new OneriAdapter(OneriListeleActivity.this, oneriler);

        btnOnerilerLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepVeOneriIntent = new Intent(OneriListeleActivity.this, TalepVeOneriActivity.class);
                startActivity(talepVeOneriIntent);
                finish();
            }
        });

        btnOnerilerAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oneriEkleIntent = new Intent(OneriListeleActivity.this, OneriEkleActivity.class);
                startActivity(oneriEkleIntent);
                finish();
            }
        });


        sefOnerileriListele();
    }

    private void sefOnerileriListele() {
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

                for (int i = 0; i < users.size(); i++) {
                    String sicil = users.get(i).getSicil();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ONERILER);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Constants.FIREBASE_CHILD + sicil)) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ONERILER).child(sicil);
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int oneriAdet = 1;
                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            String oneriDurum = snapshot.child(Constants.FIREBASE_ONERI + String.valueOf(oneriAdet)).child(Constants.FIREBASE_ONERI_DURUM).getValue().toString();
                                            if (!(oneriDurum.equals(Constants.FIREBASE_ONERI_DURUMU_FALSE))) {
                                                //    public ArizaModel(String arizaAciklama, String arizaBaslik, String arizaDurumu, String arizaTarih, String hatIstikamet, String hatKodu, String userId, String arizaAdet) {
                                                OneriModel oneri = postSnapshot.getValue(OneriModel.class);
                                                oneri.setOneriAdet(String.valueOf(oneriAdet));
                                                oneriler.add(
                                                        new OneriModel(
                                                                oneri.getOneriBaslik(),
                                                                oneri.getOneriAciklama(),
                                                                oneri.getOneriDurum(),
                                                                oneri.getOneriTarih(),
                                                                oneri.getSicil(),
                                                                oneri.getUserId(),
                                                                oneri.getOneriAdet()
                                                        )
                                                );
                                                oneriAdet++;
                                                listViewOneriler.setAdapter(oneriAdapter);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}