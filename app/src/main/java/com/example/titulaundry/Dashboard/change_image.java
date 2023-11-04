package com.example.titulaundry.Dashboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
//import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Adapter.RealPathUtil;
import com.example.titulaundry.Model.ResponseEditUser;
import com.example.titulaundry.Model.ResponseHapusFoto;
import com.example.titulaundry.Model.ResponseImg;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.R;
import com.example.titulaundry.Register;
import com.example.titulaundry.layanan.Alert_App;
import com.github.drjacky.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class change_image extends AppCompatActivity {

    Button OpenGallery,savePic,DeletePic;
    ImageButton kembali;
    EditText email , nama , phone;
    CircleImageView imgg;
    String path = "";
    ImageView circleImageView;
    ApiInterface apiInterface;
    final int  REQUEST_GALLERY = 9544;
    AlertDialog dialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);
        setOpenGallery();
        setData();
        UploadImage();
        delimage();
        kembaliLagi();
        notif(change_image.this);
    }

    public void kembaliLagi(){
        kembali = (ImageButton) findViewById(R.id.kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
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

    public void delimage(){
        DeletePic = (Button) findViewById(R.id.HapusFoto);
        DeletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
                dialog.show();

            }
        });
    }

    public void confirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(change_image.this);
        View view = getLayoutInflater().inflate(R.layout.ubah_foto,null);
        builder.setView(view);
        dialog = builder.create();
        Button delete , cancel;

        //DELETE
        delete = view.findViewById(R.id.ubah2);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiInterface = AppClient.getClient().create(ApiInterface.class);
                Call<ResponseHapusFoto> hapusFotoCall = apiInterface.hapusFoto(getIntent().getStringExtra("id_user"));
                hapusFotoCall.enqueue(new Callback<ResponseHapusFoto>() {
                    @Override
                    public void onResponse(Call<ResponseHapusFoto> call, Response<ResponseHapusFoto> response) {
                        if (response.body().getKode() == 1){
                            Toast.makeText(change_image.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            imgg.setImageResource(R.drawable.blank);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseHapusFoto> call, Throwable t) {

                    }
                });
            }
        });
        //Cancel
        cancel = view.findViewById(R.id.batalBro);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void setData(){
        email = (EditText) findViewById(R.id.editEmail);
        nama = (EditText) findViewById(R.id.editNama);
        phone = (EditText) findViewById(R.id.editTelp);
        imgg = (CircleImageView) findViewById(R.id.imgProfile);

        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseUser> userCall = apiInterface.getDataUser(getIntent().getStringExtra("id_user"));
        userCall.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                email.setText(response.body().getData().getEmail());
                nama.setText(response.body().getData().getNama());
                phone.setText(response.body().getData().getNoTelpon());
                Picasso.get().load(AppClient.profileIMG+response.body().getData().getProfile_img()).error(R.drawable.blank).into(imgg);
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });

    }
    public void UploadImage(){
        savePic = (Button) findViewById(R.id.simpanPerubahan);
        savePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path.equals("")){
                    System.out.println("PATH KOSONGGG");
                    if (nama.getText().toString().equals("")||email.getText().toString().equals("") ||phone.getText().toString().equals("")){
                        Alert_App.alertBro(change_image.this,"Data Tidak Boleh Kosong!");

                    } else {
                        if (phone.getText().toString().length()<10){
                            Alert_App.alertBro(change_image.this,"Panjang Nomer telephone kurang");
                        } else {

                            apiInterface = AppClient.getClient().create(ApiInterface.class);
                            Call<ResponseEditUser> userCall = apiInterface.getUpdateDataUser(getIntent().getStringExtra("id_user"),nama.getText().toString(),email.getText().toString(),phone.getText().toString());
                            userCall.enqueue(new Callback<ResponseEditUser>() {
                                @Override
                                public void onResponse(Call<ResponseEditUser> call, Response<ResponseEditUser> response) {
                                    if (response.body().getKode() == 1){
                                        Toast.makeText(change_image.this, "DATA SAJA BERHASIL UPDATE", Toast.LENGTH_SHORT).show();
                                    } else {
                                        System.out.println(response.body().getKode());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseEditUser> call, Throwable t) {

                                }
                            });
                        }
                    }

                } else {
                    File file = new File(path);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("imageupload", file.getName(), requestFile);
                    RequestBody cus_name = RequestBody.create(MediaType.parse("multipart/form-data"),getIntent().getStringExtra("id_user"));
                    RequestBody namaUSer = RequestBody.create(MediaType.parse("multipart/form-data"),nama.getText().toString());
                    RequestBody emailUser = RequestBody.create(MediaType.parse("multipart/form-data"),email.getText().toString());
                    RequestBody telpUSer = RequestBody.create(MediaType.parse("multipart/form-data"),phone.getText().toString());
                    if (nama.getText().toString().equals("")||email.getText().toString().equals("") ||phone.getText().toString().equals("")){
                        Alert_App.alertBro(change_image.this,"Data Tidak Boleh Kosong!");

                    } else {
                        if (phone.getText().toString().length()<10){
                            Alert_App.alertBro(change_image.this,"Panjang Nomer telephone kurang");
                        } else {
                            apiInterface = AppClient.getClient().create(ApiInterface.class);
                            Call<ResponseImg> imgCall = apiInterface.uploadImage(body,cus_name,namaUSer,emailUser,telpUSer);
                            imgCall.enqueue(new Callback<ResponseImg>() {
                                @Override
                                public void onResponse(Call<ResponseImg> call, Response<ResponseImg> response) {
                                    if (response.body().getKode() == 1){
                                        Toast.makeText(change_image.this, "Berhasil Upload", Toast.LENGTH_SHORT).show();
                                    } else {
                                        System.out.println(response.body().getKode());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseImg> call, Throwable t) {
                                    Toast.makeText(change_image.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    System.out.println(t.getMessage());
                                }
                            });
                        }

                    }

                }


            }
        });
    }
    public void setOpenGallery(){
        circleImageView = (ImageView) findViewById(R.id.imgProfile);
        OpenGallery = (Button) findViewById(R.id.ubah);
        OpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(getApplicationContext(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Open Gallery"),REQUEST_GALLERY);
//                    Toast.makeText(change_image.this, "Open Galleerrtyy", Toast.LENGTH_SHORT).show();
//                } else {
//                    ActivityCompat.requestPermissions(change_image.this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//                    Toast.makeText(change_image.this, "gagal", Toast.LENGTH_SHORT).show();
//                }
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 10);
                ImagePicker.Companion.with(change_image.this)
                        .galleryOnly()
                        .crop()
                        .cropOval()
                        .compress(1024)
                        .maxResultSize(1080 , 1080)
                        .start(REQUEST_GALLERY);

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Context context = change_image.this;
                path = RealPathUtil.getRealPath(context, uri);

                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imgg.setImageBitmap(bitmap);
                System.out.println("Tes Pathhh");



            } else {
                System.out.println("Mbuoh gagal");
            }
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