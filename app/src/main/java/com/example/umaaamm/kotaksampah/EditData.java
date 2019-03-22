package com.example.umaaamm.kotaksampah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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


public class EditData extends AppCompatActivity {
    private ProgressDialog progressDialog;
    public TextView nama,email,username,password,alamat;
    private String userName,key;
    private Button EditButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Data Diri");
        progressDialog = new ProgressDialog(this,R.style.AppTheme_Dark_Dialog);
        nama = (TextView) findViewById(R.id.edSignUpFullName);
        email = (TextView) findViewById(R.id.edSignUpEmail);
        alamat = (TextView) findViewById(R.id.edSetAlamat);
        EditButton = (Button) findViewById(R.id.btnSignUp);
        email.setEnabled(false);

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
        //key = zz.getRef().getKey();
        zz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String uid = data.child("uid").getValue(String.class);
                    if (uid.trim().equals(userName.toLowerCase().trim())) {
                        nama.setText(data.child("nama").getValue().toString());
                        email.setText(data.child("email").getValue(String.class));
                        alamat.setText(data.child("alamat").getValue(String.class));
                       key = data.getKey();
                    }else {
                        Snackbar.make(findViewById(android.R.id.content), "Data Tidak Ada.", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit(nama.getText().toString(),alamat.getText().toString());
            }
        });
    }

    public void Edit(String nama,String Alamat){
        if(!nama.isEmpty() && !Alamat.isEmpty()) {
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Proses ...");
            progressDialog.show();

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("User");
            ref.keepSynced(true);
            ref.child(key).child("nama").setValue(nama);
            ref.child(key).child("alamat").setValue(Alamat);
            progressDialog.dismiss();
            Snackbar.make(findViewById(android.R.id.content),"Perubahan Data berhasil.", Snackbar.LENGTH_SHORT).show();
            Intent intent = new Intent(EditData.this, Detail.class);
            startActivity(intent);

        }else{
            Snackbar.make(findViewById(android.R.id.content),"Periksa kembali data Anda.", Snackbar.LENGTH_SHORT).show();
        }
    }
}