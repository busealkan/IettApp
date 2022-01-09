package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.adaptor.AcilTelefonlarAdapter;
import com.h5190059.iett.adaptor.AvrupaBolgesiDeparHatlariAdapter;
import com.h5190059.iett.model.AcilTelefonlarModel;
import com.h5190059.iett.model.AnadoluBolgesiDeparHatlariModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class KutuphaneBilgilerAcilTelefonlarActivity extends AppCompatActivity {

    private Button btnKutuphaneBilgilerAcilTelefonlarBaslikIcon,btnKutuphaneBilgilerAcilTelefonlarBaslik,btnAcilTelefonlarAra;
    private EditText txtAcilTelefonlarAranacakTelefon;
    private DatabaseReference mDatabase;
    private ArrayList<AcilTelefonlarModel> acilTelefonlar;
    private ListView listViewAcilTelefonlar;
    private AcilTelefonlarAdapter acilTelefonlarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kutuphane_bilgiler_acil_telefonlar);
        init();
    }

    private void init() {
        listViewAcilTelefonlar = findViewById(R.id.listViewAcilTelefonlar);
        btnKutuphaneBilgilerAcilTelefonlarBaslikIcon = findViewById(R.id.btnKutuphaneBilgilerAcilTelefonlarBaslikIcon);
        btnKutuphaneBilgilerAcilTelefonlarBaslik = findViewById(R.id.btnKutuphaneBilgilerAcilTelefonlarBaslik);
        btnAcilTelefonlarAra = findViewById(R.id.btnAcilTelefonlarAra);
        txtAcilTelefonlarAranacakTelefon = findViewById(R.id.txtAcilTelefonlarAranacakTelefon);

        acilTelefonlar = new ArrayList<>();
        acilTelefonlarAdapter = new AcilTelefonlarAdapter(KutuphaneBilgilerAcilTelefonlarActivity.this, acilTelefonlar);

        btnKutuphaneBilgilerAcilTelefonlarBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kutuphaneBilgilerIntent = new Intent(KutuphaneBilgilerAcilTelefonlarActivity.this, KutuphaneBilgilerActivity.class);
                startActivity(kutuphaneBilgilerIntent);
                finish();
            }
        });

        btnAcilTelefonlarAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filtreAcilTelefon = txtAcilTelefonlarAranacakTelefon.getText().toString().trim().toLowerCase();
                filtreliAcilTelefonlariGetir(filtreAcilTelefon);
            }
        });

        txtAcilTelefonlarAranacakTelefon.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filtreAcilTelefon = txtAcilTelefonlarAranacakTelefon.getText().toString().trim();

                if(filtreAcilTelefon.equals(Constants.BOS_KONTROL)){
                    acilTelefonlar.clear();

                    acilTelefonlariGetir();
                }
            }
        });


        acilTelefonlariGetir();
    }

    private void filtreliAcilTelefonlariGetir(String filtreAcilTelefon) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_BILGILER).child(Constants.FIREBASE_KUTUPHANE_BILGILER_ACIL_TELEFONLAR_CHILD);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int filtreyeUygunTelefonVarMi=0;
                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String telefonBaslik = postSnapshot.getKey();
                    if(telefonBaslik.contains(Constants.FIREBASE_KUTUPHANE_BILGILER_ACIL_TELEFON)) {
                        if (snapshot.child(telefonBaslik).child(Constants.FIREBASE_KUTUPHANE_BILGILER_ACIL_TELEFON_BASLIK).getValue().toString().toLowerCase().contains(filtreAcilTelefon) || snapshot.child(telefonBaslik).child(Constants.FIREBASE_KUTUPHANE_BILGILER_ACIL_TELEFON_NUMARA).getValue().toString().toLowerCase().contains(filtreAcilTelefon)) {
                            if(filtreyeUygunTelefonVarMi==0){
                                acilTelefonlar.clear();
                            }
                            filtreyeUygunTelefonVarMi++;
                            AcilTelefonlarModel acilTelefonlarModel = postSnapshot.getValue(AcilTelefonlarModel.class);
                            acilTelefonlar.add(
                                    new AcilTelefonlarModel(
                                            //String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad
                                            acilTelefonlarModel.getAcilTelefonBaslik(),
                                            acilTelefonlarModel.getAcilTelefonNumara()

                                    )
                            );
                            listViewAcilTelefonlar.setAdapter(acilTelefonlarAdapter);
                        }
                        else{
                            if(filtreyeUygunTelefonVarMi==0){
                                listViewAcilTelefonlar.setVisibility(View.INVISIBLE);
                            }
                            else{
                                listViewAcilTelefonlar.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void acilTelefonlariGetir() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_BILGILER);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KUTUPHANE).child(Constants.FIREBASE_KUTUPHANE_BILGILER).child(Constants.FIREBASE_KUTUPHANE_BILGILER_ACIL_TELEFONLAR_CHILD);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String telefonBaslik = postSnapshot.getKey();
                            if(telefonBaslik.contains(Constants.FIREBASE_KUTUPHANE_BILGILER_ACIL_TELEFON)){
                                AcilTelefonlarModel acilTelefonlarModel = postSnapshot.getValue(AcilTelefonlarModel.class);
                                acilTelefonlar.add(
                                        new AcilTelefonlarModel(
                                                //String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad
                                                acilTelefonlarModel.getAcilTelefonBaslik(),
                                                acilTelefonlarModel.getAcilTelefonNumara()

                                        )
                                );
                                listViewAcilTelefonlar.setAdapter(acilTelefonlarAdapter);
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