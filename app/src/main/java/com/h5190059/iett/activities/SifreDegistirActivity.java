package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class SifreDegistirActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ImageView imgSifreDegistirlLogo;
    private TextView txtDegistirSifre, txtDegistirSifreTekrar;
    private Button btnSifreDegistir;
    private View viewSifreDegistirBg;
    private HashMap<String, Object> mData;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);
        init();
    }

    private void init() {
        imgSifreDegistirlLogo = findViewById(R.id.imgSifreDegistirlLogo);
        txtDegistirSifre = findViewById(R.id.txtDegistirSifre);
        txtDegistirSifreTekrar = findViewById(R.id.txtDegistirSifreTekrar);
        btnSifreDegistir = findViewById(R.id.btnSifreDegistir);
        viewSifreDegistirBg = findViewById(R.id.viewSifreDegistirBg);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        btnSifreDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt();

            }
        });
    }

    private void bosAlanVarMiKontrolEt() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        String sifre = txtDegistirSifre.getText().toString().trim();
        String sifreTekrar = txtDegistirSifreTekrar.getText().toString().trim();

        if (sifre.equals(Constants.BOS_KONTROL) || sifreTekrar.equals(Constants.BOS_KONTROL)) {
            Toast.makeText(SifreDegistirActivity.this, R.string.toastZorunluBosAlan, Toast.LENGTH_LONG).show();
        } else {
            sifreKontrolEt(sifre, sifreTekrar, userId);
        }
    }

    private void sifreKontrolEt(String sifre, String sifreTekrar, String userId) {
        if (sifre.length() >= 6 && sifreTekrar.length() >= 6) {
            if (!(sifre.equals(sifreTekrar))) {
                Toast.makeText(SifreDegistirActivity.this, R.string.toastSifreUyusmuyor, Toast.LENGTH_LONG).show();
            } else {
                if (harfVeRakamKontrolEt(sifre) == true) {
                    eskiSifreVeYeniSifreAyniMi(sifre, userId);

                } else {
                    Toast.makeText(SifreDegistirActivity.this, R.string.toastSifre, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(SifreDegistirActivity.this, R.string.toastSifreKarakter, Toast.LENGTH_LONG).show();
        }
    }

    public static boolean harfVeRakamKontrolEt(String sifre) {
        char[] sifreArray = sifre.toCharArray();
        int buyukHarfSayac = 0;
        int kucukHarfSayac = 0;
        int rakamSayac = 0;
        int ozelKarakterSayac = 0;

        for (int i = 0; i < sifreArray.length; i++) {
            if (sifreArray[i] >= 'A' && sifreArray[i] <= 'Z') {
                buyukHarfSayac += 1;
            } else if (sifreArray[i] >= 'a' && sifreArray[i] <= 'z') {
                kucukHarfSayac += 1;
            } else if (sifreArray[i] >= '0' && sifreArray[i] <= '9') {
                rakamSayac += 1;
            } else {
                ozelKarakterSayac += 1;
            }
        }

        if (buyukHarfSayac >= 1 && rakamSayac >= 1 && kucukHarfSayac >= 1) {
            return true;
        } else {
            return false;
        }
    }

    private void eskiSifreVeYeniSifreAyniMi(String sifre, String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eskiSifre = dataSnapshot.child(Constants.FIREBASE_KULLANICI_SIFRE).getValue().toString();

                if (sifre.equals(eskiSifre)) {
                    Toast.makeText(getApplicationContext(), R.string.toastEskiSifeIleAyni, Toast.LENGTH_SHORT).show();

                } else {
                    authenticationSifreGuncelle(sifre);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void authenticationSifreGuncelle(String yeniSifre) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sifre = snapshot.child(Constants.FIREBASE_KULLANICI_SIFRE).getValue().toString();
                String email = snapshot.child(Constants.FIREBASE_KULLANICI_EMAIL).getValue().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(email, sifre);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mUser.updatePassword(yeniSifre)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    realtimeDbSifreGuncelle(yeniSifre);
                                                } else {
                                                    Toast.makeText(SifreDegistirActivity.this, R.string.toastSifreGuncellemeBasarisiz, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void realtimeDbSifreGuncelle(String yeniSifre) {
        Map sifreUpdate = new HashMap();
        sifreUpdate.put(Constants.FIREBASE_KULLANICI_SIFRE, yeniSifre);
        sifreUpdate.put(Constants.FIREBASE_KULLANICI_ILK_GIRIS, Constants.ILK_GIRIS_FALSE);
        mDatabase.updateChildren(sifreUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SifreDegistirActivity.this, R.string.toastSifreGuncellemeBasarili, Toast.LENGTH_LONG).show();
                    anaSayfaActivity();
                } else {
                    Toast.makeText(SifreDegistirActivity.this, R.string.toastSifreGuncellemeBasarisiz, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void anaSayfaActivity() {
        Intent anaSayfaIntent = new Intent(SifreDegistirActivity.this, AnaSayfaActivity.class);
        startActivity(anaSayfaIntent);
        finish();
    }
}