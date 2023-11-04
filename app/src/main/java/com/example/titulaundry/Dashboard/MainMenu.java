package com.example.titulaundry.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.titulaundry.API.CheckConnected;
import com.example.titulaundry.API.NetworkChangeListener;
import com.example.titulaundry.Adapter.AdapterBarang;
import com.example.titulaundry.KonfirmasiSukses;
import com.example.titulaundry.Login;
import com.example.titulaundry.R;
import com.example.titulaundry.layanan.Alert_App;
import com.google.android.material.tabs.TabLayout;

public class MainMenu extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    CardView v;
    RadioButton btn1;
    private home_fragment home_fragment;
    private Service_fragment service_fragment;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        switchMenu();
        notif(MainMenu.this);

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
    public void switchMenu(){
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);


            fragmentSwitch swtch = new fragmentSwitch(getSupportFragmentManager(), 0);
            swtch.addFragment(new home_fragment(),"Beranda");
            swtch.addFragment(new Service_fragment(),"Layanan");
            swtch.addFragment(new order_fragment(),"Pemesanan");
            swtch.addFragment(new account_fragment(),"Akun");
            viewPager.setAdapter(swtch);

            tabLayout.getTabAt(0).setIcon(R.drawable.home);
            tabLayout.getTabAt(1).setIcon(R.drawable.menu);
            tabLayout.getTabAt(2).setIcon(R.drawable.shop);
            tabLayout.getTabAt(3).setIcon(R.drawable.account);



    }
    @Override
    public void onBackPressed() {
//        Intent i = new Intent(getApplicationContext(), Login.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//
//        finish();
        Alert_App.exitAlertBruh(this);
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