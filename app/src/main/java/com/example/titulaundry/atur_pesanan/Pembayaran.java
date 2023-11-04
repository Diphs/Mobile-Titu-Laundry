package com.example.titulaundry.atur_pesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Dashboard.MainMenu;
import com.example.titulaundry.Model.ResponseCod;
import com.example.titulaundry.R;
import com.example.titulaundry.layanan.Alert_App;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pembayaran extends AppCompatActivity {
    TextView layanan , Harga;
    RadioButton cod , shoopePay , Bri;
    Button Chat;
    MaterialButton bckHome;
    ApiInterface apiInterface;
    CardView nomerShopay , NomerBri;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        notif(Pembayaran.this);
        inisiasi();
        switchBuy();
        KeMainMenu();
        setBayar();
        setChat();
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

    public void inisiasi(){
        layanan = (TextView) findViewById(R.id.pesananUser);
        Harga = (TextView) findViewById(R.id.HargaPembayaran);
        cod = (RadioButton) findViewById(R.id.radio1);
        shoopePay = (RadioButton) findViewById(R.id.radio2);
        Bri = (RadioButton) findViewById(R.id.radio3);
        Chat = (Button) findViewById(R.id.SubmitBayar);
        bckHome = (MaterialButton) findViewById(R.id.backToHome);
        nomerShopay = (CardView) findViewById(R.id.NomerShoopePay);
        NomerBri = (CardView) findViewById(R.id.NomerBri);
    }
    public void setBayar(){
        layanan.setText(getIntent().getStringExtra("layanan"));
        Harga.setText(getIntent().getStringExtra("hargaCucian"));
    }
    public void KeMainMenu(){
        bckHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!shoopePay.isChecked() && !Bri.isChecked() && !cod.isChecked()){
                    Alert_App.alertBro(Pembayaran.this,"Harap Pilih Salah Satu Pembayaran");

                } else {
                    if (cod.isChecked()){
                        apiInterface = AppClient.getClient().create(ApiInterface.class);
                        Call<ResponseCod> call = apiInterface.getCod(getIntent().getStringExtra("pesananId"),getIntent().getStringExtra("waktuuu"));
                        call.enqueue(new Callback<ResponseCod>() {
                            @Override
                            public void onResponse(Call<ResponseCod> call, Response<ResponseCod> response) {
                                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                                i.putExtra("id_user",getIntent().getStringExtra("id_user"));
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseCod> call, Throwable t) {

                            }
                        });

                    } else {
                        Intent i = new Intent(getApplicationContext(), MainMenu.class);
                        i.putExtra("id_user",getIntent().getStringExtra("id_user"));
                        startActivity(i);
                        finish();
                    }
                }

            }
        });
    }

    public void setChat(){
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=6285851065295";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void switchBuy(){
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bri.setChecked(false);
                cod.setChecked(true);
                shoopePay.setChecked(false);
                Bri.setChecked(false);
                NomerBri.setVisibility(View.GONE);
                nomerShopay.setVisibility(View.GONE);
            }
        });
        shoopePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoopePay.setChecked(true);
                Bri.setChecked(false);
                cod.setChecked(false);
                nomerShopay.setVisibility(View.VISIBLE);
                NomerBri.setVisibility(View.GONE);
            }
        });
        Bri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoopePay.setChecked(false);
                Bri.setChecked(true);
                cod.setChecked(false);
                nomerShopay.setVisibility(View.GONE);
                NomerBri.setVisibility(View.VISIBLE);
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