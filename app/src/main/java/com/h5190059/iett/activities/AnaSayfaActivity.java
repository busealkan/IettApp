package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.ProgressUtil;
import android.view.WindowManager;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import java.util.ArrayList;
import java.util.List;

import com.h5190059.iett.utils.Constants;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class AnaSayfaActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgProfilFoto;
    private TextView txtKullaniciAdSoyad, txtHosgeldiniz;
    private Button btnAnaSayfaSicil, btnAnaSayfaArizaOlustur, btnAnaSayfaRaporOlustur, btnAnaSayfaArizaOnay, btnAnaSayfaArizalarim, btnAnaSayfaIzinIslemleri,
            btnAnaSayfaBordro, btnAnaSayfaTalepVeOneri, btnAnaSayfaIsListeIslemler, btnAnaSayfaIsListesi, btnAnaSayfaKutuphane, btnAnaSayfaIzinler, btnAnaSayfaHatSorgula, btnAnaSayfaRaporlar;
    private ProgressDialog progressDialog;
    private Toolbar tbarAnaSayfa;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Uri imageUri;
    private ImageSlider mainslider;
    private ConstraintLayout constraintLayout;
    private ConstraintSet applyConstraintSet;
    private ConstraintSet resetConstraintSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);
        init();
        setSupportActionBar(tbarAnaSayfa);
        getSupportActionBar().setTitle(Constants.ACTION_BAR_SET_TITLE);
    }

    private void init() {
        girisYapilmisMi();
        imageUri = null;
        tbarAnaSayfa = findViewById(R.id.tbarAnaSayfa);
        imgProfilFoto = findViewById(R.id.imgProfilFoto);
        txtKullaniciAdSoyad = findViewById(R.id.txtKullaniciAdSoyad);
        txtHosgeldiniz = findViewById(R.id.txtHosgeldiniz);

        btnAnaSayfaSicil = findViewById(R.id.btnAnaSayfaSicil);
        btnAnaSayfaArizaOlustur = findViewById(R.id.btnAnaSayfaArizaOlustur);
        btnAnaSayfaRaporOlustur = findViewById(R.id.btnAnaSayfaRaporOlustur);
        btnAnaSayfaArizalarim = findViewById(R.id.btnAnaSayfaArizalarim);
        btnAnaSayfaIzinIslemleri = findViewById(R.id.btnAnaSayfaIzinIslemleri);
        btnAnaSayfaBordro = findViewById(R.id.btnAnaSayfaBordro);
        btnAnaSayfaTalepVeOneri = findViewById(R.id.btnAnaSayfaTalepVeOneri);
        btnAnaSayfaIsListeIslemler = findViewById(R.id.btnAnaSayfaIsListeIslemler);
        btnAnaSayfaIsListesi = findViewById(R.id.btnAnaSayfaIsListesi);
        btnAnaSayfaKutuphane = findViewById(R.id.btnAnaSayfaKutuphane);
        btnAnaSayfaIzinler = findViewById(R.id.btnAnaSayfaIzinler);
        btnAnaSayfaHatSorgula = findViewById(R.id.btnAnaSayfaHatSorgula);
        btnAnaSayfaRaporlar = findViewById(R.id.btnAnaSayfaRaporlar);

        constraintLayout = findViewById(R.id.anaSayfaLayout);
        applyConstraintSet = new ConstraintSet();
        resetConstraintSet = new ConstraintSet();
        resetConstraintSet.clone(constraintLayout);
        applyConstraintSet.clone(constraintLayout);

        btnAnaSayfaSicil.setOnClickListener(this);
        btnAnaSayfaArizaOlustur.setOnClickListener(this);
        btnAnaSayfaRaporOlustur.setOnClickListener(this);
        btnAnaSayfaArizalarim.setOnClickListener(this);
        btnAnaSayfaIzinIslemleri.setOnClickListener(this);
        btnAnaSayfaBordro.setOnClickListener(this);
        btnAnaSayfaTalepVeOneri.setOnClickListener(this);
        btnAnaSayfaIsListeIslemler.setOnClickListener(this);
        btnAnaSayfaIsListesi.setOnClickListener(this);
        btnAnaSayfaKutuphane.setOnClickListener(this);
        btnAnaSayfaIzinler.setOnClickListener(this);
        btnAnaSayfaHatSorgula.setOnClickListener(this);
        btnAnaSayfaRaporlar.setOnClickListener(this);

        kullaniciBilgisiGetir(txtKullaniciAdSoyad);
        sliderGoster();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAnaSayfaSicil:
                Intent sicilIntent = new Intent(AnaSayfaActivity.this, SicilActivity.class);
                startActivity(sicilIntent);
                break;

            case R.id.btnAnaSayfaArizaOlustur:
                Intent arizaOlusturIntent = new Intent(AnaSayfaActivity.this, ArizaOlusturActivity.class);
                startActivity(arizaOlusturIntent);
                break;

            case R.id.btnAnaSayfaRaporOlustur:
                Intent raporOlusturIntent = new Intent(AnaSayfaActivity.this, RaporOlusturActivity.class);
                startActivity(raporOlusturIntent);
                break;

            case R.id.btnAnaSayfaBordro:
                Intent bordroIntent = new Intent(AnaSayfaActivity.this, BordroActivity.class);
                startActivity(bordroIntent);
                break;

            case R.id.btnAnaSayfaIsListesi:
                Intent isListeIntent = new Intent(AnaSayfaActivity.this, com.h5190059.iett.activities.IslerimActivity.class);
                startActivity(isListeIntent);
                break;

            case R.id.btnAnaSayfaIzinler:
                Intent izinSorgulaIntent = new Intent(AnaSayfaActivity.this, IzinlerimActivity.class);
                startActivity(izinSorgulaIntent);
                break;

            case R.id.btnAnaSayfaTalepVeOneri:
                Intent talepVeOneriIntent = new Intent(AnaSayfaActivity.this, TalepVeOneriActivity.class);
                startActivity(talepVeOneriIntent);
                break;

            case R.id.btnAnaSayfaArizalarim:
                Intent arizaListeleIntent = new Intent(AnaSayfaActivity.this, ArizaListeleActivity.class);
                startActivity(arizaListeleIntent);
                break;

            case R.id.btnAnaSayfaHatSorgula:
                Intent hatAramaIntent = new Intent(AnaSayfaActivity.this, HatAramaActivity.class);
                startActivity(hatAramaIntent);
                break;

            case R.id.btnAnaSayfaIsListeIslemler:
                Intent isOlusturIntent = new Intent(AnaSayfaActivity.this, IsIslemleriActivity.class);
                startActivity(isOlusturIntent);
                break;


            case R.id.btnAnaSayfaRaporlar:
                Intent raporListeleIntent = new Intent(AnaSayfaActivity.this, RaporListeleActivity.class);
                startActivity(raporListeleIntent);
                break;

            case R.id.btnAnaSayfaIzinIslemleri:
                Intent izinIslemleriIntent = new Intent(AnaSayfaActivity.this, IzinIslemleriActivity.class);
                startActivity(izinIslemleriIntent);
                break;


            case R.id.btnAnaSayfaKutuphane:
                Intent kutuphaneDosyalarIntent = new Intent(AnaSayfaActivity.this, KutuphaneDosyalarActivity.class);
                startActivity(kutuphaneDosyalarIntent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_anasayfa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.profileGit) {
            Intent profilIntent = new Intent(AnaSayfaActivity.this, ProfilActivity.class);
            startActivity(profilIntent);
        } else if (item.getItemId() == R.id.sifreDegistir) {
            Intent sifreDegistirIntent = new Intent(AnaSayfaActivity.this, SifreDegistirActivity.class);
            startActivity(sifreDegistirIntent);
        } else {
            mAuth.signOut();
            progressDialog = ProgressUtil.progressDialogOlustur(AnaSayfaActivity.this, getResources().getString(R.string.oturumKapatiliyor));
            Intent loginIntent = new Intent(AnaSayfaActivity.this, GirisActivity.class);
            startActivity(loginIntent);
        }
        return true;
    }

    private void kullaniciBilgisiGetir(TextView txtKullaniciAdSoyad) {
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String gorevi = snapshot.child(Constants.FIREBASE_KULLANICI_GOREVI).getValue().toString().toLowerCase();
                String resim = snapshot.child(Constants.FIREBASE_KULLANICI_RESIM).getValue().toString();

                txtKullaniciAdSoyad.setText(ad + " " + soyad);

                imageUri = Uri.parse(resim);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profildefault);
                Glide.with(AnaSayfaActivity.this).setDefaultRequestOptions(requestOptions).load(imageUri).into(imgProfilFoto);

                goreviKontrolEt(gorevi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void goreviKontrolEt(String gorevi) {
        if (gorevi.toLowerCase().equals(Constants.GOREV_AMIR.toLowerCase())) {
            applyConstraintSet.connect(R.id.btnAnaSayfaRaporOlustur, ConstraintSet.TOP, R.id.btnAnaSayfaBordro, ConstraintSet.BOTTOM, 0);
            applyConstraintSet.connect(R.id.btnAnaSayfaRaporlar, ConstraintSet.TOP, R.id.btnAnaSayfaRaporOlustur, ConstraintSet.BOTTOM, 0);

            applyConstraintSet.applyTo(constraintLayout);

            btnAnaSayfaArizalarim.setText(R.string.arizalar);

            btnAnaSayfaIzinIslemleri.setVisibility(View.INVISIBLE);
            btnAnaSayfaArizaOlustur.setVisibility(View.INVISIBLE);
            btnAnaSayfaIsListeIslemler.setVisibility(View.INVISIBLE);

        } else if (gorevi.toLowerCase().equals(Constants.GOREV_SEF.toLowerCase())) {
            btnAnaSayfaRaporlar.setBackgroundColor(getResources().getColor(R.color.baslikButonRengi));
            btnAnaSayfaIzinIslemleri.setBackgroundColor(getResources().getColor(R.color.baslikButonRengi));

            applyConstraintSet.connect(R.id.btnAnaSayfaIzinIslemleri, ConstraintSet.TOP, R.id.btnAnaSayfaTalepVeOneri, ConstraintSet.BOTTOM, 0);
            applyConstraintSet.connect(R.id.btnAnaSayfaRaporlar, ConstraintSet.TOP, R.id.btnAnaSayfaBordro, ConstraintSet.BOTTOM, 0);
            applyConstraintSet.applyTo(constraintLayout);

            btnAnaSayfaArizaOlustur.setVisibility(View.INVISIBLE);
            btnAnaSayfaArizalarim.setVisibility(View.INVISIBLE);
            btnAnaSayfaIsListeIslemler.setVisibility(View.INVISIBLE);
            btnAnaSayfaRaporOlustur.setVisibility(View.INVISIBLE);
            btnAnaSayfaRaporOlustur.setVisibility(View.INVISIBLE);

        } else if (gorevi.equals(Constants.GOREV_BOLGE_YONETICISI.toLowerCase())) {
            btnAnaSayfaArizaOlustur.setVisibility(View.INVISIBLE);
            btnAnaSayfaIsListeIslemler.setBackgroundColor(getResources().getColor(R.color.baslikButonRengi));

            applyConstraintSet.connect(R.id.btnAnaSayfaIsListeIslemler, ConstraintSet.TOP, R.id.btnAnaSayfaBordro, ConstraintSet.BOTTOM, 0);
            applyConstraintSet.applyTo(constraintLayout);

            btnAnaSayfaIzinIslemleri.setVisibility(View.INVISIBLE);
            btnAnaSayfaRaporOlustur.setVisibility(View.INVISIBLE);
            btnAnaSayfaRaporlar.setVisibility(View.INVISIBLE);
            btnAnaSayfaRaporOlustur.setVisibility(View.INVISIBLE);
            btnAnaSayfaArizalarim.setVisibility(View.INVISIBLE);

        } else {
            btnAnaSayfaIzinIslemleri.setVisibility(View.INVISIBLE);
            btnAnaSayfaIsListeIslemler.setVisibility(View.INVISIBLE);
            btnAnaSayfaRaporOlustur.setVisibility(View.INVISIBLE);
            btnAnaSayfaRaporlar.setVisibility(View.INVISIBLE);
            btnAnaSayfaRaporOlustur.setVisibility(View.INVISIBLE);

        }
    }

    private void sliderGoster() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainslider = findViewById(R.id.imgSlider);

        final List<SlideModel> remoteimages = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SLIDER);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    remoteimages.add(new SlideModel(data.child(Constants.FIREBASE_SLIDER_URL).getValue().toString(), data.child(Constants.FIREBASE_SLIDER_TITLE).getValue().toString(), ScaleTypes.FIT));
                }
                mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                /*
                mainslider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Toast.makeText(getApplicationContext(), remoteimages.get(i).getTitle().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void girisYapilmisMi() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Intent loginIntent = new Intent(AnaSayfaActivity.this, GirisActivity.class);
            startActivity(loginIntent);
        }
    }

}