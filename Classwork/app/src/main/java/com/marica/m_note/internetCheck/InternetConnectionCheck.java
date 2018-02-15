package com.marica.m_note.internetCheck;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Neston on 01/01/2018.
 */

public class InternetConnectionCheck {
    private Context mctxt;

    public InternetConnectionCheck(Context context) {
        mctxt=context;
    }

    public static boolean isConnectedToInternet(Context context)
    {
        // Check intenet connectivity
        boolean connected;
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager)context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            connected = (   conMgr.getActiveNetworkInfo() != null &&
                    conMgr.getActiveNetworkInfo().isAvailable() &&
                    conMgr.getActiveNetworkInfo().isConnected()   );
        }catch (Exception e)
        {
            return false;
        }

        return connected;

    }
}
