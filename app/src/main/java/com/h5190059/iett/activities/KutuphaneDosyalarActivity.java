package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.h5190059.iett.R;
import com.h5190059.iett.adaptor.ArizaAdapter;
import com.h5190059.iett.adaptor.KutuphaneDosyaAdapter;
import com.h5190059.iett.model.KutuphaneDosyalarModel;
import com.h5190059.iett.model.RaporModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class KutuphaneDosyalarActivity extends AppCompatActivity {

    private Button btnKutuphaneDosyalarBaslikIcon,btnKutuphaneDosyalarBaslik,btnKutuphaneDosyalarDosyalar,btnKutuphaneDosyalarBilgiler;
    private FirebaseStorage storage;
    private StorageReference storageReference,ref;
    private DatabaseReference mDatabase;
    private ArrayList<KutuphaneDosyalarModel> dosyalar;
    private ListView listViewKutuphaneDosyalar;
    private KutuphaneDosyaAdapter dosyaAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kutuphane_dosyalar);
        init();
    }

    private void init() {
        listViewKutuphaneDosyalar = findViewById(R.id.listViewKutuphaneDosyalar);
        btnKutuphaneDosyalarBaslikIcon = findViewById(R.id.btnKutuphaneDosyalarBaslikIcon);
        btnKutuphaneDosyalarBaslik = findViewById(R.id.btnKutuphaneDosyalarBaslik);
        btnKutuphaneDosyalarDosyalar = findViewById(R.id.btnKutuphaneDosyalarDosyalar);
        btnKutuphaneDosyalarBilgiler = findViewById(R.id.btnKutuphaneDosyalarBilgiler);

        dosyalar = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        dosyaAdapter = new KutuphaneDosyaAdapter(KutuphaneDosyalarActivity.this, dosyalar);


        btnKutuphaneDosyalarBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(KutuphaneDosyalarActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnKutuphaneDosyalarBilgiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kutuphaneBilgilerIntent = new Intent(KutuphaneDosyalarActivity.this, KutuphaneBilgilerActivity.class);
                startActivity(kutuphaneBilgilerIntent);
                finish();
            }
        });
        dosyalariGetir();
    }

    private void dosyalariGetir() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_DOSYALAR);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_DOSYALAR);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int dosyaAdet=1;
                            for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                KutuphaneDosyalarModel kutuphaneDosyalarModel = postSnapshot.getValue(KutuphaneDosyalarModel.class);
                                kutuphaneDosyalarModel.setDosyaAdet(String.valueOf(dosyaAdet));
                                dosyalar.add(
                                        new KutuphaneDosyalarModel(
                                                //String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad
                                                kutuphaneDosyalarModel.getDosyaAdi(),
                                                kutuphaneDosyalarModel.getDosyaAdet(),
                                                kutuphaneDosyalarModel.getDosyaUrl(),
                                                kutuphaneDosyalarModel.getDosyaBaslik()

                                        )
                                );
                                dosyaAdet++;
                                listViewKutuphaneDosyalar.setAdapter(dosyaAdapter);
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