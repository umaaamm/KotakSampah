package com.example.umaaamm.kotaksampah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private ProgressDialog loading;
    private EditText nama,email,username,password,alamat;
    DatabaseReference database;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Sign Up");

        database =  FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        password = (EditText) findViewById(R.id.edSignUpPassword);
        email = (EditText) findViewById(R.id.edSignUpEmail);
        Button btn_simpan = (Button) findViewById(R.id.btnSignUp);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Temail = email.getText().toString();
                String Tpassword = password.getText().toString();

                if (Temail.equals("")) {
                    email.setError("Silahkan Input Email Anda.");
                    email.requestFocus();

                } else if (Tpassword.equals("")) {
                    password.setError("Silahkan Input Password Anda.");
                    password.requestFocus();
                } else {
                    loading = ProgressDialog.show(SignUp.this, "Sign Up", "Loading ...", true, false);
                    firebaseAuth.createUserWithEmailAndPassword(Temail.toLowerCase(),Tpassword)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);
                                        alertDialogBuilder.setTitle("SignUp");
                                        alertDialogBuilder.setMessage("Akun anda telah dibuat, silahkan login dengan email dan password anda.");
                                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                finish();
                                            }
                                        });
                                        alertDialogBuilder.show();

                                        Intent intent = new Intent(SignUp.this, Login.class);
                                        startActivity(intent);

                                    } else {
                                        task.getException().printStackTrace();
                                        //  signup fail
                                        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Akun anda gagal dibuat, silahkan coba lagi.", Snackbar.LENGTH_INDEFINITE);
                                        snackbar.setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                snackbar.dismiss();
                                            }
                                        });
                                        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                                        snackbar.show();
                                    }
                                }
                            });
                }
            }
        });
    }

}
