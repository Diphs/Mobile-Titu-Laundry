package com.example.titulaundry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AboutApp extends AppCompatActivity {
    CardView gasToGmail;
    TextView setEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        notif(AboutApp.this);
        setupSendGmail();
    }

    public void notif(Activity activity){
        //change color notif bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        //set icons notifbar
        View decor = activity.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void setupSendGmail(){
        gasToGmail = findViewById(R.id.toGmail);
        setEmail = findViewById(R.id.setEmaill);

        String emailDevelopment = "romu2ateam@gmail.com";
        setEmail.setText("Gmail : "+ emailDevelopment);

        gasToGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", emailDevelopment, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
                startActivity(Intent.createChooser(emailIntent, null));
            }
        });
    }
}