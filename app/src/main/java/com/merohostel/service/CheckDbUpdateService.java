package com.merohostel.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.merohostel.Constants;
import com.merohostel.task.FirstTimeHostelsPullTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asimsangram on 2/6/16.
 */
public class CheckDbUpdateService extends IntentService {

    public static final String LOG_TAG = "CheckDbUpdateService";

    public CheckDbUpdateService() {
        super("CheckDbUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(!new NetworkService(getApplicationContext()).isNetworkAvailable()) {
            Log.d(LOG_TAG, "Network is not available. Stopping service.");
            stopSelf();
        }
        Date serverDateModified;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString;
        try {
            response = httpclient.execute(new HttpGet(Constants.SERVER_URL_GET_DATE_MODIFIED));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();

                serverDateModified = format.parse(responseString);

                SharedPreferences prefs = getSharedPreferences(Constants.APP_PACKAGE, Context.MODE_PRIVATE);
                if(prefs.contains(Constants.DATE_MODIFIED)){

                    Date appDateModified = format.parse(prefs.getString(Constants.DATE_MODIFIED, null));

                    if(appDateModified.before(serverDateModified)){

                        pullHostelsFromServer();
                    }
                }

                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {

            Log.e("Error", "Could not check for update");
        }
    }

    public void pullHostelsFromServer(){

        final String serverURL = Constants.SERVER_URL_GET_HOSTEL;
        String responseString = null;
        try {
            if(!new NetworkService(getApplicationContext()).hasActiveInternetConnection())
                throw new Exception("No Internet Connection!");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;

            response = httpclient.execute(new HttpGet(serverURL));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {

            //Log.e("Error", );
            e.printStackTrace();
        }
    }
}
