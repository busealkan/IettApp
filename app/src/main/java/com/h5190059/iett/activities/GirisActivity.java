package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

public class GirisActivity extends AppCompatActivity {

    private ImageView imgGirisYapLogo;
    private TextView txtGirisEmail, txtGirisSifre, txtSifreUnuttum;
    private Button btnGirisYap;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent anaSayfaIntent = new Intent(GirisActivity.this, AnaSayfaActivity.class);
            startActivity(anaSayfaIntent);
            finish();
        }
    }

    private void init() {
        imgGirisYapLogo = findViewById(R.id.imgGirisYapLogo);
        txtGirisEmail = findViewById(R.id.txtGirisEmail);
        txtGirisSifre = findViewById(R.id.txtGirisSifre);
        txtSifreUnuttum = findViewById(R.id.txtSifreUnuttum);
        btnGirisYap = findViewById(R.id.btnGirisYap);
        mAuth = FirebaseAuth.getInstance();

        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt();
            }
        });

        txtSifreUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sifremiUnuttumActivity();
            }
        });
    }

    private void bosAlanVarMiKontrolEt() {
        String email = txtGirisEmail.getText().toString().trim();
        String sifre = txtGirisSifre.getText().toString().trim();
        if (email.equals(Constants.BOS_KONTROL) || sifre.equals(Constants.BOS_KONTROL)) {
            Toast.makeText(GirisActivity.this, R.string.toastZorunluBosAlan, Toast.LENGTH_LONG).show();
        } else {
            if (emailFormatiGecerliMi(email) == true) {
                kayitliEmailVarMi(email, sifre);
            } else {
                Toast.makeText(GirisActivity.this, R.string.toastEmailFormat, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean emailFormatiGecerliMi(CharSequence email) {
        // e-posta formatı kontrol
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private void kayitliEmailVarMi(String email, String sifre) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            //Email bulunamadi
                            Toast.makeText(GirisActivity.this, R.string.toastEmailBulunamadi, Toast.LENGTH_LONG).show();
                        } else {
                            //Email bulundu
                            girisYap(email, sifre);
                        }
                    }
                }
            }
        });
    }

    private void girisYap(String email, String sifre) {
        mAuth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    kullaniciDurumuAktifMi();
                } else {
                    Toast.makeText(GirisActivity.this, R.string.toastGirisBasarisiz, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void kullaniciDurumuAktifMi() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kullaniciDurumu = snapshot.child(Constants.FIREBASE_KULLANICI_DURUMU).getValue().toString();

                if (kullaniciDurumu.toLowerCase().equals(Constants.KULLANICI_DURUMU_AKTIF.toLowerCase())) {
                    kullanicininIlkGirisiMi();
                }else{
                    Toast.makeText(GirisActivity.this, R.string.toastKullaniciDurumuPasif, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void kullanicininIlkGirisiMi() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ilkGirisMi = snapshot.child(Constants.FIREBASE_KULLANICI_ILK_GIRIS).getValue().toString();
                String sifre = snapshot.child(Constants.FIREBASE_KULLANICI_SIFRE).getValue().toString();
                if (ilkGirisMi.toLowerCase().equals(Constants.ILK_GIRIS_TRUE)) {
                    sifreDegistirActivity();
                } else {
                    anaSayfaActivity(sifre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sifreDegistirActivity() {
        Intent sifreDegistirIntent = new Intent(GirisActivity.this, SifreDegistirActivity.class);
        startActivity(sifreDegistirIntent);
        finish();
    }

    private void sifremiUnuttumActivity() {
        Intent sifremiUnuttumIntent = new Intent(GirisActivity.this, SifremiUnuttumActivity.class);
        startActivity(sifremiUnuttumIntent);
    }

    private void anaSayfaActivity(String sifre) {
        Intent anaSayfaIntent = new Intent(GirisActivity.this, AnaSayfaActivity.class);
        anaSayfaIntent.putExtra(Constants.FIREBASE_KULLANICI_ESKI_SIFRE, sifre);
        startActivity(anaSayfaIntent);
        finish();
    }
}