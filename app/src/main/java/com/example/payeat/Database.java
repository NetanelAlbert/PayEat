package com.example.payeat;

import com.firebase.client.Firebase;

public class Database extends android.app.Application {

    private static Firebase firebaseReference;
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        firebaseReference = new Firebase("https://payeat-4a103.firebaseio.com/");
    }
    public static Firebase getDataBaseInstance() { return firebaseReference;}
}
