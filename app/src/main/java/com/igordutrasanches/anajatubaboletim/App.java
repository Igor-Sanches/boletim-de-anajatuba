package com.igordutrasanches.anajatubaboletim;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.activity.ActivityUpdate;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.conta.User;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.models.Builder;
import com.igordutrasanches.anajatubaboletim.models.Support;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        getShotSupport();
    }

    private void getShotSupport(){
        FirebaseUtils.getDatabaseRef()
                .child(Chave.PRIMARIA)
                .child(Chave.SUPPORT)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null){
                            Support support = dataSnapshot.getValue(Support.class);
                            if(support!= null) {
                                Data.setSupport(support, getApplicationContext());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void shotUpdate(){
        FirebaseUtils.getDatabaseRef()
                .child(Chave.PRIMARIA)
                .child("Update")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null){
                            Builder builder = dataSnapshot.getValue(Builder.class);
                            if(builder != null){
                                if(builder.isUpdate()){
                                    verifiqueVersion(builder.getVersion());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void verifiqueVersion(String version) {
        String versionApp = "";
        try {
            version = getPackageManager().getPackageInfo((String)getPackageName(), (int) 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(!version.equals("")){
            if(!versionApp.equals(version)){
                startActivity(new Intent(this, ActivityUpdate.class));
            }
        }
    }
}
