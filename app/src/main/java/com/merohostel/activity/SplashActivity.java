package com.merohostel.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.merohostel.Constants;
import com.merohostel.R;
import com.merohostel.task.FirstTimeHostelsPullTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startHostelsPullService();
    }

    public void startHostelsPullService() {

        SharedPreferences settings = getSharedPreferences(Constants.APP_PACKAGE, 0);

        if (settings.getBoolean(Constants.FIRST_TIME, true)) {
            //the app is being launched for first time, do something

            try {
                new FirstTimeHostelsPullTask(this).execute(Constants.SERVER_URL_GET_HOSTEL);
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No Internet Connection!");

                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startHostelsPullService();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {

            Log.d("DEBUG", "Starting main activity directly");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
