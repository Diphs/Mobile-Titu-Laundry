package com.example.titulaundry.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.Adapter.AdapterAlamat;
import com.example.titulaundry.Adapter.AdapterAlamat2;
import com.example.titulaundry.Model.ResponseAlamatLain;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.ModelMySQL.DataItemAlamatLain;
import com.example.titulaundry.R;
import com.example.titulaundry.layanan.Alert_App;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tampung_Alamat extends AppCompatActivity {
    ApiInterface apiInterface;
    TextView alamat1;
    Button editAlamatUtama,TambahAlamat;
    static RecyclerView recyclerView;
    static FrameLayout emptylist;
    List<DataItemAlamatLain> alamatLainList = new ArrayList<>();
    AdapterAlamat2 adapterAlamat2;
    String id_alamatUtama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampung_alamat);
        notif(Tampung_Alamat.this);
        setupAlamatUtama();
        pindahAlamatUtama();
        setupAlamatLain();
        TambahAlamatLain();
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
    public void setupAlamatUtama(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        String id_user = sharedPreferences.getString("KEY_ID","");
        alamat1 = findViewById(R.id.alamatTambahanUtama);
        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseUser> userCall = apiInterface.getDataUser(id_user);
        userCall.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                alamat1.setText(response.body().getData().getAlamat());
                id_alamatUtama = response.body().getData().getId_alamat();
                System.out.println("Tes ID alamat = "+ id_alamatUtama);
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });

    }

    public void pindahAlamatUtama(){
        editAlamatUtama = findViewById(R.id.buttonEditUtama);
        editAlamatUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Change_Address.class);
                i.putExtra("alamatTerbawah",alamat1.getText().toString());
                i.putExtra("idAlamat",id_alamatUtama);
                i.putExtra("aksi","editAlamat");
                startActivity(i);
                finish();

            }
        });
    }

    public void TambahAlamatLain(){
        TambahAlamat = findViewById(R.id.buttonTambah);
        TambahAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Change_Address.class);
                i.putExtra("aksi","tambahAlamat");
                startActivity(i);
                finish();
            }
        });
    }

    public void setupAlamatLain(){
        recyclerView = findViewById(R.id.recycleAlamatLain);
        emptylist = findViewById(R.id.kosongHati);

        AdapterAlamat2.getCountAlamat alamat = new AdapterAlamat2.getCountAlamat() {
            @Override
            public void jumlahList(int todd) {
                if (todd == 0){
                    recyclerView.setVisibility(View.INVISIBLE);
                    emptylist.setVisibility(View.VISIBLE);
                }
                System.out.println("Bro you really ? == "+todd);
            }
        };
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        String id_user = sharedPreferences.getString("KEY_ID","");

        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseAlamatLain> alamatLainCall = apiInterface.getAlamatKirim(id_user);
        alamatLainCall.enqueue(new Callback<ResponseAlamatLain>() {
            @Override
            public void onResponse(Call<ResponseAlamatLain> call, Response<ResponseAlamatLain> response) {

                if (response.body().getKode() == 1){

                    recyclerView.setVisibility(View.VISIBLE);
                    emptylist.setVisibility(View.INVISIBLE);
                    alamatLainList = response.body().getData();
                    adapterAlamat2 = new AdapterAlamat2(alamatLainList,Tampung_Alamat.this,alamat);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapterAlamat2);
                    adapterAlamat2.notifyDataSetChanged();
                    System.out.println("Ngengek mencret == "+adapterAlamat2.getItemCount());
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    emptylist.setVisibility(View.VISIBLE);
                    System.out.println("banh apa kesini");
                }

            }

            @Override
            public void onFailure(Call<ResponseAlamatLain> call, Throwable t) {
                System.out.println("banh apa kesini ka KAMUUU");
            }
        });
    }


}