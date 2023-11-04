package com.example.titulaundry.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.ModelMySQL.DataPesananSemua;
import com.example.titulaundry.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterPesananSemua extends RecyclerView.Adapter<AdapterPesananSemua.ViewHolder> {
    Context ctx;
    List<DataPesananSemua> pesananSemuas;
    AdapterPesananSemua.PassData DataSemua;
    int berat_cucian = 0;
    int todHarga = 0;

    public AdapterPesananSemua(Context ctx, List<DataPesananSemua> pesananSemuas, PassData dataSemua) {
        this.ctx = ctx;
        this.pesananSemuas = pesananSemuas;
        this.DataSemua = dataSemua;


    }
    public interface PassData {
        void passData(int berat,int hargaa);
    }
    public void clear() {
        int size = pesananSemuas.size();
        pesananSemuas.clear();
        notifyItemRangeRemoved(0, size);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pesanan,parent,false);
        return new AdapterPesananSemua.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DataPesananSemua db = pesananSemuas.get(position);
        holder.Status_pesanan.setText(String.valueOf(db.getStatusPesanan()));
        if (db.getStatusPesanan().equals("Sedang diproses")){
            holder.Status_pesanan.setBackground(ContextCompat.getDrawable(ctx,R.drawable.bunder_text_proses));
            holder.Status_pesanan.setTextColor(Color.rgb(246, 185, 131));
        } else if (db.getStatusPesanan().equals("Sedang dalam pengiriman")){
            holder.Status_pesanan.setBackground(ContextCompat.getDrawable(ctx,R.drawable.bunder_text_antar));
            holder.Status_pesanan.setTextColor(Color.rgb(69, 141, 239));

            if (db.getWaktuAntar().equals("00:00:00")){
                holder.waktu_antar.setText("");
                String ya = "<b> Antar Sendiri </b>";
                holder.estimasi.setText(Html.fromHtml(ya));

            } else {
                holder.estimasi.setText("Pengiriman : ");
                holder.waktu_antar.setText(String.valueOf(db.getWaktuAntar().substring(0,5)+" WIB"));
            }
        } else if (db.getStatusPesanan().equals("Menunggu pembayaran")){
            holder.Status_pesanan.setBackground(ContextCompat.getDrawable(ctx,R.drawable.bunder_text_bayar));
            holder.Status_pesanan.setTextColor(Color.rgb(242, 201, 76));
        } else if (db.getStatusPesanan().equals("Pesanan dibatalkan")){
            holder.Status_pesanan.setBackground(ContextCompat.getDrawable(ctx,R.drawable.bunder_text_batal));
            holder.Status_pesanan.setTextColor(Color.rgb(235, 87, 87));
        } else if (db.getStatusPesanan().equals("Sedang dijemput")){
            holder.Status_pesanan.setBackground(ContextCompat.getDrawable(ctx,R.drawable.bunder_text_antar));
            holder.Status_pesanan.setTextColor(Color.rgb(69, 141, 239));

            if (db.getWaktu_penjemputan().equals("00:00:00")){
                holder.waktu_antar.setText("");
                String ya = "<b> Antar Sendiri </b>";
                holder.estimasi.setText(Html.fromHtml(ya));

            } else {
                holder.estimasi.setText("Penjemputan : ");
                holder.waktu_antar.setText(String.valueOf(db.getWaktuAntar().substring(0,5)+" WIB"));
            }
        } else if (db.getStatusPesanan().equals("Menunggu diantar")){
            holder.Status_pesanan.setBackground(ContextCompat.getDrawable(ctx,R.drawable.bunder_text_pink));
            holder.Status_pesanan.setTextColor(Color.rgb(245, 111, 179));

        }

        holder.jenis_layanan.setText(String.valueOf(db.getJenisJasa()+" "+db.getTotalBerat()+" Kg"));
        holder.harga.setText(String.valueOf(convertRupiah(Integer.parseInt(db.getTotalHarga()))));
        Picasso.get().load(AppClient.URL_IMG+db.getImage()).error(R.drawable.meki).into(holder.imageView);

        berat_cucian += Integer.parseInt(db.getTotalBerat());
        todHarga += Integer.parseInt(db.getTotalHarga());
        DataSemua.passData(berat_cucian,todHarga);
        System.out.println("Tes Berat pada adapter "+ berat_cucian);
    }

    @Override
    public int getItemCount() {
        return pesananSemuas.size();
    }


    public static String convertRupiah(int price){
        Locale locale = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String strFormat = format.format(price);
        return strFormat.replace(",00","");
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jenis_layanan , Status_pesanan , waktu_antar,harga,estimasi;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jenis_layanan = itemView.findViewById(R.id.jasa);
            Status_pesanan = itemView.findViewById(R.id.status);
            waktu_antar = itemView.findViewById(R.id.timePick);
            harga = itemView.findViewById(R.id.totalHarga);
            estimasi = itemView.findViewById(R.id.waktuEst);
            imageView = itemView.findViewById(R.id.img1);
        }
    }
}
