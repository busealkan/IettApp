package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.h5190059.iett.R;

import java.util.HashMap;

public class KayitOlActivity extends AppCompatActivity {

    private ImageView imgKayitOlLogo;
    private TextView txtKayitAd,txtKayitSoyad,txtKayitSicil,txtKayitEmail,txtKayitSifre,txtKayitSifreTekrar,txtHesapVar,txtGirisYap;
    private Button btnKayitOl;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        init();
    }

    private void init() {
        imgKayitOlLogo = findViewById(R.id.imgSifreDegistirlLogo);
        txtKayitAd = findViewById(R.id.txtKayitAd);
        txtKayitSoyad = findViewById(R.id.txtKayitSoyad);
        txtKayitSicil = findViewById(R.id.txtKayitSicil);
        txtKayitEmail = findViewById(R.id.txtKayitEmail);
        txtKayitSifre = findViewById(R.id.txtKayitSifre);
        txtKayitSifreTekrar = findViewById(R.id.txtKayitSifreTekrar);
        txtHesapVar = findViewById(R.id.txtHesapVar);
        txtGirisYap = findViewById(R.id.txtGirisYap);
        btnKayitOl = findViewById(R.id.btnKayitOl);
        mAuth = FirebaseAuth.getInstance();

        txtGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYapActivity();
            }
        });

        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitOl();
            }
        });


    }

    private void kayitOl() {
        String ad = txtKayitAd.getText().toString();
        String soyad = txtKayitSoyad.getText().toString();
        String sicil = txtKayitSicil.getText().toString();
        String email = txtKayitEmail.getText().toString();
        String sifre = txtKayitSifre.getText().toString();
        String sifreTekrar = txtKayitSifreTekrar.getText().toString();

        if(ad.toString().trim().equals("")|| soyad.toString().trim().equals("") || sicil.toString().trim().equals("") || email.toString().trim().equals("") || sifre.toString().trim().equals("") || sifreTekrar.toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),R.string.toastBosAlan,Toast.LENGTH_LONG).show();
        }
        else{
           emailVeSifreKontrolEt(ad,soyad,sicil,email,sifre,sifreTekrar);
        }
    }

    private void emailVeSifreKontrolEt(String ad,String soyad, String sicil,String email, String sifre, String sifreTekrar) {
        if(emailGecerliMi(email)==true){
            emailVarMi(ad,soyad,sicil,email,sifre,sifreTekrar);
        }
        else{
            Toast.makeText(getApplicationContext(),R.string.toastEmailFormat ,Toast.LENGTH_LONG).show();
        }
    }

    private void emailVarMi(String ad,String soyad, String sicil,String email, String sifre, String sifreTekrar) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            //Eğer Kayitli Email Bulunamadıysa
                            sifreKontrolEt(ad,soyad,sicil,email,sifre,sifreTekrar);
                        } else {
                            //Kayitli Email Bulunduysa
                            Toast.makeText(getApplicationContext(),R.string.toastEmailBulundu,Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void sifreKontrolEt(String ad,String soyad, String sicil,String email, String sifre, String sifreTekrar) {
        if(sifre.length()>=6 && sifreTekrar.length()>=6){
            if(!(sifre.equals(sifreTekrar))){
                Toast.makeText(getApplicationContext(),R.string.toastSifreUyusmuyor,Toast.LENGTH_LONG).show();
            }
            else{
                if(harfVeRakamKontrolEt(sifre)==true){
                    kullaniciKayit(ad,soyad,sicil,email,sifre);
                }
                else{
                    Toast.makeText(getApplicationContext(),R.string.toastSifre,Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),R.string.toastSifreKarakter,Toast.LENGTH_LONG).show();
        }
    }

    public boolean emailGecerliMi(CharSequence email) {
        // e-posta formatı kontrol
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean harfVeRakamKontrolEt(String sifre) {
        char[] sifreArray = sifre.toCharArray();
        int buyukHarfSayac=0;
        int kucukHarfSayac=0;
        int rakamSayac=0;
        int ozelKarakterSayac=0;
        for(int i=0;i<sifreArray.length;i++){
            if(sifreArray[i]>='A' && sifreArray[i]<='Z'){
                buyukHarfSayac+=1;
            }
            else if(sifreArray[i]>='a' && sifreArray[i]<='z'){
                kucukHarfSayac+=1;
            }
            else if (sifreArray[i]>='0' && sifreArray[i]<='9'){
                rakamSayac+=1;
            }
            else{
                ozelKarakterSayac+=1;
            }
        }

        if(buyukHarfSayac>=1 && rakamSayac>=1  && kucukHarfSayac>=1){
            return true;
        }
        else{
            return false;
        }
    }

    private void kullaniciKayit(String ad,String soyad, String sicil,String email, String sifre) {
        mAuth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId = mAuth.getCurrentUser().getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("ad",ad);
                    userMap.put("soyad",soyad);
                    userMap.put("sicil",sicil);
                    userMap.put("email",email);
                    userMap.put("sifre",sifre);

                    userMap.put("resim","default");
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),R.string.toastKayitBasarili,Toast.LENGTH_LONG).show();

                                Intent anaSayfaIntent = new Intent(KayitOlActivity.this, AnaSayfaActivity.class);
                                startActivity(anaSayfaIntent);
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(),R.string.toastKayitBasarisiz ,Toast.LENGTH_LONG).show();
                    Log.e("Kayıt Ol Hatası",task.getException().getMessage());
                }
            }

        });
    }

    private void girisYapActivity() {
        Intent girisYapIntent = new Intent(KayitOlActivity.this, GirisActivity.class);
        startActivity(girisYapIntent);
    }
}