package com.merohostel.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.merohostel.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by asimsangram on 2/6/16.
 */
public class NetworkService {

    private Context context;

    public NetworkService(Context context){

        this.context = context;
    }

    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL(Constants.CHECK_CONNECTION_URL)
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                Log.d("DEBUG", String.valueOf(urlc.getResponseCode()));
                Log.d("DEBUG", urlc.getResponseMessage());
                Log.d("DEBUG", String.valueOf(urlc.getContentLength()));
                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e("Error", "Error checking internet connection", e);
            }
        } else {
            Log.d("Error", "No network available!");
        }
        return false;
    }
}
