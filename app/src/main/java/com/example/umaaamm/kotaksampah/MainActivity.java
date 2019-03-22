package com.example.umaaamm.kotaksampah;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String userName;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView txt_berat;
        startService(new Intent(MainActivity.this, AndroidService.class));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Datadiri.class);
                startActivity(intent);
            }
        });

               //Toast.makeText(MainActivity.this,"Token Ku "+FirebaseInstanceId.getInstance().getToken(),Toast.LENGTH_LONG).show();
               //Log.d("tokenku","Token kuuu= "+FirebaseInstanceId.getInstance().getToken());


//       fab.hide();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            try {
                userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }else{
            userName = "";
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User");
        ref.keepSynced(true);
        Query zz = ref.orderByChild("uid").equalTo(userName.toLowerCase().trim());
        zz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String uid = data.child("uid").getValue(String.class);
                    if (uid.trim().equals(userName.toLowerCase().trim())) {
                        fab.show();
                    }else {
                        fab.hide();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference MyRef = database.getReference("sensor");


        ImageButton btn_set = (ImageButton) findViewById(R.id.pencet);
        final EditText set = (EditText) findViewById(R.id.setberat);

        txt_berat = (TextView) findViewById(R.id.txt_berat);

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyRef.child("batas").setValue(set.getText().toString());
                Snackbar.make(v, "Batas berat berhasil di set.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MyRef.keepSynced(true);


        MyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String temp = snapshot.child("berat").getValue().toString();
                //Float f = Float.parseFloat(temp);
                String temp1 = snapshot.child("batas").getValue().toString();
                txt_berat.setText(temp);
                set.setText(temp1);

                Float b = Float.parseFloat(snapshot.child("berat").getValue().toString());
                Float be = Float.parseFloat(snapshot.child("batas").getValue().toString());
                if (b > be ){
                    //Toast.makeText(MainActivity.this,"kkkkkkk",Toast.LENGTH_LONG).show();
                    showNotification("Kotak Sampah","Kotak Sampah Telah Melebihi Batas Yang Ditentukan Silahkan Buang Sampah.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.beranda) {
            // Handle the camera action
        } else if (id == R.id.detail) {
            Intent intent = new Intent(MainActivity.this, Detail.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder
                .setTitle("Keluar dari App Kotak Sampah")
                .setMessage("Apakah Anda ingin keluar?")
                .setCancelable(true)
                .setPositiveButton("Keluar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        TextView pesan = (TextView) alertDialog.findViewById(android.R.id.message);
        pesan.setTextSize(15);

        Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        b.setTextColor(getResources().getColor(R.color.colorPrimary));

        Button c = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        c.setTextColor(getResources().getColor(R.color.colorHitam));
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
