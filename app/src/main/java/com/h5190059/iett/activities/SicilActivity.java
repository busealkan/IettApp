package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

public class SicilActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSicilLeftIcon,btnSicilBaslik,btnCezalarim,btnKazalarim,btnUyariVeKararlarim;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ConstraintLayout constraintLayout;
    private ConstraintSet applyConstraintSet;
    private ConstraintSet resetConstraintSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sicil);
        init();
    }

    private void init() {
        btnSicilLeftIcon = findViewById(R.id.btnSicilLeftIcon);
        btnSicilBaslik = findViewById(R.id.btnSicilBaslik);
        btnCezalarim = findViewById(R.id.btnCezalarim);
        btnKazalarim = findViewById(R.id.btnKazalarim);
        btnUyariVeKararlarim = findViewById(R.id.btnUyariVeKararlarim);

        constraintLayout = findViewById(R.id.sicilLayout);
        applyConstraintSet = new ConstraintSet();
        resetConstraintSet = new ConstraintSet();
        resetConstraintSet.clone(constraintLayout);
        applyConstraintSet.clone(constraintLayout);

        btnSicilLeftIcon.setOnClickListener(this);
        btnSicilBaslik.setOnClickListener(this);
        btnCezalarim.setOnClickListener(this);
        btnKazalarim.setOnClickListener(this);
        btnUyariVeKararlarim.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        gorevBilgisiGetir();

    }

    private void gorevBilgisiGetir() {
        String userId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString().toLowerCase();
                gorevKontrolEt(gorevi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gorevKontrolEt(String gorevi) {
        if (gorevi.toLowerCase().equals(Constants.GOREV_AMIR.toLowerCase())) {
            btnKazalarim.setVisibility(View.INVISIBLE);

        } else if (gorevi.toLowerCase().equals(Constants.GOREV_SEF.toLowerCase())) {
            btnKazalarim.setVisibility(View.INVISIBLE);
        } else if (gorevi.equals(Constants.GOREV_BOLGE_YONETICISI.toLowerCase())) {
            btnKazalarim.setVisibility(View.INVISIBLE);
        } else {
            btnKazalarim.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSicilLeftIcon:
                Intent anaSayfaIntent = new Intent(SicilActivity.this, AnaSayfaActivity.class);
                startActivity(anaSayfaIntent);
                finish();
                break;

            case R.id.btnCezalarim:
                Intent cezalarimIntent = new Intent(SicilActivity.this, CezalarActivity.class);
                startActivity(cezalarimIntent);
                break;

            case R.id.btnKazalarim:
                Intent kazaIntent = new Intent(SicilActivity.this, KazalarActivity.class);
                startActivity(kazaIntent);
                break;

            case R.id.btnUyariVeKararlarim:
                Intent uyariVeKararlarimIntent = new Intent(SicilActivity.this, UyariVeKararlarActivity.class);
                startActivity(uyariVeKararlarimIntent);
                break;
        }
    }


}