package com.example.titulaundry.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.Model.DataItemBanner;
import com.example.titulaundry.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBanner extends RecyclerView.Adapter<AdapterBanner.SliderViewHolder> {

    private List<DataItemBanner> itemBanners;
    private ViewPager2 viewPager2;
    Context ctx;

    public AdapterBanner(List<DataItemBanner> itemBanners, ViewPager2 viewPager2, Context ctx) {
        this.itemBanners = itemBanners;
        this.viewPager2 = viewPager2;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.slide_image,parent ,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(itemBanners.get(position));
        if (position == itemBanners.size() -2){
            viewPager2.post(runnable);
        }


    }

    @Override
    public int getItemCount() {
        return itemBanners.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;
//        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }
        void setImage(DataItemBanner dataItemBanner){
            Picasso.get().load(AppClient.Banner+dataItemBanner.getNamaBanner()).resize(200,100).into(imageView);

        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            itemBanners.addAll(itemBanners);
            notifyDataSetChanged();
        }
    };
}
