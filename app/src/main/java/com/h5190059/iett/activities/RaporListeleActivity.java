package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.adaptor.RaporAdapter;
import com.h5190059.iett.model.RaporModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;


public class RaporListeleActivity extends AppCompatActivity {
    private ArrayList<RaporModel> raporlar;
    private ArrayList<Users> users;
    private Button btnRaporlarBaslikIcon, btnRaporlarBaslik;
    private RaporAdapter raporAdapter;
    private DatabaseReference mDatabase;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapor_listele);
        init();
    }

    private void init() {
        listView = findViewById(R.id.listViewRaporlar);
        btnRaporlarBaslikIcon = findViewById(R.id.btnRaporlarBaslikIcon);
        btnRaporlarBaslik = findViewById(R.id.btnRaporlarBaslik);
        raporlar = new ArrayList<>();
        users = new ArrayList<>();
        raporAdapter = new RaporAdapter(RaporListeleActivity.this, raporlar);

        btnRaporlarBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(RaporListeleActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });
        raporlariGetir();
    }

    private void raporlariGetir() {
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

                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Constants.FIREBASE_CHILD+ sicil)) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TUTANAKLAR).child(sicil);
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int raporAdet=1;
                                        for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            String raporDurum = snapshot.child(Constants.FIREBASE_TUTANAK+String.valueOf(raporAdet)).child(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM).getValue().toString();
                                            if(raporDurum.equals(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM_TRUE) || raporDurum.equals(Constants.FIREBASE_TUTANAKLAR_TUTANAK_DURUM_ONAY_BEKLENIYOR)){
                                                RaporModel rapor = postSnapshot.getValue(RaporModel.class);
                                                rapor.setTutanakAdet(String.valueOf(raporAdet));
                                                raporlar.add(
                                                        new RaporModel(
                                                                rapor.getTutanakAciklama(),
                                                                rapor.getTutanakBaslik(),
                                                                rapor.getTutanakDurum(),
                                                                rapor.getTutanakTarih(),
                                                                rapor.getTutanakYazanSicil(),
                                                                rapor.getTutanakYazilanSicil(),
                                                                rapor.getTutanakAdet(),
                                                                rapor.getTutanakYazilanUserId(),
                                                                rapor.getTutanakYazanUserId()
                                                        )
                                                );
                                                raporAdet++;
                                                listView.setAdapter(raporAdapter);
                                            }
                                            else{
                                                raporAdet++;

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

