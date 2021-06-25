package com.example.shopapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.shopapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                SharedPreferences sharedPreferences = getSharedPreferences("userDetails", 0);
                String loggedIn = sharedPreferences.getString("loggedIn", "");
                if (loggedIn.equals("loggedIn")) {
                    intent = new Intent(SplashActivity.this, ShopListActivity.class);
                    intent.putExtra("from", "SPLASH");
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }, 2000);


    }
}