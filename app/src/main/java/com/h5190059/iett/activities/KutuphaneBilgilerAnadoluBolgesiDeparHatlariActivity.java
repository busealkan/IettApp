package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.h5190059.iett.R;
import com.h5190059.iett.adaptor.AnadoluBolgesiDeparHatlariAdapter;
import com.h5190059.iett.adaptor.KutuphaneBilgilerAdapter;
import com.h5190059.iett.adaptor.KutuphaneDosyaAdapter;
import com.h5190059.iett.model.AnadoluBolgesiDeparHatlariModel;
import com.h5190059.iett.model.KutuphaneDosyalarModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class KutuphaneBilgilerAnadoluBolgesiDeparHatlariActivity extends AppCompatActivity {

    private Button btnKutuphaneBilgilerAnadoluBolgesiDeparHatlariBaslikIcon,btnKutuphaneBilgilerAnadoluBolgesiDeparHatlariBaslik,btnAnadoluBolgesiDeparHatlari;

    private FirebaseStorage storage;
    private StorageReference storageReference,ref;
    private DatabaseReference mDatabase;
    private ArrayList<AnadoluBolgesiDeparHatlariModel> anadoluBolgesiDeparHatlari;
    private ListView listViewAnadoluBolgesiDeparHatlari;
    private AnadoluBolgesiDeparHatlariAdapter anadoluBolgesiDeparHatlariAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kutuphane_bilgiler_anadolu_bolgesi_depar_hatlari);
        init();
    }

    private void init() {
        listViewAnadoluBolgesiDeparHatlari = findViewById(R.id.listViewAnadoluBolgesiDeparHatlari);
        btnKutuphaneBilgilerAnadoluBolgesiDeparHatlariBaslikIcon = findViewById(R.id.btnKutuphaneBilgilerAnadoluBolgesiDeparHatlariBaslikIcon);
        btnKutuphaneBilgilerAnadoluBolgesiDeparHatlariBaslik = findViewById(R.id.btnKutuphaneBilgilerAnadoluBolgesiDeparHatlariBaslik);
        btnAnadoluBolgesiDeparHatlari = findViewById(R.id.btnAnadoluBolgesiDeparHatlari);

        anadoluBolgesiDeparHatlari = new ArrayList<>();
        anadoluBolgesiDeparHatlariAdapter = new AnadoluBolgesiDeparHatlariAdapter(KutuphaneBilgilerAnadoluBolgesiDeparHatlariActivity.this, anadoluBolgesiDeparHatlari);

        btnKutuphaneBilgilerAnadoluBolgesiDeparHatlariBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kutuphaneBilgilerIntent = new Intent(KutuphaneBilgilerAnadoluBolgesiDeparHatlariActivity.this, KutuphaneBilgilerActivity.class);
                startActivity(kutuphaneBilgilerIntent);
                finish();
            }
        });

        anadoluBolgesiDeparHatlariGetir();

    }

    private void anadoluBolgesiDeparHatlariGetir() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_BILGILER);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_BILGILER).child(Constants.FIREBASE_KUTUPHANE_BILGILER_ANADOLU_BOLGESI_DEPAR_HATLARI);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int deparHatAdet=1;
                        for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String bilgilerBaslik = postSnapshot.getKey();
                            if(bilgilerBaslik.contains(Constants.FIREBASE_KUTUPHANE_BILGILER_DEPAR)){
                                AnadoluBolgesiDeparHatlariModel anadoluBolgesiDeparHatlariModel = postSnapshot.getValue(AnadoluBolgesiDeparHatlariModel.class);
                                anadoluBolgesiDeparHatlari.add(
                                        new AnadoluBolgesiDeparHatlariModel(
                                                //String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad
                                                anadoluBolgesiDeparHatlariModel.getDeparHatKod(),
                                                anadoluBolgesiDeparHatlariModel.getDeparHatKodBolgeleri(),
                                                anadoluBolgesiDeparHatlariModel.getDeparHatAdet()

                                        )
                                );
                                deparHatAdet++;
                                listViewAnadoluBolgesiDeparHatlari.setAdapter(anadoluBolgesiDeparHatlariAdapter);
                            }


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