package com.anshultiwari.androidassignment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class MyVolley {
    private static MyVolley mInstance = null;
    private RequestQueue mRequestQueue;

    private MyVolley() {
        //request queue
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static MyVolley getInstance() {
        if (mInstance == null) {
            mInstance = new MyVolley();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }


    public static String handleError(VolleyError error) {
        String message = null;
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            message = "Bad network Connection";
        } else if (error instanceof AuthFailureError) {
            message = "Failed to perform a request";
        } else if (error instanceof ServerError) {
            message = "Server error";
        } else if (error instanceof NetworkError) {
            message = "Network error while performing a request";
        } else if (error instanceof ParseError) {
            message = "Server response could not be parsed";
        }
        return message;
    }
}
