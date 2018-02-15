package com.marica.m_note.requestqueue;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Neston on 15/12/2017.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    public static Context context;

    public MySingleton(Context mcontext){
        context=mcontext;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context){
        if (mInstance==null){
            mInstance=new MySingleton(context);
        }
        return mInstance;
    }

    public <T>void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }


}
