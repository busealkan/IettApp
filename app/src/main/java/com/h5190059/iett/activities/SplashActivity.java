package com.h5190059.iett.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.h5190059.iett.R;
import com.h5190059.iett.utils.AlertUtil;
import com.h5190059.iett.utils.Constants;
import com.h5190059.iett.utils.NetworkUtil;
import com.h5190059.iett.utils.PrefUtil;

public class SplashActivity extends AppCompatActivity {

    private ImageView imgBackground;
    private View viewGirisBg;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        imgBackground = findViewById(R.id.imgLogo);
        viewGirisBg = findViewById(R.id.viewSifreDegistirBg);
        mAuth = FirebaseAuth.getInstance();
        zamanlayici();
    }

    private void zamanlayici() {
        int prefTimerMillisInFuture = Integer.parseInt(getResources().getString(R.string.prefTimerMillisInFuture));
        int prefTimerInterval = Integer.parseInt(getResources().getString(R.string.prefTimerInterval));
        CountDownTimer countDownTimer = new CountDownTimer(prefTimerMillisInFuture, prefTimerInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                internetKontrolEt();
            }
        };
        countDownTimer.start();
    }

    private void internetKontrolEt() {
        if (NetworkUtil.internetVarMi(SplashActivity.this)) {
            girisActivity();
        } else {
            internetAlert();
        }
    }

    private void internetAlert() {
        PrefUtil.setStringPref(SplashActivity.this, Constants.PREF_ALERT_SECILEN, Constants.PREF_ALERT_INTERNET);
        AlertUtil.alertGoster(SplashActivity.this, R.style.AlertDialogTheme, getResources().getDrawable(R.drawable.interneticon), getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertMessage), getResources().getString(R.string.alertNegativeButon), getResources().getString(R.string.alertPozitiveButon));
    }

    private void girisActivity() {
        Intent girisIntent = new Intent(SplashActivity.this, GirisActivity.class);
        startActivity(girisIntent);
        finish();
    }
}