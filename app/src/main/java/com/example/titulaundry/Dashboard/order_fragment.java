package com.example.titulaundry.Dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.titulaundry.Adapter.AdapterPesananSemua;
import com.example.titulaundry.ModelMySQL.DataPesananSemua;
import com.example.titulaundry.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;


public class order_fragment extends Fragment {
    ChipNavigationBar chipNavigationBar;
    TextView textView;
    RecyclerView recyclerView;
    //use DB MySQL
    AdapterPesananSemua adapterPesanan1;
    private List<DataPesananSemua> pesananSemuaList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

//        SemuaPesanan();
        chipNavigationBar = (ChipNavigationBar) getView().findViewById(R.id.barPesanan);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.semua:
//                        pesananSemuaList.clear();
//                        SemuaPesanan();
                        Frg_Semua home = new Frg_Semua();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmee,home);
                        fragmentTransaction.commit();
                        break;
                    case R.id.SaatIni:
//                        pesananSemuaList.clear();
//                        SemuaSaatIni();
                        Frg_SaatIni home2 = new Frg_SaatIni();
                        FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frmee,home2);
                        fragmentTransaction2.commit();
                        break;
                    case R.id.Selesai:
//                        pesananSemuaList.clear();
//                        PesananSelesai();
                        Frg_Selesai home3 = new Frg_Selesai();
                        FragmentTransaction fragmentTransaction3 = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.frmee,home3);
                        fragmentTransaction3.commit();
                        break;
                }
            }
        });
    }

//    public void SemuaPesanan(){
//        String id_user = getActivity().getIntent().getStringExtra("id_user");
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
//        id_user = sharedPreferences.getString("KEY_ID","");
//        System.out.println(id_user);
//        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
//        Call<SemuaPesanan> pesananCall = apiInterface.getPesananSemua(id_user);
//        pesananCall.enqueue(new Callback<SemuaPesanan>() {
//            @Override
//            public void onResponse(Call<SemuaPesanan> call, Response<SemuaPesanan> response) {
//                if (response.body().getKode()==1){
//                    pesananSemuaList = response.body().getData();
//                    adapterPesanan1 = new AdapterPesananSemua(getContext(),pesananSemuaList);
//                    recyclerView = getView().findViewById(R.id.recycleSemua);
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                    recyclerView.setAdapter(adapterPesanan1);
//                    adapterPesanan1.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), "Kosong", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SemuaPesanan> call, Throwable t) {
//
//            }
//        });
//    }

//    public void SemuaSaatIni(){
//        String id_user = getActivity().getIntent().getStringExtra("id_user");
//
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
//         id_user = sharedPreferences.getString("KEY_ID","");
//        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
//        Call<SemuaPesanan> pesananCall = apiInterface.getPesananSaatIni(id_user);
//        pesananCall.enqueue(new Callback<SemuaPesanan>() {
//            @Override
//            public void onResponse(Call<SemuaPesanan> call, Response<SemuaPesanan> response) {
//                if (response.body().getKode()==1){
//                    pesananSemuaList = response.body().getData();
//                    adapterPesanan1 = new AdapterPesananSemua(getContext(),pesananSemuaList);
//                    recyclerView = getView().findViewById(R.id.recycleSemua);
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                    recyclerView.setAdapter(adapterPesanan1);
//                    adapterPesanan1.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), "Kosong", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SemuaPesanan> call, Throwable t) {
//
//            }
//        });
//    }

//    public void PesananSelesai(){
//        String id_user = getActivity().getIntent().getStringExtra("id_user");
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
//        id_user = sharedPreferences.getString("KEY_ID","");
//        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
//        Call<SemuaPesanan> pesananCall = apiInterface.getPesananSelesai(id_user);
//        pesananCall.enqueue(new Callback<SemuaPesanan>() {
//            @Override
//            public void onResponse(Call<SemuaPesanan> call, Response<SemuaPesanan> response) {
//                if (response.body().getKode()==1){
//                    pesananSemuaList = response.body().getData();
//                    adapterPesanan1 = new AdapterPesananSemua(getContext(),pesananSemuaList);
//                    recyclerView = getView().findViewById(R.id.recycleSemua);
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                    recyclerView.setAdapter(adapterPesanan1);
//                    adapterPesanan1.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), "Kosong", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SemuaPesanan> call, Throwable t) {
//
//            }
//        });
//    }
}