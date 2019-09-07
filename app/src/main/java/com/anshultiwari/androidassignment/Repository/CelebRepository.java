package com.anshultiwari.androidassignment.Repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.anshultiwari.androidassignment.Database.CelebDatabase;
import com.anshultiwari.androidassignment.Database.CelebrityDao;
import com.anshultiwari.androidassignment.Model.Celebrity;
import com.anshultiwari.androidassignment.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.lifecycle.LiveData;

public class CelebRepository {
    private static final String TAG = "CelebRepository";

    private CelebrityDao celebrityDao;
    private LiveData<List<Celebrity>> celebs;
    private Context context;
    private RequestQueue mRequestQueue;

    public CelebRepository(Application application) {
        context = application;
        celebrityDao = CelebDatabase.getInstance(application).celebrityDao();
        celebs = celebrityDao.getAllCelebs();
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
    }

    public LiveData<List<Celebrity>> getAllCelebs() {
        return celebs;
    }


    public void celebsApi() {
        Log.d(TAG, "celebsApi: called");
        final StringRequest request = new StringRequest(Request.Method.GET, "https://api.myjson.com/bins/6e60g",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Celebrity> celebsRemote = new ArrayList<>();

                        Log.d(TAG, "onResponse: celebs list response = " + response);

                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONObject celebsObj = responseObj.getJSONObject("celebrities");

                            Iterator<String> iterator = celebsObj.keys(); // Fetch all the keys

                            while (iterator.hasNext()) {
                                String celebKey = iterator.next();
                                JSONObject singleCelebObj = celebsObj.getJSONObject(celebKey);
                                String celebHeight = singleCelebObj.getString("height");
                                String celebAge = singleCelebObj.getString("age");
                                String celebPopularity = singleCelebObj.getString("popularity");
                                String celebImageUrl = singleCelebObj.getString("photo");

                                Celebrity celebrity = new Celebrity(celebKey, celebHeight, celebAge, celebPopularity, celebImageUrl);
                                celebsRemote.add(celebrity);

                            }

                            // Store the celebs retreived from API to the database
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    celebrityDao.insertAllCelebs(celebsRemote);
                                }
                            }).start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.e(TAG, "onErrorResponse: error = " + error.getMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                if (error.networkResponse != null) {
//                    int statusCode = error.networkResponse.statusCode;
//                    try {
//                        String body = new String(error.networkResponse.data, "UTF-8");
//                        Log.e(TAG, "onErrorResponse: error body = " + body);
//                        if (statusCode == 400 || statusCode == 401 || statusCode == 422) {
//                            JSONObject obj = new JSONObject(body);
//                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show();
//
//                        } else {
//                            String errorString = MyVolley.handleError(error);
//                            Toast.makeText(MainActivity.this, errorString, Toast.LENGTH_LONG).show();
//                        }
//
//                    } catch (UnsupportedEncodingException | JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(MainActivity.this, e + "", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "Bad Network", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);

    }
}
