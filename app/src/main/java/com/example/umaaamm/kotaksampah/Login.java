package com.example.umaaamm.kotaksampah;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    DatabaseReference database;
    FirebaseAuth firebaseAuth;
    Button login;
    TextView daftar;
    EditText email,passwordku;
    private boolean loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        loggedIn = isLoggedIn();
        if (loggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        login = (Button) findViewById(R.id.btnLogin);
        daftar = (TextView) findViewById(R.id.edSignUp);

        email = (EditText) findViewById(R.id.edUsername);
        passwordku = (EditText) findViewById(R.id.edPassword);


        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = email.getText().toString().trim();
                String password = passwordku.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    Snackbar.make(findViewById(android.R.id.content), "Alamat email tidak boleh kosong", Snackbar.LENGTH_LONG)
                            .show();
                } else if (TextUtils.isEmpty(password)) {
                    Snackbar.make(findViewById(android.R.id.content), "Kata sandi tidak boleh kosong", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(Login.this,R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Proses ...");
                    progressDialog.show();
                    try {
                        firebaseAuth.signInWithEmailAndPassword(username, password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            Snackbar.make(findViewById(android.R.id.content), "Akun Anda Tidak Terdaftar", Snackbar.LENGTH_LONG)
                                                    .show();
                                            progressDialog.hide();
                                        }
                                    }
                                });
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    private boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }
}
