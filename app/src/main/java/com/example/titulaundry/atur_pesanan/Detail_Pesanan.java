package com.example.titulaundry.atur_pesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Dashboard.MainMenu;
import com.example.titulaundry.Model.ResponseInsertPesanan;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.R;
import com.example.titulaundry.Dashboard.Voucher;
import com.example.titulaundry.layanan.Alert_App;
//import com.example.titulaundry.db_help.Database_Tb_Pesanan;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_Pesanan extends AppCompatActivity {

    String layanan , hargaLayanan , beratCucian,hariJemput,hariKirim,waktuJemput,waktuKirim
            ,alamatUserPick,alamatUserSend,idOrg,IdJasa,idVoucher,hargaDisc = "",desc,waktuKerja,imgg;

    TextView jnslyn,txHariJemput,txHariKirim,beratXharga,totalHarga,alamatDetailPick,alamatDetailSend,diskon,deeskon,user,userkirim;
    String formatHariJemput,formatHariKirim;
    int GettotalHarga = 0;
    Button backToHome;
    ImageButton bckk;
    ConstraintLayout lyt;
    ApiInterface apiInterface;
    CardView goVoucher;
NetworkChangeListener networkChangeListener = new NetworkChangeListener();
//
//    Database_Tb_Pesanan PS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);
        notif(Detail_Pesanan.this);
        System.out.println("TES ID VOUCER = "+ getIntent().getStringExtra("idcer"));
        setLayoutPesanan();

        getDataFromPesanan();
        setUser();
        setFormatHari();
        setGoVoucher();
        setAlamat();
        KembaliSayang();

        setDataFromPesanan();
//        InsertPesanan();


    }
    public static String convertRupiah(int price){
        Locale locale = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String strFormat = format.format(price);
        return strFormat.replace(",00","");
    }
    public void KembaliSayang(){
        bckk = (ImageButton) findViewById(R.id.kembali);
        bckk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                idOrg = sharedPreferences.getString("KEY_ID","");
                IdJasa = getIntent().getStringExtra("id_jasa");
                desc = getIntent().getStringExtra("deskripsi");
                waktuKerja = getIntent().getStringExtra("waktu");
                imgg = getIntent().getStringExtra("imagee");

                Intent i = new Intent(getApplicationContext(), pesanan.class);
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
                startActivity(i);
            }
        });
    }

    public void setGoVoucher(){
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

        goVoucher = (CardView) findViewById(R.id.menu2);
        goVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GettotalHarga>=30000){
                    Intent i = new Intent(getApplicationContext(), Voucher.class);
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
                    startActivity(i);
                } else {
//                    Toast.makeText(Detail_Pesanan.this, "Harga Belanjaan Kurang", Toast.LENGTH_SHORT).show();
                    Alert_App.alertBro(Detail_Pesanan.this,"Harga belanjaan kurang minimal Rp.30.000 untuk menggunakan voucher");
                }

            }
        });
    }

    public void setLayoutPesanan(){
        lyt = (ConstraintLayout) findViewById(R.id.menuKurir);

        String layout = getIntent().getStringExtra("hariJemput");
        if (layout.equals("Antar Sendiri")){
            lyt.setVisibility(View.GONE);
        } else {

        }

    }
    public void setUser(){
        user = findViewById(R.id.NamaUser);
        userkirim = findViewById(R.id.NamaUserKirim);
        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
        String id_user = getIntent().getStringExtra("id_user");
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("KEY_ID","");
        Call<ResponseUser> userCall = apiInterface.getDataUser(id_user);
        userCall.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                user.setText(response.body().getData().getNama());
                userkirim.setText(response.body().getData().getNama());
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });
    }

    public void setAlamat(){
        alamatDetailPick = (TextView) findViewById(R.id.alamatDetail);
        alamatDetailSend = (TextView) findViewById(R.id.alamatDetailKirim);
        alamatDetailPick.setText(alamatUserPick);
        alamatDetailSend.setText(alamatUserSend);
    }

    public static int HitungBro(String hargaLayanan , String beratCucian){
        String fm = hargaLayanan;
        fm = fm.replaceAll("[^\\d.]", "");
        fm = fm.replace(".","");
        int harga = Integer.parseInt(fm);
        int berat = Integer.parseInt(beratCucian);
        int ppn = 1320;
        return (harga*berat)+ppn;
    }

    public void setDataFromPesanan(){
        //layanan
        jnslyn = (TextView) findViewById(R.id.jenisLayanan);
        jnslyn.setText("Layanan " + layanan + " "+ beratCucian + " Kg");

        //hari Jemput
        txHariJemput = (TextView) findViewById(R.id.jamJemput);
        txHariJemput.setText(formatHariJemput+"-"+waktuJemput+" WIB");

        //hari Kirim
        txHariKirim = (TextView) findViewById(R.id.jamKirim);
        txHariKirim.setText(formatHariKirim+"-"+waktuKirim+" WIB");

        //harga x berat
        beratXharga = (TextView) findViewById(R.id.totalBeratCucian);
        beratXharga.setText(String.valueOf(hargaLayanan)+" X "+String.valueOf(beratCucian)+" Kg");

        diskon = (TextView) findViewById(R.id.diskon);
        totalHarga = (TextView) findViewById(R.id.hargaFix);
        deeskon = (TextView) findViewById(R.id.inpowaktu);

        if (hargaDisc == null){
            diskon.setText("0");
            GettotalHarga = HitungBro(hargaLayanan,beratCucian);
            totalHarga.setText(convertRupiah(GettotalHarga));
        } else {
            diskon.setText(hargaDisc);
            int diskon = Integer.parseInt(hargaDisc);
            GettotalHarga = HitungBro(hargaLayanan,beratCucian) - diskon;
            System.out.println(diskon+" "+GettotalHarga);
            totalHarga.setText(convertRupiah(GettotalHarga));
            deeskon.setText("Potongan Harga : "+hargaDisc);
            System.out.println("Harga udah Diskon = "+GettotalHarga);
        }

    }

    public void setFormatHari(){
        if (!hariJemput.equals("Antar Sendiri")||!hariKirim.equals("Antar Sendiri")){
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format3 = new SimpleDateFormat("EEEE");
            SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date date = format1.parse(hariJemput);
                Date date1 = format1.parse(hariKirim);
                formatHariJemput = format3.format(date);
                formatHariKirim = format3.format(date1);
                hariJemput = format4.format(date);
                hariKirim = format4.format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void getDataFromPesanan(){
        idVoucher = getIntent().getStringExtra("idcer");
        hargaDisc = getIntent().getStringExtra("potong_harga");
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


        backToHome = (Button) findViewById(R.id.buatPesanan);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //harga
                int hargaa2 = 0;
                if (hargaDisc == null){
                    hargaa2 = HitungBro(hargaLayanan,beratCucian);
                    hargaDisc = "0";
                    idVoucher = "1";
                } else {
                    int diskon = Integer.parseInt(hargaDisc);
                    hargaa2 = HitungBro(hargaLayanan,beratCucian) - diskon;
                }



                if (waktuJemput == null || waktuKirim == null || alamatUserPick == null || alamatUserSend == null){
                    waktuJemput = "";
                    waktuKirim = "";
                    alamatUserPick = "";
                    alamatUserSend = "";
                } else {
                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date date = format1.parse(hariJemput);
                        Date date1 = format1.parse(hariKirim);
                        hariJemput = format4.format(date);
                        hariKirim = format4.format(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //waktuJemput
                String pickAku = hariJemput+" "+waktuJemput;
                String SendAku = hariKirim+" "+waktuKirim;

                System.out.println("Id User = "+idOrg);
                System.out.println("Id Jasa = "+IdJasa);
                System.out.println("total_berat = "+beratCucian);
                System.out.println("total_harga = "+hargaa2);
                System.out.println("Waktu Penjemputan = "+hariJemput);
                System.out.println("Waktu Kirim = "+hariKirim);


                System.out.println("Saat ada tanggal e "+pickAku);
                System.out.println("Saat ada tanggal e  "+SendAku);
                System.out.println(alamatUserPick + alamatUserSend);
                System.out.println("id Voucher = "+idVoucher);
                System.out.println("Harga Diskon = "+hargaDisc);

                //Insert Data HERE
                apiInterface = AppClient.getClient().create(ApiInterface.class);
                Call<ResponseInsertPesanan> insertPesananCall = apiInterface.getDataPesanan(beratCucian,
                        String.valueOf(hargaa2),hargaDisc,pickAku,SendAku,idVoucher,IdJasa,idOrg,alamatUserPick,alamatUserSend);
                insertPesananCall.enqueue(new Callback<ResponseInsertPesanan>() {
                    @Override
                    public void onResponse(Call<ResponseInsertPesanan> call, Response<ResponseInsertPesanan> response) {
                        int kode = response.body().getKode();
                        String kode_pesanan = response.body().getData().getId_pesanan();

                        System.out.println("Apakah Masuk =  "+kode +" Id Pesanan User = "+kode_pesanan);
                        Intent i = new Intent(getApplicationContext(),Pembayaran.class);
                        i.putExtra("id_user",idOrg);
                        i.putExtra("pesananId",kode_pesanan);
                        i.putExtra("hargaCucian",totalHarga.getText().toString());
                        i.putExtra("layanan",jnslyn.getText().toString());
                        i.putExtra("waktuuu",hariJemput);
                        System.out.println("Data Hari Jemput = "+ hariJemput);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseInsertPesanan> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });

            }
        });

    }

//    public void InsertPesanan(){
//
//        backToHome = (Button) findViewById(R.id.buatPesanan);
//        backToHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int min = 1;
//                int max = 10000;
//
//                //Generate random int value from 50 to 100
//                System.out.println("Random value in int from "+min+" to "+max+ ":");
//                int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
//                System.out.println(random_int);
//                String numberRandom = String.valueOf(random_int);
//                Intent i = new Intent(getApplicationContext(), MainMenu.class);
//                i.putExtra("id_user",getIntent().getStringExtra("id_user"));
//                System.out.println("ID USER TO MAIN MENU == "+getIntent().getStringExtra("id_user"));
//                startActivity(i);
//                finish();
//            }
//        });
//
//    }
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