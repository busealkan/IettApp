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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.h5190059.iett.R;
import com.h5190059.iett.adaptor.KutuphaneBilgilerAdapter;
import com.h5190059.iett.adaptor.KutuphaneDosyaAdapter;
import com.h5190059.iett.model.KutuphaneBilgilerModel;
import com.h5190059.iett.model.KutuphaneDosyalarModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class KutuphaneBilgilerActivity extends AppCompatActivity {

    private Button btnKutuphaneBilgilerBaslikIcon,btnKutuphaneBilgilerBaslik,btnKutuphaneBilgilerDosyalar,btnKutuphaneBilgilerBilgiler;
    private FirebaseStorage storage;
    private StorageReference storageReference,ref;
    private DatabaseReference mDatabase;
    private ArrayList<KutuphaneBilgilerModel> bilgiler;
    private ListView listViewKutuphaneBilgiler;
    private KutuphaneBilgilerAdapter bilgiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kutuphane_bilgiler);
        init();
    }

    private void init() {
        listViewKutuphaneBilgiler = findViewById(R.id.listViewKutuphaneBilgiler);
        btnKutuphaneBilgilerBaslikIcon = findViewById(R.id.btnKutuphaneBilgilerBaslikIcon);
        btnKutuphaneBilgilerBaslik = findViewById(R.id.btnKutuphaneBilgilerBaslik);
        btnKutuphaneBilgilerDosyalar = findViewById(R.id.btnKutuphaneBilgilerDosyalar);
        btnKutuphaneBilgilerBilgiler = findViewById(R.id.btnKutuphaneBilgilerBilgiler);

        bilgiler = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        bilgiAdapter = new KutuphaneBilgilerAdapter(KutuphaneBilgilerActivity.this, bilgiler);

        btnKutuphaneBilgilerBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(KutuphaneBilgilerActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnKutuphaneBilgilerDosyalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kutuphaneDosyalarIntent = new Intent(KutuphaneBilgilerActivity.this, KutuphaneDosyalarActivity.class);
                startActivity(kutuphaneDosyalarIntent);
                finish();
            }
        });
        bilgileriGetir();


    }

    private void bilgileriGetir() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_BILGILER);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_BILGILER);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int bilgiAdet=1;
                        for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                            KutuphaneBilgilerModel kutuphaneBilgilerModel = postSnapshot.getValue(KutuphaneBilgilerModel.class);
                            bilgiler.add(
                                    new KutuphaneBilgilerModel(
                                            //String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad
                                            kutuphaneBilgilerModel.getBilgilerBaslik()

                                    )
                            );
                            bilgiAdet++;
                            listViewKutuphaneBilgiler.setAdapter(bilgiAdapter);
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