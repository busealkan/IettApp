package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;

import android.widget.PopupMenu;

public class ProfilActivity extends AppCompatActivity {

    private ImageView imgProfilResim;
    private TextView txtProfilResimDegistir;
    private EditText txtProfilAd, txtProfilSoyad, txtProfilEmail;
    private Button btnProfilBaslikIcon, btnProfilBaslik, btnDegisiklikleriKaydet,btnProfilKanGrubu,btnProfilDate,btnProfilSicil;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Uri imageUri = null;
    Toolbar tbarProfil;
    private Uri filePath;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;
    private DatePickerDialog datePickerDialog;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    private boolean resimDegistiMi=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        init();

    }

    private void init() {
        btnProfilKanGrubu = findViewById(R.id.btnProfilKanGrubu);
        txtProfilResimDegistir = findViewById(R.id.txtProfilResimDegistir);
        btnProfilDate = findViewById(R.id.btnProfilDate);
        imgProfilResim = findViewById(R.id.imgProfilResmi);
        txtProfilAd = findViewById(R.id.txtProfilAd);
        txtProfilSoyad = findViewById(R.id.txtProfilSoyad);
        btnProfilSicil = findViewById(R.id.btnProfilSicil);
        txtProfilEmail = findViewById(R.id.txtProfilEmail);
        btnProfilBaslikIcon = findViewById(R.id.btnProfilBaslikIcon);
        btnProfilBaslik = findViewById(R.id.btnProfilBaslik);
        btnDegisiklikleriKaydet = findViewById(R.id.btnDegisiklikleriKaydet);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnProfilSicil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilActivity.this, R.string.toastSiciliniziDegistiremezsiniz, Toast.LENGTH_SHORT).show();
            }
        });

        btnProfilBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(ProfilActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnDegisiklikleriKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degisiklikleriKaydet();
            }
        });

        txtProfilResimDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeriIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galeriIntent.setType(Constants.IMAGE);
                startActivityForResult(galeriIntent, Constants.SELECT_IMAGE);

            }
        });

        btnProfilDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(ProfilActivity.this,R.style.DateTimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String dogumTarihi = btnProfilDate.getText().toString();
                                if(dogumTarihi.equals(R.string.profilDogumTarihiSec)){
                                    dogumTarihi = "";
                                }
                                btnProfilDate.setText(day + "/" + (month+1) + "/" + year);

                                String btndogumTarih = btnProfilDate.getText().toString().trim();
                                SimpleDateFormat tarihFormat = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date dogumTarih = tarihFormat.parse(btndogumTarih);
                                    btnProfilDate.setText(tarihFormat.format(dogumTarih));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        btnProfilKanGrubu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ProfilActivity.this, btnProfilKanGrubu);
                popup.getMenuInflater().inflate(R.menu.menu_kangrubu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String kanGrubu = item.getTitle().toString();
                        if(kanGrubu.equals(R.string.kanGrubuSeciniz)){
                            kanGrubu = "";
                        }
                        else{
                            btnProfilKanGrubu.setText(item.getTitle().toString());
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });

        profilBilgileriGetir(imgProfilResim, txtProfilAd, txtProfilSoyad, btnProfilSicil, txtProfilEmail);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.SELECT_IMAGE && resultCode == RESULT_OK  && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgProfilResim.setImageBitmap(bitmap);
                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(R.string.toastResimYukleniyor);
            progressDialog.show();


            String userId = mAuth.getCurrentUser().getUid();

            StorageReference ref = storageReference.child(Constants.FIREBASE_STORAGE_IMAGES).child(Constants.FIREBASE_STORAGE_PROFIL_IMAGE).child(userId);

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    resimDegistiMi = true;

                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String downloadUrl = task.getResult().toString();
                            Map imageUpdate = new HashMap();
                            imageUpdate.put(Constants.FIREBASE_KULLANICI_RESIM,downloadUrl);
                            String userId = mAuth.getCurrentUser().getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
                            mDatabase.updateChildren(imageUpdate).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        profilBilgileriGetir(imgProfilResim, txtProfilAd, txtProfilSoyad, btnProfilSicil, txtProfilEmail);
                                        Toast.makeText(ProfilActivity.this, R.string.toastResimYuklemeBasarili, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    resimDegistiMi = false;
                    Toast.makeText(ProfilActivity.this, R.string.toastResimYuklemeGerceklesemedi, Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage(Constants.FIREBASE_STORAGE_IMAGE_YUKLENIYOR + (int) progress + "%");
                }
            });
        }
    }

    private void degisiklikleriKaydet() {
        String ad = txtProfilAd.getText().toString().trim();
        String soyad = txtProfilSoyad.getText().toString().trim();
        String email = txtProfilEmail.getText().toString().trim();


        if (ad.equals(Constants.BOS_KONTROL) || soyad.equals(Constants.BOS_KONTROL) ||  email.equals(Constants.BOS_KONTROL) ||  btnProfilSicil.getText().toString().trim().equals(Constants.BOS_KONTROL)) {
            Toast.makeText(getApplicationContext(), R.string.toastZorunluBosAlan, Toast.LENGTH_LONG).show();
        } else {
            emailKontrolEt(ad, soyad, email);
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

    private void emailKontrolEt(String ad, String soyad, String email) {
        if (emailGecerliMi(email) == true) {
            emailVarMi(ad, soyad, email);
        } else {
            Toast.makeText(getApplicationContext(), R.string.toastEmailFormat, Toast.LENGTH_LONG).show();
        }
    }

    private void emailVarMi(String ad, String soyad, String email) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            //Email bulunamadi
                            authenticationEmailGuncelle(ad, soyad, email);
                        } else {
                            //Kayıtlı email bulunduysa
                            String userEmail = mAuth.getCurrentUser().getEmail();
                            String userId = mAuth.getCurrentUser().getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String dbAd = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                                    String dbSoyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                                    String dbKanGrubu = snapshot.child(Constants.FIREBASE_KULLANICI_KAN_GRUBU).getValue().toString();
                                    String dbDogumTarihi = snapshot.child(Constants.FIREBASE_KULLANICI_DOGUM_TARIHI).getValue().toString();
                                    String dogumTarihi = btnProfilDate.getText().toString();
                                    String kanGrubu = btnProfilKanGrubu.getText().toString();
                                    if(email.equals(userEmail.trim())) {//db email ile textview aynı email ise

                                        if((resimDegistiMi==true) || !(ad.equals(dbAd)) || !(soyad.equals(dbSoyad)) || !(kanGrubu.equals(dbKanGrubu)) || !(dogumTarihi.equals(dbDogumTarihi))){

                                            authenticationEmailGuncelle(ad, soyad, email);
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(), R.string.toastEmailBulundu, Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }
        });

    }

    private void authenticationEmailGuncelle(String ad, String soyad, String email) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String emailDb = snapshot.child(Constants.FIREBASE_KULLANICI_EMAIL).getValue().toString();
                String sifreDb = snapshot.child(Constants.FIREBASE_KULLANICI_SIFRE).getValue().toString();
                String dbKanGrubu = snapshot.child(Constants.FIREBASE_KULLANICI_KAN_GRUBU).getValue().toString();
                String dbDogumTarihi = snapshot.child(Constants.FIREBASE_KULLANICI_DOGUM_TARIHI).getValue().toString();



                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(emailDb, sifreDb);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(txtProfilEmail.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    String kanGrubu = btnProfilKanGrubu.getText().toString();
                                                    String dogumTarihi = btnProfilDate.getText().toString();
                                                    realtimeDbProfilGuncelle(ad, soyad, email,kanGrubu,dogumTarihi);
                                                } else {
                                                    Toast.makeText(ProfilActivity.this, R.string.toastEmailGuncellemeBasarisiz, Toast.LENGTH_SHORT).show();

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

    private void realtimeDbProfilGuncelle(String ad, String soyad, String email,String kanGrubu,String dogumTarihi) {
        Map profilUpdate = new HashMap();
        profilUpdate.put(Constants.FIREBASE_KULLANICI_AD, ad);
        profilUpdate.put(Constants.FIREBASE_KULLANICI_SOYAD, soyad);
        profilUpdate.put(Constants.FIREBASE_KULLANICI_EMAIL, email);
        profilUpdate.put(Constants.FIREBASE_KULLANICI_KAN_GRUBU, kanGrubu);
        profilUpdate.put(Constants.FIREBASE_KULLANICI_DOGUM_TARIHI, dogumTarihi);

        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.updateChildren(profilUpdate).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfilActivity.this, getResources().getString(R.string.toastProfilGuncellemeBasarili), Toast.LENGTH_SHORT).show();
                    profilBilgileriGetir(imgProfilResim, txtProfilAd, txtProfilSoyad, btnProfilSicil, txtProfilEmail);

                } else {
                    Toast.makeText(ProfilActivity.this, getResources().getString(R.string.toastProfilGuncellemeBasarisiz), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void profilBilgileriGetir(ImageView imgProfilResim, TextView txtProfilAd, TextView txtProfilSoyad, TextView txtProfilSicil, TextView txtProfilEmail) {
        mAuth = FirebaseAuth.getInstance();

        String userId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();
                String email = snapshot.child(Constants.FIREBASE_KULLANICI_EMAIL).getValue().toString();
                String resim = snapshot.child(Constants.FIREBASE_KULLANICI_RESIM).getValue().toString();
                String kanGrubu = snapshot.child(Constants.FIREBASE_KULLANICI_KAN_GRUBU).getValue().toString();
                String dogumTarihi = snapshot.child(Constants.FIREBASE_KULLANICI_DOGUM_TARIHI).getValue().toString();

                txtProfilAd.setText(ad);
                txtProfilSoyad.setText(soyad);
                txtProfilSicil.setText(sicil);
                txtProfilEmail.setText(email);

                if(kanGrubu.equals(Constants.BOS_KONTROL)){
                    btnProfilKanGrubu.setText(R.string.kanGrubuSeciniz);
                }
                else{
                    btnProfilKanGrubu.setText(kanGrubu);

                }

                if(dogumTarihi.equals(Constants.BOS_KONTROL)){
                    btnProfilDate.setText(R.string.profilDogumTarihiSec);
                }
                else{
                    btnProfilDate.setText(dogumTarihi);

                }


                imageUri = Uri.parse(resim);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profildefault);
                Glide.with(ProfilActivity.this).setDefaultRequestOptions(requestOptions).load(imageUri).into(imgProfilResim);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
