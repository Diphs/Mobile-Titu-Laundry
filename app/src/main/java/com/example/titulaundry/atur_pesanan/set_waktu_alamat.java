package com.example.titulaundry.atur_pesanan;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Adapter.AdapterAlamat;
import com.example.titulaundry.Login;
import com.example.titulaundry.Model.DataItemBanner;
import com.example.titulaundry.Model.ResponseAlamatLain;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.ModelMySQL.DataItemAlamatLain;
import com.example.titulaundry.R;
import com.example.titulaundry.layanan.Alert_App;
//import com.example.titulaundry.db_help.Database_Tb_user;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class set_waktu_alamat extends AppCompatActivity {
    RadioButton rBtn1 , rBtn2;
    ImageButton bckToPesanan;
    Button makePesanan,submit;
    Cursor cursor;
    DatePickerDialog picker;
//    public Database_Tb_user dbHelper;
    ConstraintLayout viewMenu;
    TextView tgl1 , tgl2,jam1 ,jam2,alamatDetailJemput,alamatDetailKirim,BtnAlamatJemput,BtnAlamatKirim,namaUser,namaKirim,jarak1,jarak2;
    AlertDialog dialog;
    int hour , minute;
    ApiInterface apiInterface;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    String waktuJemput , waktuPengembalian,hariJemput,hariKembali;

    //alamat
    RecyclerView recyclerView;
    AdapterAlamat adapterAlamat;
    List<DataItemAlamatLain> alamatLainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_waktu_alamat);

        notif(set_waktu_alamat.this);
        PilihTanggal();
        setUser();
        setBckToPesanan();
        checkedButton();
        PickerTime();
        setAlamat();
        bawahDataToPesanan();
    }

    static double distance(double lat1,double lon1 ){
        double theta = lon1 - 111.89372907334791;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(-7.603485181020061)) + Math.cos(deg2rad(lat1)) *
                Math.cos(deg2rad(-7.603485181020061))*  Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.515;
        dist = dist * 1.60934;
        dist = Math.round(dist);
        return (dist);
    }

    static double deg2rad(double deg){
        return (deg * Math.PI / 180.0);
    }

    static double rad2deg(double rad){
        return (rad * 180.0 / Math.PI);
    }

    public void setAlamat(){
        alamatDetailJemput = (TextView) findViewById(R.id.alamatDetail);
        alamatDetailKirim = (TextView) findViewById(R.id.alamatDetailKirim);
        jarak1 = (TextView) findViewById(R.id.jarak_jemput);
        jarak2 = (TextView) findViewById(R.id.jarak_kirim);
        namaUser = (TextView) findViewById(R.id.NamaUser);
        namaKirim = (TextView) findViewById(R.id.NamaUserKirim);
        apiInterface = AppClient.getClient().create(ApiInterface.class);
        String id_user = getIntent().getStringExtra("id_user");
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("KEY_ID","");
        Call<ResponseUser> getData = apiInterface.getDataUser(id_user);
        getData.enqueue(new Callback<ResponseUser>() {

            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                alamatDetailJemput.setText(response.body().getData().getAlamat());
                alamatDetailKirim.setText(response.body().getData().getAlamat());

                Geocoder geocoder = new Geocoder(set_waktu_alamat.this);
                List<Address> addresses;
                List<Address> addresses1;
                try {
                    addresses = geocoder.getFromLocationName(alamatDetailJemput.getText().toString(),1);
                    addresses1 = geocoder.getFromLocationName(alamatDetailKirim.getText().toString(),1);

                    if (addresses != null){

                        if (addresses.size() == 0 || addresses1.size() == 0){

                        } else {
                            double lat = addresses.get(0).getLatitude();
                            double lon = addresses.get(0).getLongitude();
                            double lat1 = addresses1.get(0).getLatitude();
                            double lon1 = addresses1.get(0).getLongitude();

                            double lokasi = distance(lat,lon);
                            double lokasi2 = distance(lat1,lon1);

                            if (lokasi>20){
                                jarak1.setBackground(ContextCompat.getDrawable(set_waktu_alamat.this,R.drawable.bunder_text_batal));
                                jarak1.setTextColor(Color.rgb(235,87,87));
                            } else {
                                jarak1.setBackground(ContextCompat.getDrawable(set_waktu_alamat.this,R.drawable.bunder_text_hijau));
                                jarak1.setTextColor(Color.rgb(33, 150, 83));
                            }
                            jarak1.setText(String.valueOf(lokasi)+" Km");

                            if (lokasi2>20){
                                jarak2.setBackground(ContextCompat.getDrawable(set_waktu_alamat.this,R.drawable.bunder_text_batal));
                                jarak2.setTextColor(Color.rgb(235,87,87));
                            } else {
                                jarak2.setBackground(ContextCompat.getDrawable(set_waktu_alamat.this,R.drawable.bunder_text_hijau));
                                jarak2.setTextColor(Color.rgb(33, 150, 83));
                            }

                            jarak2.setText(String.valueOf(lokasi2)+" Km");
                        }
                    } else {
//                        Toast.makeText(set_waktu_alamat.this, "Lokasi Tidak Terdeteksi", Toast.LENGTH_SHORT).show();
                        Alert_App.alertBro(set_waktu_alamat.this,"Lokasi Tidak Terdeteksi");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                namaUser.setText(response.body().getData().getNama());
                namaKirim.setText(response.body().getData().getNama());
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });

    }

    public void setUser(){

//        namaUser = (TextView) findViewById(R.id.NamaUser);
//        namaKirim = (TextView) findViewById(R.id.NamaUserKirim);
//        dbHelper = new Database_Tb_user(this);
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        cursor = db.rawQuery("SELECT nama FROM user WHERE email = '"+getIntent().getStringExtra("email")+"'",null);
//        cursor.moveToFirst();
//        if (cursor.getCount()>0){
//            cursor.moveToPosition(0);
//            namaUser.setText(cursor.getString(0));
//            namaKirim.setText(cursor.getString(0));
//        }
    }
    public void balek(){
        String layanan;
        String waktu;
        String harga;
        String berat;
        String gambar;
        String id;
        String desc;
        String id_jasa;

        layanan = getIntent().getStringExtra("layanan");
        waktu = getIntent().getStringExtra("waktu");
        harga = getIntent().getStringExtra("harga");
        berat = getIntent().getStringExtra("berat");
        gambar = getIntent().getStringExtra("imagee");
        id = getIntent().getStringExtra("id_user");
        desc = getIntent().getStringExtra("deskripsi");
        id_jasa = getIntent().getStringExtra("id_jasa");

        Intent i = new Intent(getApplicationContext(),pesanan.class);

            i.putExtra("layanan",layanan);
            i.putExtra("waktu",waktu);
            i.putExtra("harga",harga);
            i.putExtra("berat",berat);
            i.putExtra("email",getIntent().getStringExtra("email"));
            i.putExtra("imagee",gambar);
            i.putExtra("id_user",id);
            i.putExtra("deskripsi",desc);
            i.putExtra("id_jasa",id_jasa);

        startActivity(i);

//        System.out.println("jemput = "+hariJemput);
//        System.out.println("kembali = "+hariKembali);
//        System.out.println("waktu jemput = "+waktuJemput);
//        System.out.println("Waktu Kembali = "+waktuPengembalian);
//        System.out.println("Alamat Pick = "+alamatDetailJemput.getText().toString());
//        System.out.println("Alamat Send = "+alamatDetailKirim.getText().toString());

        //data dari class sebelumnya


    }

    public void IntentPesanan(){
        String layanan;
        String waktu;
        String harga;
        String berat;
        String gambar;
        String id;
        String desc;
        String id_jasa;

        layanan = getIntent().getStringExtra("layanan");
        waktu = getIntent().getStringExtra("waktu");
        harga = getIntent().getStringExtra("harga");
        berat = getIntent().getStringExtra("berat");
        gambar = getIntent().getStringExtra("imagee");
        id = getIntent().getStringExtra("id_user");
        desc = getIntent().getStringExtra("deskripsi");
        id_jasa = getIntent().getStringExtra("id_jasa");


        if (rBtn1.isChecked()) {
            Intent i = new Intent(getApplicationContext(),pesanan.class);
            i.putExtra("hariJemput","Antar Sendiri");
            i.putExtra("hariKembali","Antar Sendiri");
            i.putExtra("layanan",layanan);
            i.putExtra("waktu",waktu);
            i.putExtra("harga",harga);
            i.putExtra("berat",berat);
            i.putExtra("email",getIntent().getStringExtra("email"));
            i.putExtra("imagee",gambar);
            i.putExtra("id_user",id);
            i.putExtra("deskripsi",desc);
            i.putExtra("id_jasa",id_jasa);
            startActivity(i);
            finish();

        }else {
            System.out.println("Tes hari jemput = "+hariJemput);
            System.out.println("Tes hari jemput2 = "+hariKembali);
            System.out.println("Waktu Kerja Laundry"+waktu);

            String hariKerja = waktu.replace(" hari","").trim();
            long batasan = Long.parseLong(hariKerja);

            //Membuat batasan input hari
            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date1 = myFormat.parse(hariJemput);
                Date date2 = myFormat.parse(hariKembali);
                long diff = date2.getTime() - date1.getTime();
                long baru = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                if (batasan<=baru){
                    System.out.println("Boleh Transaksi");
                    Intent i = new Intent(getApplicationContext(),pesanan.class);
                    i.putExtra("hariJemput",hariJemput);
                    i.putExtra("hariKembali",hariKembali);
                    i.putExtra("waktuJemput",waktuJemput);
                    i.putExtra("waktuKembali",waktuPengembalian);
                    i.putExtra("alamatUserPick",alamatDetailJemput.getText().toString());
                    i.putExtra("alamatUserSend",alamatDetailKirim.getText().toString());
                    i.putExtra("layanan",layanan);
                    i.putExtra("waktu",waktu);
                    i.putExtra("harga",harga);
                    i.putExtra("berat",berat);
                    i.putExtra("email",getIntent().getStringExtra("email"));
                    i.putExtra("imagee",gambar);
                    i.putExtra("id_user",id);
                    i.putExtra("deskripsi",desc);
                    i.putExtra("id_jasa",id_jasa);
                    startActivity(i);
                    finish();
                }else {
                    Alert_App.alertBro(this,"Pengerjaan Laundry minimal "+batasan+" hari");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        startActivity(i);

//        System.out.println("jemput = "+hariJemput);
//        System.out.println("kembali = "+hariKembali);
//        System.out.println("waktu jemput = "+waktuJemput);
//        System.out.println("Waktu Kembali = "+waktuPengembalian);
//        System.out.println("Alamat Pick = "+alamatDetailJemput.getText().toString());
//        System.out.println("Alamat Send = "+alamatDetailKirim.getText().toString());

        //data dari class sebelumnya
    }
    private void showALertKirim() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.alert_edit_alamat,null);
        EditText alamat;
        ImageView clsBtn;
        TextView locNotFound , LocEth , alertNul;
        ImageButton showAlam;
        locNotFound = (TextView) view.findViewById(R.id.alertggl);
        LocEth = (TextView) view.findViewById(R.id.contoh);
        alamat = (EditText) view.findViewById(R.id.inputAlamat);
        showAlam = (ImageButton) view.findViewById(R.id.showAlamat);
        alertNul = (TextView) view.findViewById(R.id.alertnull);

        recyclerView = view.findViewById(R.id.recycleAlamat);
        AdapterAlamat.getAlamat alamat1 = new AdapterAlamat.getAlamat() {
            @Override
            public void alamatDipilih(String alamatUsernya) {
                alamat.setText(alamatUsernya);
            }
        };

        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseAlamatLain> alamatLainCall = apiInterface.getAlamatKirim("6");
        alamatLainCall.enqueue(new Callback<ResponseAlamatLain>() {
            @Override
            public void onResponse(Call<ResponseAlamatLain> call, Response<ResponseAlamatLain> response) {
                if (response.body().getKode() == 1){
                    alamatLainList = response.body().getData();
                    adapterAlamat = new AdapterAlamat(alamatLainList,getApplicationContext(),alamat1);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapterAlamat);
                    adapterAlamat.notifyDataSetChanged();
                } else {

                }

            }

            @Override
            public void onFailure(Call<ResponseAlamatLain> call, Throwable t) {

            }
        });
        showAlam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView.getVisibility() == GONE){
                    recyclerView.setVisibility(View.VISIBLE);

                    if (alamatLainList.size() == 0){
                        alertNul.setVisibility(View.VISIBLE);
                    } else {
                        alertNul.setVisibility(GONE);
                    }

                    showAlam.setImageDrawable(getResources().getDrawable(R.drawable.up));
                } else {
                    recyclerView.setVisibility(GONE);
                    alertNul.setVisibility(GONE);
                    showAlam.setImageDrawable(getResources().getDrawable(R.drawable.down));
                }
            }
        });


        clsBtn = (ImageView) view.findViewById(R.id.closeButton);
        clsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        submit = (Button) view.findViewById(R.id.submitAlamat);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(set_waktu_alamat.this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(alamat.getText().toString(),1);

                    if (addresses != null){
                        if (addresses.size() == 0){
                            locNotFound.setVisibility(view.VISIBLE);
                            LocEth.setVisibility(view.VISIBLE);
                        } else {
                            locNotFound.setVisibility(GONE);
                            LocEth.setVisibility(GONE);
                            double lat = addresses.get(0).getLatitude();
                            double lon = addresses.get(0).getLongitude();

                            System.out.println("Latitude : "+lat);
                            System.out.println("Langotitue : "+lon);
                            alamatDetailKirim.setText(alamat.getText().toString());
                            double lokasi = distance(lat,lon);
                            if (lokasi>20){
                                jarak2.setBackground(ContextCompat.getDrawable(set_waktu_alamat.this,R.drawable.bunder_text_batal));
                                jarak2.setTextColor(Color.rgb(235,87,87));
                            } else {
                                jarak2.setBackground(ContextCompat.getDrawable(set_waktu_alamat.this,R.drawable.bunder_text_hijau));
                                jarak2.setTextColor(Color.rgb(33, 150, 83));
                            }
                            jarak2.setText(String.valueOf(lokasi)+" Km");
                            dialog.dismiss();
                        }

                    } else {
                        Toast.makeText(set_waktu_alamat.this, "Lokasi Tidak Terdeteksi", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                alamatDetailKirim.setText(alamat.getText().toString());
//                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
    }
    public void showALertJemput(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.alert_edit_alamat,null);
        EditText alamat;
        ImageView clsBtn;
        TextView locNotFound , LocEth, alertNul;
        ImageButton showAlam;
        locNotFound = (TextView) view.findViewById(R.id.alertggl);
        LocEth = (TextView) view.findViewById(R.id.contoh);
        alamat = (EditText) view.findViewById(R.id.inputAlamat);
        showAlam = (ImageButton) view.findViewById(R.id.showAlamat);
        alertNul = (TextView) view.findViewById(R.id.alertnull);
        recyclerView = view.findViewById(R.id.recycleAlamat);


        AdapterAlamat.getAlamat alamat1 = new AdapterAlamat.getAlamat() {
            @Override
            public void alamatDipilih(String alamatUsernya) {
                alamat.setText(alamatUsernya);
            }
        };

        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseAlamatLain> alamatLainCall = apiInterface.getAlamatKirim("6");
        alamatLainCall.enqueue(new Callback<ResponseAlamatLain>() {
            @Override
            public void onResponse(Call<ResponseAlamatLain> call, Response<ResponseAlamatLain> response) {
                if (response.body().getKode() == 1){
                    alamatLainList = response.body().getData();
                    adapterAlamat = new AdapterAlamat(alamatLainList,getApplicationContext(),alamat1);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapterAlamat);
                    adapterAlamat.notifyDataSetChanged();
                } else {

                }

            }

            @Override
            public void onFailure(Call<ResponseAlamatLain> call, Throwable t) {

            }
        });
        showAlam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView.getVisibility() == GONE){
                    recyclerView.setVisibility(View.VISIBLE);

                    if (alamatLainList.size() == 0){
                        alertNul.setVisibility(View.VISIBLE);
                    } else {
                        alertNul.setVisibility(GONE);
                    }
                    showAlam.setImageDrawable(getResources().getDrawable(R.drawable.up));
                } else {
                    recyclerView.setVisibility(GONE);
                    alertNul.setVisibility(GONE);
                    showAlam.setImageDrawable(getResources().getDrawable(R.drawable.down));
                }
            }
        });
        clsBtn = (ImageView) view.findViewById(R.id.closeButton);
        clsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        submit = (Button) view.findViewById(R.id.submitAlamat);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(set_waktu_alamat.this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(alamat.getText().toString(),1);

                    if (addresses != null ){
                        if (addresses.size() == 0){
                            locNotFound.setVisibility(view.VISIBLE);
                            LocEth.setVisibility(view.VISIBLE);
                        } else {
                            locNotFound.setVisibility(GONE);
                            LocEth.setVisibility(GONE);
                            double lat = addresses.get(0).getLatitude();
                            double lon = addresses.get(0).getLongitude();

                            System.out.println("Latitude : "+lat);
                            System.out.println("Langotitue : "+lon);
                            alamatDetailJemput.setText(alamat.getText().toString());
                            double lokasi = distance(lat,lon);

                            if (lokasi>20){
                                jarak1.setBackground(ContextCompat.getDrawable(set_waktu_alamat.this,R.drawable.bunder_text_batal));
                                jarak1.setTextColor(Color.rgb(235,87,87));
                            } else {
                                jarak1.setBackground(ContextCompat.getDrawable(set_waktu_alamat.this,R.drawable.bunder_text_hijau));
                                jarak1.setTextColor(Color.rgb(33, 150, 83));
                            }
                            jarak1.setText(String.valueOf(lokasi)+" Km");
                            dialog.dismiss();
                        }

                    } else {
                        Toast.makeText(set_waktu_alamat.this, "Lokasi Tidak Terdeteksi", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                alamatDetailJemput.setText(alamat.getText().toString());
//                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();

    }
    @Override
    public void onBackPressed() {
        balek();
    }
    public void setBckToPesanan(){
        bckToPesanan = (ImageButton) findViewById(R.id.kembali);
        bckToPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                balek();
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

    public String getMonth(int month) {

        return new DateFormatSymbols().getMonths()[month-1];
    }

    public void PilihTanggal(){
        tgl1 = (TextView) findViewById(R.id.TanggalJemput1);
        tgl2 = (TextView) findViewById(R.id.TanggalJemput2);

        final Calendar cldr = Calendar.getInstance();
        final Calendar cldr2 = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-M-yyyy");


        tgl1.setText(simpleDateFormat.format(cldr.getTime()));
        hariJemput = simpleDateFormat2.format(cldr.getTime());

        //get durasi
        String waktu = getIntent().getStringExtra("waktu");
        String hariKerja = waktu.replace(" hari","").trim();
        int batasan = Integer.parseInt(hariKerja);

        cldr2.add(Calendar.DATE,batasan);

        tgl2.setText(simpleDateFormat.format(cldr2.getTime()));
        hariKembali= simpleDateFormat2.format(cldr2.getTime());
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        tgl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(set_waktu_alamat.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month ,int day) {
                        String hariIni = simpleDateFormat2.format(cldr.getTime());
                        String pilihan = String.valueOf(day+"-"+(month+1)+"-"+year);

                        try {
                            Date date1 = simpleDateFormat2.parse(hariIni);
                            Date date2 = simpleDateFormat2.parse(pilihan);

                            if (date2.after(date1) || date2.equals(date1)){

                                tgl1.setText(String.valueOf(day) + " " + getMonth((month+1)) + " " + String.valueOf(year));
                                hariJemput = pilihan;

                            } else {
                                Alert_App.alertBro(set_waktu_alamat.this,"Pilih tanggal hari ini / setelahnya");
                            }
                        } catch (Exception e){
                            throw new RuntimeException(e);
                        }

                    }
                },year,month,day);
                picker.show();
            }
        });
        int day2 = cldr2.get(Calendar.DAY_OF_MONTH);
        int month2 = cldr2.get(Calendar.MONTH);
        int year2 = cldr2.get(Calendar.YEAR);
        tgl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(set_waktu_alamat.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month ,int day) {


                        tgl2.setText(String.valueOf(day) + " " + getMonth((month+1)) + " " + String.valueOf(year));
                        hariKembali = String.valueOf(day+"-"+(month+1)+"-"+year);
                    }
                },year2,month2,day2);
                picker.show();
            }
        });
    }

    public void checkedButton(){
        rBtn1 = (RadioButton) findViewById(R.id.radioButton1);
        rBtn2 = (RadioButton) findViewById(R.id.radioButton2);
        makePesanan = (Button)findViewById(R.id.buatPesanan);

        viewMenu = (ConstraintLayout) findViewById(R.id.kotakMenu);
        rBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rBtn1.isChecked()){
                    rBtn2.setChecked(false);
                    rBtn1.setChecked(true);
                    makePesanan.setVisibility(view.VISIBLE);
                    viewMenu.setVisibility(view.INVISIBLE);
//                    jam1.setText("07:00 WIB");
//                    jam2.setText("07:00 WIB");
                }
            }
        });

        rBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rBtn1.setChecked(false);
                rBtn2.setChecked(true);
                makePesanan.setVisibility(view.VISIBLE);
                viewMenu.setVisibility(view.VISIBLE);
                jam2.setText("07:00 WIB");
                jam1.setText("07:00 WIB");
            }
        });
    }

    public void setInterval(TimePickerDialog timePicker){
        try {
            NumberPicker minutePicker = timePicker.findViewById(Resources.getSystem().getIdentifier("minute","id","android"));
            String[] display = new String[]{"0","30"};

            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(display.length-1);
            minutePicker.setDisplayedValues(display);

        }catch (Exception e){
            Log.d("Error : ",e.getMessage());
        }
    }

    public void PickerTime() {
        jam1 = (TextView) findViewById(R.id.jam1);
        jam2 = (TextView) findViewById(R.id.jam2);
        //waktu jemput
        jam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute = i1;
                        System.out.println(String.valueOf(hour));
                        if (rBtn1.isChecked()){
                            if (hour>21 || hour<=7){
//                                Toast.makeText(set_waktu_alamat.this,"Gaboleh",Toast.LENGTH_LONG).show();
                                Alert_App.alertBro(set_waktu_alamat.this,"Melebihi Jam penjemputan Kurir");
                            } else {
                                jam1.setText(String.format(Locale.getDefault(),"%02d : %02d WIB",hour,minute));
                                waktuJemput = String.format(Locale.getDefault(),"%02d:%02d",hour,minute);

                            }
                        } else if (rBtn2.isChecked()){
                            if (hour>15 || hour<7){
//                                Toast.makeText(set_waktu_alamat.this,"Gaboleh",Toast.LENGTH_LONG).show();
                                Alert_App.alertBro(set_waktu_alamat.this,"Melebihi Jam penjemputan Kurir");
                            } else {
                                jam1.setText(String.format(Locale.getDefault(),"%02d : %02d WIB",hour,minute));
                                waktuJemput = String.format(Locale.getDefault(),"%02d:%02d",hour,minute);
                            }
                        } else {
                            Toast.makeText(set_waktu_alamat.this,"Mohon checklist dulu",Toast.LENGTH_LONG).show();
                        }
                    }
                };
                int style = AlertDialog.THEME_HOLO_LIGHT;
                TimePickerDialog timePickerDialog = new TimePickerDialog(set_waktu_alamat.this,style,onTimeSetListener,hour,minute,true);
                setInterval(timePickerDialog);
                timePickerDialog.show();
            }
        });
        //waktu pengembalian
        jam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute = i1;
                        if (rBtn1.isChecked()){
                            if (hour>21 || hour<7){
                                Toast.makeText(set_waktu_alamat.this,"Gaboleh",Toast.LENGTH_LONG).show();
                            } else {
                                jam2.setText(String.format(Locale.getDefault(),"%02d : %02d WIB",hour,minute));
                                waktuPengembalian = String.format(Locale.getDefault(),"%02d:%02d",hour,minute);
                            }
                        } else if (rBtn2.isChecked()){
                            if (hour>15 || hour<7){
//                                Toast.makeText(set_waktu_alamat.this,"Gaboleh",Toast.LENGTH_LONG).show();
                                Alert_App.alertBro(set_waktu_alamat.this,"Melebihi Jam pengantaran Kurir");
                            } else {
                                jam2.setText(String.format(Locale.getDefault(),"%02d : %02d WIB",hour,minute));
                                waktuPengembalian = String.format(Locale.getDefault(),"%02d:%02d",hour,minute);
                            }
                        } else {
                            Toast.makeText(set_waktu_alamat.this,"Mohon checklist dulu",Toast.LENGTH_LONG).show();
                        }
                    }
                };
                int style = AlertDialog.THEME_HOLO_LIGHT;
                TimePickerDialog timePickerDialog = new TimePickerDialog(set_waktu_alamat.this,style,onTimeSetListener,hour,minute,true);
                timePickerDialog.show();
            }
        });
    }

    public void bawahDataToPesanan(){
        makePesanan = (Button) findViewById(R.id.buatPesanan);
        makePesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rBtn2.isChecked()){
                    //membawah data dari pesanan dan berat
                    if (waktuJemput == null ){
//                        Toast.makeText(set_waktu_alamat.this,"Harap pilih jam Penjemputan",Toast.LENGTH_SHORT).show();
                        Alert_App.alertBro(set_waktu_alamat.this,"Harap pilih jam penjemputan");
                    } else if (waktuPengembalian == null){
//                        Toast.makeText(set_waktu_alamat.this,"Harap pilih jam pengiriman",Toast.LENGTH_SHORT).show();
                        Alert_App.alertBro(set_waktu_alamat.this,"Harap pilih jam pengiriman");
                    }else {
                        //Pembatasan Jarak Kirim
                        String j = jarak1.getText().toString().replace(" Km","");
                        String k = jarak2.getText().toString().replace(" Km","");

                        double jrk2 = Double.parseDouble(k);
                        double jrk = Double.parseDouble(j);

                        if (jrk<20 && jrk2<20){
//                          Intent i = new Intent(getApplicationContext(),pesanan.class);
                            IntentPesanan();

                        } else {

//                            Toast.makeText(set_waktu_alamat.this, "Terlalu Jauh Kasian Kurir", Toast.LENGTH_SHORT).show();
                            Alert_App.alertBro(set_waktu_alamat.this,"Mohon Maaf , Lokasi terlalu jauh tidak bisa menggunakan jasa kurir");
                        }

                    }
                } else {
                        //Antar Sendiri
//                        Intent i = new Intent(getApplicationContext(),pesanan.class);
                        IntentPesanan();
                        finish();
                }


            }
        });
    }

    public void funcEdit(View view) {
        BtnAlamatJemput = (TextView) findViewById(R.id.getAlamatJemput);
        BtnAlamatJemput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showALertJemput();
                dialog.show();
            }
        });
    }

    public void funcEditKirim(View view) {
        BtnAlamatKirim = (TextView) findViewById(R.id.getAlamatKirim);
        BtnAlamatKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showALertKirim();
                dialog.show();
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