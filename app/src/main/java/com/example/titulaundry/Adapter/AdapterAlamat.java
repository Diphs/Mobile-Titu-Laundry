package com.example.titulaundry.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.titulaundry.Model.DataItemBanner;
import com.example.titulaundry.ModelMySQL.DataItemAlamatLain;
import com.example.titulaundry.R;

import java.util.List;

public class AdapterAlamat extends RecyclerView.Adapter<AdapterAlamat.HolderAlamat> {
    private List<DataItemAlamatLain> alamatLains;
    Context ctx;
    AdapterAlamat.getAlamat getAlamat;


    public AdapterAlamat(List<DataItemAlamatLain> alamatLains, Context ctx,getAlamat alamat) {
        this.alamatLains = alamatLains;
        this.ctx = ctx;
        this.getAlamat = alamat;
    }

    public interface getAlamat{
        void alamatDipilih(String alamatUsernya);
    }

    @NonNull
    @Override
    public HolderAlamat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.alamat_lay,parent ,false);
        return new AdapterAlamat.HolderAlamat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAlamat holder, int position) {
        DataItemAlamatLain dataItemAlamatLain = alamatLains.get(position);
        holder.alamat.setText(dataItemAlamatLain.getAlamat());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alamatKepilih = dataItemAlamatLain.getAlamat();
                getAlamat.alamatDipilih(alamatKepilih);
            }
        });

    }

    @Override
    public int getItemCount() {
        return alamatLains.size();
    }

    public class HolderAlamat extends RecyclerView.ViewHolder {
        TextView alamat;
        CardView cardView;
        public HolderAlamat(@NonNull View itemView) {
            super(itemView);
            alamat = itemView.findViewById(R.id.alllt);
            cardView = itemView.findViewById(R.id.cardAlamat);
        }
    }
}
