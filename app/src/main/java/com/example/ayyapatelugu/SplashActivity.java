package com.example.ayyapatelugu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ayyapatelugu.User.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread loading = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    Intent main = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(main);
                }

                catch (Exception e) {
                    e.printStackTrace();
                }

                finally {
                    finish();
                }
            }
        };

        loading.start();
    }

}