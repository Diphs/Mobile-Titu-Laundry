package com.example.titulaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Send_Email.JavaMailAPI;

import java.util.Random;

public class lupaPassword2 extends AppCompatActivity {
    PinView kode;
    Button Continue;
    String KodeVerify;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TextView sendAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password2);
        notif(lupaPassword2.this);
        getKodeEmail();
        submit();
        sendKodeAgain();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),LupaPassword.class);
        i.putExtra("EmailLupa",getIntent().getStringExtra("EmailLupa"));
        startActivity(i);
    }
    public void notif(Activity activity){
        //change color notif bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        //set icons notifbar
        View decor = activity.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    public void sendKodeAgain(){
        sendAgain = (TextView) findViewById(R.id.text1);
        String text = "Belum menerima kode? <b><font color=#2f80ed>Kirim ulang</font></b>";
        sendAgain.setText(Html.fromHtml(text));
        sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailLupa = getIntent().getStringExtra("EmailLupa");
                JavaMailAPI javaMailAPI = new JavaMailAPI(lupaPassword2.this,emailLupa,"LUPA PASSWORD","Kode = "+KodeVerify);
                javaMailAPI.execute();
            }
        });

    }

    public void submit(){
        Continue = (Button) findViewById(R.id.continueSelamat);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kode = (PinView) findViewById(R.id.firstPinView);
                String mathPw = kode.getText().toString();
                System.out.println("mathPw == "+mathPw);
                System.out.println("GENERATE =="+KodeVerify);
                if (mathPw.equals(KodeVerify)){
                    Toast.makeText(lupaPassword2.this, "BENAR BRUH", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),lupaPassword3.class);
                    i.putExtra("EmailLupa",getIntent().getStringExtra("EmailLupa"));
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(lupaPassword2.this, "Salah", Toast.LENGTH_SHORT).show();
                    kode.setText("");
                }
            }
        });
    }

    public void getKodeEmail(){
        String emailLupa = getIntent().getStringExtra("EmailLupa");
        Random r = new Random( System.currentTimeMillis() );
        int x =  10000 + r.nextInt(20000);
        KodeVerify = String.valueOf(x);
        JavaMailAPI javaMailAPI = new JavaMailAPI(lupaPassword2.this,emailLupa,"LUPA PASSWORD","Kode Lupa Password : "+KodeVerify);
        javaMailAPI.execute();

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}