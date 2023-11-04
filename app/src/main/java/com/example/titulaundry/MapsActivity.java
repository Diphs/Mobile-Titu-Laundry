package com.example.titulaundry;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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
import com.example.titulaundry.Model.ResponseAlamat;
import com.example.titulaundry.layanan.Alert_App;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.titulaundry.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private boolean oke = false;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TextView alamat;
    EditText inputAlamat;
    ApiInterface apiInterface;
    ImageButton searchAddress , kembali;
    Button submit;
    String id_user,alamatGet;
    List<Address> addressList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        alamat = findViewById(R.id.setAlamat);
        inputAlamat = findViewById(R.id.getAlamatUser);
        searchAddress = findViewById(R.id.searchAddress);
        kembali = findViewById(R.id.kembali);


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        notif(MapsActivity.this);
        setKembali();

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
                searchAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String location = inputAlamat.getText().toString();
                        if (location == null) {
                            Toast.makeText(MapsActivity.this, "turu", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                addressList = geocoder.getFromLocationName(location, 1);
                                if (addressList.size()==0){
                                    Toast.makeText(MapsActivity.this, "Alamat Tidak ada", Toast.LENGTH_SHORT).show();
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
                confirmAlamat();
            }

        });
        //kene

    }

    @Override
    public void onBackPressed() {
        String idd = getIntent().getStringExtra("UserId");
        Intent i = new Intent(getApplicationContext(),KonfirmasiSukses.class);
        i.putExtra("UserId",idd);
        startActivity(i);
        finish();
    }

    public void setKembali(){
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idd = getIntent().getStringExtra("UserId");
                Intent i = new Intent(getApplicationContext(),KonfirmasiSukses.class);
                i.putExtra("UserId",idd);
                startActivity(i);
                finish();
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

    public void confirmAlamat(){
        id_user = getIntent().getStringExtra("UserId");
        String alamatUser = alamat.getText().toString();
        submit = findViewById(R.id.confirmAddress);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("alamatnya adalah = "+alamatUser);
                System.out.println("ID NYA ADALAH = "+id_user);

                apiInterface = AppClient.getClient().create(ApiInterface.class);
                Call<ResponseAlamat> Alamat = apiInterface.setAlamat(id_user,alamatGet);
                Alamat.enqueue(new Callback<ResponseAlamat>() {
                    @Override
                    public void onResponse(Call<ResponseAlamat> call, Response<ResponseAlamat> response) {
                        int kode = response.body().getKode();
                        if (kode == 1){
                            Toast.makeText(MapsActivity.this, "Alamat Terupdate", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),all_is_set.class);
                            startActivity(i);
                            finish();
                        } else {
//                            Toast.makeText(MapsActivity.this, "gagal Terupdate", Toast.LENGTH_SHORT).show();
                            Alert_App.alertBro(MapsActivity.this,"Alamat gagal diupdate!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseAlamat> call, Throwable t) {
                        Toast.makeText(MapsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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