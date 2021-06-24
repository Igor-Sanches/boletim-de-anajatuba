package com.igordutrasanches.anajatubaboletim.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.MainActivity;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.models.Boletim;

public class ServiceTask extends Service {

    private Context context = this;
    private DatabaseReference mRef;
    @Override
    public void onCreate(){

    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue() != null){
                Boletim mBoletim = (Boletim)dataSnapshot.getValue(Boletim.class);
//https://docs.google.com/document/d/1YNVVPP2Ek-kUntTXI6IQZBgQwtmZsBCoq8mkJX8BgSE/edit?usp=drivesdk
                        showNotification();

                }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void showNotification() {
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String title = "Boletim atualizado!";
        String body = "Veja agora mesmo o Boletim";
        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder _builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(som)
                .setContentIntent(pending);

        NotificationManager _NotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        _NotificationManager.notify(0, _builder.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRef = FirebaseUtils.getDatabaseRef();
        mRef.child(Chave.PRIMARIA).child(Chave.BOLETIM).addValueEventListener(valueEventListener);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mRef = FirebaseUtils.getDatabaseRef();
        mRef.child(Chave.PRIMARIA).child(Chave.BOLETIM).removeEventListener(valueEventListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
