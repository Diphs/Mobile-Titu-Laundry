package com.example.titulaundry.Dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.Adapter.AdapterBanner;
import com.example.titulaundry.Adapter.AdapterBarang;
import com.example.titulaundry.Adapter.AdapterPesanan;
import com.example.titulaundry.KonfirmasiSukses;
import com.example.titulaundry.Model.DataItemBanner;
import com.example.titulaundry.Model.ResponeBarang;
import com.example.titulaundry.Model.ResponseBanner;
import com.example.titulaundry.Model.ResponsePesanan;
import com.example.titulaundry.Model.ResponseUser;
import com.example.titulaundry.ModelMySQL.DataBarang;
import com.example.titulaundry.ModelMySQL.DataPesanan;
import com.example.titulaundry.R;
import com.example.titulaundry.atur_pesanan.pesanan;
import com.example.titulaundry.layanan.Alert_App;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class home_fragment extends Fragment {
    public TextView greeting;
    String waktu;
    TextView getGreeting,alamatUser;
    CircleImageView profilePic;
    private ViewPager2 viewPager2;
    List<DataItemBanner> itemBanners = new ArrayList<>();
    ProgressDialog mProgressDialog;
    RecyclerView recyclerView,recyclerView2;
    //use DB MySQL
    RecyclerView.Adapter adData;
    private Handler slideHandler = new Handler();
    private List<DataBarang> dataBarangList = new ArrayList<>();
    private List<DataPesanan> pesananList = new ArrayList<>();

    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setGreeting();
        RecycleMySQL();
        RecycleMySQLPesanan();
        BannerSlideShow();


    }



    private void BannerSlideShow(){
//        ShowProgress();
        Alert_App.ShowLoadScreenData(getContext());
        viewPager2 = (ViewPager2) getView().findViewById(R.id.banner);
        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponseBanner> bannerCall = apiInterface.getBanner();
        bannerCall.enqueue(new Callback<ResponseBanner>() {
            @Override
            public void onResponse(Call<ResponseBanner> call, Response<ResponseBanner> response) {

                System.out.println("Data Banner = "+response.body().getPesan());
                if (response.body().getKode() == 1 ){
                    itemBanners = response.body().getData();
                    viewPager2.setAdapter(new AdapterBanner(itemBanners,viewPager2,getContext()));
//                HideProgressDialog();
                    Alert_App.HideLoadScreenData(getContext());
                    viewPager2.setClipToPadding(false);
                    viewPager2.setClipChildren(false);
                    viewPager2.setOffscreenPageLimit(3);
                    viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                    compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View page, float position) {
                            float r = 1 - Math.abs(position);
                            page.setScaleY(0.85f + r * 0.15f);

                        }
                    });

                    viewPager2.setPageTransformer(compositePageTransformer);
                    viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            slideHandler.removeCallbacks(slideRunnable);
                            slideHandler.postDelayed(slideRunnable,3000);

                        }
                    });
                } else {
                    //Banner Kosong
                }

            }

            @Override
            public void onFailure(Call<ResponseBanner> call, Throwable t) {

            }
        });
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };


    private void RecycleMySQLPesanan() {
        String id_user = getActivity().getIntent().getStringExtra("id_user");
        recyclerView = getView().findViewById(R.id.recyclePesanan);
        frameLayout = (FrameLayout) getView().findViewById(R.id.frmPesananKosong);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("KEY_ID","");
        System.out.println("ID USER PADA HOME == "+id_user);
        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);


        Call<ResponsePesanan> responsePesananCall = apiInterface.getPesanan(id_user);
        responsePesananCall.enqueue(new Callback<ResponsePesanan>() {
            @Override
            public void onResponse(Call<ResponsePesanan> call, Response<ResponsePesanan> response) {
                if (response.body().getKode()==1){
                    String dataAda = response.body().getPesan();
                    pesananList = response.body().getData();

                    adData = new AdapterPesanan(getContext(),pesananList,getActivity().getIntent());

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    recyclerView.setAdapter(adData);
                    adData.notifyDataSetChanged();
                    frameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    frameLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponsePesanan> call, Throwable t) {

            }
        });
    }

    public void setGreeting(){
        String id_user = getActivity().getIntent().getStringExtra("id_user");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("KEY_ID","");
        getGreeting = (TextView)getView().findViewById(R.id.greeting);
        alamatUser = (TextView) getView().findViewById(R.id.alamat);
        profilePic = (CircleImageView) getView().findViewById(R.id.profile_image);

        //set waktu
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);

        if (hour <= 6 || hour <= 11) {
            waktu = "Pagi";
        } else if (hour <= 15) {
            waktu = "Siang";
        } else if (hour <= 17){
            waktu = "Sore";
        }else if (hour <= 24) {
            waktu = "Malam" ;
        }

        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
        System.out.println("Tes id User apakah koson= "+id_user);
        Call<ResponseUser> dataUser = apiInterface.getDataUser(id_user);
        dataUser.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                getGreeting.setText("Selamat "+waktu+" "+String.valueOf(response.body().getData().getNama()));

                getGreeting.setSelected(true);
                alamatUser.setText(String.valueOf(response.body().getData().getAlamat()));
                Picasso.get().load(AppClient.profileIMG+response.body().getData().getProfile_img()).error(R.drawable.blank).into(profilePic);
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });

    }

    public void RecycleMySQL(){
        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<ResponeBarang> GetData = apiInterface.getRetrive();
        GetData.enqueue(new Callback<ResponeBarang>() {

            @Override
            public void onResponse(Call<ResponeBarang> call, Response<ResponeBarang> response) {
                int kode = response.body().getKode();
                String Pesan = response.body().getPesan();

                dataBarangList = response.body().getData();

                adData = new AdapterBarang(getContext(),dataBarangList,getActivity().getIntent());

                recyclerView2 = getView().findViewById(R.id.recycleLayanan);
                recyclerView2.setHasFixedSize(true);
                recyclerView2.setLayoutManager(new GridLayoutManager(getActivity(),2));

                recyclerView2.setAdapter(adData);
                adData.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<ResponeBarang> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}