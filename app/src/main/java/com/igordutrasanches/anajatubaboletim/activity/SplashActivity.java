package com.igordutrasanches.anajatubaboletim.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.igordutrasanches.anajatubaboletim.MainActivity;
import com.igordutrasanches.anajatubaboletim.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        if(timer == null){
            timer= new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    start();
                }
            }, 1500);
        }
        super.onResume();
    }

    private void start() {
        try{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }catch (Exception x){
            Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        if(timer!= null){
            timer.cancel();
            timer= null;
        }
        super.onPause();
    }
}