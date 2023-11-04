package com.example.titulaundry.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.titulaundry.ModelMySQL.DataItemVoucher;
import com.example.titulaundry.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterVoucher extends RecyclerView.Adapter<AdapterVoucher.ViewHolder> {
    Context ctx;
    List<DataItemVoucher> voucherList;
    List<Integer> selectCheck = new ArrayList<>();
    AdapterVoucher.PassDataVoucher dataVoucher;

    public AdapterVoucher(Context ctx, List<DataItemVoucher> voucherList , PassDataVoucher dataVoucher ) {
        this.ctx = ctx;
        this.voucherList = voucherList;
        this.dataVoucher =  dataVoucher;

        for (int i = 0; i<voucherList.size(); i++){
            selectCheck.add(0);
        }

    }
    public interface PassDataVoucher {
        void passData(String id_jasa , String potongan_harga , String slot);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_layout,parent,false);
        return new AdapterVoucher.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DataItemVoucher db = voucherList.get(position);

        if (selectCheck.get(position) ==1){
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int k = 0; k<selectCheck.size(); k++){
                    if (k == position){
                        selectCheck.set(k,1);
                    } else {
                        selectCheck.set(k,0);
                    }
                }
                notifyDataSetChanged();
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    Toast.makeText(ctx, db.getIdVoucher(), Toast.LENGTH_SHORT).show();
                    dataVoucher.passData(db.getIdVoucher(),db.getPotonganHarga(),db.getSlotVoucher());

                }
            }
        });

        holder.id_voucher.setText("#"+db.getIdVoucher());
        holder.potongan_harga.setText("Rp."+db.getPotonganHarga());
        holder.slot.setText("Slot : "+db.getSlotVoucher());
        holder.namaVoucher.setText(db.getNamaVoucher());
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_voucher , potongan_harga , slot,namaVoucher;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_voucher = itemView.findViewById(R.id.idVoucher);
            potongan_harga = itemView.findViewById(R.id.potongan_harga);
            slot = itemView.findViewById(R.id.slot_voucher);
            checkBox = itemView.findViewById(R.id.checkVoucher);
            namaVoucher = itemView.findViewById(R.id.headerVoucher);
        }
    }
}
