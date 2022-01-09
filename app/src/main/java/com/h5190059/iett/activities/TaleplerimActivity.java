package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.adaptor.TalepAdapter;
import com.h5190059.iett.model.TalepModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class TaleplerimActivity extends AppCompatActivity {

    private Button btnTaleplerimLeftIcon,btnTaleplerimBaslik,btnTaleplerimAddIcon;
    private ListView listViewTaleplerim;
    private ArrayList<Users> users;
    private ArrayList<TalepModel> talepler;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TalepAdapter talepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taleplerim);
        init();
    }

    private void init() {
        btnTaleplerimLeftIcon = findViewById(R.id.btnTaleplerimLeftIcon);
        btnTaleplerimBaslik = findViewById(R.id.btnTaleplerimBaslik);
        btnTaleplerimAddIcon = findViewById(R.id.btnTaleplerimAddIcon);
        listViewTaleplerim = findViewById(R.id.listViewTaleplerim);

        talepler = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        talepAdapter = new TalepAdapter(TaleplerimActivity.this, talepler);

        btnTaleplerimLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepVeOneriIntent = new Intent(TaleplerimActivity.this, TalepVeOneriActivity.class);
                startActivity(talepVeOneriIntent);
                finish();
            }
        });

        btnTaleplerimAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepEkleIntent = new Intent(TaleplerimActivity.this, TalepEkleActivity.class);
                startActivity(talepEkleIntent);
                finish();
            }
        });

        taleplerimiListele();
    }

    private void taleplerimiListele() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
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
                                        listViewTaleplerim.setAdapter(talepAdapter);
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
}