package com.igordutrasanches.anajatubaboletim.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.igordutrasanches.anajatubaboletim.R;

public class ActivityUpdate extends AppCompatActivity {

    private long countClick = 0;

    @Override
    public void onBackPressed() {
         exit();
    }

    private void exit(){

        if(System.currentTimeMillis() - countClick > 2000){
            countClick = System.currentTimeMillis();
            Toast.makeText(this, "Clique novamente para sair!", Toast.LENGTH_SHORT).show();
            return;
        }
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ((Button)findViewById(R.id.update))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    goUpdate();
                     }
                });
    }

    private void goUpdate(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.igordutrasanches.anajatubaboletim")));

    }
}