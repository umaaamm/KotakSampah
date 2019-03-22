package com.example.umaaamm.kotaksampah;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AndroidService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference MyRef = database.getReference("sensor");
        MyRef.keepSynced(true);
        MyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//                String temp = snapshot.child("berat").getValue().toString();
//                //Float f = Float.parseFloat(temp);
//                String temp1 = snapshot.child("batas").getValue().toString();
//                txt_berat.setText(temp);
//                set.setText(temp1);

                Float b = Float.parseFloat(snapshot.child("berat").getValue().toString());
                Float be = Float.parseFloat(snapshot.child("batas").getValue().toString());
                if (b > be ){
//                    Toast.makeText(MainActivity.this,"kkkkkkk",Toast.LENGTH_LONG).show();
                    showNotification("Kotak Sampah","Kotak Sampah Telah Melebihi Batas Yang Ditentukan Silahkan Buang Sampah.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return START_STICKY;
    }
    void showNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Umam",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel notif Kotak Sampah");
            mNotificationManager.createNotificationChannel(channel);
        }
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.baseline_delete_outline_black_24)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(defaultSoundUri)
                .setAutoCancel(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
