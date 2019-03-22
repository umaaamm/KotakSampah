package com.example.umaaamm.kotaksampah;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Detail extends AppCompatActivity {

    private static final String Database_Path = "User/";
    private DatabaseReference mDatabaseReference;
    private Query query;
    private String userName;
    public TextView nama,email,username,password,alamat;
    public ImageButton Reset,Edit,EditEmail;
    private ProgressDialog progressDialog;
    private String m_Text = "input dialog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Data Diri");
        progressDialog = new ProgressDialog(this,R.style.AppTheme_Dark_Dialog);
        nama = (TextView) findViewById(R.id.edSignUpFullName);
        email = (TextView) findViewById(R.id.edSignUpEmail);
        alamat = (TextView) findViewById(R.id.edSetAlamat);
        Reset = (ImageButton) findViewById(R.id.gantipass);
        Edit = (ImageButton) findViewById(R.id.editdata);
        EditEmail = (ImageButton) findViewById(R.id.editemail);

        EditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
                builder.setTitle("Edit Email");
                View viewInflated = LayoutInflater.from(Detail.this).inflate(R.layout.input_email,null, false);
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //m_Text = input.getText().toString();
                        gantiEmail(input.getText().toString());
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Detail.this, R.style.AppCompatAlertDialogStyle);
                alertDialogBuilder
                        .setTitle("Reset Password")
                        .setMessage("Apakah Anda Yakin Ingin Merubah Password ?")
                        .setCancelable(true)
                        .setPositiveButton("Yakin",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Detail.this, R.style.AppCompatAlertDialogStyle);
                                                    alertDialogBuilder
                                                            .setTitle("Reset Password")
                                                            .setMessage("Perubahan Password telah kami kirim ke email " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + " silahkan ikuti prosedur yang ada di email.")
                                                            .setCancelable(false)
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    Intent intent = new Intent(Detail.this, Login.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                                    progressDialog.dismiss();
                                                    alertDialog.show();
                                                }
                                            }
                                        });
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
        });


        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail.this, EditData.class);
                startActivity(intent);
//                Toast.makeText(Detail.this,"Edit Data",Toast.LENGTH_LONG).show();
            }
        });



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
                            nama.setText(data.child("nama").getValue().toString());
                            email.setText(data.child("email").getValue(String.class));
                            alamat.setText(data.child("alamat").getValue(String.class));
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

    }

    private void gantiEmail(final String emailBaru){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            user.updateEmail(emailBaru)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Detail.this, R.style.AppCompatAlertDialogStyle);
                                alertDialogBuilder
                                        .setTitle("Perubahan Email")
                                        .setMessage("Perubahan email telah berhasil.")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(Detail.this, Detail.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                final AlertDialog alertDialog = alertDialogBuilder.create();
                                progressDialog.dismiss();
                                alertDialog.show();
                            }
                        }
                    });
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
