package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.h5190059.iett.adaptor.ArizaAdapter;
import com.h5190059.iett.adaptor.TalepAdapter;
import com.h5190059.iett.model.ArizaModel;
import com.h5190059.iett.model.TalepModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class TalepListeleActivity extends AppCompatActivity {

    private Button btnTaleplerLeftIcon,btnTaleplerBaslik,btnTaleplerAddIcon;
    private ListView listViewTalepler;
    private ArrayList<Users> users;
    private ArrayList<TalepModel> talepler;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TalepAdapter talepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_listele);
        init();

    }

    private void init() {
        btnTaleplerLeftIcon = findViewById(R.id.btnTaleplerLeftIcon);
        btnTaleplerBaslik = findViewById(R.id.btnTaleplerBaslik);
        btnTaleplerAddIcon = findViewById(R.id.btnTaleplerAddIcon);
        listViewTalepler = findViewById(R.id.listViewTalepler);

        users = new ArrayList<>();
        talepler = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        talepAdapter = new TalepAdapter(TalepListeleActivity.this, talepler);

        btnTaleplerLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepVeOneriIntent = new Intent(TalepListeleActivity.this, TalepVeOneriActivity.class);
                startActivity(talepVeOneriIntent);
                finish();
            }
        });

        btnTaleplerAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepEkleIntent = new Intent(TalepListeleActivity.this, TalepEkleActivity.class);
                startActivity(talepEkleIntent);
                finish();
            }
        });

        sefTalepleriListele();
    }

    private void sefTalepleriListele() {
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
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TALEPLER);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Constants.FIREBASE_CHILD + sicil)) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TALEPLER).child(sicil);
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int talepAdet = 1;
                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            String talepDurum = snapshot.child(Constants.FIREBASE_TALEP + String.valueOf(talepAdet)).child(Constants.FIREBASE_TALEP_DURUM).getValue().toString();
                                            if (!(talepDurum.equals(Constants.FIREBASE_TALEP_DURUMU_FALSE))) {
                                                //    public ArizaModel(String arizaAciklama, String arizaBaslik, String arizaDurumu, String arizaTarih, String hatIstikamet, String hatKodu, String userId, String arizaAdet) {
                                                TalepModel talep = postSnapshot.getValue(TalepModel.class);
                                                talep.setTalepAdet(String.valueOf(talepAdet));
                                                talepler.add(
                                                        new TalepModel(
                                                                talep.getTalepBaslik(),
                                                                talep.getTalepAciklama(),
                                                                talep.getTalepDurum(),
                                                                talep.getTalepTarih(),
                                                                talep.getSicil(),
                                                                talep.getUserId(),
                                                                talep.getTalepAdet()
                                                        )
                                                );
                                                talepAdet++;
                                                listViewTalepler.setAdapter(talepAdapter);
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