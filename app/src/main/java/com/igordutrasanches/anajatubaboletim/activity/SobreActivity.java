package com.igordutrasanches.anajatubaboletim.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.igordutrasanches.anajatubaboletim.R;

import java.util.Objects;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ((ImageButton)findViewById(R.id.rate))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.igordutrasanches.anajatubaboletim")));
                    }
                });
        ((ImageButton)findViewById(R.id.shared))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.SUBJECT", "");
                        intent.putExtra("android.intent.extra.TEXT", getPlainText());
                        startActivity(Intent.createChooser(intent, "Boletim de Anajatuba"));
                    }
                });
        ((Button)findViewById(R.id.profile))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SobreActivity.this, ProfileInfoActivity.class));
                    }
                });

        ((Button)findViewById(R.id.privacy))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1YNVVPP2Ek-kUntTXI6IQZBgQwtmZsBCoq8mkJX8BgSE/edit?usp=drivesdk")));
                    }
                });

        String version = "";
        try {
            version = getPackageManager().getPackageInfo((String)getPackageName(), (int) 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        toolbar.setSubtitle("Versão: " + version);
    }

    private String getPlainText() {
        String text = "Olá, que tal ficar informado sobre os casos da pandemia em Anajatuba?\nBaixe este app para acompanhar a situção dos casos.\n";
        return text + "https://play.google.com/store/apps/details?id=com.igordutrasanches.anajatubaboletim";
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit(){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            exit();
        }

        return super.onOptionsItemSelected(item);
    }
}