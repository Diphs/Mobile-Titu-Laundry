package com.example.titulaundry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.titulaundry.Dashboard.MainMenu;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        notif();

        new Handler().postDelayed(new Runnable() {

            @Override

            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                String key = sharedPreferences.getString("KEY_ID","");
                if(key == "" || key.isEmpty()){
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                    finish();
                } else{
                    Intent i = new Intent(SplashScreen.this, MainMenu.class);
                    startActivity(i);
                    finish();
                }


            }

        }, 2*1000); // wait for 5 seconds


    }
    public void notif(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.niceBlue));
    }
}