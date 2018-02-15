package com.marica.m_note.SharedPreference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.marica.m_note.Login;
import com.marica.m_note.pojoclass.User;

/**
 * Created by Neston on 31/12/2017.
 */

public class MyPreferenceManager {
    //the constants
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_NOTE = "keynote";
    private static final String KEY_ID = "keyid";

    private static MyPreferenceManager mInstance;
    private static Context mCtxt;

    private MyPreferenceManager(Context context){
        mCtxt=context;
    }

    public static synchronized MyPreferenceManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new MyPreferenceManager(context);
        }
        return mInstance;
    }

    //method to let user login;
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtxt.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME,user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.apply();
    }


    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtxt.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtxt.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtxt.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(mCtxt,Login.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtxt.startActivity(intent);
    }

}
