package com.example.titulaundry.atur_pesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Dashboard.MainMenu;
import com.example.titulaundry.Dashboard.home_fragment;
import com.example.titulaundry.Dashboard.order_fragment;
import com.example.titulaundry.Login;
import com.example.titulaundry.R;
import com.example.titulaundry.layanan.Alert_App;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class pesanan extends AppCompatActivity {
    ImageButton toDashboard,Kembali;
    ImageView imgLayanan;
    Button gasPesanan;
    CardView setAlamat,setBeratCuci;
    TextView header , headerBawah,hargaCucian,lamaWaktu,setWaktualamat,setBeratCucian,deskripsi;
    public String layanan , desc , waktu , harga,Imgg,idWong,id_jasa;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);
        notif(pesanan.this);

        setAlamat();
        setPesanan();
        setBeratCucuian();
        dataFromAlamat();
        dataFromBerat();
        getPesanan();
        KembaliBruh();
        toKembali();
    }

    public void KembaliBruh(){
        Kembali = (ImageButton) findViewById(R.id.kembali);
        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainMenu.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainMenu.class);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("id_user",idWong);
        startActivity(i);
       finish();
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
    public void setBeratCucuian(){
        setBeratCuci = (CardView) findViewById(R.id.menu2);
        setBeratCuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),BeratCucian.class);
                //bawah data dari class pesanan
                i.putExtra("hargaLaundry",String.valueOf(harga));
                i.putExtra("layanan",layanan);
                i.putExtra("waktu",waktu);
                i.putExtra("harga",harga);
                i.putExtra("email",getIntent().getStringExtra("email"));
                i.putExtra("imagee",Imgg);
                i.putExtra("id_user",idWong);
                i.putExtra("deskripsi",desc);
                i.putExtra("id_jasa",id_jasa);
                i.putExtra("hariJemput",getIntent().getStringExtra("hariJemput"));
                i.putExtra("alamatUserPick",getIntent().getStringExtra("alamatUserPick"));
                i.putExtra("alamatUserSend",getIntent().getStringExtra("alamatUserSend"));
                i.putExtra("hariKembali",getIntent().getStringExtra("hariKembali"));
                i.putExtra("waktuJemput",getIntent().getStringExtra("waktuJemput"));
                i.putExtra("waktuKembali",getIntent().getStringExtra("waktuKembali"));
                startActivity(i);
            }
        });

    }

    public void dataFromBerat(){
        setBeratCucian = (TextView) findViewById(R.id.beartCucian);
        String setBerat = getIntent().getStringExtra("berat");

        if (setBerat == null){
            setBeratCucian.setText("Atur Berat Cucian");
        } else {
            setBeratCucian.setText("Berat : "+setBerat + " Kg");
        }
    }

    public void dataFromAlamat()  {

        setWaktualamat = (TextView) findViewById(R.id.inpowaktu);
        String setAlamat = getIntent().getStringExtra("hariJemput");
        System.out.println("Jika Sendiri" + setAlamat);

        if (setAlamat == null){
            setWaktualamat.setText("Pilih waktu jemput dan pengiriman");
            System.out.println("kenek 2");
        } else if(setAlamat.equals("Antar Sendiri")){
            setWaktualamat.setText(setAlamat);
        }else{
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMMM yyyy");
            try {
                Date date = format1.parse(setAlamat);
                setWaktualamat.setText("Penjemputan : "+format2.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }
    public static String convertRupiah(int price){
        Locale locale = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String strFormat = format.format(price);
        return strFormat.replace(",00","");
    }
    public static String toRupiah(int rupiah){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(rupiah).replace(".00","");
    }
    public void setPesanan(){
        header = (TextView) findViewById(R.id.header);
        headerBawah = (TextView) findViewById(R.id.headerbawah);
        lamaWaktu = (TextView) findViewById(R.id.keterangan);
        hargaCucian = (TextView) findViewById(R.id.totalBerat);
        imgLayanan = (ImageView) findViewById(R.id.imgMsg);
        deskripsi = (TextView) findViewById(R.id.isiDeskripsi);

        layanan = getIntent().getStringExtra("layanan");
        waktu = getIntent().getStringExtra("waktu");
        harga = getIntent().getStringExtra("harga");
        Imgg = getIntent().getStringExtra("imagee");
        desc = getIntent().getStringExtra("deskripsi");
        idWong = getIntent().getStringExtra("id_user");
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        idWong = sharedPreferences.getString("KEY_ID","");
        id_jasa = getIntent().getStringExtra("id_jasa");


        deskripsi.setText(desc);
        header.setText(layanan);
        headerBawah.setText(layanan);
        lamaWaktu.setText(waktu + " waktu pengerjaan");
        hargaCucian.setText(convertRupiah(Integer.parseInt(harga)));
        Picasso.get().load(AppClient.URL_IMG+Imgg).resize(750,500).error(R.drawable.cuci_kering).into(imgLayanan);

    }
    public void setAlamat(){
        setAlamat = (CardView) findViewById(R.id.menu1);
        setAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),set_waktu_alamat.class);
                //bawah data dari class pesanan
                i.putExtra("layanan",layanan);
                i.putExtra("waktu",waktu);
                i.putExtra("harga",harga);
                i.putExtra("imagee",Imgg);
                i.putExtra("email",getIntent().getStringExtra("email"));
                i.putExtra("id_user",idWong);
                i.putExtra("deskripsi",desc);
                i.putExtra("id_jasa",id_jasa);
                i.putExtra("berat",getIntent().getStringExtra("berat"));
                startActivity(i);
            }
        });
    }
    public void toKembali(){
        toDashboard = (ImageButton) findViewById(R.id.kembali);
        toDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainMenu.class);
                i.putExtra("id_user",idWong);
                startActivity(i);
            }
        });
    }

    public void getPesanan(){
        gasPesanan = (Button) findViewById(R.id.buatPesanan) ;
        gasPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dayPick =getIntent().getStringExtra("hariJemput");
                String daySend = getIntent().getStringExtra("hariKembali");
                String timePick = getIntent().getStringExtra("waktuJemput");
                String timeSend = getIntent().getStringExtra("waktuKembali");
                String addressPick = getIntent().getStringExtra("alamatUserPick");
                String addressSend = getIntent().getStringExtra("alamatUserSend");
                String weight = getIntent().getStringExtra("berat");


                System.out.println("Jenis Pesanan = "+layanan + "" + getIntent().getStringExtra("berat")+" Kg");
                System.out.println("Penjemputan = " + getIntent().getStringExtra("hariJemput"));
                System.out.println("jam jemput = "+getIntent().getStringExtra("waktuJemput"));
                System.out.println("Pengiriman = "+getIntent().getStringExtra("hariKembali"));
                System.out.println("Jam pengiriman = "+getIntent().getStringExtra("waktuKembali"));
                System.out.println("Alamat = "+getIntent().getStringExtra("alamatUserPick"));
                System.out.println("Alamat = "+getIntent().getStringExtra("alamatUserSend"));
                System.out.println("harga Laundry = "+harga);
                System.out.println("total Berat = "+getIntent().getStringExtra("berat"));
                System.out.println("ID Orang nya ke Detail "+idWong);
                System.out.println("Id Layanan ke Detail"+id_jasa);

                if (dayPick == null  || weight ==null ){
//                    Toast.makeText(pesanan.this,"Isi Lengkap Terlebih dahulu",Toast.LENGTH_SHORT).show();
                    Alert_App.alertBro(pesanan.this,"Isi Lengkap Terlebih dahulu");

                } else {
                    Intent i = new Intent(getApplicationContext(),Detail_Pesanan.class);
                    i.putExtra("layanan",layanan);
                    i.putExtra("berat",weight);
                    i.putExtra("hariJemput",dayPick);
                    i.putExtra("waktuJemput",timePick);
                    i.putExtra("hariKembali",daySend);
                    i.putExtra("waktuKembali",timeSend);
                    i.putExtra("alamatUserPick",addressPick);
                    i.putExtra("alamatUserSend",addressSend);
                    i.putExtra("harga",harga);
                    i.putExtra("email",getIntent().getStringExtra("email"));
                    i.putExtra("id_user",idWong);
                    i.putExtra("id_jasa",id_jasa);
                    i.putExtra("deskripsi",desc);
                    i.putExtra("waktu",waktu);
                    i.putExtra("imagee",Imgg);
                    startActivity(i);
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