package com.example.titulaundry.Dashboard;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.example.titulaundry.MapsActivity;
import com.example.titulaundry.Model.ResponseAlamat;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.R;
import com.example.titulaundry.databinding.ActivityRubahAlamat2Binding;
import com.example.titulaundry.layanan.Alert_App;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Change_Address extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityRubahAlamat2Binding binding;
    private boolean oke = false;
    EditText Inputalamat,alamat;
    TextView LocNow,Head,HeadNow,HeadBaruw;
    Button carii,simpan;
    ImageButton bcktoSet;
    List<Address> addressList = null;
    String id_user,alamatGet;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRubahAlamat2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Inputalamat = findViewById(R.id.cariAlamat);
        carii = findViewById(R.id.cari);
        alamat = findViewById(R.id.setAlamat);
        LocNow = findViewById(R.id.alamatSekarang);
        simpan = findViewById(R.id.simpan);
        Head = findViewById(R.id.header);
        HeadBaruw = findViewById(R.id.HeadAlamatBaru);
        HeadNow = findViewById(R.id.HeaderAlamatSaatIni);

        bcktoSet = findViewById(R.id.kembali);
        bcktoSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Tampung_Alamat.class);
                startActivity(i);
                finish();
            }
        });
        notif(Change_Address.this);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, new LocationListener() {


            @Override
            public void onLocationChanged(@NonNull Location location) {
//                try {
//                    addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                    if (addressList != null){
//                        Address returnAdd = addressList.get(0);
//                        StringBuilder stringBuilder = new StringBuilder("");
//                        for (int i = 0; i<returnAdd.getMaxAddressLineIndex(); i++){
//                            stringBuilder.append(returnAdd.getAddressLine(i)).append("\n");
//                        }
//                        Log.w("my Location",stringBuilder.toString());
//                    } else {
//                        Log.w("my Location","No Address");
//                    }
//                } catch (Exception e){
//                    System.out.println(e.getMessage());
//                }
                if (oke) {
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng latLng) {
                            try {
                                addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                String addressLine = addressList.get(0).getAddressLine(0);
                                String p = "";
                                if (addressLine.contains("+")){
                                    p = addressLine.substring(9).trim();
                                    System.out.println("Hilangi =" + p);
                                } else {
                                    p = addressLine;
                                }
                                LatLng Posisi = new LatLng(latLng.latitude, latLng.longitude);
                                mMap.addMarker(new MarkerOptions().position(Posisi).title("Posisi"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(Posisi));
                                alamat.setText(p);
                                alamatGet = p;
                                float zoomLevel = 16.0f;
                                mMap.clear();
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                                mMap.addMarker(markerOptions);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                }
                carii.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String location = Inputalamat.getText().toString();
                        if (location == null) {
                            Toast.makeText(Change_Address.this, "turu", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                addressList = geocoder.getFromLocationName(location, 1);
                                if (addressList.size()==0){
                                    Toast.makeText(Change_Address.this, "Alamat Tidak ada", Toast.LENGTH_SHORT).show();
                                } else {
                                    LatLng Posisi = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(Posisi);
                                    String addressLine = addressList.get(0).getAddressLine(0);

                                    mMap.addMarker(new MarkerOptions().position(Posisi).title("Posisi"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Posisi));
                                    alamat.setText(addressLine);
                                    alamatGet = addressLine;

                                    mMap.clear();
                                    float zoomLevel = 16.0f;
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Posisi, zoomLevel));
                                    mMap.addMarker(markerOptions);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }

        });
//        confrimAlamat();
        UbahAlamat();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        oke = true;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-7.602617404056041, 111.90098881751203);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Nganjuk"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        float zoomLevel = 16.0f;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void setUserAddress(){
        id_user = getIntent().getStringExtra("id_user");
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("KEY_ID","");
        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseUser> userCall = apiInterface.getDataUser(id_user);
        userCall.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                LocNow.setText(response.body().getData().getAlamat());
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });
    }
    public void notif(Activity activity) {
        //change color notif bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        //set icons notifbar
        View decor = activity.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    public void confrimAlamat(){
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alamat.getText().toString().equals("")){
                    apiInterface = AppClient.getClient().create(ApiInterface.class);
                    Call<ResponseAlamat> alamatCall = apiInterface.setAlamat(id_user,alamat.getText().toString());
                    alamatCall.enqueue(new Callback<ResponseAlamat>() {
                        @Override
                        public void onResponse(Call<ResponseAlamat> call, Response<ResponseAlamat> response) {
                            if (response.body().getKode() == 1){
//                            Toast.makeText(RubahAlamat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Alert_App.alertBro(Change_Address.this,response.body().getMessage());
                                LocNow.setText(alamat.getText().toString());
                                alamat.setText("");
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseAlamat> call, Throwable t) {

                        }
                    });

                } else {
                    Alert_App.alertBro(Change_Address.this,"Alamat yang dimasukkan kosong");
                }
            }
        });
    }

    public void UbahAlamat(){
        String aksi = getIntent().getStringExtra("aksi");
        switch (aksi){
            case "editAlamat":
                System.out.println("Mengedit alamat utama");
                String almtSaiki = getIntent().getStringExtra("alamatTerbawah");
                LocNow.setText(almtSaiki);
                String id_alamatt = getIntent().getStringExtra("idAlamat");

                simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!alamat.getText().toString().equals("")){
                            apiInterface = AppClient.getClient().create(ApiInterface.class);
                            Call<ResponseAlamat> alamatCall = apiInterface.EditAlamatUtama(id_alamatt,alamat.getText().toString());
                            alamatCall.enqueue(new Callback<ResponseAlamat>() {
                                @Override
                                public void onResponse(Call<ResponseAlamat> call, Response<ResponseAlamat> response) {
                                    if (response.body().getKode() == 1){
//                            Toast.makeText(RubahAlamat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        Alert_App.alertBro(Change_Address.this,response.body().getMessage());
                                        LocNow.setText(alamat.getText().toString());
                                        alamat.setText("");
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseAlamat> call, Throwable t) {

                                }
                            });

                        } else {
                            Alert_App.alertBro(Change_Address.this,"Alamat yang dimasukkan kosong");
                        }
                    }
                });
                break;

            case "tambahAlamat":
                System.out.println("Menambahkan alamat");
                //setup Layout
                LocNow.setVisibility(View.GONE);
                Head.setText("Tambah Alamat");
                HeadNow.setVisibility(View.GONE);
                HeadBaruw.setText("Alamat dipilih");
                simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!alamat.getText().toString().equals("")){

                            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                            String id_user = sharedPreferences.getString("KEY_ID","");
                            apiInterface = AppClient.getClient().create(ApiInterface.class);
                            Call<ResponseAlamat> call = apiInterface.TambahAlamat(id_user,alamat.getText().toString());
                            call.enqueue(new Callback<ResponseAlamat>() {
                                @Override
                                public void onResponse(Call<ResponseAlamat> call, Response<ResponseAlamat> response) {
                                    System.out.println(response.body().getMessage());
                                    Intent i = new Intent(getApplicationContext() , Tampung_Alamat.class);
                                    startActivity(i);
                                    finish();

                                }

                                @Override
                                public void onFailure(Call<ResponseAlamat> call, Throwable t) {

                                }
                            });
                        } else {
                            Alert_App.alertBro(Change_Address.this,"Alamat yang dimasukkan kosong");
                        }
                    }
                });
                break;
        }
    }
    @Override
    public void onBackPressed() {
//        Intent i = new Intent(getApplicationContext(), Login.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//
//        finish();
        Intent i = new Intent(getApplicationContext(), Tampung_Alamat.class);
        startActivity(i);
        finish();
    }
}