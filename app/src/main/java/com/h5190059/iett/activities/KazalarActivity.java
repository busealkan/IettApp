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
import com.h5190059.iett.adaptor.KazaAdapter;
import com.h5190059.iett.model.CezaModel;
import com.h5190059.iett.model.KazaModel;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class KazalarActivity extends AppCompatActivity {

    private ArrayList<KazaModel> kazalar;
    private ArrayList<Users> users;
    private Button btnKazalarBaslikIcon, btnKazalarBaslik,btnKazaTarihBaslik,btnKazaTuruBaslik,btnKazaHasarBaslik;
    private KazaAdapter kazaAdapter;
    private DatabaseReference mDatabase;
    private ListView listViewKazalar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kazalar);
        init();
    }

    private void init() {
        listViewKazalar = findViewById(R.id.listViewKazalar);
        btnKazalarBaslikIcon = findViewById(R.id.btnKazalarBaslikIcon);
        btnKazalarBaslik = findViewById(R.id.btnKazalarBaslik);
        btnKazaTarihBaslik = findViewById(R.id.btnKazaTarihBaslik);
        btnKazaTuruBaslik = findViewById(R.id.btnKazaTuruBaslik);
        btnKazaHasarBaslik = findViewById(R.id.btnKazaHasarBaslik);

        mAuth = FirebaseAuth.getInstance();
        kazalar = new ArrayList<>();
        users = new ArrayList<>();
        kazaAdapter = new KazaAdapter(KazalarActivity.this, kazalar);

        btnKazalarBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(KazalarActivity.this, SicilActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });
        kazalariGetir();
    }

    private void kazalariGetir() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KAZALAR);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(Constants.FIREBASE_CHILD + userId)) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_KAZALAR).child(userId);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int kazaAdet = 1;

                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                //    public ArizaModel(String arizaAciklama, String arizaBaslik, String arizaDurumu, String arizaTarih, String hatIstikamet, String hatKodu, String userId, String arizaAdet) {
                                KazaModel kaza = postSnapshot.getValue(KazaModel.class);
                                kaza.setKazaAdet(String.valueOf(kazaAdet));
                                kazalar.add(
                                        new KazaModel(
                                                kaza.getKazaTarihi(),
                                                kaza.getKazaTuru(),
                                                kaza.getKazaHasar(),
                                                kaza.getKazaSicil(),
                                                kaza.getKazaAdet()
                                        )
                                );
                                kazaAdet++;

                                listViewKazalar.setAdapter(kazaAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(KazalarActivity.this, getResources().getString(R.string.toastKazalar), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}