package com.example.titulaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.input.InputManager;
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
import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Model.ResponseEmail;
import com.example.titulaundry.Model.VerifEmail;
import com.example.titulaundry.Send_Email.JavaMailAPI;
import com.example.titulaundry.layanan.Alert_App;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Konfirmasi extends AppCompatActivity {
    Button btnContinue;
    PinView getCode;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ApiInterface apiInterface;
    TextView desc,sendCodeAgain;
    String id_user,kodeVerif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);
        LoginToConfirm();
        setDesc();
        showDataEmail();
        sendEmailAgain();
        notif(Konfirmasi.this);
    }
    public void setDesc(){
        String emaile = getIntent().getStringExtra("EmailUser");
        desc = (TextView) findViewById(R.id.deskripsi);
        String text = "Sebuah kode autentikasi telah dikirim ke alamat email kamu <b>"+emaile+"</b>";
        desc.setText(Html.fromHtml(text));
    }

    public void sendEmailAgain(){
        sendCodeAgain = (TextView) findViewById(R.id.text1);
        String text = "Belum menerima kode? <b><font color=#2f80ed>Kirim ulang</font></b>";
        sendCodeAgain.setText(Html.fromHtml(text));
        sendCodeAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendEmail();
            }
        });
    }
    public void LoginToConfirm(){
        btnContinue = (Button) findViewById(R.id.continueSelamat);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyEMail();
            }
        });
    }
    public void SendEmail(){
        String emaile = getIntent().getStringExtra("EmailUser");
        String email = emaile;
        String header = "VERIFIKASI EMAIL TO ACCES APP TITU LAUNDRY";
        String pesan = "Kode Verif Account : "+kodeVerif;
        JavaMailAPI javaMailAPI = new JavaMailAPI(Konfirmasi.this,email,header,pesan);
        javaMailAPI.execute();
    }
    public void showDataEmail(){
        id_user = getIntent().getStringExtra("Userid");
        System.out.println("ID USernya adalah = "+id_user);
        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseEmail> show = apiInterface.getVerifEmail(id_user);
        show.enqueue(new Callback<ResponseEmail>() {
            @Override
            public void onResponse(Call<ResponseEmail> call, Response<ResponseEmail> response) {
                kodeVerif = response.body().getData().getKodeVerifikasi();
                System.out.println(kodeVerif);
                SendEmail();
            }

            @Override
            public void onFailure(Call<ResponseEmail> call, Throwable t) {

            }
        });

    }

    public void VerifyEMail(){
        id_user = getIntent().getStringExtra("Userid");
        getCode = (PinView) findViewById(R.id.firstPinView);

        String inputKodeUSer = getCode.getText().toString();
        System.out.println("Input Usernya adalah "+inputKodeUSer);

        if (inputKodeUSer.equals(kodeVerif)){

            apiInterface = AppClient.getClient().create(ApiInterface.class);
            Call<VerifEmail> verifEmailCall = apiInterface.setUpdateEmail(id_user);
            verifEmailCall.enqueue(new Callback<VerifEmail>() {
                @Override
                public void onResponse(Call<VerifEmail> call, Response<VerifEmail> response) {
                    int kode = response.body().getKode();
                    if (kode == 1){
//                        Toast.makeText(Konfirmasi.this, "Berhasil Verif", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),KonfirmasiSukses.class);
                        i.putExtra("UserId",id_user);
                        startActivity(i);
                        finish();
                    } else {
//                        Toast.makeText(Konfirmasi.this, "Gagal Verif", Toast.LENGTH_SHORT).show();
                        Alert_App.alertBro(Konfirmasi.this,"Gagal Verifikasi Account");
                    }
                }

                @Override
                public void onFailure(Call<VerifEmail> call, Throwable t) {

                }
            });
        } else {
//            Toast.makeText(this, "Kode Salah", Toast.LENGTH_SHORT).show();
            Alert_App.alertBro(this,"Kode yang anda masukkan salah!");
            getCode.setText("");
        }
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