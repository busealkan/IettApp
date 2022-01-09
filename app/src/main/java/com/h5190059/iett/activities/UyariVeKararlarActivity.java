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
import com.h5190059.iett.adaptor.UyariVeKararlarAdapter;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.model.UyariVeKararlarModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class UyariVeKararlarActivity extends AppCompatActivity {

    private ArrayList<UyariVeKararlarModel> uyariVeKararlar;
    private ArrayList<Users> users;
    private Button btnUyariVeKararlarBaslikIcon, btnUyariVeKararlarBaslik,btnUyariVeKararlarTarihBaslik,btnUyariVeKararlarAciklamaBaslik,btnUyariVeKararlarDisiplinBaslik;
    private UyariVeKararlarAdapter uyariVeKararlarAdapter;
    private DatabaseReference mDatabase;
    private ListView listViewUyariVeKararlar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uyari_ve_kararlar);
        init();
    }

    private void init() {
        listViewUyariVeKararlar = findViewById(R.id.listViewUyariVeKararlar);
        btnUyariVeKararlarBaslikIcon = findViewById(R.id.btnUyariVeKararlarBaslikIcon);
        btnUyariVeKararlarBaslik = findViewById(R.id.btnUyariVeKararlarBaslik);
        btnUyariVeKararlarTarihBaslik = findViewById(R.id.btnUyariVeKararlarTarihBaslik);
        btnUyariVeKararlarAciklamaBaslik = findViewById(R.id.btnUyariVeKararlarAciklamaBaslik);
        btnUyariVeKararlarDisiplinBaslik = findViewById(R.id.btnUyariVeKararlarDisiplinBaslik);

        mAuth = FirebaseAuth.getInstance();
        uyariVeKararlar = new ArrayList<>();
        users = new ArrayList<>();
        uyariVeKararlarAdapter = new UyariVeKararlarAdapter(UyariVeKararlarActivity.this, uyariVeKararlar);

        btnUyariVeKararlarBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(UyariVeKararlarActivity.this, SicilActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });
        uyariVeKararlariGetir();
    }

    private void uyariVeKararlariGetir() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_UYARI_VE_KARARLAR);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(Constants.FIREBASE_CHILD + userId)) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_UYARI_VE_KARARLAR).child(userId);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int uyariVeKararAdet = 1;
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    //    public ArizaModel(String arizaAciklama, String arizaBaslik, String arizaDurumu, String arizaTarih, String hatIstikamet, String hatKodu, String userId, String arizaAdet) {
                                    UyariVeKararlarModel uyariVeKarar = postSnapshot.getValue(UyariVeKararlarModel.class);
                                    uyariVeKarar.setUyariVeKararAdet(String.valueOf(uyariVeKararAdet));
                                    uyariVeKararlar.add(
                                            new UyariVeKararlarModel(
                                                    uyariVeKarar.getUyariVeKararTarihi(),
                                                    uyariVeKarar.getUyariVeKararAciklama(),
                                                    uyariVeKarar.getUyariVeKararDisiplin(),
                                                    uyariVeKarar.getUyariVeKararAdet(),
                                                    uyariVeKarar.getUyariVeKararSicil()
                                            )
                                    );
                                    uyariVeKararAdet++;

                                    listViewUyariVeKararlar.setAdapter(uyariVeKararlarAdapter);
                                }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(UyariVeKararlarActivity.this, getResources().getString(R.string.toastUyariVeKararlar), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}