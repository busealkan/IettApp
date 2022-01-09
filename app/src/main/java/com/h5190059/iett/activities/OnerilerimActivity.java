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
import com.h5190059.iett.adaptor.OneriAdapter;
import com.h5190059.iett.model.OneriModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class OnerilerimActivity extends AppCompatActivity {

    private Button btnOnerilerimLeftIcon, btnOnerilerimBaslik, btnOnerilerimAddIcon;
    private ListView listViewOnerilerim;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<Users> users;
    private ArrayList<OneriModel> oneriler;
    private OneriAdapter oneriAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onerilerim);
        init();

    }

    private void init() {
        btnOnerilerimLeftIcon = findViewById(R.id.btnOnerilerimLeftIcon);
        btnOnerilerimBaslik = findViewById(R.id.btnOnerilerimBaslik);
        btnOnerilerimAddIcon = findViewById(R.id.btnOnerilerimAddIcon);
        listViewOnerilerim = findViewById(R.id.listViewOnerilerim);

        oneriler = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        oneriAdapter = new OneriAdapter(OnerilerimActivity.this, oneriler);

        btnOnerilerimLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepVeOneriIntent = new Intent(OnerilerimActivity.this, TalepVeOneriActivity.class);
                startActivity(talepVeOneriIntent);
                finish();
            }
        });

        btnOnerilerimAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oneriEkleIntent = new Intent(OnerilerimActivity.this, OneriEkleActivity.class);
                startActivity(oneriEkleIntent);
                finish();
            }
        });

        onerilerimiListele();

    }

    private void onerilerimiListele() {
        mAuth = FirebaseAuth.getInstance();
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
                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ONERILER).child(sicil);
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int oneriAdet = 1;
                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
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
                                        listViewOnerilerim.setAdapter(oneriAdapter);
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