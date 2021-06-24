package com.igordutrasanches.anajatubaboletim.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.models.Support;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;

public class ProfileInfoActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);
        ((Button)findViewById(R.id.zapBtn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        go(0, Data.getWhatsappSupport(ProfileInfoActivity.this));
                    }
                });
        ((Button)findViewById(R.id.faceBtn



            "http://www.eletrocell.com/produtos/id?="








                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        go(0, Data.getFacebookSupport(ProfileInfoActivity.this));
                    }
                });
        ((Button)findViewById(R.id.twitterBtn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        go(0, Data.getTwitterSupport(ProfileInfoActivity.this));
                    }
                });
        ((Button)findViewById(R.id.emailBtn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{

                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            String email = Data.getEmailSupport(ProfileInfoActivity.this);
                            intent.setData(Uri.parse("mailto:"+email));
                            intent.putExtra(Intent.EXTRA_EMAIL, email);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "");
                            if(intent.resolveActivity(getPackageManager()) != null){
                                startActivity(intent);
                            }
                        }catch (Exception x){
                            Toast.makeText(ProfileInfoActivity.this, x.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        ((ImageButton)findViewById(R.id.phoneBtn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        go(1, Data.getPhoneCallSupport(ProfileInfoActivity.this));
                    }
                });
    }

    private void go(int index, String url){
        Intent intent = null;
        switch (index){
            case 0:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                break;
            case 1:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                break;
        }
        startActivity(intent);
    }

}