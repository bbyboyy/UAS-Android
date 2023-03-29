package com.example.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    ImageView logo;
    TextView text;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        handler = new Handler(Looper.getMainLooper());

        logo.startAnimation(animation);
        text.startAnimation(animation);
        
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    private void initViews() {
        logo = (ImageView) findViewById(R.id.splash_img_logo);
        text = (TextView) findViewById(R.id.splash_text);
    }
}