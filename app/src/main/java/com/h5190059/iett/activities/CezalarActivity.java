package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.h5190059.iett.adaptor.CezaAdapter;
import com.h5190059.iett.model.CezaModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class CezalarActivity extends AppCompatActivity {
    private ArrayList<CezaModel> cezalar;
    private ArrayList<Users> users;
    private Button btnCezalarBaslikIcon, btnCezalarBaslik,btnCezaYeriBaslik,btnCezaBaslik,btnCezaTarihBaslik;
    private CezaAdapter cezaAdapter;
    private DatabaseReference mDatabase;
    private ListView listViewCezalar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cezalar);
        init();
    }

    private void init() {
        listViewCezalar = findViewById(R.id.listViewCezalar);
        btnCezalarBaslikIcon = findViewById(R.id.btnCezalarBaslikIcon);
        btnCezalarBaslik = findViewById(R.id.btnCezalarBaslik);
        btnCezaYeriBaslik = findViewById(R.id.btnCezaYeriBaslik);
        btnCezaBaslik = findViewById(R.id.btnCezaBaslik);
        btnCezaTarihBaslik = findViewById(R.id.btnCezaTarihBaslik);

        mAuth = FirebaseAuth.getInstance();
        cezalar = new ArrayList<>();
        users = new ArrayList<>();
        cezaAdapter = new CezaAdapter(CezalarActivity.this, cezalar);

        btnCezalarBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(CezalarActivity.this, SicilActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });
        cezalariGetir();
    }

    private void cezalariGetir() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CEZALAR);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(Constants.FIREBASE_CHILD + userId)) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CEZALAR).child(userId);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int cezaAdet = 1;

                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                //    public ArizaModel(String arizaAciklama, String arizaBaslik, String arizaDurumu, String arizaTarih, String hatIstikamet, String hatKodu, String userId, String arizaAdet) {
                                CezaModel ceza = postSnapshot.getValue(CezaModel.class);
                                ceza.setCezaAdet(String.valueOf(cezaAdet));
                                cezalar.add(
                                        new CezaModel(
                                                ceza.getCezaYeri(),
                                                ceza.getCeza(),
                                                ceza.getCezaTarihi(),
                                                ceza.getCezaSicil(),
                                                ceza.getCezaAdet()
                                        )
                                );
                                cezaAdet++;

                                listViewCezalar.setAdapter(cezaAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(CezalarActivity.this, getResources().getString(R.string.toastCezalar), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}