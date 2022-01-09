package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.model.Users;
import com.h5190059.iett.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HatAramaActivity extends AppCompatActivity {

    private Button btnHatAramaArizaSoforSicilBaslik,btnHatAramaBaslikIcon,btnHatAramaBaslik,btnHatAra,btnHatAramaHatKoduBaslik,btnHatAramaHatTabelaBaslik,btnHatAramaHatIstikametBaslik,btnHatAramaArizaDurumuBaslik,btnHatAramaArizaBaslikBaslik;
    private TextView txtHatAramaSoforSicil,txtHatKodu,txtHatAramaHatKodu,txtHatAramaHatTabela,txtHatAramaHatIstikamet,txtHatAramaHatArizaDurumu,txtHatAramaHatArizaBaslik;
    private DatabaseReference mDatabase;
    private ArrayList<Users> users;
    private int i,arizaAdet;
    private ConstraintLayout constraintLayout;
    private ConstraintSet applyConstraintSet;
    private ConstraintSet resetConstraintSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat_arama);
        init();
    }

    private void init() {
        btnHatAramaBaslikIcon = findViewById(R.id.btnHatAramaBaslikIcon);
        btnHatAramaBaslik = findViewById(R.id.btnHatAramaBaslik);
        btnHatAra = findViewById(R.id.btnHatAra);
        btnHatAramaHatKoduBaslik = findViewById(R.id.btnHatAramaHatKoduBaslik);
        btnHatAramaHatTabelaBaslik = findViewById(R.id.btnHatAramaHatTabelaBaslik);
        btnHatAramaHatIstikametBaslik = findViewById(R.id.btnHatAramaHatIstikametBaslik);
        btnHatAramaArizaDurumuBaslik = findViewById(R.id.btnHatAramaArizaDurumuBaslik);
        btnHatAramaArizaBaslikBaslik = findViewById(R.id.btnHatAramaArizaBaslikBaslik);
        btnHatAramaArizaSoforSicilBaslik = findViewById(R.id.btnHatAramaArizaSoforSicilBaslik);

        txtHatKodu = findViewById(R.id.txtHatKodu);
        txtHatAramaHatKodu = findViewById(R.id.txtHatAramaHatKodu);
        txtHatAramaHatTabela = findViewById(R.id.txtHatAramaHatTabela);
        txtHatAramaHatIstikamet = findViewById(R.id.txtHatAramaHatIstikamet);
        txtHatAramaHatArizaDurumu = findViewById(R.id.txtHatAramaHatArizaDurumu);
        txtHatAramaHatArizaBaslik = findViewById(R.id.txtHatAramaHatArizaBaslik);
        txtHatAramaSoforSicil = findViewById(R.id.txtHatAramaSoforSicil);

        constraintLayout = findViewById(R.id.hatAramaLayout);
        applyConstraintSet = new ConstraintSet();
        resetConstraintSet = new ConstraintSet();
        resetConstraintSet.clone(constraintLayout);
        applyConstraintSet.clone(constraintLayout);

        i=0;
        users = new ArrayList<>();

        bilgileriGizle();

        btnHatAramaBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anaSayfaIntent = new Intent(HatAramaActivity.this, AnaSayfaActivity.class);
                startActivity(anaSayfaIntent);
                finish();
            }
        });

        btnHatAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt();
            }
        });
    }

    private void bilgileriGizle() {
        btnHatAramaHatKoduBaslik.setVisibility(View.INVISIBLE);
        btnHatAramaHatTabelaBaslik.setVisibility(View.INVISIBLE);
        btnHatAramaHatIstikametBaslik.setVisibility(View.INVISIBLE);
        btnHatAramaArizaDurumuBaslik.setVisibility(View.INVISIBLE);
        btnHatAramaArizaBaslikBaslik.setVisibility(View.INVISIBLE);
        btnHatAramaArizaSoforSicilBaslik.setVisibility(View.INVISIBLE);

        txtHatAramaHatKodu.setVisibility(View.INVISIBLE);
        txtHatAramaHatTabela.setVisibility(View.INVISIBLE);
        txtHatAramaHatIstikamet.setVisibility(View.INVISIBLE);
        txtHatAramaHatArizaDurumu.setVisibility(View.INVISIBLE);
        txtHatAramaHatArizaBaslik.setVisibility(View.INVISIBLE);
        txtHatAramaSoforSicil.setVisibility(View.INVISIBLE);
    }

    private void bilgileriGoster() {
        btnHatAramaHatKoduBaslik.setVisibility(View.VISIBLE);
        btnHatAramaHatTabelaBaslik.setVisibility(View.VISIBLE);
        btnHatAramaHatIstikametBaslik.setVisibility(View.VISIBLE);
        btnHatAramaArizaDurumuBaslik.setVisibility(View.VISIBLE);
        btnHatAramaArizaBaslikBaslik.setVisibility(View.VISIBLE);
        btnHatAramaArizaSoforSicilBaslik.setVisibility(View.VISIBLE);


        txtHatAramaHatKodu.setVisibility(View.VISIBLE);
        txtHatAramaHatTabela.setVisibility(View.VISIBLE);
        txtHatAramaHatIstikamet.setVisibility(View.VISIBLE);
        txtHatAramaHatArizaDurumu.setVisibility(View.VISIBLE);
        txtHatAramaHatArizaBaslik.setVisibility(View.VISIBLE);
        txtHatAramaSoforSicil.setVisibility(View.VISIBLE);

    }

    private void bosAlanVarMiKontrolEt() {
        String hatKod = txtHatKodu.getText().toString().trim().toUpperCase();


        if (hatKod.equals(Constants.BOS_KONTROL) || hatKod.equals(getResources().getString(R.string.hatKodArama))) {
            Toast.makeText(getApplicationContext(), R.string.toastZorunluBosAlan, Toast.LENGTH_LONG).show();
        } else {
            hatKoduVarMi(hatKod);
        }
    }

    private void hatKoduVarMi(String hatKod) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // /Araclar/C-1907
                if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD + hatKod)) {
                    aractaArizaVarMi(hatKod);
                } else {
                    Toast.makeText(HatAramaActivity.this, R.string.toastHatKodunaSahipAracBulunamadi, Toast.LENGTH_SHORT).show();
                    bilgileriGizle();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void aractaArizaVarMi(String hatKod) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String aracArizaDurumu = dataSnapshot.child(Constants.FIREBASE_ARACLAR_ARIZA_DURUMU).getValue().toString();

                if(aracArizaDurumu.equals(Constants.FIREBASE_ARIZA_DURUMU_FALSE)){
                    isListesiAra(hatKod);
                }
                else{
                    arizaBul(hatKod);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void arizaBul(String hatKod) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Users user = postSnapshot.getValue(Users.class);
                    users.add(
                            new Users(
                                    //String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad
                                    user.getAd(),
                                    user.getDogumTarihi(),
                                    user.getDurumu(),
                                    user.getEmail(),
                                    user.getGorevi(),
                                    user.getIlkGiris(),
                                    user.getKanGrubu(),
                                    user.getResim(),
                                    user.getSicil(),
                                    user.getSifre(),
                                    user.getSoyad()
                            )
                    );
                }

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String userId = postSnapshot.getKey();
                            String sicil = dataSnapshot.child(userId).child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR);
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // /Araclar/C-1907
                                    if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD+sicil)) {
                                        arizaAdet = 1;
                                        while ((dataSnapshot.hasChild(Constants.FIREBASE_CHILD+sicil+Constants.FIREBASE_ARIZA_CHILD + arizaAdet))) {
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARIZALAR).child(sicil).child(Constants.FIREBASE_ARIZA+arizaAdet);
                                            mDatabase.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.child(Constants.FIREBASE_ARIZA_HAT_KODU).getValue().toString().equals(hatKod)){
                                                        if(snapshot.child(Constants.FIREBASE_ARIZA_DURUMU).getValue().toString()!=Constants.FIREBASE_ARIZA_DURUMU_FALSE){
                                                            //aracBilgileriGetir(hatKod);
                                                            String arizaBaslik = snapshot.child(Constants.FIREBASE_ARIZA_BASLIK).getValue().toString();

                                                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
                                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    // /Araclar/C-1907
                                                                    String arizaDurumu = getResources().getString(R.string.arizali);
                                                                    String hatIstikamet1 = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_ISTIKAMET1).getValue().toString();
                                                                    String hatIstikamet2 = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_ISTIKAMET2).getValue().toString();
                                                                    String hatTabela = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_TABELA).getValue().toString();
                                                                    String hatKodu = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_KODU).getValue().toString();

                                                                    txtHatAramaHatArizaDurumu.setText(arizaDurumu);
                                                                    txtHatAramaHatIstikamet.setText(hatIstikamet1+" - "+hatIstikamet2);
                                                                    txtHatAramaHatTabela.setText(hatTabela);
                                                                    txtHatAramaHatKodu.setText(hatKodu);
                                                                    txtHatAramaHatArizaBaslik.setText(arizaBaslik);
                                                                    txtHatAramaSoforSicil.setText(sicil);
                                                                    bilgileriGoster();

                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });
                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }

                                            });


                                            arizaAdet=arizaAdet+1;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isListesiAra(String hatKod) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Users user = postSnapshot.getValue(Users.class);
                    users.add(
                            new Users(
                                    //String ad, String dogumTarihi, String durumu, String email, String gorevi, String ilkGiris, String kanGrubu, String resim, String sicil, String sifre, String soyad
                                    user.getAd(),
                                    user.getDogumTarihi(),
                                    user.getDurumu(),
                                    user.getEmail(),
                                    user.getGorevi(),
                                    user.getIlkGiris(),
                                    user.getKanGrubu(),
                                    user.getResim(),
                                    user.getSicil(),
                                    user.getSifre(),
                                    user.getSoyad()
                            )
                    );
                }

                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        i=0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String userId = postSnapshot.getKey();
                            String sicil = dataSnapshot.child(userId).child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                                mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_IS_LISTESI);
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        Calendar takvim = Calendar.getInstance();
                                        takvim.setTime(new Date());
                                        String tarih = tarihFormat.format(takvim.getTime());


                                            if (dataSnapshot.hasChild(Constants.FIREBASE_CHILD + sicil + Constants.FIREBASE_CHILD + tarih.replace('/', '_'))) {
                                                String dbHatKod = dataSnapshot.child(sicil).child(tarih.replace('/', '_')).child(Constants.FIREBASE_IS_LISTESI_BOLGE).getValue().toString();

                                                if (dbHatKod.contains(hatKod)) {
                                                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
                                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            // /Araclar/C-1907
                                                            String hatIstikamet1 = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_ISTIKAMET1).getValue().toString();
                                                            String hatIstikamet2 = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_ISTIKAMET2).getValue().toString();
                                                            String hatTabela = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_TABELA).getValue().toString();
                                                            String hatKodu = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_KODU).getValue().toString();

                                                            txtHatAramaHatIstikamet.setText(hatIstikamet1 + " - " + hatIstikamet2);
                                                            txtHatAramaHatTabela.setText(hatTabela);
                                                            txtHatAramaHatKodu.setText(hatKodu);
                                                            txtHatAramaSoforSicil.setText(sicil);
                                                            bilgileriGoster();
                                                            txtHatAramaHatArizaDurumu.setVisibility(View.INVISIBLE);
                                                            btnHatAramaArizaDurumuBaslik.setVisibility(View.INVISIBLE);
                                                            btnHatAramaArizaBaslikBaslik.setVisibility(View.INVISIBLE);
                                                            txtHatAramaHatArizaBaslik.setVisibility(View.INVISIBLE);

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                    i=users.size();
                                                }
                                                else {
                                                    if (i == users.size()-1) {
                                                        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
                                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                // /Araclar/C-1907
                                                                String hatIstikamet1 = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_ISTIKAMET1).getValue().toString();
                                                                String hatIstikamet2 = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_ISTIKAMET2).getValue().toString();
                                                                String hatTabela = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_TABELA).getValue().toString();
                                                                String hatKodu = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_KODU).getValue().toString();

                                                                txtHatAramaHatIstikamet.setText(hatIstikamet1 + " - " + hatIstikamet2);
                                                                txtHatAramaHatTabela.setText(hatTabela);
                                                                txtHatAramaHatKodu.setText(hatKodu);
                                                                bilgileriGoster();
                                                                btnHatAramaArizaBaslikBaslik.setVisibility(View.INVISIBLE);
                                                                txtHatAramaHatArizaBaslik.setVisibility(View.INVISIBLE);
                                                                btnHatAramaArizaSoforSicilBaslik.setVisibility(View.INVISIBLE);
                                                                txtHatAramaSoforSicil.setVisibility(View.INVISIBLE);
                                                                txtHatAramaHatArizaDurumu.setVisibility(View.INVISIBLE);
                                                                btnHatAramaArizaDurumuBaslik.setVisibility(View.INVISIBLE);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                            }
                                                        });
                                                    }
                                                }

                                            }
                                            else {
                                                if (i == users.size() - 1) {
                                                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ARACLAR).child(hatKod);
                                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            // /Araclar/C-1907
                                                            String hatIstikamet1 = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_ISTIKAMET1).getValue().toString();
                                                            String hatIstikamet2 = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_ISTIKAMET2).getValue().toString();
                                                            String hatTabela = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_TABELA).getValue().toString();
                                                            String hatKodu = dataSnapshot.child(Constants.FIREBASE_ARACLAR_HAT_KODU).getValue().toString();

                                                            txtHatAramaHatIstikamet.setText(hatIstikamet1 + " - " + hatIstikamet2);
                                                            txtHatAramaHatTabela.setText(hatTabela);
                                                            txtHatAramaHatKodu.setText(hatKodu);
                                                            bilgileriGoster();
                                                            btnHatAramaArizaBaslikBaslik.setVisibility(View.INVISIBLE);
                                                            txtHatAramaHatArizaBaslik.setVisibility(View.INVISIBLE);
                                                            btnHatAramaArizaSoforSicilBaslik.setVisibility(View.INVISIBLE);
                                                            txtHatAramaSoforSicil.setVisibility(View.INVISIBLE);
                                                            txtHatAramaHatArizaDurumu.setVisibility(View.INVISIBLE);
                                                            btnHatAramaArizaDurumuBaslik.setVisibility(View.INVISIBLE);
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                            }
                                            i++;
                                        }




                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}