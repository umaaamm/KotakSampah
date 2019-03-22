package com.example.umaaamm.kotaksampah;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        //Mendapatkan Instance dan Memperoleh Token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Menampilkan Token pada Log
        Log.d(TAG, "Token Saya : "+ refreshedToken);
        saveToken(refreshedToken);
    }

    //Method berikut ini digunakan untuk memperoleh token dan mennyimpannya ke server atau sistem lainnya
    private void saveToken(String refreshedToken) {

    }
}