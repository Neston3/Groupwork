package com.marica.m_note.AppManager;

import android.app.Application;
import android.content.Context;

/**
 * Created by Marica on 11/02/2018.
 */

public class AppManager extends Application {
    private  static Context context;

    public  void onCreate() {
        super.onCreate();

        AppManager.context=getApplicationContext();
    }

    public static Context getAppContext(){
        return AppManager.context;
    }

}
