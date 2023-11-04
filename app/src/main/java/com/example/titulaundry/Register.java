package com.example.titulaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Model.ResponseRegister;
import com.example.titulaundry.layanan.Alert_App;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    TextView takonAkun,bckToLogin;
    EditText txNama , txTlp,txEmail,txPw,matchPw;
    Button btnRegist;
    CheckBox syrt;
    ApiInterface apiInterface;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        notif(Register.this);
        setTectxtColour();
        RegistAccount();
        setCheckBox();
        kembaliKeLogin();
    }

    public void setCheckBox(){
        syrt = (CheckBox) findViewById(R.id.syarat);
        String text = "Dengan mendaftar, kamu menyetujui <b><font color=#2f80ed>Syarat dan Ketentuan</font></b> dan <b><font color=#2f80ed>Kebijakan Privasi</font></b> kami";
        syrt.setText(Html.fromHtml(text));
        syrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (syrt.isChecked()){
                    syrt.setBackgroundTintList(getResources().getColorStateList(R.color.niceBlue));

                }
            }
        });



    }

    public void setTectxtColour(){
        takonAkun = (TextView) findViewById(R.id.takonAkun);
        String text = "<font color=#333333>Sudah Punya Akun?</font> <font color=#2f80ed> Masuk</font>";
        takonAkun.setText(Html.fromHtml(text));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),Login.class);
        startActivity(i);
        finish();
    }

    public void kembaliKeLogin(){
        bckToLogin = (TextView) findViewById(R.id.takonAkun);
        bckToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
    }
    public void RegistAccount(){
        txNama = (EditText) findViewById(R.id.inputNama);
        txTlp = (EditText) findViewById(R.id.txTlp);
        txEmail = (EditText) findViewById(R.id.txEmail);
        txPw = (EditText) findViewById(R.id.txPw);
        matchPw = (EditText) findViewById(R.id.matchPw);
        btnRegist = (Button)findViewById(R.id.signUP);
        syrt = (CheckBox) findViewById(R.id.syarat);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNama = txNama.getText().toString().trim();
                String getTelp = txTlp.getText().toString().trim();
                String getEmail = txEmail.getText().toString().trim();
                String getPw = txPw.getText().toString().trim();
                String matPw = matchPw.getText().toString().trim();

                int min = 1;
                int max = 10000;

                //Generate random int value from 50 to 100
                System.out.println("Random value in int from "+min+" to "+max+ ":");
                int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
                System.out.println(random_int);
                String numberRandom = String.valueOf(random_int);

                String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
                Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

                if (!getPw.equals(matPw)){
//                    Toast.makeText(Register.this,"Password Tidak Sama",Toast.LENGTH_LONG).show();
                    Alert_App.alertBro(Register.this,"Password Tidak Sama");
                }else if (getPw.length()<7 || !specailCharPatten.matcher(getPw).find()){
//                    Toast.makeText(Register.this, "Password kurang dari 8 dan harus mengandung karakter spesial", Toast.LENGTH_SHORT).show();
                    Alert_App.alertBro(Register.this,"Password kurang dari 8 dan harus mengandung karakter spesial");
                }else if  (getNama.equals("")||getTelp.equals("")||getEmail.equals("")||getPw.equals("")||matPw.equals("")||!syrt.isChecked()){
//                    Toast.makeText(Register.this,"Harap Lengkapi",Toast.LENGTH_LONG).show();
                    Alert_App.alertBro(Register.this,"Harap Lengkapi");
                } else if (!getEmail.matches(emailPattern)||!getEmail.contains("@gmail.com")){
//                    Toast.makeText(Register.this,"Email tidak valid",Toast.LENGTH_LONG).show();
                    Alert_App.alertBro(Register.this,"Email tidak valid");
                } else if( getTelp.length()<10){
                    Alert_App.alertBro(Register.this,"Panjang Nomer telephone kurang");
                }else{
                    apiInterface = AppClient.getClient().create(ApiInterface.class);
                    Call<ResponseRegister> simpan = apiInterface.registerResponse(getNama,getTelp,getEmail,getPw);
                    simpan.enqueue(new Callback<ResponseRegister>() {
                        @Override
                        public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                            int kode = response.body().getKode();
                            if (kode == 1){
                                Toast.makeText(Register.this,"Berhasil Daftar",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(),Konfirmasi.class);
                                String id = String.valueOf(response.body().getData().getIdUser());
                                System.out.println("ID USERNYA PADA REGIST "+id);
                                i.putExtra("Userid",id);
                                i.putExtra("EmailUser",getEmail);
                                startActivity(i);
                            } else if(kode == 3){
//                                Toast.makeText(Register.this,"Email Already Exist",Toast.LENGTH_SHORT).show();
                                Alert_App.alertBro(Register.this,"Email Already Exist");
                            }else {
//                                Toast.makeText(Register.this,"Gagal Daftar",Toast.LENGTH_SHORT).show();
                                Alert_App.alertBro(Register.this,"Gagal Daftar");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseRegister> call, Throwable t) {

                        }
                    });


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