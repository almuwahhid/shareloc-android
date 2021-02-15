package id.akakom.bimo.shareloc.app.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.login.LoginActivity;
import id.akakom.bimo.shareloc.module.Activity.ShareLocPermissionActivity;

public class SplashScreenActivity extends ShareLocPermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getContext(), LoginActivity.class));
                finish();
            }
        }, 1000);
    }
}