package com.example.titulaundry.Dashboard;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.AboutApp;
import com.example.titulaundry.Adapter.RealPathUtil;
import com.example.titulaundry.Login;
import com.example.titulaundry.Model.ResponseImg;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.R;
import com.example.titulaundry.layanan.Alert_App;
import com.github.drjacky.imagepicker.ImagePicker;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class account_fragment extends Fragment {

    AlertDialog dialog;
    CircleImageView circleImageView,circleImageView2;
    ApiInterface apiInterface;
    String part_image;
    String path;
    CardView setAccount,setPassword,logOut,changeAlamat,about_apps;
    public String URI_IMGG = "";
    Button savePic;
    TextView mail,name;
    final int REQUEST_GALLERY = 9544;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_fragment, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        setProfile();
        getPreviewImg();
//        UploadImage();
        toSetAccount();
        changePw();
        LogOut();
        gantiAlamat();
        setAbout_apps();
    }

    public void gantiAlamat(){
        changeAlamat = (CardView) getView().findViewById(R.id.ganti_alamat);
        changeAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_user = getActivity().getIntent().getStringExtra("id_user");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                id_user = sharedPreferences.getString("KEY_ID","");
                Intent i = new Intent(getContext(),Tampung_Alamat.class);
                i.putExtra("id_user",id_user);
                startActivity(i);

            }
        });
    }

    public void LogOut(){
        logOut = (CardView) getView().findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
//                sharedPreferences.edit().clear().commit();
//                Intent loginscreen=new Intent(getContext(), Login.class);
//                loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(loginscreen);
                Alert_App.exitAlertBruh(getContext());
            }
        });


    }

    public void setAbout_apps(){
        about_apps = (CardView) getView().findViewById(R.id.pengaturan_lainnya);
        about_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_user = getActivity().getIntent().getStringExtra("id_user");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                id_user = sharedPreferences.getString("KEY_ID","");
                Intent i = new Intent(getContext(), AboutApp.class);
                i.putExtra("id_user",id_user);
                startActivity(i);
            }
        });
    }
    public void changePw(){
        setPassword = (CardView) getView().findViewById(R.id.ganti_password);
        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_user = getActivity().getIntent().getStringExtra("id_user");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                id_user = sharedPreferences.getString("KEY_ID","");
                Intent i = new Intent(getContext(),ubah_password.class);
                i.putExtra("id_user",id_user);
                startActivity(i);
            }
        });
    }

    public void setProfile(){
        String id_user = getActivity().getIntent().getStringExtra("id_user");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("KEY_ID","");
        mail = (TextView) getView().findViewById(R.id.profile_email);
        name = (TextView) getView().findViewById(R.id.profile_name);
        circleImageView = (CircleImageView) getView().findViewById(R.id.profile_image);
        Alert_App.ShowLoadScreenData(getContext());
        apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseUser> userCall = apiInterface.getDataUser(id_user);
        userCall.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                String m = response.body().getData().getNama();
                String e = response.body().getData().getEmail();
                URI_IMGG = response.body().getData().getProfile_img();
                System.out.println("String img == "+URI_IMGG);
                mail.setText(e);
                name.setText(m);
                Picasso.get().load(AppClient.profileIMG+URI_IMGG).error(R.drawable.blank).into(circleImageView);
                Alert_App.HideLoadScreenData(getContext());
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });

    }

    public void getPreviewImg(){
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert_App.previewImage(getContext(),URI_IMGG);
            }
        });
    }

    public void toSetAccount(){

        setAccount = (CardView) getView().findViewById(R.id.pengaturan_akun);
        setAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_user = getActivity().getIntent().getStringExtra("id_user");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                id_user = sharedPreferences.getString("KEY_ID","");
                Intent i = new Intent(getContext(),change_image.class);
                i.putExtra("id_user",id_user);
                startActivity(i);
            }
        });
    }

//    public void UploadImage(){
//        savePic = (Button) getView().findViewById(R.id.savePic);
//        savePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                File file = new File(path);
//                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                MultipartBody.Part body = MultipartBody.Part.createFormData("imageupload", file.getName(), requestFile);
//                RequestBody cus_name = RequestBody.create(MediaType.parse("multipart/form-data"),"1");
//
//                apiInterface = AppClient.getClient().create(ApiInterface.class);
//                Call<ResponseImg> imgCall = apiInterface.uploadImage(body,cus_name);
//                imgCall.enqueue(new Callback<ResponseImg>() {
//                    @Override
//                    public void onResponse(Call<ResponseImg> call, Response<ResponseImg> response) {
//                        if (response.body().getKode() == 1){
//                            Toast.makeText(getContext(), "Berhasil Upload", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseImg> call, Throwable t) {
//                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                        System.out.println(t.getMessage());
//                    }
//                });
//
//            }
//        });
//    }


//    public void ShowAlert(){
//        String id_user = getActivity().getIntent().getStringExtra("id_user");
//        System.out.println("In ubah foto"+URI_IMGG);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        //Inisiasi
//        View view =getLayoutInflater().inflate(R.layout.ubah_foto,null);
//        Button OpenGalery;
//        ImageView clsBtn;
//        ImageView foto;
//
//
//        //set Foto
//
//        circleImageView2 = (CircleImageView) view.findViewById(R.id.imgProfile) ;
//        Picasso.get().load(AppClient.profileIMG+URI_IMGG).error(R.mipmap.ic_launcher).into(circleImageView2);
//
//        //Dismis Dialog
//        clsBtn = (ImageView) view.findViewById(R.id.closee);
//        clsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        //open Galery
//        OpenGalery = view.findViewById(R.id.ubah2);
//        OpenGalery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent,"Open Gallery"),REQUEST_GALLERY);
//                    Toast.makeText(getContext(), "Open Galleerrtyy", Toast.LENGTH_SHORT).show();
//                } else {
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//                    Toast.makeText(getContext(), "Open BO", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        savePic = (Button) view.findViewById(R.id.savePic);
//        savePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                File file = new File(path);
//                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                MultipartBody.Part body = MultipartBody.Part.createFormData("imageupload", file.getName(), requestFile);
//                RequestBody cus_name = RequestBody.create(MediaType.parse("multipart/form-data"),id_user);
//
//                apiInterface = AppClient.getClient().create(ApiInterface.class);
//                Call<ResponseImg> imgCall = apiInterface.uploadImage(body,cus_name);
//                imgCall.enqueue(new Callback<ResponseImg>() {
//                    @Override
//                    public void onResponse(Call<ResponseImg> call, Response<ResponseImg> response) {
//                        if (response.body().getKode() == 1){
//                            Toast.makeText(getContext(), "Berhasil Upload", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                            startActivity(getActivity().getIntent());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseImg> call, Throwable t) {
//                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                        System.out.println(t.getMessage());
//                    }
//                });
//            }
//        });
//
//
//
//        builder.setView(view);
//        dialog = builder.create();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getData();
//            Context context = getContext();
//            path = RealPathUtil.getRealPath(context, uri);
//            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            circleImageView2.setImageBitmap(bitmap);
//
//
//        } else {
//            System.out.println("Mbuoh gagal");
//        }
//    }


}