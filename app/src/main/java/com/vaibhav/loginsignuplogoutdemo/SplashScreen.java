package com.vaibhav.loginsignuplogoutdemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread n=new Thread(){
          @Override
          public void run(){
              try {
                  Thread.sleep(3000);
                  Intent i=new Intent(SplashScreen.this,MainActivity.class);
                  startActivity(i);
                  overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_in);
                  finish();
              }catch(Exception e){}
          }
        };
        n.start();
    }
}
