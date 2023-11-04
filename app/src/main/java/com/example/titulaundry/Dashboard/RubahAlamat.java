package com.example.titulaundry.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.MapsActivity;
import com.example.titulaundry.Model.ResponseAlamat;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.R;
import com.example.titulaundry.databinding.ActivityMapsBinding;
import com.example.titulaundry.layanan.Alert_App;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RubahAlamat extends AppCompatActivity{
    EditText setAlamat;
    TextView alamat;
    ApiInterface apiInterface;
    Button edt , submit;
    ImageButton bck;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubah_alamat);
        alamat = (TextView) findViewById(R.id.setAlamat);
        setSetAlamat ();
        notif(RubahAlamat.this);
        updateAlamat();
        kembali();
    }


    private void kembali() {
        bck = findViewById(R.id.kembali);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_user = getIntent().getStringExtra("id_user");
                Intent i = new Intent(getApplicationContext(),MainMenu.class);
                i.putExtra("id_user",id_user);
                startActivity(i);
            }
        });
    }

    public void updateAlamat(){
        String id_user = getIntent().getStringExtra("id_user");
        submit = (Button) findViewById(R.id.gantiAlamat);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!setAlamat.getText().toString().equals("")){
                    apiInterface = AppClient.getClient().create(ApiInterface.class);
                    Call<ResponseAlamat> alamatCall = apiInterface.setAlamat(id_user,setAlamat.getText().toString());
                    alamatCall.enqueue(new Callback<ResponseAlamat>() {
                        @Override
                        public void onResponse(Call<ResponseAlamat> call, Response<ResponseAlamat> response) {
                            if (response.body().getKode() == 1){
//                            Toast.makeText(RubahAlamat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Alert_App.alertBro(RubahAlamat.this,response.body().getMessage());
                                alamat.setText(setAlamat.getText().toString());
                                setAlamat.setText("");
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseAlamat> call, Throwable t) {

                        }
                    });

                } else {
                    Alert_App.alertBro(RubahAlamat.this,"Alamat yang dimasukkan kosong");
                }

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

    public void setSetAlamat (){
        setAlamat = (EditText) findViewById(R.id.textAlamat);

        String id_user = getIntent().getStringExtra("id_user");
        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseUser> userCall = apiInterface.getDataUser(id_user);
        userCall.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                alamat.setText(response.body().getData().getAlamat());
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

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