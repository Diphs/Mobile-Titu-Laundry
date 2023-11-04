package com.example.titulaundry.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.titulaundry.LupaPassword;
import com.example.titulaundry.Model.ResponseRubahPw;
import com.example.titulaundry.R;
import com.example.titulaundry.layanan.Alert_App;
import com.example.titulaundry.lupaPassword3;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ubah_password extends AppCompatActivity {

    EditText passwordLama , passwordBaru , confirmPassword;

    Button submitPassword;
    ApiInterface apiInterface;
    ImageButton bck;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);
        notif(ubah_password.this);
        rubahPassword();
        backMainMenu();
    }

    private void backMainMenu() {
        bck = findViewById(R.id.kembali);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),MainMenu.class);
//                i.putExtra("id_user",getIntent().getStringExtra("id_user"));
//                startActivity(i);

                onBackPressed();
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

    public void rubahPassword(){
        passwordLama = (EditText) findViewById(R.id.passwordLama);
        passwordBaru = (EditText) findViewById(R.id.passwordBaru);
        confirmPassword = (EditText) findViewById(R.id.confrimPassword);
        submitPassword = (Button) findViewById(R.id.simpanPerubahan);

        submitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordBaru.getText().toString().equals(confirmPassword.getText().toString())){

                    Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                    if (passwordBaru.getText().toString().length()<7 || !specailCharPatten.matcher(passwordBaru.getText().toString()).find()){
//                        Toast.makeText(ubah_password.this, "Password kurang dari 8 dan harus mengandung karakter spesial", Toast.LENGTH_SHORT).show();
                        Alert_App.alertBro(ubah_password.this,"Password kurang dari 8 dan harus mengandung karakter spesial");
                    } else {
                        apiInterface = AppClient.getClient().create(ApiInterface.class);
                        Call<ResponseRubahPw> pwCall = apiInterface.UbahPw(getIntent().getStringExtra("id_user"),passwordLama.getText().toString(),passwordBaru.getText().toString());
                        pwCall.enqueue(new Callback<ResponseRubahPw>() {
                            @Override
                            public void onResponse(Call<ResponseRubahPw> call, Response<ResponseRubahPw> response) {

                                if (response.body().getKode() == 1){
//                                    Toast.makeText(ubah_password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Alert_App.alertBro(ubah_password.this,response.body().getMessage());
                                } else if (response.body().getKode() == 2){
//                                    Toast.makeText(ubah_password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Alert_App.alertBro(ubah_password.this,response.body().getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseRubahPw> call, Throwable t) {
                                Toast.makeText(ubah_password.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
//                    Toast.makeText(ubah_password.this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                    Alert_App.alertBro(ubah_password.this,"Password Tidak Sama");
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