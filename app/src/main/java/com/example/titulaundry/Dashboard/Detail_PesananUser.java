package com.example.titulaundry.Dashboard;

import static androidx.constraintlayout.motion.widget.Key.VISIBILITY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Model.ResponsePesananUser;
import com.example.titulaundry.R;
import com.example.titulaundry.layanan.Alert_App;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_PesananUser extends AppCompatActivity {

    ApiInterface apiInterface;
    ImageButton back;
    ImageView gambarJasa;
    TextView jenis_jasa , status , tgl , est , namaUser , alamatJemput , namaUserKirim , alamatKirim,berat,harga,deskon;
    Context ctx;
    FrameLayout frameLayout;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_user);
        notif(Detail_PesananUser.this);
        setDetailPesanan();
        balick();

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
    public void balick(){


        back = (ImageButton) findViewById(R.id.kembali);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainMenu.class);
                i.putExtra("id_user",getIntent().getStringExtra("id_user"));

                startActivity(i);
            }
        });
    }
    public static String convertRupiah(int price){
        Locale locale = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String strFormat = format.format(price);
        return strFormat.replace(",00","");
    }
    public void setDetailPesanan(){

        //Inisiasi
        gambarJasa = (ImageView) findViewById(R.id.gbr);
        jenis_jasa = (TextView) findViewById(R.id.jenisJasa);
        status = (TextView) findViewById(R.id.status);
        tgl = (TextView) findViewById(R.id.setTanggal);
        est = (TextView) findViewById(R.id.setEstimasi);
        namaUser = (TextView) findViewById(R.id.NamaUser);
        alamatJemput = (TextView) findViewById(R.id.alamatDetail);
        namaUserKirim = (TextView) findViewById(R.id.NamaUserKirim);
        alamatKirim = (TextView) findViewById(R.id.alamatDetailKirim);
        berat = (TextView) findViewById(R.id.totalBeratCucian);
        harga = (TextView) findViewById(R.id.hargaFix);
        frameLayout = (FrameLayout) findViewById(R.id.frmee);
        deskon = (TextView) findViewById(R.id.diskon);



        System.out.println(getIntent().getStringExtra("id_pesanan")+"turuuu");
        String id_pesan = getIntent().getStringExtra("id_pesanan");

        //set
        Alert_App.ShowLoadScreenData(this);
        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponsePesananUser> userCall = apiInterface.getDataPesanan(id_pesan);
        userCall.enqueue(new Callback<ResponsePesananUser>() {
            @Override
            public void onResponse(Call<ResponsePesananUser> call, Response<ResponsePesananUser> response) {
                Picasso.get().load(AppClient.URL_IMG+response.body().getData().getImage()).error(R.drawable.meki).into(gambarJasa);
                jenis_jasa.setText(response.body().getData().getJenisJasa()+" "+response.body().getData().getTotalBerat()+" Kg");
                status.setText(response.body().getData().getStatusPesanan());
                if (response.body().getData().getStatusPesanan().equals("Sedang diproses")){
                    status.setBackground(ContextCompat.getDrawable(Detail_PesananUser.this,R.drawable.bunder_text_proses));
                    status.setTextColor(Color.rgb(246, 185, 131));
                } else if (response.body().getData().getStatusPesanan().equals("Sedang dalam pengiriman")){
                    status.setBackground(ContextCompat.getDrawable(Detail_PesananUser.this,R.drawable.bunder_text_antar));
                    status.setTextColor(Color.rgb(69, 141, 239));
                } else if (response.body().getData().getStatusPesanan().equals("Menunggu pembayaran")){
                    status.setBackground(ContextCompat.getDrawable(Detail_PesananUser.this,R.drawable.bunder_text_bayar));
                    status.setTextColor(Color.rgb(242, 201, 76));
                } else if (response.body().getData().getStatusPesanan().equals("Pesanan dibatalkan")){
                    status.setBackground(ContextCompat.getDrawable(Detail_PesananUser.this,R.drawable.bunder_text_batal));
                    status.setTextColor(Color.rgb(235, 87, 87));
                } else if (response.body().getData().getStatusPesanan().equals("Menunggu diantar")){
                    status.setBackground(ContextCompat.getDrawable(Detail_PesananUser.this,R.drawable.bunder_text_pink));
                    status.setTextColor(Color.rgb(245, 111, 179));
                } else if (response.body().getData().getStatusPesanan().equals("Sedang dijemput")){
                    status.setBackground(ContextCompat.getDrawable(Detail_PesananUser.this,R.drawable.bunder_text_antar));
                    status.setTextColor(Color.rgb(69, 141, 239));
                }


                tgl.setText(response.body().getData().getTanggal());

                if (response.body().getData().getWaktuAntar().equals("0000-00-00 00:00:00")) {
                    est.setText("COD / ANTAR SENDIRI");
                } else {
                    est.setText(response.body().getData().getWaktuAntar());
                }


                if (response.body().getData().getAlamatPenjemputan().equals("")){
                    frameLayout.setVisibility(View.GONE);
                } else {
                    namaUser.setText(response.body().getData().getNama());
                    namaUserKirim.setText(response.body().getData().getNama());
                    alamatJemput.setText(response.body().getData().getAlamatPenjemputan());
                    alamatKirim.setText(response.body().getData().getAlamatPengiriman());

                }
                berat.setText("Rp."+response.body().getData().getHarga()+" X "+response.body().getData().getTotalBerat()+"Kg");

                harga.setText(convertRupiah(Integer.parseInt(response.body().getData().getTotalHarga())));
                deskon.setText(response.body().getData().getHarga_diskon());
                Alert_App.HideLoadScreenData(Detail_PesananUser.this);



            }

            @Override
            public void onFailure(Call<ResponsePesananUser> call, Throwable t) {

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