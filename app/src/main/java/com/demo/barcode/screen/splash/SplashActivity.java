package com.demo.barcode.screen.splash;

import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.barcode.R;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.screen.dashboard.DashboardActivity;
import com.demo.barcode.screen.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (UserManager.getInstance().getUser() != null) {
                    DashboardActivity.start(SplashActivity.this);
                    SplashActivity.this.finish();
                }else {
                    LoginActivity.start(SplashActivity.this);
                }
            }
        };
        countDownTimer.start();

    }


}
