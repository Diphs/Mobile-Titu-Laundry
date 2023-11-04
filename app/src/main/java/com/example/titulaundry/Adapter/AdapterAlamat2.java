package com.example.titulaundry.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.titulaundry.API.ApiInterface;
import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.Dashboard.Change_Address;
import com.example.titulaundry.Model.ResponseAlamat;
import com.example.titulaundry.ModelMySQL.DataItemAlamatLain;
import com.example.titulaundry.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAlamat2 extends RecyclerView.Adapter<AdapterAlamat2.Hold> {
    private List<DataItemAlamatLain> alamatLains;
    Context ctx;
    AdapterAlamat2.getCountAlamat getCountAlamat;

    public AdapterAlamat2(List<DataItemAlamatLain> alamatLains, Context ctx , getCountAlamat getCountAlamat) {
        this.alamatLains = alamatLains;
        this.ctx = ctx;
        this.getCountAlamat = getCountAlamat;
    }

    public interface getCountAlamat{
        void jumlahList(int todd);
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alamat_lain_settings,parent ,false);
        return new AdapterAlamat2.Hold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, @SuppressLint("RecyclerView") int position) {
        DataItemAlamatLain dataItemAlamatLain = alamatLains.get(position);
        holder.alamatnya.setText(dataItemAlamatLain.getAlamat());
        holder.showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.aksi.getVisibility() == View.GONE){
                    holder.aksi.setVisibility(View.VISIBLE);
                    holder.showHide.setImageDrawable(ctx.getResources().getDrawable(R.drawable.up));
                } else {
                    holder.aksi.setVisibility(View.GONE);
                    holder.showHide.setImageDrawable(ctx.getResources().getDrawable(R.drawable.down));
                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, Change_Address.class);
                i.putExtra("alamatTerbawah",dataItemAlamatLain.getAlamat());
                i.putExtra("idAlamat",dataItemAlamatLain.getIdAlamat());
                i.putExtra("aksi","editAlamat");
                ctx.startActivity(i);
                ((Activity)ctx).finish();
            }
        });

        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TawaranDelete(dataItemAlamatLain.getIdAlamat(),position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return alamatLains.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView alamatnya;
        ImageButton showHide;
        Button edit , hapus;
        LinearLayout aksi;
        public Hold(@NonNull View itemView) {
            super(itemView);
            alamatnya = itemView.findViewById(R.id.alamatTambahanUser);
            showHide = itemView.findViewById(R.id.showHideButton);
            edit = itemView.findViewById(R.id.aksiUbah);
            hapus = itemView.findViewById(R.id.aksiHapus);
            aksi = itemView.findViewById(R.id.tampungButtonAksi);
        }
    }
    public void setData(List<DataItemAlamatLain> newData) {
        this.alamatLains = newData;
//        this.alamatLains.clear();
//        this.alamatLains.addAll(newData);
        notifyDataSetChanged();
    }

    public void TawaranDelete(String idAlamat,int pcc){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View view = LayoutInflater.from(ctx).inflate(R.layout.confirm_delete,null);
        builder.setView(view);

        Button okHapus = view.findViewById(R.id.ok);
        Button okBatal = view.findViewById(R.id.no);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        okHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiInterface = AppClient.getClient().create(ApiInterface.class);
                Call<ResponseAlamat> call = apiInterface.DeleteAlamat(idAlamat);
                call.enqueue(new Callback<ResponseAlamat>() {
                    @Override
                    public void onResponse(Call<ResponseAlamat> call, Response<ResponseAlamat> response) {
                        Toast.makeText(ctx, "Alamat Terhapus", Toast.LENGTH_SHORT).show();
                        alamatLains.remove(pcc);
                        notifyItemRemoved(pcc);
                        dialog.dismiss();
                        int tod = alamatLains.size();
                        getCountAlamat.jumlahList(tod);

                    }

                    @Override
                    public void onFailure(Call<ResponseAlamat> call, Throwable t) {

                    }
                });
            }
        });

        okBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}
