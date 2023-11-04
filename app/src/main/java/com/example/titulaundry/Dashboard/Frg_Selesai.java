package com.example.titulaundry.Dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.Adapter.AdapterPesananSemua;
import com.example.titulaundry.Model.SemuaPesanan;
import com.example.titulaundry.ModelMySQL.DataPesananSemua;
import com.example.titulaundry.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Frg_Selesai extends Fragment {
    RecyclerView recyclerView;
    //use DB MySQL
    AdapterPesananSemua adapterPesanan1;
    private List<DataPesananSemua> pesananSemuaList = new ArrayList<>();
    TextView beratCuci,pesanan,toddHargaa,psn1,psn2;
    ImageButton searchPesanan,refreshPesanan,pilih_mulai , pilih_akhir;
    EditText getTglMulai , getTglAkhir;
    DatePickerDialog pickerDialog;
    LinearLayout alertNull;
    CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selesai, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        PesananSelesai();
        cariPesananSelesai();
        refresh();
    }
    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c", count / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
    }

    public static String ConvertAgain(String convert){


        if (convert.contains("M")){
            return convert.replace("M","Jt");


        } else if (convert.contains("k")) {
            return convert.replace("k","Rb");


        }else if (convert.contains("G")) {
            return convert.replace("G","M");

        } else {
            return "Tidak sesuai";
        }

    }
    private void PesananSelesai() {
        beratCuci = getView().findViewById(R.id.todbrt);
        toddHargaa = getView().findViewById(R.id.todKeluar);
        pesanan = getView().findViewById(R.id.akehPesenan);
        recyclerView = getView().findViewById(R.id.recycleSelesai);
        alertNull = getView().findViewById(R.id.kosongHatiku);
        cardView = getView().findViewById(R.id.cardTodPes);
        AdapterPesananSemua.PassData passData = new AdapterPesananSemua.PassData() {
            @Override
            public void passData(int berat ,int hargaa){
                System.out.println(String.valueOf("Berat barang adalah = "+berat));
                beratCuci.setText(String.valueOf(berat)+ " Kg");
                long p = hargaa;
                String cvt = withSuffix(p);
                String ringkasHarga = ConvertAgain(cvt);
                toddHargaa.setText(ringkasHarga);

            }
        };
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
        String id_user = sharedPreferences.getString("KEY_ID","");
        System.out.println(id_user);
        ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
        Call<SemuaPesanan> pesananCall = apiInterface.getPesananSelesai(id_user);
        pesananCall.enqueue(new Callback<SemuaPesanan>() {
            @Override
            public void onResponse(Call<SemuaPesanan> call, Response<SemuaPesanan> response) {
                if (response.body().getKode()==1){
                    recyclerView.setVisibility(View.VISIBLE);
                    alertNull.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                    pesananSemuaList = response.body().getData();
                    adapterPesanan1 = new AdapterPesananSemua(getContext(),pesananSemuaList,passData);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapterPesanan1);
                    int p = recyclerView.getAdapter().getItemCount();
                    pesanan.setText(String.valueOf(p));
                    adapterPesanan1.notifyDataSetChanged();
                } else {

                    recyclerView.setVisibility(View.GONE);
                    beratCuci.setText("0 Kg");
                    toddHargaa.setText("0");
                    alertNull.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<SemuaPesanan> call, Throwable t) {

            }
        });
    }

    private void cariPesananSelesai() {
        searchPesanan = (ImageButton) getView().findViewById(R.id.cariPesanan);
        pilih_mulai = (ImageButton) getView().findViewById(R.id.pilih_mulai);
        pilih_akhir = (ImageButton) getView().findViewById(R.id.pilih_akhir);
        getTglMulai = (EditText) getView().findViewById(R.id.tgl_mulai);
        getTglAkhir = (EditText) getView().findViewById(R.id.tgl_akhir);
        recyclerView = getView().findViewById(R.id.recycleSelesai);
        psn1 = getView().findViewById(R.id.pesn1);
        psn2 = getView().findViewById(R.id.pesn2);
        alertNull = getView().findViewById(R.id.kosongHatiku);
        cardView = getView().findViewById(R.id.cardTodPes);

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        pilih_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String tgl1 = year + "-"+(month+1)+"-"+day;
                        getTglMulai.setText(tgl1);
                    }
                },year,month,day);
                pickerDialog.show();
            }
        });
        pilih_akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String tgl1 = year + "-"+(month+1)+"-"+day;
                        getTglAkhir.setText(tgl1);
                    }
                },year,month,day);
                pickerDialog.show();
            }
        });
        searchPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdapterPesananSemua.PassData passData = new AdapterPesananSemua.PassData() {
                    @Override
                    public void passData(int berat ,int hargaa){
                        System.out.println(String.valueOf("Berat barang adalah = "+berat));
                        beratCuci.setText(String.valueOf(berat)+ " Kg");
                        long p = hargaa;
                        String cvt = withSuffix(p);
                        String ringkasHarga = ConvertAgain(cvt);
                        toddHargaa.setText(ringkasHarga);

                    }
                };
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                String id_user = sharedPreferences.getString("KEY_ID","");
                System.out.println(id_user);
                ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
                Call<SemuaPesanan> pesananCall = apiInterface.getCariPesananSelesai(id_user,getTglMulai.getText().toString(),getTglAkhir.getText().toString());
                pesananCall.enqueue(new Callback<SemuaPesanan>() {
                    @Override
                    public void onResponse(Call<SemuaPesanan> call, Response<SemuaPesanan> response) {
                        if (response.body().getKode()==1){
                            recyclerView.setVisibility(View.VISIBLE);
                            alertNull.setVisibility(View.GONE);
                            cardView.setVisibility(View.VISIBLE);
                            pesananSemuaList = response.body().getData();
                            adapterPesanan1 = new AdapterPesananSemua(getContext(),pesananSemuaList,passData);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapterPesanan1);
                            int p = recyclerView.getAdapter().getItemCount();
                            pesanan.setText(String.valueOf(p));
                            adapterPesanan1.notifyDataSetChanged();

                        } else {

                            if (pesananSemuaList.size() != 0){
                                adapterPesanan1.clear();
                            }
                            recyclerView.setVisibility(View.GONE);
                            cardView.setVisibility(View.GONE);
                            alertNull.setVisibility(View.VISIBLE);
                            beratCuci.setText("0 Kg");
                            toddHargaa.setText("0");
                            psn1.setText("Data yang anda cari tidak tersedia!");
                            psn2.setText("Silakan cari data lain atau muat ulang data");
                        }

                    }

                    @Override
                    public void onFailure(Call<SemuaPesanan> call, Throwable t) {

                    }
                });
            }
        });

    }

    private void refresh() {
        refreshPesanan = (ImageButton) getView().findViewById(R.id.refresh);
        refreshPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PesananSelesai();
                getTglMulai.setText(null);
                getTglAkhir.setText(null);
            }
        });
    }
}