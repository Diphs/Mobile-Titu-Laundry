package com.example.titulaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Model.CheckEmail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupaPassword extends AppCompatActivity {
    ImageButton btnKembali;
    Button submit;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    EditText emailPassword;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        kembali();
        notif(LupaPassword.this);
        getEmailUser();
        setEmail();
    }

    public void setEmail(){
        String email = getIntent().getStringExtra("EmailLupa");
        if (email == null || email.equals("")){
            emailPassword.setText("");
        } else {
            emailPassword.setText(email);
        }
    }

    public void getEmailUser(){
        emailPassword = (EditText) findViewById(R.id.inputEmailLupaPw);
        submit = (Button) findViewById(R.id.Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eml = emailPassword.getText().toString();
                apiInterface = AppClient.getClient().create(ApiInterface.class);
                Call<CheckEmail> check = apiInterface.getCheckEmail(eml);
                check.enqueue(new Callback<CheckEmail>() {
                    @Override
                    public void onResponse(Call<CheckEmail> call, Response<CheckEmail> response) {
                        int kode = response.body().getKode();
                        if (kode == 1){
                            Intent i = new Intent(getApplicationContext(),lupaPassword2.class);
                            i.putExtra("EmailLupa",eml);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LupaPassword.this,"Email Tidak Terdaftar",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckEmail> call, Throwable t) {

                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),Login.class);
        startActivity(i);
        finish();
    }
    public void kembali(){
        btnKembali = (ImageButton) findViewById(R.id.kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });
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