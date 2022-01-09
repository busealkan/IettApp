package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

public class TalepVeOneriActivity extends AppCompatActivity {

    private Button btnTalepveOneriLeftIcon,btnTalepveOneri,btnTalepEkle,btnTaleplerim,btnTalepler,btnOneriEkle,btnOnerilerim,btnOneriler;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ConstraintLayout constraintLayout;
    private ConstraintSet applyConstraintSet;
    private ConstraintSet resetConstraintSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_ve_oneri);
        init();
    }

    private void init() {
        btnTalepveOneriLeftIcon = findViewById(R.id.btnTalepveOneriLeftIcon);
        btnTalepveOneri = findViewById(R.id.btnTalepveOneri);
        btnTalepEkle = findViewById(R.id.btnTalepEkle);
        btnTaleplerim = findViewById(R.id.btnTaleplerim);
        btnTalepler = findViewById(R.id.btnTalepler);
        btnOneriEkle = findViewById(R.id.btnOneriEkle);
        btnOnerilerim = findViewById(R.id.btnOnerilerim);
        btnOneriler = findViewById(R.id.btnOneriler);

        constraintLayout = findViewById(R.id.talepVeOneriLayout);
        applyConstraintSet = new ConstraintSet();
        resetConstraintSet = new ConstraintSet();
        resetConstraintSet.clone(constraintLayout);
        applyConstraintSet.clone(constraintLayout);

        mAuth = FirebaseAuth.getInstance();

        gorevKontrolEt();


        btnTalepveOneriLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anaSayfaIntent = new Intent(TalepVeOneriActivity.this, AnaSayfaActivity.class);
                startActivity(anaSayfaIntent);
                finish();
            }
        });

        btnTalepEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepEkleIntent = new Intent(TalepVeOneriActivity.this, TalepEkleActivity.class);
                startActivity(talepEkleIntent);
                finish();
            }
        });

        btnTaleplerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taleplerimIntent = new Intent(TalepVeOneriActivity.this, TaleplerimActivity.class);
                startActivity(taleplerimIntent);
                finish();
            }
        });

        btnTalepler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taleplerimIntent = new Intent(TalepVeOneriActivity.this, TalepListeleActivity.class);
                startActivity(taleplerimIntent);
                finish();
            }
        });

        btnOneriEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oneriEkleIntent = new Intent(TalepVeOneriActivity.this, OneriEkleActivity.class);
                startActivity(oneriEkleIntent);
                finish();
            }
        });

        btnOnerilerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent onerilerimIntent = new Intent(TalepVeOneriActivity.this, OnerilerimActivity.class);
                startActivity(onerilerimIntent);
                finish();
            }
        });

        btnOneriler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent onerilerimIntent = new Intent(TalepVeOneriActivity.this, OneriListeleActivity.class);
                startActivity(onerilerimIntent);
                finish();
            }
        });
    }

    private void gorevKontrolEt() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString().toLowerCase();
                if(!(gorevi.equals(Constants.GOREV_SEF.toLowerCase()))){

                    btnTalepler.setVisibility(View.INVISIBLE);

                    applyConstraintSet.connect(R.id.btnOneriEkle, ConstraintSet.TOP, R.id.btnTaleplerim, ConstraintSet.BOTTOM, 0);
                    applyConstraintSet.applyTo(constraintLayout);

                    btnOneriler.setVisibility(View.INVISIBLE);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}