package com.igordutrasanches.anajatubaboletim.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.igordutrasanches.anajatubaboletim.R;

public abstract class BaseViewActivity extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (WelcomeActivity.shouldDisplay(this)) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        }
    }
}
