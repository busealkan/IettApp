package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.adaptor.ArizaAdapter;
import com.h5190059.iett.adaptor.RaporAdapter;
import com.h5190059.iett.model.ArizaModel;
import com.h5190059.iett.model.RaporModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class ArizaListeleActivity extends AppCompatActivity {
    private ArrayList<ArizaModel> arizalar;
    private ArrayList<Users> users;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button btnArizalarimBaslik, btnArizalarimBaslikIcon;
    private ListView listViewArizalar;
    private ArizaAdapter arizaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariza_listele);
        init();

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        listViewArizalar = findViewById(R.id.listViewArizalar);
        btnArizalarimBaslik = findViewById(R.id.btnArizalarimBaslik);
        btnArizalarimBaslikIcon = findViewById(R.id.btnArizalarimBaslikIcon);

        arizalar = new ArrayList<>();
        users = new ArrayList<>();
        arizaAdapter = new ArizaAdapter(ArizaListeleActivity.this, arizalar);

        btnArizalarimBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(ArizaListeleActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        gorevKontrolEt();
    }

    private void gorevKontrolEt() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString();
                if (gorevi.toLowerCase().equals(Constants.GOREV_SOFOR.toLowerCase())) {
                    soforArizalariListele(userId);
                } else {
                    btnArizalarimBaslik.setText(R.string.arizalar);
                    bolgeYoneticisiArizalariListele();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void soforArizalariListele(String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(Constants.FIREBASE_CHILD + sicil)) {
                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil);
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int arizaAdet = 1;
                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                        //    public ArizaModel(String arizaAciklama, String arizaBaslik, String arizaDurumu, String arizaTarih, String hatIstikamet, String hatKodu, String userId, String arizaAdet) {
                                        ArizaModel ariza = postSnapshot.getValue(ArizaModel.class);
                                        ariza.setArizaAdet(String.valueOf(arizaAdet));
                                        arizalar.add(
                                                new ArizaModel(
                                                        ariza.getArizaAciklama(),
                                                        ariza.getArizaBaslik(),
                                                        ariza.getArizaDurumu(),
                                                        ariza.getArizaTarih(),
                                                        ariza.getHatIstikamet(),
                                                        ariza.getHatKodu(),
                                                        ariza.getUserId(),
                                                        ariza.getArizaAdet()
                                                )
                                        );
                                        arizaAdet++;
                                        listViewArizalar.setAdapter(arizaAdapter);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bolgeYoneticisiArizalariListele() {
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
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Constants.FIREBASE_CHILD + sicil)) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil);
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int arizaAdet = 1;
                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            String arizaDurum = snapshot.child(Constants.FIREBASE_ARIZA + String.valueOf(arizaAdet)).child(Constants.FIREBASE_ARIZA_DURUMU).getValue().toString();
                                            if ((arizaDurum.equals(Constants.FIREBASE_ARIZA_DURUMU_TRUE)) || (arizaDurum.equals(Constants.FIREBASE_ARIZA_DURUMU_ONAY_BEKLENIYOR))) {
                                                //    public ArizaModel(String arizaAciklama, String arizaBaslik, String arizaDurumu, String arizaTarih, String hatIstikamet, String hatKodu, String userId, String arizaAdet) {
                                                ArizaModel ariza = postSnapshot.getValue(ArizaModel.class);
                                                ariza.setArizaAdet(String.valueOf(arizaAdet));
                                                arizalar.add(
                                                        new ArizaModel(
                                                                ariza.getArizaAciklama(),
                                                                ariza.getArizaBaslik(),
                                                                ariza.getArizaDurumu(),
                                                                ariza.getArizaTarih(),
                                                                ariza.getHatIstikamet(),
                                                                ariza.getHatKodu(),
                                                                ariza.getUserId(),
                                                                ariza.getArizaAdet()
                                                        )
                                                );
                                                arizaAdet++;
                                                listViewArizalar.setAdapter(arizaAdapter);
                                            }
                                            else{
                                                arizaAdet++;

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