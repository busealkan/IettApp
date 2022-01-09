package com.h5190059.iett.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.Constants;
import com.whiteelephant.monthpicker.MonthPickerDialog;
import java.util.Calendar;

public class BordroActivity extends AppCompatActivity {

    private Button btnBordroBaslik,btnBordroBaslikIcon,btnBordroAra,btnBordroTarih,btnBordroTur,btnBordroAd,btnBordroSoyad,btnBordroSicil,btnBordroIsYeri,
            btnBordroSskNo,btnBordroSskTescil,btnBordroUnvani,btnBordroGrup,btnBordroSaatUcreti,btnBordroSigortaGunu;

    private TextView txtBordroKisiselBilgiler,txtBordroAd,txtBordroSoyad,txtBordroSicil,txtBordroIsYeri,txtBordroSskNo,txtBordroSskTescil,txtBordroUnvani,txtBordroGrup,txtBordroSaatUcreti,txtBordroSigortaGunu,txtBordroUcret;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private Button btnBordroUcretSSkNoId,btnBordroUcretNetUcret,btnBordroUcretGunVeSaatToplamUcret,btnBordroUcretUcretToplam,btnBordroUcretAylikSigortaMatrahi,
            btnBordroUcretSgkTavaniniAsanMiktar,btnBordroUcretAylikVergiMatrahi,btnBordroUcretYillikTgvm,btnBordroUcretHayatSigortasi,btnBordroUcretAskerlikBorclari,
            btnBordroUcretEngelliIndirimi,btnBordroUcretIsverenSsk,btnBordroUcretIsverenIssizlik,btnBordroUcretUcretMaliyeti,btnBordroUcretYasalKesintiToplami,
            btnBordroUcretOzelKesintiToplami,btnBordroUcretKesintilerToplami;

    private TextView txtBordroUcretSSkNoId,txtBordroUcretNetUcret,txtBordroUcretGunVeSaatToplamUcret,txtBordroUcretUcretToplam,txtBordroUcretAylikSigortaMatrahi,
            txtBordroUcretAylikVergiMatrahi,txtBordroUcretYillikTgvm,txtBordroUcretHayatSigortasi,txtBordroUcretAskerlikBorclari,txtBordroUcretEngelliIndirimi,
            txtBordroUcretIsverenSsk,txtBordroUcretIsverenIssizlik,txtBordroUcretUcretMaliyeti,txtBordroUcretYasalKesintiToplami,txtBordroUcretOzelKesintiToplami,
            txtBordroUcretKesintilerToplami,txtBordroUcretSgkTavaniniAsanMiktar;

    private Button btnBordroKazanclarSskNoId,btnBordroKazanclarNormalMesaiGun,btnBordroKazanclarNormalMesaiSaat,btnBordroKazanclarNormalMesaiTutar,btnBordroKazanclarVardiyaZammiSaat,
            btnBordroKazanclarVardiyaZammiTutar,btnBordroKazanclarHaftaTatiliGun,btnBordroKazanclarHaftaTatiliSaat,btnBordroKazanclarHaftaTatiliTutar,btnBordroKazanclarFazlaMesaiSaatYuzdeYuz,
            btnBordroKazanclarFazlaMesaiTutarYuzdeYuz,btnBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz,btnBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz,btnBordroKazanclarFazlaMesaiSaatYuzdeUcYuz,
            btnBordroKazanclarFazlaMesaiTutarYuzdeUcYuz,btnBordroKazanclarChttAltiGunSaat,btnBordroKazanclarChttAltiGunTutar,btnBordroKazanclarDinlenmeliCalismaSaati,
            btnBordroKazanclarDinlenmeliCalismaTutar,btnBordroKazanclarMukabilCalismaSaati,btnBordroKazanclarMukabilCalismaTutar,btnBordroKazanclarYillikUcretliIzinGun,
            btnBordroKazanclarYillikUcretliIzinSaat,btnBordroKazanclarYillikUcretliIzinTutar,btnBordroKazanclarHastalıkVeIstIzniGun,btnBordroKazanclarHastalikVeIstGunSaat,
            btnBordroKazanclarHastalikVeIstIzniTutar,btnBordroKazanclarSosyalIzinGun,btnBordroKazanclarSosyalIzinSaat,btnBordroKazanclarSosyalIzinTutar,btnBordroKazanclarUcretsizIzinGun,
            btnBordroKazanclarUcretsizIzinSaat,btnBordroKazanclarUcretsizIzinTutar,btnBordroKazanclarYoneticiTazminati,btnBordroKazanclarCezaIade,btnBordroKazanclarHacizIade,
            btnBordroKazanclarHasarIade,btnBordroKazanclarMuhtelifArti,btnBordroKazanclarMuhtelifEksi,btnBordroKazanclarYemek,btnBordroKazanclarNetIlave,btnBordroKazanclarNetIlave2,
            btnBordroKazanclarSosyalPaket,btnBordroKazanclarAgi;

    private TextView txtBordroKazanclarSskNoId,txtBordroKazanclarNormalMesaiGun,txtBordroKazanclarNormalMesaiSaat,txtBordroKazanclarNormalMesaiTutar,txtBordroKazanclarVardiyaZammiSaat,
            txtBordroKazanclarVardiyaZammiTutar,txtBordroKazanclarHaftaTatiliGun,txtBordroKazanclarHaftaTatiliSaat,txtBordroKazanclarHaftaTatiliTutar,txtBordroKazanclarFazlaMesaiSaatYuzdeYuz,
            txtBordroKazanclarFazlaMesaiTutarYuzdeYuz,txtBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz,txtBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz,txtBordroKazanclarFazlaMesaiSaatYuzdeUcYuz,
            txtBordroKazanclarFazlaMesaiTutarYuzdeUcYuz,txtBordroKazanclarChttAltiGunSaat,txtBordroKazanclarChttAltiGunTutar,txtBordroKazanclarDinlenmeliCalismaSaati,
            txtBordroKazanclarDinlenmeliCalismaTutar,txtBordroKazanclarMukabilCalismaSaati,txtBordroKazanclarMukabilCalismaTutar,txtBordroKazanclarYillikUcretliIzinGun,
            txtBordroKazanclarYillikUcretliIzinSaat,txtBordroKazanclarYillikUcretliIzinTutar,txtBordroKazanclarHastalıkVeIstIzniGun,txtBordroKazanclarHastalikVeIstGunSaat,
            txtBordroKazanclarHastalikVeIstIzniTutar,txtBordroKazanclarSosyalIzinGun,txtBordroKazanclarSosyalIzinSaat,txtBordroKazanclarSosyalIzinTutar,txtBordroKazanclarUcretsizIzinGun,
            txtBordroKazanclarUcretsizIzinSaat,txtBordroKazanclarUcretsizIzinTutar,txtBordroKazanclarYoneticiTazminati,txtBordroKazanclarCezaIade,txtBordroKazanclarHacizIade,
            txtBordroKazanclarHasarIade,txtBordroKazanclarMuhtelifArti,txtBordroKazanclarMuhtelifEksi,txtBordroKazanclarYemek,txtBordroKazanclarNetIlave,txtBordroKazanclarNetIlave2,
            txtBordroKazanclarSosyalPaket,txtBordroKazanclarAgi;

    private Button btnBordroYasalKesintilerSskNoId,btnBordroYasalKesintilerSigorta,btnBordroYasalKesintilerIssizlikSigortasi,btnBordroYasalKesintilerGelirVergisi,btnBordroYasalKesintilerDamgaPulu,btnBordroYasalKesintilerSendikaKesintisi;

    private TextView txtBordroYasalKesintiler,txtBordroYasalKesintilerSskNoId,txtBordroYasalKesintilerSigorta,txtBordroYasalKesintilerIssizlikSigortasi,txtBordroYasalKesintilerGelirVergisi,txtBordroYasalKesintilerDamgaPulu,txtBordroYasalKesintilerSendikaKesintisi;

    private Button btnBordroOzelKesintilerSskNoId,btnBordroOzelKesintilerAvans,btnBordroOzelKesintilerHaciz,btnBordroOzelKesintilerNafaka,btnBordroOzelKesintilerHasar,btnBordroOzelKesintilerCeza,
            btnBordroOzelKesintilerTrafikBorcu,btnBordroOzelKesintilerMuhtelifBorc,btnBordroOzelKesintilerKefalet,btnBordroOzelKesintilerTelefon,btnBordroOzelKesintilerKres,btnBordroOzelKesintilerBerber,btnBordroOzelKesintilerVergiSskBorcu;


    private TextView txtBordroOzelKesintilerSskNoId,txtBordroOzelKesintilerAvans,txtBordroOzelKesintilerHaciz,txtBordroOzelKesintilerNafaka,txtBordroOzelKesintilerHasar,
            txtBordroOzelKesintilerCeza,txtBordroOzelKesintilerTrafikBorcu,txtBordroOzelKesintilerMuhtelifBorc,txtBordroOzelKesintilerKefalet,txtBordroOzelKesintilerTelefon,
            txtBordroOzelKesintilerKres,txtBordroOzelKesintilerBerber,txtBordroOzelKesintilerVergiSskBorcu,txtBordroOzelKesintiler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bordro);
        init();
    }

    private void init() {
        btnBordroBaslik = findViewById(R.id.btnBordroBaslik);
        btnBordroAra = findViewById(R.id.btnBordroAra);
        btnBordroTarih = findViewById(R.id.btnBordroTarih);
        btnBordroTur= findViewById(R.id.btnBordroTur);
        btnBordroBaslikIcon= findViewById(R.id.btnBordroBaslikIcon);

        btnBordroAd= findViewById(R.id.btnBordroAd);
        btnBordroSoyad= findViewById(R.id.btnBordroSoyad);
        btnBordroSicil= findViewById(R.id.btnBordroSicil);
        btnBordroIsYeri= findViewById(R.id.btnBordroIsYeri);
        btnBordroSskNo= findViewById(R.id.btnBordroSskNo);
        btnBordroSskTescil= findViewById(R.id.btnBordroSskTescil);
        btnBordroUnvani= findViewById(R.id.btnBordroUnvani);
        btnBordroGrup= findViewById(R.id.btnBordroGrup);
        btnBordroSaatUcreti= findViewById(R.id.btnBordroSaatUcreti);
        btnBordroSigortaGunu= findViewById(R.id.btnBordroSigortaGunu);

        txtBordroKisiselBilgiler= findViewById(R.id.txtBordroKisiselBilgiler);
        txtBordroAd= findViewById(R.id.txtBordroAd);
        txtBordroSoyad= findViewById(R.id.txtBordroSoyad);
        txtBordroSicil= findViewById(R.id.txtBordroSicil);
        txtBordroIsYeri= findViewById(R.id.txtBordroIsYeri);
        txtBordroSskNo= findViewById(R.id.txtBordroSskNo);
        txtBordroSskTescil= findViewById(R.id.txtBordroSskTescil);
        txtBordroUnvani= findViewById(R.id.txtBordroUnvani);
        txtBordroGrup= findViewById(R.id.txtBordroGrup);
        txtBordroSaatUcreti= findViewById(R.id.txtBordroSaatUcreti);
        txtBordroSigortaGunu= findViewById(R.id.txtBordroSigortaGunu);


        txtBordroUcret= findViewById(R.id.txtBordroUcret);
        btnBordroUcretSSkNoId= findViewById(R.id.btnBordroUcretSSkNoId);
        btnBordroUcretNetUcret= findViewById(R.id.btnBordroUcretNetUcret);
        btnBordroUcretGunVeSaatToplamUcret= findViewById(R.id.btnBordroUcretGunVeSaatToplamUcret);
        btnBordroUcretUcretToplam= findViewById(R.id.btnBordroUcretUcretToplam);
        btnBordroUcretAylikSigortaMatrahi= findViewById(R.id.btnBordroUcretAylikSigortaMatrahi);
        btnBordroUcretSgkTavaniniAsanMiktar= findViewById(R.id.btnBordroUcretSgkTavaniniAsanMiktar);
        btnBordroUcretAylikVergiMatrahi= findViewById(R.id.btnBordroUcretAylikVergiMatrahi);
        btnBordroUcretYillikTgvm= findViewById(R.id.btnBordroUcretYillikTgvm);
        btnBordroUcretHayatSigortasi= findViewById(R.id.btnBordroUcretHayatSigortasi);
        btnBordroUcretAskerlikBorclari= findViewById(R.id.btnBordroUcretAskerlikBorclari);
        btnBordroUcretEngelliIndirimi= findViewById(R.id.btnBordroUcretEngelliIndirimi);
        btnBordroUcretIsverenSsk= findViewById(R.id.btnBordroUcretIsverenSsk);
        btnBordroUcretIsverenIssizlik= findViewById(R.id.btnBordroUcretIsverenIssizlik);
        btnBordroUcretUcretMaliyeti= findViewById(R.id.btnBordroUcretUcretMaliyeti);
        btnBordroUcretYasalKesintiToplami= findViewById(R.id.btnBordroUcretYasalKesintiToplami);
        btnBordroUcretOzelKesintiToplami= findViewById(R.id.btnBordroUcretOzelKesintiToplami);
        btnBordroUcretKesintilerToplami= findViewById(R.id.btnBordroUcretKesintilerToplami);
        txtBordroUcretSSkNoId= findViewById(R.id.txtBordroUcretSSkNoId);
        txtBordroUcretNetUcret= findViewById(R.id.txtBordroUcretNetUcret);
        txtBordroUcretGunVeSaatToplamUcret= findViewById(R.id.txtBordroUcretGunVeSaatToplamUcret);
        txtBordroUcretUcretToplam= findViewById(R.id.txtBordroUcretUcretToplam);
        txtBordroUcretAylikSigortaMatrahi= findViewById(R.id.txtBordroUcretAylikSigortaMatrahi);
        txtBordroUcretSgkTavaniniAsanMiktar= findViewById(R.id.txtBordroUcretSgkTavaniniAsanMiktar);
        txtBordroUcretAylikVergiMatrahi= findViewById(R.id.txtBordroUcretAylikVergiMatrahi);
        txtBordroUcretYillikTgvm= findViewById(R.id.txtBordroUcretYillikTgvm);
        txtBordroUcretHayatSigortasi= findViewById(R.id.txtBordroUcretHayatSigortasi);
        txtBordroUcretAskerlikBorclari= findViewById(R.id.txtBordroUcretAskerlikBorclari);
        txtBordroUcretEngelliIndirimi= findViewById(R.id.txtBordroUcretEngelliIndirimi);
        txtBordroUcretIsverenSsk= findViewById(R.id.txtBordroUcretIsverenSsk);
        txtBordroUcretIsverenIssizlik= findViewById(R.id.txtBordroUcretIsverenIssizlik);
        txtBordroUcretUcretMaliyeti= findViewById(R.id.txtBordroUcretUcretMaliyeti);
        txtBordroUcretYasalKesintiToplami= findViewById(R.id.txtBordroUcretYasalKesintiToplami);
        txtBordroUcretOzelKesintiToplami= findViewById(R.id.txtBordroUcretOzelKesintiToplami);
        txtBordroUcretKesintilerToplami= findViewById(R.id.txtBordroUcretKesintilerToplami);

        btnBordroKazanclarSskNoId= findViewById(R.id.btnBordroKazanclarSskNoId);
        btnBordroKazanclarNormalMesaiGun= findViewById(R.id.btnBordroKazanclarNormalMesaiGun);
        btnBordroKazanclarNormalMesaiSaat= findViewById(R.id.btnBordroKazanclarNormalMesaiSaat);
        btnBordroKazanclarNormalMesaiTutar= findViewById(R.id.btnBordroKazanclarNormalMesaiTutar);
        btnBordroKazanclarVardiyaZammiSaat= findViewById(R.id.btnBordroKazanclarVardiyaZammiSaat);
        btnBordroKazanclarVardiyaZammiTutar= findViewById(R.id.btnBordroKazanclarVardiyaZammiTutar);
        btnBordroKazanclarHaftaTatiliSaat= findViewById(R.id.btnBordroKazanclarHaftaTatiliSaat);
        btnBordroKazanclarHaftaTatiliGun= findViewById(R.id.btnBordroKazanclarHaftaTatiliGun);
        btnBordroKazanclarHaftaTatiliTutar= findViewById(R.id.btnBordroKazanclarHaftaTatiliTutar);
        btnBordroKazanclarFazlaMesaiSaatYuzdeYuz= findViewById(R.id.btnBordroKazanclarFazlaMesaiSaatYuzdeYuz);
        btnBordroKazanclarFazlaMesaiTutarYuzdeYuz= findViewById(R.id.btnBordroKazanclarFazlaMesaiTutarYuzdeYuz);
        btnBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz= findViewById(R.id.btnBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz);
        btnBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz= findViewById(R.id.btnBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz);
        btnBordroKazanclarFazlaMesaiSaatYuzdeUcYuz= findViewById(R.id.btnBordroKazanclarFazlaMesaiSaatYuzdeUcYuz);
        btnBordroKazanclarFazlaMesaiTutarYuzdeUcYuz= findViewById(R.id.btnBordroKazanclarFazlaMesaiTutarYuzdeUcYuz);
        btnBordroKazanclarDinlenmeliCalismaSaati= findViewById(R.id.btnBordroKazanclarDinlenmeliCalismaSaati);
        btnBordroKazanclarDinlenmeliCalismaTutar= findViewById(R.id.btnBordroKazanclarDinlenmeliCalismaTutar);
        btnBordroKazanclarMukabilCalismaSaati= findViewById(R.id.btnBordroKazanclarMukabilCalismaSaati);
        btnBordroKazanclarMukabilCalismaTutar= findViewById(R.id.btnBordroKazanclarMukabilCalismaTutar);
        btnBordroKazanclarYillikUcretliIzinGun= findViewById(R.id.btnBordroKazanclarYillikUcretliIzinGun);
        btnBordroKazanclarYillikUcretliIzinSaat= findViewById(R.id.btnBordroKazanclarYillikUcretliIzinSaat);
        btnBordroKazanclarYillikUcretliIzinTutar= findViewById(R.id.btnBordroKazanclarYillikUcretliIzinTutar);
        btnBordroKazanclarHastalıkVeIstIzniGun= findViewById(R.id.btnBordroKazanclarHastalıkVeIstIzniGun);
        btnBordroKazanclarHastalikVeIstGunSaat= findViewById(R.id.btnBordroKazanclarHastalikVeIstGunSaat);
        btnBordroKazanclarHastalikVeIstIzniTutar= findViewById(R.id.btnBordroKazanclarHastalikVeIstIzniTutar);
        btnBordroKazanclarSosyalIzinGun= findViewById(R.id.btnBordroKazanclarSosyalIzinGun);
        btnBordroKazanclarSosyalIzinSaat= findViewById(R.id.btnBordroKazanclarSosyalIzinSaat);
        btnBordroKazanclarSosyalIzinTutar= findViewById(R.id.btnBordroKazanclarSosyalIzinTutar);
        btnBordroKazanclarUcretsizIzinGun= findViewById(R.id.btnBordroKazanclarUcretsizIzinGun);
        btnBordroKazanclarUcretsizIzinSaat= findViewById(R.id.btnBordroKazanclarUcretsizIzinSaat);
        btnBordroKazanclarUcretsizIzinTutar= findViewById(R.id.btnBordroKazanclarUcretsizIzinTutar);
        btnBordroKazanclarYoneticiTazminati= findViewById(R.id.btnBordroKazanclarYoneticiTazminati);
        btnBordroKazanclarCezaIade= findViewById(R.id.btnBordroKazanclarCezaIade);
        btnBordroKazanclarHacizIade= findViewById(R.id.btnBordroKazanclarHacizIade);
        btnBordroKazanclarHasarIade= findViewById(R.id.btnBordroKazanclarHasarIade);
        btnBordroKazanclarMuhtelifArti= findViewById(R.id.btnBordroKazanclarMuhtelifArti);
        btnBordroKazanclarMuhtelifEksi= findViewById(R.id.btnBordroKazanclarMuhtelifEksi);
        btnBordroKazanclarYemek= findViewById(R.id.btnBordroKazanclarYemek);
        btnBordroKazanclarNetIlave= findViewById(R.id.btnBordroKazanclarNetIlave);
        btnBordroKazanclarNetIlave2= findViewById(R.id.btnBordroKazanclarNetIlave2);
        btnBordroKazanclarSosyalPaket= findViewById(R.id.btnBordroOzelKesintilerTrafikBorcu);
        btnBordroKazanclarAgi= findViewById(R.id.btnBordroOzelKesintilerBerber);
        btnBordroKazanclarChttAltiGunSaat= findViewById(R.id.btnBordroKazanclarChttAltiGunSaat);
        btnBordroKazanclarChttAltiGunTutar= findViewById(R.id.btnBordroKazanclarChttAltiGunSaat);

        txtBordroKazanclarSskNoId= findViewById(R.id.txtBordroKazanclarSskNoId);
        txtBordroKazanclarNormalMesaiGun= findViewById(R.id.txtBordroKazanclarNormalMesaiGun);
        txtBordroKazanclarNormalMesaiSaat= findViewById(R.id.txtBordroKazanclarNormalMesaiSaat);
        txtBordroKazanclarNormalMesaiTutar= findViewById(R.id.txtBordroKazanclarNormalMesaiTutar);
        txtBordroKazanclarVardiyaZammiSaat= findViewById(R.id.txtBordroKazanclarVardiyaZammiSaat);
        txtBordroKazanclarVardiyaZammiTutar= findViewById(R.id.txtBordroKazanclarVardiyaZammiTutar);
        txtBordroKazanclarHaftaTatiliGun= findViewById(R.id.txtBordroKazanclarHaftaTatiliGun);
        txtBordroKazanclarHaftaTatiliSaat= findViewById(R.id.txtBordroKazanclarHaftaTatiliSaat);
        txtBordroKazanclarHaftaTatiliTutar= findViewById(R.id.txtBordroKazanclarHaftaTatiliTutar);
        txtBordroKazanclarFazlaMesaiSaatYuzdeYuz= findViewById(R.id.txtBordroKazanclarFazlaMesaiSaatYuzdeYuz);
        txtBordroKazanclarFazlaMesaiTutarYuzdeYuz= findViewById(R.id.txtBordroKazanclarFazlaMesaiTutarYuzdeYuz);
        txtBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz= findViewById(R.id.txtBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz);
        txtBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz= findViewById(R.id.txtBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz);
        txtBordroKazanclarFazlaMesaiSaatYuzdeUcYuz= findViewById(R.id.txtBordroKazanclarFazlaMesaiSaatYuzdeUcYuz);
        txtBordroKazanclarFazlaMesaiTutarYuzdeUcYuz= findViewById(R.id.txtBordroKazanclarFazlaMesaiTutarYuzdeUcYuz);
        txtBordroKazanclarChttAltiGunSaat= findViewById(R.id.txtBordroKazanclarChttAltiGunSaat);
        txtBordroKazanclarChttAltiGunTutar= findViewById(R.id.txtBordroKazanclarChttAltiGunTutar);
        txtBordroKazanclarDinlenmeliCalismaSaati= findViewById(R.id.txtBordroKazanclarDinlenmeliCalismaSaati);
        txtBordroKazanclarDinlenmeliCalismaTutar= findViewById(R.id.txtBordroKazanclarDinlenmeliCalismaTutar);
        txtBordroKazanclarMukabilCalismaSaati= findViewById(R.id.txtBordroKazanclarMukabilCalismaSaati);
        txtBordroKazanclarMukabilCalismaTutar= findViewById(R.id.txtBordroKazanclarMukabilCalismaTutar);
        txtBordroKazanclarYillikUcretliIzinGun= findViewById(R.id.txtBordroKazanclarYillikUcretliIzinGun);
        txtBordroKazanclarYillikUcretliIzinSaat= findViewById(R.id.txtBordroKazanclarYillikUcretliIzinSaat);
        txtBordroKazanclarYillikUcretliIzinTutar= findViewById(R.id.txtBordroKazanclarYillikUcretliIzinTutar);
        txtBordroKazanclarHastalıkVeIstIzniGun= findViewById(R.id.txtBordroKazanclarHastalıkVeIstIzniGun);
        txtBordroKazanclarHastalikVeIstGunSaat= findViewById(R.id.txtBordroKazanclarHastalikVeIstGunSaat);
        txtBordroKazanclarHastalikVeIstIzniTutar= findViewById(R.id.txtBordroKazanclarHastalikVeIstIzniTutar);
        txtBordroKazanclarSosyalIzinGun= findViewById(R.id.txtBordroKazanclarSosyalIzinGun);
        txtBordroKazanclarSosyalIzinSaat= findViewById(R.id.txtBordroKazanclarSosyalIzinSaat);
        txtBordroKazanclarSosyalIzinTutar= findViewById(R.id.txtBordroKazanclarSosyalIzinTutar);
        txtBordroKazanclarUcretsizIzinGun= findViewById(R.id.txtBordroKazanclarUcretsizIzinGun);
        txtBordroKazanclarUcretsizIzinSaat= findViewById(R.id.txtBordroKazanclarUcretsizIzinSaat);
        txtBordroKazanclarUcretsizIzinTutar= findViewById(R.id.txtBordroKazanclarUcretsizIzinTutar);
        txtBordroKazanclarYoneticiTazminati= findViewById(R.id.txtBordroKazanclarYoneticiTazminati);
        txtBordroKazanclarCezaIade= findViewById(R.id.txtBordroKazanclarCezaIade);
        txtBordroKazanclarHacizIade= findViewById(R.id.txtBordroKazanclarHacizIade);
        txtBordroKazanclarHasarIade= findViewById(R.id.txtBordroKazanclarHasarIade);
        txtBordroKazanclarMuhtelifArti= findViewById(R.id.txtBordroKazanclarMuhtelifArti);
        txtBordroKazanclarMuhtelifEksi= findViewById(R.id.txtBordroKazanclarMuhtelifEksi);
        txtBordroKazanclarYemek= findViewById(R.id.txtBordroKazanclarYemek);
        txtBordroKazanclarNetIlave= findViewById(R.id.txtBordroKazanclarNetIlave);
        txtBordroKazanclarNetIlave2= findViewById(R.id.txtBordroKazanclarNetIlave2);
        txtBordroKazanclarSosyalPaket= findViewById(R.id.txtBordroKazanclarSosyalPaket);
        txtBordroKazanclarAgi= findViewById(R.id.txtBordroKazanclarAgi);



        btnBordroYasalKesintilerSskNoId= findViewById(R.id.btnBordroYasalKesintilerSskNoId);
        btnBordroYasalKesintilerSigorta= findViewById(R.id.btnBordroYasalKesintilerSigorta);
        btnBordroYasalKesintilerIssizlikSigortasi= findViewById(R.id.btnBordroYasalKesintilerIssizlikSigortasi);
        btnBordroYasalKesintilerGelirVergisi= findViewById(R.id.btnBordroYasalKesintilerGelirVergisi);
        btnBordroYasalKesintilerDamgaPulu= findViewById(R.id.btnBordroOzelKesintilerTrafikBorcu);
        btnBordroYasalKesintilerSendikaKesintisi= findViewById(R.id.btnBordroYasalKesintilerSendikaKesintisi);
        txtBordroYasalKesintilerSskNoId= findViewById(R.id.txtBordroYasalKesintilerSskNoId);
        txtBordroYasalKesintilerSigorta= findViewById(R.id.txtBordroYasalKesintilerSigorta);
        txtBordroYasalKesintilerIssizlikSigortasi= findViewById(R.id.txtBordroYasalKesintilerIssizlikSigortasi);
        txtBordroYasalKesintilerGelirVergisi= findViewById(R.id.txtBordroYasalKesintilerGelirVergisi);
        txtBordroYasalKesintilerDamgaPulu= findViewById(R.id.txtBordroYasalKesintilerDamgaPulu);
        txtBordroYasalKesintilerSendikaKesintisi= findViewById(R.id.txtBordroYasalKesintilerSendikaKesintisi);
        txtBordroYasalKesintiler= findViewById(R.id.txtBordroYasalKesintiler);



        btnBordroOzelKesintilerSskNoId= findViewById(R.id.btnBordroOzelKesintilerSskNoId);
        btnBordroOzelKesintilerAvans= findViewById(R.id.btnBordroOzelKesintilerAvans);
        btnBordroOzelKesintilerHaciz= findViewById(R.id.btnBordroOzelKesintilerHaciz);
        btnBordroOzelKesintilerNafaka= findViewById(R.id.btnBordroOzelKesintilerNafaka);
        btnBordroOzelKesintilerHasar= findViewById(R.id.btnBordroOzelKesintilerHasar);
        btnBordroOzelKesintilerCeza= findViewById(R.id.btnBordroOzelKesintilerCeza);
        btnBordroOzelKesintilerTrafikBorcu= findViewById(R.id.btnBordroOzelKesintilerTrafikBorcu);
        btnBordroOzelKesintilerMuhtelifBorc= findViewById(R.id.btnBordroOzelKesintilerMuhtelifBorc);
        btnBordroOzelKesintilerKefalet= findViewById(R.id.btnBordroOzelKesintilerKefalet);
        btnBordroOzelKesintilerTelefon= findViewById(R.id.btnBordroOzelKesintilerTelefon);
        btnBordroOzelKesintilerKres= findViewById(R.id.btnBordroOzelKesintilerKres);
        btnBordroOzelKesintilerBerber= findViewById(R.id.btnBordroOzelKesintilerBerber);
        btnBordroOzelKesintilerVergiSskBorcu= findViewById(R.id.btnBordroOzelKesintilerVergiSskBorcu);

        txtBordroOzelKesintilerSskNoId= findViewById(R.id.txtBordroOzelKesintilerSskNoId);
        txtBordroOzelKesintilerAvans= findViewById(R.id.txtBordroOzelKesintilerAvans);
        txtBordroOzelKesintilerHaciz= findViewById(R.id.txtBordroOzelKesintilerHaciz);
        txtBordroOzelKesintilerNafaka= findViewById(R.id.txtBordroOzelKesintilerNafaka);
        txtBordroOzelKesintilerHasar= findViewById(R.id.txtBordroOzelKesintilerHasar);
        txtBordroOzelKesintilerCeza= findViewById(R.id.txtBordroOzelKesintilerCeza);
        txtBordroOzelKesintilerTrafikBorcu= findViewById(R.id.txtBordroOzelKesintilerTrafikBorcu);
        txtBordroOzelKesintilerMuhtelifBorc= findViewById(R.id.txtBordroOzelKesintilerMuhtelifBorc);
        txtBordroOzelKesintilerKefalet= findViewById(R.id.txtBordroOzelKesintilerKefalet);
        txtBordroOzelKesintilerTelefon= findViewById(R.id.txtBordroOzelKesintilerTelefon);
        txtBordroOzelKesintilerKres= findViewById(R.id.txtBordroOzelKesintilerKres);
        txtBordroOzelKesintilerBerber= findViewById(R.id.txtBordroOzelKesintilerBerber);
        txtBordroOzelKesintilerVergiSskBorcu= findViewById(R.id.txtBordroOzelKesintilerVergiSskBorcu);
        txtBordroOzelKesintiler= findViewById(R.id.txtBordroOzelKesintiler);


        mAuth = FirebaseAuth.getInstance();

        bilgileriGizle();

        btnBordroBaslikIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfaIntent = new Intent(BordroActivity.this, AnaSayfaActivity.class);
                startActivity(anasayfaIntent);
                finish();
            }
        });

        btnBordroTur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(BordroActivity.this, btnBordroTur);
                popup.getMenuInflater().inflate(R.menu.menu_bordro_tur, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        btnBordroTur.setText(item.getTitle().toString());
                        return false;
                    }
                });

                popup.show();
            }
        });

        btnBordroTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(BordroActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                btnBordroTarih.setText((selectedMonth+1)+"/"+selectedYear);

                            }
                        },today.get(Calendar.YEAR),today.get(Calendar.MONTH));

                builder.setActivatedMonth(Calendar.JULY)
                        .setMinYear(1990)
                        .setActivatedYear(today.get(Calendar.YEAR))
                        .setMaxYear(2030)
                        .build()
                        .show();


            }

        });


        btnBordroAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bosAlanVarMiKontrolEt();
            }
        });

    }

    private void bosAlanVarMiKontrolEt() {
        String bordroTarih = btnBordroTarih.getText().toString().trim();
        String bordroTur = btnBordroTur.getText().toString().trim();


        if (bordroTarih.equals(Constants.BOS_KONTROL) || bordroTarih.equals(getResources().getString(R.string.bordroTarihTools)) || bordroTur.equals(Constants.BOS_KONTROL) || bordroTur.equals(getResources().getString(R.string.bordroTurSeciniz))) {
            Toast.makeText(getApplicationContext(), R.string.toastZorunluBosAlan, Toast.LENGTH_LONG).show();
        } else {
            String firebaseBordroTur;
            if(bordroTur.equals(getResources().getString(R.string.bordroTur1))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR1;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur2))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR2;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur3))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR3;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur4))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR4;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur5))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR5;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur6))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR6;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur7))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR7;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur8))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR8;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur9))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR9;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur10))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR10;
            }
            else if(bordroTur.equals(getResources().getString(R.string.bordroTur11))){
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR11;
            }
            else{
                firebaseBordroTur=Constants.FIREBASE_BORDRO_TUR12;

            }
            bordroBilgisiVarMi(bordroTarih,firebaseBordroTur);
        }
    }

    private void bordroBilgisiVarMi(String bordroTarih,String bordroTur) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_BORDRO).child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(Constants.FIREBASE_CHILD+bordroTarih.replace('/','_')+Constants.FIREBASE_CHILD+bordroTur)){
                    kisiselBilgileriGetir(bordroTarih,bordroTur,userId);
                }
                else{
                    Toast.makeText(BordroActivity.this, getResources().getString(R.string.toastBordroBulunmamaktadir), Toast.LENGTH_SHORT).show();
                    bilgileriGizle();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void kisiselBilgileriGetir(String bordroTarih,String bordroTur,String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS).child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ad = snapshot.child(Constants.FIREBASE_KULLANICI_AD).getValue().toString();
                String soyad = snapshot.child(Constants.FIREBASE_KULLANICI_SOYAD).getValue().toString();
                String sicil = snapshot.child(Constants.FIREBASE_KULLANICI_SICIL).getValue().toString();

                txtBordroAd.setText(ad);
                txtBordroSoyad.setText(soyad);
                txtBordroSicil.setText(sicil);


                bordroBilgileriGetir(bordroTarih,bordroTur,userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bordroBilgileriGetir(String bordroTarih, String bordroTur, String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_BORDRO).child(userId).child(bordroTarih.replace('/','_')).child(bordroTur);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isYeri = snapshot.child(Constants.FIREBASE_BORDRO_IS_YERI).getValue().toString();
                String sskNo = snapshot.child(Constants.FIREBASE_BORDRO_SSK_NO).getValue().toString();
                String sskTescil = snapshot.child(Constants.FIREBASE_BORDRO_SSK_TESCİL).getValue().toString();
                String unvani = snapshot.child(Constants.FIREBASE_BORDRO_UNVANI).getValue().toString();
                String grup = snapshot.child(Constants.FIREBASE_BORDRO_GRUP).getValue().toString();
                String saatUcreti = snapshot.child(Constants.FIREBASE_BORDRO_SAAT_UCRETI).getValue().toString();
                String sigortaGunu = snapshot.child(Constants.FIREBASE_BORDRO_SIGORTA_GUNU).getValue().toString();

                txtBordroIsYeri.setText(isYeri);
                txtBordroSskNo.setText(sskNo);
                txtBordroSskTescil.setText(sskTescil);
                txtBordroUnvani.setText(unvani);
                txtBordroGrup.setText(grup);
                txtBordroSaatUcreti.setText(saatUcreti);
                txtBordroSigortaGunu.setText(sigortaGunu);

                txtBordroUcretSSkNoId.setText(snapshot.child(Constants.FIREBASE_BORDRO_SSK_NO_ID).getValue().toString());
                txtBordroUcretNetUcret.setText(snapshot.child(Constants.FIREBASE_BORDRO_NET_UCRET).getValue().toString());
                txtBordroUcretGunVeSaatToplamUcret.setText(snapshot.child(Constants.FIREBASE_BORDRO_GUN_VE_SAAT_TOPLAM_UCRET).getValue().toString());
                txtBordroUcretUcretToplam.setText(snapshot.child(Constants.FIREBASE_BORDRO_UCRET_TOPLAM).getValue().toString());
                txtBordroUcretAylikSigortaMatrahi.setText(snapshot.child(Constants.FIREBASE_BORDRO_AYLIK_SIGORTA_MATRAHI).getValue().toString());
                txtBordroUcretSgkTavaniniAsanMiktar.setText(snapshot.child(Constants.FIREBASE_BORDRO_SGK_TAVANINI_ASAN_MIKTAR).getValue().toString());
                txtBordroUcretAylikVergiMatrahi.setText(snapshot.child(Constants.FIREBASE_BORDRO_AYLIK_VERGI_MATRAHI).getValue().toString());
                txtBordroUcretYillikTgvm.setText(snapshot.child(Constants.FIREBASE_BORDRO_YILLIK_TGVM).getValue().toString());
                txtBordroUcretHayatSigortasi.setText(snapshot.child(Constants.FIREBASE_BORDRO_HAYAT_SIGORTASI).getValue().toString());
                txtBordroUcretAskerlikBorclari.setText(snapshot.child(Constants.FIREBASE_BORDRO_ASKERLIK_BORCLARI).getValue().toString());
                txtBordroUcretEngelliIndirimi.setText(snapshot.child(Constants.FIREBASE_BORDRO_ENGELLI_INDIRIMI).getValue().toString());
                txtBordroUcretIsverenSsk.setText(snapshot.child(Constants.FIREBASE_BORDRO_ISVEREN_SSK).getValue().toString());
                txtBordroUcretIsverenIssizlik.setText(snapshot.child(Constants.FIREBASE_BORDRO_ISVEREN_ISSIZLIK).getValue().toString());
                txtBordroUcretUcretMaliyeti.setText(snapshot.child(Constants.FIREBASE_BORDRO_UCRET_MALIYETI).getValue().toString());
                txtBordroUcretYasalKesintiToplami.setText(snapshot.child(Constants.FIREBASE_BORDRO_YASAL_KESINTI_TOPLAMI).getValue().toString());
                txtBordroUcretOzelKesintiToplami.setText(snapshot.child(Constants.FIREBASE_BORDRO_OZEL_KESINTI_TOPLAMI).getValue().toString());
                txtBordroUcretKesintilerToplami.setText(snapshot.child(Constants.FIREBASE_BORDRO_KESINTILER_TOPLAMI).getValue().toString());


                txtBordroKazanclarSskNoId.setText(snapshot.child(Constants.FIREBASE_BORDRO_SSK_NO_ID).getValue().toString());
                txtBordroKazanclarNormalMesaiGun.setText(snapshot.child(Constants.FIREBASE_BORDRO_NORMAL_MESAI_GUN).getValue().toString());
                txtBordroKazanclarNormalMesaiSaat.setText(snapshot.child(Constants.FIREBASE_BORDRO_NORMAL_MESAI_SAAT).getValue().toString());
                txtBordroKazanclarNormalMesaiTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_NORMAL_MESAI_TUTAR).getValue().toString());
                txtBordroKazanclarVardiyaZammiSaat.setText(snapshot.child(Constants.FIREBASE_BORDRO_VARDIYA_ZAMMI_SAAT).getValue().toString());
                txtBordroKazanclarVardiyaZammiTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_VARDIYA_ZAMMI_TUTAR).getValue().toString());
                txtBordroKazanclarHaftaTatiliGun.setText(snapshot.child(Constants.FIREBASE_BORDRO_HAFTA_TATILI_GUN).getValue().toString());
                txtBordroKazanclarHaftaTatiliSaat.setText(snapshot.child(Constants.FIREBASE_BORDRO_HAFTA_TATILI_SAAT).getValue().toString());
                txtBordroKazanclarHaftaTatiliTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_HAFTA_TATILI_TUTAR).getValue().toString());
                txtBordroKazanclarFazlaMesaiSaatYuzdeYuz.setText(snapshot.child(Constants.FIREBASE_BORDRO_FAZLA_MESAI_SAAT_YUZDE_YUZ).getValue().toString());
                txtBordroKazanclarFazlaMesaiTutarYuzdeYuz.setText(snapshot.child(Constants.FIREBASE_BORDRO_FAZLA_MESAI_TUTAR_YUZDE_YUZ).getValue().toString());
                txtBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz.setText(snapshot.child(Constants.FIREBASE_BORDRO_FAZLA_MESAI_SAAT_YUZDE_IKI_YUZ).getValue().toString());
                txtBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz.setText(snapshot.child(Constants.FIREBASE_BORDRO_FAZLA_MESAI_TUTAR_YUZDE_IKI_YUZ).getValue().toString());
                txtBordroKazanclarFazlaMesaiSaatYuzdeUcYuz.setText(snapshot.child(Constants.FIREBASE_BORDRO_FAZLA_MESAI_SAAT_YUZDE_UC_YUZ).getValue().toString());
                txtBordroKazanclarFazlaMesaiTutarYuzdeUcYuz.setText(snapshot.child(Constants.FIREBASE_BORDRO_FAZLA_MESAI_TUTAR_YUZDE_UC_YUZ).getValue().toString());
                txtBordroKazanclarChttAltiGunSaat.setText(snapshot.child(Constants.FIREBASE_BORDRO_CHTT_ALTI_GUN_SAAT).getValue().toString());
                txtBordroKazanclarChttAltiGunTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_CHTT_ALTI_GUN_TUTAR).getValue().toString());
                txtBordroKazanclarDinlenmeliCalismaSaati.setText(snapshot.child(Constants.FIREBASE_BORDRO_DINLENMELI_CALISMA_SAATI).getValue().toString());
                txtBordroKazanclarDinlenmeliCalismaTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_DINLENMELI_CALISMA_TUTAR).getValue().toString());
                txtBordroKazanclarMukabilCalismaSaati.setText(snapshot.child(Constants.FIREBASE_BORDRO_MUKABIL_CALISMA_SAATI).getValue().toString());
                txtBordroKazanclarMukabilCalismaTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_MUKABIL_CALISMA_TUTAR).getValue().toString());
                txtBordroKazanclarYillikUcretliIzinGun.setText(snapshot.child(Constants.FIREBASE_BORDRO_YILLIK_UCRETLI_IZIN_GUN).getValue().toString());
                txtBordroKazanclarYillikUcretliIzinSaat.setText(snapshot.child(Constants.FIREBASE_BORDRO_YILLIK_UCRETLI_IZIN_SAAT).getValue().toString());
                txtBordroKazanclarYillikUcretliIzinTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_YILLIK_UCRETLI_IZIN_TUTAR).getValue().toString());
                txtBordroKazanclarHastalıkVeIstIzniGun.setText(snapshot.child(Constants.FIREBASE_BORDRO_HASTALIK_VE_IST_IZNI_GUN).getValue().toString());
                txtBordroKazanclarHastalikVeIstGunSaat.setText(snapshot.child(Constants.FIREBASE_BORDRO_HASTALIK_VE_IST_IZNI_SAAT).getValue().toString());
                txtBordroKazanclarHastalikVeIstIzniTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_HASTALIK_VE_IST_IZNI_TUTAR).getValue().toString());
                txtBordroKazanclarSosyalIzinGun.setText(snapshot.child(Constants.FIREBASE_BORDRO_SOSYAL_IZIN_GUN).getValue().toString());
                txtBordroKazanclarSosyalIzinSaat.setText(snapshot.child(Constants.FIREBASE_BORDRO_SOSYAL_IZIN_SAAT).getValue().toString());
                txtBordroKazanclarSosyalIzinTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_SOSYAL_IZIN_TUTAR).getValue().toString());
                txtBordroKazanclarUcretsizIzinGun.setText(snapshot.child(Constants.FIREBASE_BORDRO_UCRETSIZ_IZIN_GUN).getValue().toString());
                txtBordroKazanclarUcretsizIzinSaat.setText(snapshot.child(Constants.FIREBASE_BORDRO_UCRETSIZ_IZIN_SAAT).getValue().toString());
                txtBordroKazanclarUcretsizIzinTutar.setText(snapshot.child(Constants.FIREBASE_BORDRO_UCRETSIZ_IZIN_TUTAR).getValue().toString());
                txtBordroKazanclarYoneticiTazminati.setText(snapshot.child(Constants.FIREBASE_BORDRO_YONETICI_TAZMINATI).getValue().toString());
                txtBordroKazanclarCezaIade.setText(snapshot.child(Constants.FIREBASE_BORDRO_CEZA_IADE).getValue().toString());
                txtBordroKazanclarHacizIade.setText(snapshot.child(Constants.FIREBASE_BORDRO_HACIZ_IADE).getValue().toString());
                txtBordroKazanclarHasarIade.setText(snapshot.child(Constants.FIREBASE_BORDRO_HASAR_IADE).getValue().toString());
                txtBordroKazanclarMuhtelifArti.setText(snapshot.child(Constants.FIREBASE_BORDRO_MUHTELIF_ARTI).getValue().toString());
                txtBordroKazanclarMuhtelifEksi.setText(snapshot.child(Constants.FIREBASE_BORDRO_MUHTELIF_EKSI).getValue().toString());
                txtBordroKazanclarYemek.setText(snapshot.child(Constants.FIREBASE_BORDRO_YEMEK).getValue().toString());
                txtBordroKazanclarNetIlave.setText(snapshot.child(Constants.FIREBASE_BORDRO_NET_ILAVE).getValue().toString());
                txtBordroKazanclarNetIlave2.setText(snapshot.child(Constants.FIREBASE_BORDRO_NET_ILAVE2).getValue().toString());
                txtBordroKazanclarSosyalPaket.setText(snapshot.child(Constants.FIREBASE_BORDRO_SOSYAL_PAKET).getValue().toString());
                txtBordroKazanclarAgi.setText(snapshot.child(Constants.FIREBASE_BORDRO_AGI).getValue().toString());


                txtBordroYasalKesintilerSskNoId.setText(snapshot.child(Constants.FIREBASE_BORDRO_SSK_NO_ID).getValue().toString());
                txtBordroYasalKesintilerSigorta.setText(snapshot.child(Constants.FIREBASE_BORDRO_SIGORTA).getValue().toString());
                txtBordroYasalKesintilerIssizlikSigortasi.setText(snapshot.child(Constants.FIREBASE_BORDRO_ISSIZLIK_SIGORTASI).getValue().toString());
                txtBordroYasalKesintilerGelirVergisi.setText(snapshot.child(Constants.FIREBASE_BORDRO_GELIR_VERGISI).getValue().toString());
                txtBordroYasalKesintilerDamgaPulu.setText(snapshot.child(Constants.FIREBASE_BORDRO_DAMGA_PULU).getValue().toString());
                txtBordroYasalKesintilerSendikaKesintisi.setText(snapshot.child(Constants.FIREBASE_BORDRO_SENDIKA_KESINTISI).getValue().toString());


                txtBordroOzelKesintilerSskNoId.setText(snapshot.child(Constants.FIREBASE_BORDRO_SSK_NO_ID).getValue().toString());
                txtBordroOzelKesintilerAvans.setText(snapshot.child(Constants.FIREBASE_BORDRO_AVANS).getValue().toString());
                txtBordroOzelKesintilerHaciz.setText(snapshot.child(Constants.FIREBASE_BORDRO_HACIZ).getValue().toString());
                txtBordroOzelKesintilerNafaka.setText(snapshot.child(Constants.FIREBASE_BORDRO_NAFAKA).getValue().toString());
                txtBordroOzelKesintilerHasar.setText(snapshot.child(Constants.FIREBASE_BORDRO_HASAR).getValue().toString());
                txtBordroOzelKesintilerCeza.setText(snapshot.child(Constants.FIREBASE_BORDRO_CEZA).getValue().toString());
                txtBordroOzelKesintilerTrafikBorcu.setText(snapshot.child(Constants.FIREBASE_BORDRO_TRAFIK_BORCU).getValue().toString());
                txtBordroOzelKesintilerMuhtelifBorc.setText(snapshot.child(Constants.FIREBASE_BORDRO_MUHTELIF_BORC).getValue().toString());
                txtBordroOzelKesintilerKefalet.setText(snapshot.child(Constants.FIREBASE_BORDRO_KEFALET).getValue().toString());
                txtBordroOzelKesintilerTelefon.setText(snapshot.child(Constants.FIREBASE_BORDRO_TELEFON).getValue().toString());
                txtBordroOzelKesintilerKres.setText(snapshot.child(Constants.FIREBASE_BORDRO_KRES).getValue().toString());
                txtBordroOzelKesintilerBerber.setText(snapshot.child(Constants.FIREBASE_BORDRO_BERBER).getValue().toString());
                txtBordroOzelKesintilerVergiSskBorcu.setText(snapshot.child(Constants.FIREBASE_BORDRO_VERGI_SSK_BORCU).getValue().toString());


                bilgileriGoster();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bilgileriGoster() {
        btnBordroAd.setVisibility(View.VISIBLE);
        btnBordroSoyad.setVisibility(View.VISIBLE);
        btnBordroSicil.setVisibility(View.VISIBLE);
        btnBordroIsYeri.setVisibility(View.VISIBLE);
        btnBordroSskNo.setVisibility(View.VISIBLE);
        btnBordroSskTescil.setVisibility(View.VISIBLE);
        btnBordroUnvani.setVisibility(View.VISIBLE);
        btnBordroGrup.setVisibility(View.VISIBLE);
        btnBordroSaatUcreti.setVisibility(View.VISIBLE);
        btnBordroSigortaGunu.setVisibility(View.VISIBLE);
        txtBordroKisiselBilgiler.setVisibility(View.VISIBLE);
        txtBordroAd.setVisibility(View.VISIBLE);
        txtBordroSoyad.setVisibility(View.VISIBLE);
        txtBordroSicil.setVisibility(View.VISIBLE);
        txtBordroIsYeri.setVisibility(View.VISIBLE);
        txtBordroSskNo.setVisibility(View.VISIBLE);
        txtBordroSskTescil.setVisibility(View.VISIBLE);
        txtBordroUnvani.setVisibility(View.VISIBLE);
        txtBordroGrup.setVisibility(View.VISIBLE);
        txtBordroSaatUcreti.setVisibility(View.VISIBLE);
        txtBordroSigortaGunu.setVisibility(View.VISIBLE);
        txtBordroUcret.setVisibility(View.VISIBLE);

        txtBordroUcret.setVisibility(View.VISIBLE);
        btnBordroUcretSSkNoId.setVisibility(View.VISIBLE);
        btnBordroUcretNetUcret.setVisibility(View.VISIBLE);
        btnBordroUcretGunVeSaatToplamUcret.setVisibility(View.VISIBLE);
        btnBordroUcretUcretToplam.setVisibility(View.VISIBLE);
        btnBordroUcretAylikSigortaMatrahi.setVisibility(View.VISIBLE);
        btnBordroUcretSgkTavaniniAsanMiktar.setVisibility(View.VISIBLE);
        btnBordroUcretAylikVergiMatrahi.setVisibility(View.VISIBLE);

        btnBordroUcretYillikTgvm.setVisibility(View.VISIBLE);
        btnBordroUcretHayatSigortasi.setVisibility(View.VISIBLE);
        btnBordroUcretAskerlikBorclari.setVisibility(View.VISIBLE);
        btnBordroUcretEngelliIndirimi.setVisibility(View.VISIBLE);
        btnBordroUcretIsverenSsk.setVisibility(View.VISIBLE);
        btnBordroUcretIsverenIssizlik.setVisibility(View.VISIBLE);
        btnBordroUcretUcretMaliyeti.setVisibility(View.VISIBLE);
        btnBordroUcretYasalKesintiToplami.setVisibility(View.VISIBLE);
        btnBordroUcretOzelKesintiToplami.setVisibility(View.VISIBLE);
        btnBordroUcretKesintilerToplami.setVisibility(View.VISIBLE);
        txtBordroUcretSSkNoId.setVisibility(View.VISIBLE);
        txtBordroUcretNetUcret.setVisibility(View.VISIBLE);
        txtBordroUcretGunVeSaatToplamUcret.setVisibility(View.VISIBLE);
        txtBordroUcretUcretToplam.setVisibility(View.VISIBLE);
        txtBordroUcretAylikSigortaMatrahi.setVisibility(View.VISIBLE);
        txtBordroUcretAylikVergiMatrahi.setVisibility(View.VISIBLE);
        txtBordroUcretSgkTavaniniAsanMiktar.setVisibility(View.VISIBLE);
        txtBordroUcretYillikTgvm.setVisibility(View.VISIBLE);
        txtBordroUcretHayatSigortasi.setVisibility(View.VISIBLE);
        txtBordroUcretAskerlikBorclari.setVisibility(View.VISIBLE);
        txtBordroUcretEngelliIndirimi.setVisibility(View.VISIBLE);
        txtBordroUcretIsverenSsk.setVisibility(View.VISIBLE);
        txtBordroUcretIsverenIssizlik.setVisibility(View.VISIBLE);
        txtBordroUcretUcretMaliyeti.setVisibility(View.VISIBLE);
        txtBordroUcretYasalKesintiToplami.setVisibility(View.VISIBLE);
        txtBordroUcretOzelKesintiToplami.setVisibility(View.VISIBLE);
        txtBordroUcretKesintilerToplami.setVisibility(View.VISIBLE);

        btnBordroKazanclarSskNoId.setVisibility(View.VISIBLE);
        btnBordroKazanclarNormalMesaiGun.setVisibility(View.VISIBLE);
        btnBordroKazanclarNormalMesaiSaat.setVisibility(View.VISIBLE);
        btnBordroKazanclarNormalMesaiTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarVardiyaZammiSaat.setVisibility(View.VISIBLE);
        btnBordroKazanclarVardiyaZammiTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarHaftaTatiliSaat.setVisibility(View.VISIBLE);
        btnBordroKazanclarHaftaTatiliGun.setVisibility(View.VISIBLE);
        btnBordroKazanclarHaftaTatiliTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarFazlaMesaiSaatYuzdeYuz.setVisibility(View.VISIBLE);
        btnBordroKazanclarFazlaMesaiTutarYuzdeYuz.setVisibility(View.VISIBLE);
        btnBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz.setVisibility(View.VISIBLE);
        btnBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz.setVisibility(View.VISIBLE);
        btnBordroKazanclarFazlaMesaiSaatYuzdeUcYuz.setVisibility(View.VISIBLE);
        btnBordroKazanclarFazlaMesaiTutarYuzdeUcYuz.setVisibility(View.VISIBLE);
        btnBordroKazanclarDinlenmeliCalismaSaati.setVisibility(View.VISIBLE);
        btnBordroKazanclarDinlenmeliCalismaTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarMukabilCalismaSaati.setVisibility(View.VISIBLE);
        btnBordroKazanclarMukabilCalismaTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarYillikUcretliIzinGun.setVisibility(View.VISIBLE);
        btnBordroKazanclarYillikUcretliIzinSaat.setVisibility(View.VISIBLE);
        btnBordroKazanclarYillikUcretliIzinTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarHastalıkVeIstIzniGun.setVisibility(View.VISIBLE);
        btnBordroKazanclarHastalikVeIstGunSaat.setVisibility(View.VISIBLE);
        btnBordroKazanclarHastalikVeIstIzniTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarSosyalIzinGun.setVisibility(View.VISIBLE);
        btnBordroKazanclarSosyalIzinSaat.setVisibility(View.VISIBLE);
        btnBordroKazanclarSosyalIzinTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarUcretsizIzinGun.setVisibility(View.VISIBLE);
        btnBordroKazanclarUcretsizIzinSaat.setVisibility(View.VISIBLE);
        btnBordroKazanclarUcretsizIzinTutar.setVisibility(View.VISIBLE);
        btnBordroKazanclarYoneticiTazminati.setVisibility(View.VISIBLE);
        btnBordroKazanclarCezaIade.setVisibility(View.VISIBLE);
        btnBordroKazanclarHacizIade.setVisibility(View.VISIBLE);
        btnBordroKazanclarHasarIade.setVisibility(View.VISIBLE);
        btnBordroKazanclarMuhtelifArti.setVisibility(View.VISIBLE);
        btnBordroKazanclarMuhtelifEksi.setVisibility(View.VISIBLE);
        btnBordroKazanclarYemek.setVisibility(View.VISIBLE);
        btnBordroKazanclarNetIlave.setVisibility(View.VISIBLE);
        btnBordroKazanclarNetIlave2.setVisibility(View.VISIBLE);
        btnBordroKazanclarSosyalPaket.setVisibility(View.VISIBLE);
        btnBordroKazanclarAgi.setVisibility(View.VISIBLE);
        btnBordroKazanclarChttAltiGunSaat.setVisibility(View.VISIBLE);
        btnBordroKazanclarChttAltiGunTutar.setVisibility(View.VISIBLE);

        txtBordroKazanclarSskNoId.setVisibility(View.VISIBLE);
        txtBordroKazanclarNormalMesaiGun.setVisibility(View.VISIBLE);
        txtBordroKazanclarNormalMesaiSaat.setVisibility(View.VISIBLE);
        txtBordroKazanclarNormalMesaiTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarVardiyaZammiSaat.setVisibility(View.VISIBLE);
        txtBordroKazanclarVardiyaZammiTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarHaftaTatiliGun.setVisibility(View.VISIBLE);
        txtBordroKazanclarHaftaTatiliSaat.setVisibility(View.VISIBLE);
        txtBordroKazanclarHaftaTatiliTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarFazlaMesaiSaatYuzdeYuz.setVisibility(View.VISIBLE);
        txtBordroKazanclarFazlaMesaiTutarYuzdeYuz.setVisibility(View.VISIBLE);
        txtBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz.setVisibility(View.VISIBLE);
        txtBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz.setVisibility(View.VISIBLE);
        txtBordroKazanclarFazlaMesaiSaatYuzdeUcYuz.setVisibility(View.VISIBLE);
        txtBordroKazanclarFazlaMesaiTutarYuzdeUcYuz.setVisibility(View.VISIBLE);
        txtBordroKazanclarChttAltiGunSaat.setVisibility(View.VISIBLE);
        txtBordroKazanclarChttAltiGunTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarDinlenmeliCalismaSaati.setVisibility(View.VISIBLE);
        txtBordroKazanclarDinlenmeliCalismaTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarMukabilCalismaSaati.setVisibility(View.VISIBLE);
        txtBordroKazanclarMukabilCalismaTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarYillikUcretliIzinGun.setVisibility(View.VISIBLE);
        txtBordroKazanclarYillikUcretliIzinSaat.setVisibility(View.VISIBLE);
        txtBordroKazanclarYillikUcretliIzinTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarHastalıkVeIstIzniGun.setVisibility(View.VISIBLE);
        txtBordroKazanclarHastalikVeIstGunSaat.setVisibility(View.VISIBLE);
        txtBordroKazanclarHastalikVeIstIzniTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarSosyalIzinGun.setVisibility(View.VISIBLE);
        txtBordroKazanclarSosyalIzinSaat.setVisibility(View.VISIBLE);
        txtBordroKazanclarSosyalIzinTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarUcretsizIzinGun.setVisibility(View.VISIBLE);
        txtBordroKazanclarUcretsizIzinSaat.setVisibility(View.VISIBLE);
        txtBordroKazanclarUcretsizIzinTutar.setVisibility(View.VISIBLE);
        txtBordroKazanclarYoneticiTazminati.setVisibility(View.VISIBLE);
        txtBordroKazanclarCezaIade.setVisibility(View.VISIBLE);
        txtBordroKazanclarHacizIade.setVisibility(View.VISIBLE);
        txtBordroKazanclarHasarIade.setVisibility(View.VISIBLE);
        txtBordroKazanclarMuhtelifArti.setVisibility(View.VISIBLE);
        txtBordroKazanclarMuhtelifEksi.setVisibility(View.VISIBLE);
        txtBordroKazanclarYemek.setVisibility(View.VISIBLE);
        txtBordroKazanclarNetIlave.setVisibility(View.VISIBLE);
        txtBordroKazanclarNetIlave2.setVisibility(View.VISIBLE);
        txtBordroKazanclarSosyalPaket.setVisibility(View.VISIBLE);
        txtBordroKazanclarAgi.setVisibility(View.VISIBLE);


        btnBordroYasalKesintilerSskNoId.setVisibility(View.VISIBLE);
        btnBordroYasalKesintilerSigorta.setVisibility(View.VISIBLE);
        btnBordroYasalKesintilerIssizlikSigortasi.setVisibility(View.VISIBLE);
        btnBordroYasalKesintilerGelirVergisi.setVisibility(View.VISIBLE);
        btnBordroYasalKesintilerDamgaPulu.setVisibility(View.VISIBLE);
        btnBordroYasalKesintilerSendikaKesintisi.setVisibility(View.VISIBLE);
        txtBordroYasalKesintilerSskNoId.setVisibility(View.VISIBLE);
        txtBordroYasalKesintilerSigorta.setVisibility(View.VISIBLE);
        txtBordroYasalKesintilerIssizlikSigortasi.setVisibility(View.VISIBLE);
        txtBordroYasalKesintilerGelirVergisi.setVisibility(View.VISIBLE);
        txtBordroYasalKesintilerDamgaPulu.setVisibility(View.VISIBLE);
        txtBordroYasalKesintilerSendikaKesintisi.setVisibility(View.VISIBLE);
        txtBordroYasalKesintiler.setVisibility(View.VISIBLE);


        btnBordroOzelKesintilerSskNoId.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerAvans.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerHaciz.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerNafaka.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerHasar.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerCeza.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerTrafikBorcu.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerMuhtelifBorc.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerKefalet.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerTelefon.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerKres.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerBerber.setVisibility(View.VISIBLE);
        btnBordroOzelKesintilerVergiSskBorcu.setVisibility(View.VISIBLE);

        txtBordroOzelKesintilerSskNoId.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerAvans.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerHaciz.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerNafaka.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerHasar.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerCeza.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerTrafikBorcu.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerMuhtelifBorc.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerKefalet.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerTelefon.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerKres.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerBerber.setVisibility(View.VISIBLE);
        txtBordroOzelKesintilerVergiSskBorcu.setVisibility(View.VISIBLE);
        txtBordroOzelKesintiler.setVisibility(View.VISIBLE);

    }

    private void bilgileriGizle() {
        btnBordroAd.setVisibility(View.INVISIBLE);
        btnBordroSoyad.setVisibility(View.INVISIBLE);
        btnBordroSicil.setVisibility(View.INVISIBLE);
        btnBordroIsYeri.setVisibility(View.INVISIBLE);
        btnBordroSskNo.setVisibility(View.INVISIBLE);
        btnBordroSskTescil.setVisibility(View.INVISIBLE);
        btnBordroUnvani.setVisibility(View.INVISIBLE);
        btnBordroGrup.setVisibility(View.INVISIBLE);
        btnBordroSaatUcreti.setVisibility(View.INVISIBLE);
        btnBordroSigortaGunu.setVisibility(View.INVISIBLE);
        txtBordroKisiselBilgiler.setVisibility(View.INVISIBLE);
        txtBordroAd.setVisibility(View.INVISIBLE);
        txtBordroSoyad.setVisibility(View.INVISIBLE);
        txtBordroSicil.setVisibility(View.INVISIBLE);
        txtBordroIsYeri.setVisibility(View.INVISIBLE);
        txtBordroSskNo.setVisibility(View.INVISIBLE);
        txtBordroSskTescil.setVisibility(View.INVISIBLE);
        txtBordroUnvani.setVisibility(View.INVISIBLE);
        txtBordroGrup.setVisibility(View.INVISIBLE);
        txtBordroSaatUcreti.setVisibility(View.INVISIBLE);
        txtBordroSigortaGunu.setVisibility(View.INVISIBLE);
        txtBordroUcret.setVisibility(View.INVISIBLE);

        txtBordroUcret.setVisibility(View.INVISIBLE);
        btnBordroUcretSSkNoId.setVisibility(View.INVISIBLE);
        btnBordroUcretNetUcret.setVisibility(View.INVISIBLE);
        btnBordroUcretGunVeSaatToplamUcret.setVisibility(View.INVISIBLE);
        btnBordroUcretUcretToplam.setVisibility(View.INVISIBLE);
        btnBordroUcretAylikSigortaMatrahi.setVisibility(View.INVISIBLE);
        btnBordroUcretSgkTavaniniAsanMiktar.setVisibility(View.INVISIBLE);
        btnBordroUcretAylikVergiMatrahi.setVisibility(View.INVISIBLE);
        btnBordroUcretYillikTgvm.setVisibility(View.INVISIBLE);
        btnBordroUcretHayatSigortasi.setVisibility(View.INVISIBLE);
        btnBordroUcretAskerlikBorclari.setVisibility(View.INVISIBLE);
        btnBordroUcretEngelliIndirimi.setVisibility(View.INVISIBLE);
        btnBordroUcretIsverenSsk.setVisibility(View.INVISIBLE);
        btnBordroUcretIsverenIssizlik.setVisibility(View.INVISIBLE);
        btnBordroUcretUcretMaliyeti.setVisibility(View.INVISIBLE);
        btnBordroUcretYasalKesintiToplami.setVisibility(View.INVISIBLE);
        btnBordroUcretOzelKesintiToplami.setVisibility(View.INVISIBLE);
        btnBordroUcretKesintilerToplami.setVisibility(View.INVISIBLE);
        txtBordroUcretSSkNoId.setVisibility(View.INVISIBLE);
        txtBordroUcretNetUcret.setVisibility(View.INVISIBLE);
        txtBordroUcretGunVeSaatToplamUcret.setVisibility(View.INVISIBLE);
        txtBordroUcretUcretToplam.setVisibility(View.INVISIBLE);
        txtBordroUcretAylikSigortaMatrahi.setVisibility(View.INVISIBLE);
        txtBordroUcretSgkTavaniniAsanMiktar.setVisibility(View.INVISIBLE);
        txtBordroUcretAylikVergiMatrahi.setVisibility(View.INVISIBLE);
        txtBordroUcretYillikTgvm.setVisibility(View.INVISIBLE);
        txtBordroUcretHayatSigortasi.setVisibility(View.INVISIBLE);
        txtBordroUcretAskerlikBorclari.setVisibility(View.INVISIBLE);
        txtBordroUcretEngelliIndirimi.setVisibility(View.INVISIBLE);
        txtBordroUcretIsverenSsk.setVisibility(View.INVISIBLE);
        txtBordroUcretIsverenIssizlik.setVisibility(View.INVISIBLE);
        txtBordroUcretUcretMaliyeti.setVisibility(View.INVISIBLE);
        txtBordroUcretYasalKesintiToplami.setVisibility(View.INVISIBLE);
        txtBordroUcretOzelKesintiToplami.setVisibility(View.INVISIBLE);
        txtBordroUcretKesintilerToplami.setVisibility(View.INVISIBLE);


        btnBordroKazanclarSskNoId.setVisibility(View.INVISIBLE);
        btnBordroKazanclarNormalMesaiGun.setVisibility(View.INVISIBLE);
        btnBordroKazanclarNormalMesaiSaat.setVisibility(View.INVISIBLE);
        btnBordroKazanclarNormalMesaiTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarVardiyaZammiSaat.setVisibility(View.INVISIBLE);
        btnBordroKazanclarVardiyaZammiTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarHaftaTatiliSaat.setVisibility(View.INVISIBLE);
        btnBordroKazanclarHaftaTatiliGun.setVisibility(View.INVISIBLE);
        btnBordroKazanclarHaftaTatiliTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarFazlaMesaiSaatYuzdeYuz.setVisibility(View.INVISIBLE);
        btnBordroKazanclarFazlaMesaiTutarYuzdeYuz.setVisibility(View.INVISIBLE);
        btnBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz.setVisibility(View.INVISIBLE);
        btnBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz.setVisibility(View.INVISIBLE);
        btnBordroKazanclarFazlaMesaiSaatYuzdeUcYuz.setVisibility(View.INVISIBLE);
        btnBordroKazanclarFazlaMesaiTutarYuzdeUcYuz.setVisibility(View.INVISIBLE);
        btnBordroKazanclarDinlenmeliCalismaSaati.setVisibility(View.INVISIBLE);
        btnBordroKazanclarDinlenmeliCalismaTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarMukabilCalismaSaati.setVisibility(View.INVISIBLE);
        btnBordroKazanclarMukabilCalismaTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarYillikUcretliIzinGun.setVisibility(View.INVISIBLE);
        btnBordroKazanclarYillikUcretliIzinSaat.setVisibility(View.INVISIBLE);
        btnBordroKazanclarYillikUcretliIzinTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarHastalıkVeIstIzniGun.setVisibility(View.INVISIBLE);
        btnBordroKazanclarHastalikVeIstGunSaat.setVisibility(View.INVISIBLE);
        btnBordroKazanclarHastalikVeIstIzniTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarSosyalIzinGun.setVisibility(View.INVISIBLE);
        btnBordroKazanclarSosyalIzinSaat.setVisibility(View.INVISIBLE);
        btnBordroKazanclarSosyalIzinTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarUcretsizIzinGun.setVisibility(View.INVISIBLE);
        btnBordroKazanclarUcretsizIzinSaat.setVisibility(View.INVISIBLE);
        btnBordroKazanclarUcretsizIzinTutar.setVisibility(View.INVISIBLE);
        btnBordroKazanclarYoneticiTazminati.setVisibility(View.INVISIBLE);
        btnBordroKazanclarCezaIade.setVisibility(View.INVISIBLE);
        btnBordroKazanclarHacizIade.setVisibility(View.INVISIBLE);
        btnBordroKazanclarHasarIade.setVisibility(View.INVISIBLE);
        btnBordroKazanclarMuhtelifArti.setVisibility(View.INVISIBLE);
        btnBordroKazanclarMuhtelifEksi.setVisibility(View.INVISIBLE);
        btnBordroKazanclarYemek.setVisibility(View.INVISIBLE);
        btnBordroKazanclarNetIlave.setVisibility(View.INVISIBLE);
        btnBordroKazanclarNetIlave2.setVisibility(View.INVISIBLE);
        btnBordroKazanclarSosyalPaket.setVisibility(View.INVISIBLE);
        btnBordroKazanclarAgi.setVisibility(View.INVISIBLE);
        btnBordroKazanclarChttAltiGunSaat.setVisibility(View.INVISIBLE);
        btnBordroKazanclarChttAltiGunTutar.setVisibility(View.INVISIBLE);

        txtBordroKazanclarSskNoId.setVisibility(View.INVISIBLE);
        txtBordroKazanclarNormalMesaiGun.setVisibility(View.INVISIBLE);
        txtBordroKazanclarNormalMesaiSaat.setVisibility(View.INVISIBLE);
        txtBordroKazanclarNormalMesaiTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarVardiyaZammiSaat.setVisibility(View.INVISIBLE);
        txtBordroKazanclarVardiyaZammiTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarHaftaTatiliGun.setVisibility(View.INVISIBLE);
        txtBordroKazanclarHaftaTatiliSaat.setVisibility(View.INVISIBLE);
        txtBordroKazanclarHaftaTatiliTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarFazlaMesaiSaatYuzdeYuz.setVisibility(View.INVISIBLE);
        txtBordroKazanclarFazlaMesaiTutarYuzdeYuz.setVisibility(View.INVISIBLE);
        txtBordroKazanclarFazlaMesaiSaatYuzdeIkiYuz.setVisibility(View.INVISIBLE);
        txtBordroKazanclarFazlaMesaiTutarYuzdeIkiYuz.setVisibility(View.INVISIBLE);
        txtBordroKazanclarFazlaMesaiSaatYuzdeUcYuz.setVisibility(View.INVISIBLE);
        txtBordroKazanclarFazlaMesaiTutarYuzdeUcYuz.setVisibility(View.INVISIBLE);
        txtBordroKazanclarChttAltiGunSaat.setVisibility(View.INVISIBLE);
        txtBordroKazanclarChttAltiGunTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarDinlenmeliCalismaSaati.setVisibility(View.INVISIBLE);
        txtBordroKazanclarDinlenmeliCalismaTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarMukabilCalismaSaati.setVisibility(View.INVISIBLE);
        txtBordroKazanclarMukabilCalismaTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarYillikUcretliIzinGun.setVisibility(View.INVISIBLE);
        txtBordroKazanclarYillikUcretliIzinSaat.setVisibility(View.INVISIBLE);
        txtBordroKazanclarYillikUcretliIzinTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarHastalıkVeIstIzniGun.setVisibility(View.INVISIBLE);
        txtBordroKazanclarHastalikVeIstGunSaat.setVisibility(View.INVISIBLE);
        txtBordroKazanclarHastalikVeIstIzniTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarSosyalIzinGun.setVisibility(View.INVISIBLE);
        txtBordroKazanclarSosyalIzinSaat.setVisibility(View.INVISIBLE);
        txtBordroKazanclarSosyalIzinTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarUcretsizIzinGun.setVisibility(View.INVISIBLE);
        txtBordroKazanclarUcretsizIzinSaat.setVisibility(View.INVISIBLE);
        txtBordroKazanclarUcretsizIzinTutar.setVisibility(View.INVISIBLE);
        txtBordroKazanclarYoneticiTazminati.setVisibility(View.INVISIBLE);
        txtBordroKazanclarCezaIade.setVisibility(View.INVISIBLE);
        txtBordroKazanclarHacizIade.setVisibility(View.INVISIBLE);
        txtBordroKazanclarHasarIade.setVisibility(View.INVISIBLE);
        txtBordroKazanclarMuhtelifArti.setVisibility(View.INVISIBLE);
        txtBordroKazanclarMuhtelifEksi.setVisibility(View.INVISIBLE);
        txtBordroKazanclarYemek.setVisibility(View.INVISIBLE);
        txtBordroKazanclarNetIlave.setVisibility(View.INVISIBLE);
        txtBordroKazanclarNetIlave2.setVisibility(View.INVISIBLE);
        txtBordroKazanclarSosyalPaket.setVisibility(View.INVISIBLE);
        txtBordroKazanclarAgi.setVisibility(View.INVISIBLE);

        btnBordroYasalKesintilerSskNoId.setVisibility(View.INVISIBLE);
        btnBordroYasalKesintilerSigorta.setVisibility(View.INVISIBLE);
        btnBordroYasalKesintilerIssizlikSigortasi.setVisibility(View.INVISIBLE);
        btnBordroYasalKesintilerGelirVergisi.setVisibility(View.INVISIBLE);
        btnBordroYasalKesintilerDamgaPulu.setVisibility(View.INVISIBLE);
        btnBordroYasalKesintilerSendikaKesintisi.setVisibility(View.INVISIBLE);
        txtBordroYasalKesintilerSskNoId.setVisibility(View.INVISIBLE);
        txtBordroYasalKesintilerSigorta.setVisibility(View.INVISIBLE);
        txtBordroYasalKesintilerIssizlikSigortasi.setVisibility(View.INVISIBLE);
        txtBordroYasalKesintilerGelirVergisi.setVisibility(View.INVISIBLE);
        txtBordroYasalKesintilerDamgaPulu.setVisibility(View.INVISIBLE);
        txtBordroYasalKesintilerSendikaKesintisi.setVisibility(View.INVISIBLE);
        txtBordroYasalKesintiler.setVisibility(View.INVISIBLE);


        btnBordroOzelKesintilerSskNoId.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerAvans.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerHaciz.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerNafaka.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerHasar.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerCeza.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerTrafikBorcu.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerMuhtelifBorc.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerKefalet.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerTelefon.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerKres.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerBerber.setVisibility(View.INVISIBLE);
        btnBordroOzelKesintilerVergiSskBorcu.setVisibility(View.INVISIBLE);

        txtBordroOzelKesintilerSskNoId.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerAvans.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerHaciz.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerNafaka.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerHasar.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerCeza.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerTrafikBorcu.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerMuhtelifBorc.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerKefalet.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerTelefon.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerKres.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerBerber.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintilerVergiSskBorcu.setVisibility(View.INVISIBLE);
        txtBordroOzelKesintiler.setVisibility(View.INVISIBLE);
    }


}