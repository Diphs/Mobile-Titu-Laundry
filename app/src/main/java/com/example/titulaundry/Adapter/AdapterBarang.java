package com.example.titulaundry.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.ModelMySQL.DataBarang;
import com.example.titulaundry.R;
import com.example.titulaundry.atur_pesanan.pesanan;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterBarang extends RecyclerView.Adapter<AdapterBarang.HolderData> {
    Context ctx;
    List<DataBarang> dataBarangList;
    Intent intent;



    public AdapterBarang(Context ctx, List<DataBarang> dataBarangList,Intent intent) {
        this.ctx = ctx;
        this.dataBarangList = dataBarangList;
        this.intent = intent;
    }


    public void setFilteredList(List<DataBarang> filteredList){
        this.dataBarangList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(ctx).inflate(R.layout.grid_layanan,parent,false);
        return new HolderData(layout);
    }
    public static String convertRupiah(int price){
        Locale locale = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String strFormat = format.format(price);
        return strFormat.replace(",00","");
    }

    public static String toRupiah(int rupiah){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(rupiah).replace(".00","");
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder,  int position) {
        DataBarang db = dataBarangList.get(position);
        int harga = db.getHarga();
        String turu = String.valueOf(convertRupiah(harga));
        System.out.println();

        System.out.println(db.getDeskripsi().length());

        if (db.getDeskripsi().length()>=41){

            String ambilAwal = db.getDeskripsi().substring(0,39);
            String fromDb = new String(ambilAwal);
            StringBuilder builder = new StringBuilder(fromDb);
            builder.append("...");
            String NewBaru = builder.toString();
            holder.descLayanan.setText(NewBaru);
        } else {
            holder.descLayanan.setText(String.valueOf(db.getDeskripsi()));
        }

        holder.jenis_layanan.setText(String.valueOf(dataBarangList.get(position).getJenis_jasa()));
        holder.waktuLayanan.setText(String.valueOf(db.getDurrasi()));
        holder.hargaLayanan.setText(String.valueOf(convertRupiah(db.getHarga())));
        holder.id_jasa.setText(String.valueOf(db.getId_jasa()));
        String turuu = intent.getStringExtra("id_user");


        Picasso.get().load(AppClient.URL_IMG+dataBarangList.get(position).getImage()).error(R.drawable.cuci_kering).into(holder.gmbr);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
                String id_user = sharedPreferences.getString("KEY_ID","");
                Intent i = new Intent(ctx,pesanan.class);
                i.putExtra("layanan",db.getJenis_jasa());
                i.putExtra("deskripsi",db.getDeskripsi());
                i.putExtra("waktu",db.getDurrasi());
                i.putExtra("harga",String.valueOf(db.getHarga()));
                i.putExtra("imagee",db.getImage());
                i.putExtra("id_user",id_user);
                i.putExtra("id_jasa",String.valueOf(db.getId_jasa()));
                System.out.println("ID JASA NYA DALAH"+db.getId_jasa());
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBarangList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView jenis_layanan , descLayanan , waktuLayanan , hargaLayanan,id_jasa;
        ImageView gmbr;
        CardView cardView;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            jenis_layanan = itemView.findViewById(R.id.inpo);
            descLayanan = itemView.findViewById(R.id.tulis1);
            waktuLayanan = itemView.findViewById(R.id.waktuLamaLayanan);
            hargaLayanan = itemView.findViewById(R.id.hargaLayanan);
            id_jasa = itemView.findViewById(R.id.idJasa);
            gmbr = itemView.findViewById(R.id.imgInCard1);
            cardView = itemView.findViewById(R.id.cuciKering);

        }
    }
}
