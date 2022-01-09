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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

public class SifremiUnuttumActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView imgSifremUnuttumlLogo;
    private TextView txtSifremiUnuttumEmail;
    private Button btnSifremiUnuttum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremi_unuttum);
        init();
    }

    private void init() {
        txtSifremiUnuttumEmail = findViewById(R.id.txtSifremiUnuttumEmail);
        btnSifremiUnuttum = findViewById(R.id.btnSifremiUnuttum);
        mAuth = FirebaseAuth.getInstance();

        btnSifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt();
            }
        });
    }

    private void bosAlanVarMiKontrolEt() {
        String email = txtSifremiUnuttumEmail.getText().toString().trim();
        if (email.equals(Constants.BOS_KONTROL)) {
            Toast.makeText(SifremiUnuttumActivity.this, R.string.toastZorunluBosAlan, Toast.LENGTH_LONG).show();
        } else {
            emailKontrolEt(email);
        }
    }

    private void emailKontrolEt(String email) {
        if (emailFormatiGecerliMi(email) == true) {
            kayitliEmailVarMi(email);
        } else {
            Toast.makeText(SifremiUnuttumActivity.this, R.string.toastEmailFormat, Toast.LENGTH_LONG).show();
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

    private void kayitliEmailVarMi(String email) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            //Email bulunamadi
                            Toast.makeText(SifremiUnuttumActivity.this, R.string.toastEmailBulunamadi, Toast.LENGTH_LONG).show();
                        } else {
                            //Email bulundu
                            emailSifreGonder(email);
                        }
                    }
                }
            }
        });
    }

    private void emailSifreGonder(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SifremiUnuttumActivity.this, R.string.toastSifreGondermeBasarili, Toast.LENGTH_SHORT).show();
                    girisYapActivity();
                } else {
                    Toast.makeText(SifremiUnuttumActivity.this, R.string.toastSifreGondermeBasarisiz, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void girisYapActivity() {
        Intent girisYapIntent = new Intent(SifremiUnuttumActivity.this, GirisActivity.class);
        startActivity(girisYapIntent);
        finish();
    }
}