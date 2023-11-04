package com.example.titulaundry.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.Dashboard.Detail_PesananUser;
import com.example.titulaundry.ModelMySQL.DataPesanan;
import com.example.titulaundry.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterPesanan extends RecyclerView.Adapter<AdapterPesanan.ViewHolder> {
    Context ctx;
    List<DataPesanan> pesananList;
    Intent intent;

    public AdapterPesanan(Context ctx, List<DataPesanan> pesananList,Intent intent) {
        this.ctx = ctx;
        this.pesananList = pesananList;
        this.intent = intent;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pesanan_user,parent,false);
        return new AdapterPesanan.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataPesanan pesanan = pesananList.get(position);

        holder.jasa.setText(String.valueOf(pesanan.getJenisJasa()+" "+pesanan.getTotalBerat()+" Kg"));

        if (pesanan.getStatusPesanan().equals("Menunggu diproses")){
            holder.status.setText("Sedang diproses");
        } else {
            holder.status.setText(String.valueOf(pesanan.getStatusPesanan()));
        }

        holder.totalHarga.setText(String.valueOf(convertRupiah(pesanan.getTotalHarga())));
        holder.waktuEst.setText(String.valueOf(pesanan.getDurasi()));
        Picasso.get().load(AppClient.URL_IMG+pesananList.get(position).getImage()).error(R.drawable.meki).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String turuu = intent.getStringExtra("id_user");
                Intent i = new Intent(ctx, Detail_PesananUser.class);
                i.putExtra("id_pesanan",pesanan.getId_pesanan());
                i.putExtra("id_user",turuu);
                ctx.startActivity(i);
            }
        });

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
    public int getItemCount() {
        return pesananList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jasa , status,waktuEst,totalHarga;
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jasa = itemView.findViewById(R.id.jasa);
            status = itemView.findViewById(R.id.status);
            waktuEst = itemView.findViewById(R.id.waktuEst);
            totalHarga = itemView.findViewById(R.id.totalHarga);
            imageView = itemView.findViewById(R.id.img1);
            cardView = itemView.findViewById(R.id.cardDetail);
        }
    }
}
