package com.example.titulaundry.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Adapter.AdapterVoucher;
import com.example.titulaundry.Model.ResponseVoucher;
import com.example.titulaundry.ModelMySQL.DataItemVoucher;
import com.example.titulaundry.R;
import com.example.titulaundry.atur_pesanan.Detail_Pesanan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Voucher extends AppCompatActivity  {
    String layanan , hargaLayanan , beratCucian,hariJemput,hariKirim,
            waktuJemput,waktuKirim,alamatUserPick,alamatUserSend,idOrg,IdJasa,desc,waktuKerja,imgg;
    RecyclerView recyclerView;
    List<DataItemVoucher> voucherList = new ArrayList<>();
    Button submit;
    AdapterVoucher adapterVoucher;
    ApiInterface apiInterface;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        notif(Voucher.this);
        getDataVoucher();

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

    public void getDataVoucher(){
        submit = (Button) findViewById(R.id.submitVoucher);
        AdapterVoucher.PassDataVoucher passDataVoucher = new AdapterVoucher.PassDataVoucher() {
            @Override
            public void passData(String id_jasa, String potongan_harga, String slot) {
                layanan = getIntent().getStringExtra("layanan");
                hargaLayanan = getIntent().getStringExtra("harga");
                beratCucian = getIntent().getStringExtra("berat");
                hariJemput = getIntent().getStringExtra("hariJemput");
                hariKirim = getIntent().getStringExtra("hariKembali");
                waktuJemput = getIntent().getStringExtra("waktuJemput");
                waktuKirim = getIntent().getStringExtra("waktuKembali");
                alamatUserPick = getIntent().getStringExtra("alamatUserPick");
                alamatUserSend = getIntent().getStringExtra("alamatUserSend");
                idOrg = getIntent().getStringExtra("id_user");
                IdJasa = getIntent().getStringExtra("id_jasa");
                desc = getIntent().getStringExtra("deskripsi");
                waktuKerja = getIntent().getStringExtra("waktu");
                imgg = getIntent().getStringExtra("imagee");

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        System.out.println("ID VOUCHER YANG DIBAWAH = "+id_jasa);
                        System.out.println("Go to Detail = "+idOrg+" "+IdJasa);
                        Intent i = new Intent(getApplicationContext(), Detail_Pesanan.class);
                        i.putExtra("idcer",id_jasa);
                        i.putExtra("layanan",layanan);
                        i.putExtra("harga",hargaLayanan);
                        i.putExtra("berat",beratCucian);
                        i.putExtra("hariJemput",hariJemput);
                        i.putExtra("hariKembali",hariKirim);
                        i.putExtra("waktuJemput",waktuJemput);
                        i.putExtra("waktuKembali",waktuKirim);
                        i.putExtra("alamatUserPick",alamatUserPick);
                        i.putExtra("alamatUserSend",alamatUserSend);
                        i.putExtra("id_user",idOrg);
                        i.putExtra("id_jasa",IdJasa);
                        i.putExtra("deskripsi",desc);
                        i.putExtra("waktu",waktuKerja);
                        i.putExtra("imagee",imgg);
                        i.putExtra("potong_harga",potongan_harga);
                        startActivity(i);
                    }
                });
            }
        };

        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseVoucher> voucherCall = apiInterface.getVoucher();
        voucherCall.enqueue(new Callback<ResponseVoucher>() {
            @Override
            public void onResponse(Call<ResponseVoucher> call, Response<ResponseVoucher> response) {
                String pesan = response.body().getPesan();
                System.out.println(pesan+" Voucher");

                voucherList = response.body().getData();

                adapterVoucher = new AdapterVoucher(Voucher.this,voucherList,passDataVoucher);
                recyclerView = findViewById(R.id.recycleVoucher);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(Voucher.this));
                recyclerView.setAdapter(adapterVoucher);
                adapterVoucher.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseVoucher> call, Throwable t) {

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