package com.example.titulaundry.layanan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.titulaundry.API.AppClient;
import com.example.titulaundry.Login;
import com.example.titulaundry.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Alert_App {

    static AlertDialog.Builder data ;
    static  AlertDialog dialog2;

public static void alertBro(Context context,String pesan){
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    View layout_dialog = LayoutInflater.from(context).inflate(R.layout.alert,null);
    builder.setView(layout_dialog);

    ImageButton mengerti = layout_dialog.findViewById(R.id.mengerti);

    TextView alert = layout_dialog.findViewById(R.id.status);
    alert.setText(pesan);

    AlertDialog dialog = builder.create();
    dialog.show();
    dialog.setCancelable(false);
    dialog.getWindow().setGravity(Gravity.CENTER);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    mengerti.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    });
    new Handler().postDelayed(new Runnable() {

        @Override

        public void run() {

           dialog.dismiss();

        }

    }, 4*1000);

}

public static void previewImage(Context context,String img){
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    View view = LayoutInflater.from(context).inflate(R.layout.preview_profile,null);
    builder.setView(view);

    MaterialButton materialButton = view.findViewById(R.id.dismissa);
    CircleImageView circleImageView = view.findViewById(R.id.prewImage);
    Picasso.get().load(AppClient.profileIMG+img).error(R.drawable.blank).into(circleImageView);
    AlertDialog dialog = builder.create();
    dialog.show();;
    dialog.getWindow().setGravity(Gravity.CENTER);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    materialButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    });
}

public static void exitAlertBruh (Context context){
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    View layout_dialog = LayoutInflater.from(context).inflate(R.layout.exit_alert,null);
    builder.setView(layout_dialog);

    ImageView mengertii = layout_dialog.findViewById(R.id.pahamm);
    Button metu = layout_dialog.findViewById(R.id.ok);

    AlertDialog dialog = builder.create();
    dialog.show();
    dialog.setCancelable(false);
    dialog.getWindow().setGravity(Gravity.CENTER);

    mengertii.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    });

    metu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREF_ACCOUNT", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();
            Intent loginscreen=new Intent(context, Login.class);
            loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(loginscreen);
        }
    });
}

public static void ShowLoadScreenData(Context context){
    data = new AlertDialog.Builder(context);
    View view = LayoutInflater.from(context).inflate(R.layout.loading_screen,null);
    data.setView(view);
    dialog2 = data.create();
    dialog2.setCancelable(false);
    dialog2.getWindow().setGravity(Gravity.CENTER);

    if (dialog2 != null && !dialog2.isShowing()){
        dialog2.show();
    }


}
    public static void HideLoadScreenData(Context context){

        if (dialog2 != null && dialog2.isShowing()){
            dialog2.dismiss();
        }


    }
}
