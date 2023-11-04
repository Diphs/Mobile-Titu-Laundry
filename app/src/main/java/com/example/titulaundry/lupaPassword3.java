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
import com.example.titulaundry.Model.UpdatePassword;
import com.example.titulaundry.layanan.Alert_App;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class lupaPassword3 extends AppCompatActivity {
    EditText pw1,pw2;
    Button Riset;
    ApiInterface apiInterface;
    ImageButton kembali;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password3);
        notif(lupaPassword3.this);
        setPassword();
        backToCode();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),lupaPassword2.class);
        i.putExtra("EmailLupa",getIntent().getStringExtra("EmailLupa"));
        startActivity(i);
    }

    private void backToCode() {
        kembali = findViewById(R.id.kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),lupaPassword2.class);
                i.putExtra("EmailLupa",getIntent().getStringExtra("EmailLupa"));
                startActivity(i);
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



    public void setPassword(){
        String getGmail = getIntent().getStringExtra("EmailLupa");
        System.out.println("Email akan dirubah adalah"+getGmail);
        pw1 = (EditText) findViewById(R.id.pw1);
        pw2 = (EditText) findViewById(R.id.pw2);
        Riset = (Button) findViewById(R.id.Reset);
        Riset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String matchPw1 = pw1.getText().toString();
                String matchPw2 = pw2.getText().toString();
                System.out.println("Password Baru adalah = "+matchPw1);

                Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

                if (matchPw1.equals(matchPw2)){
                     if (matchPw1.length()<7 || !specailCharPatten.matcher(matchPw1).find()){
//                        Toast.makeText(lupaPassword3.this, "Password kurang dari 8 dan harus mengandung karakter spesial", Toast.LENGTH_SHORT).show();
                         Alert_App.alertBro(lupaPassword3.this,"Password kurang dari 8 dan harus mengandung karakter spesial");
                    } else {
                         apiInterface = AppClient.getClient().create(ApiInterface.class);
                         Call<UpdatePassword> update = apiInterface.setNewPassword(getGmail,matchPw1);
                         update.enqueue(new Callback<UpdatePassword>() {
                             @Override
                             public void onResponse(Call<UpdatePassword> call, Response<UpdatePassword> response) {
                                 if (response.body().getKode() == 1){
                                     Toast.makeText(lupaPassword3.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                     Intent i = new Intent(getApplicationContext(),lupaPasswordSucces.class);
                                     startActivity(i);
                                     finish();
                                 }
                             }

                             @Override
                             public void onFailure(Call<UpdatePassword> call, Throwable t) {

                             }
                         });
                     }

                }  else {
//                    Toast.makeText(lupaPassword3.this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                    Alert_App.alertBro(lupaPassword3.this,"Password Tidak Sama");
                }
            }
        });
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