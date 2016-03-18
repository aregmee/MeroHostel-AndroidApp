package com.merohostel.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.merohostel.Constants;
import com.merohostel.activity.MainActivity;
import com.merohostel.database.DbHelper;
import com.merohostel.database.HostelFeeEntry;
import com.merohostel.database.HostelEntry;
import com.merohostel.database.HostelFacilityEntry;
import com.merohostel.database.HostelPhotoEntry;
import com.merohostel.database.PhotoEntry;
import com.merohostel.pojo.Facility;
import com.merohostel.pojo.FeeStructure;
import com.merohostel.pojo.Hostel;
import com.merohostel.pojo.Photo;
import com.merohostel.service.NetworkService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asimsangram on 2/6/16.
 */
public class FirstTimeHostelsPullTask extends AsyncTask<String, String, String> {

    private Activity activity;

    public FirstTimeHostelsPullTask(Activity activity){

        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... uri) {

        final String serverURL = uri[0];
        String responseString = null;
        try {
            if(!new NetworkService(activity.getApplicationContext()).hasActiveInternetConnection())
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

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("No Internet Connection!");

                    builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            new FirstTimeHostelsPullTask(activity).execute(serverURL);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            e.printStackTrace();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null)
            Log.d("DEBUG", result);
        try {
            JSONArray hostelsJSONArray = new JSONArray(result);

            for(int i = 0;i < hostelsJSONArray.length(); i++){

                try {
                    Hostel hostel = new Hostel(hostelsJSONArray.getJSONObject(i));

                    SQLiteDatabase db = new DbHelper(activity.getApplicationContext()).getWritableDatabase();

                    ContentValues hostelValues = new ContentValues();

                    hostelValues.put(HostelEntry.COLUMN_NAME_HID, hostel.getHid());
                    hostelValues.put(HostelEntry.COLUMN_NAME_HNAME, hostel.getName());
                    hostelValues.put(HostelEntry.COLUMN_NAME_GENDER, hostel.getGender().toString());
                    hostelValues.put(HostelEntry.COLUMN_NAME_LOCATION, hostel.getLocation());

                    db.insert(
                            HostelEntry.TABLE_NAME,
                            null,
                            hostelValues
                    );

                    List<ContentValues> hostelPhotoValues = new ArrayList<>();
                    List<ContentValues> photoValues = new ArrayList<>();

                    for(Photo hostelPhoto: hostel.getPhotoList()){

                        ContentValues hostelPhotoValue = new ContentValues();
                        ContentValues photoValue = new ContentValues();

                        hostelPhotoValue.put(HostelPhotoEntry.COLUMN_NAME_HID, hostel.getHid());
                        hostelPhotoValue.put(HostelPhotoEntry.COLUMN_NAME_PID, hostelPhoto.getId());

                        photoValue.put(PhotoEntry.COLUMN_NAME_ID, hostelPhoto.getId());
                        photoValue.put(PhotoEntry.COLUMN_NAME_PHOTO_URL, hostelPhoto.getUrl());
                        photoValue.put(PhotoEntry.COLUMN_NAME_MAIN, hostelPhoto.isMain() ? 1 : 0);

                        hostelPhotoValues.add(hostelPhotoValue);
                        photoValues.add(photoValue);
                    }

                    if(hostel.getPhotoList().size() > 0) {

                        for(ContentValues hostelPhotoValue: hostelPhotoValues) {
                            db.insert(

                                    HostelPhotoEntry.TABLE_NAME,
                                    null,
                                    hostelPhotoValue
                            );
                        }

                        for(ContentValues photoValue: photoValues) {
                            db.insert(

                                    PhotoEntry.TABLE_NAME,
                                    null,
                                    photoValue
                            );
                        }
                    }

                    for(Facility facility: hostel.getFacilities()){

                        ContentValues facilityContentValues = new ContentValues();

                        facilityContentValues.put(HostelFacilityEntry.COLUMN_NAME_HID, hostel.getHid());
                        facilityContentValues.put(HostelFacilityEntry.COLUMN_NAME_FACILITY, facility.getName());

                        db.insert(

                                HostelFacilityEntry.TABLE_NAME,
                                null,
                                facilityContentValues
                        );
                    }

                    FeeStructure feeStructure = hostel.getFeeStructure();

                    ContentValues feeStructureContentValues = new ContentValues();

                    feeStructureContentValues.put(HostelFeeEntry.COLUMN_NAME_HID, hostel.getHid());
                    feeStructureContentValues.put(HostelFeeEntry.COLUMN_NAME_ADMISSION, feeStructure.getAdmissionFee());
                    feeStructureContentValues.put(HostelFeeEntry.COLUMN_NAME_SEC_DEPOSIT, feeStructure.getSecurityDeposit());
                    feeStructureContentValues.put(HostelFeeEntry.COLUMN_NAME_ONE_BED, feeStructure.getOneBed());
                    feeStructureContentValues.put(HostelFeeEntry.COLUMN_NAME_TWO_BED, feeStructure.getTwoBed());
                    feeStructureContentValues.put(HostelFeeEntry.COLUMN_NAME_THREE_BED, feeStructure.getThreeBed());
                    feeStructureContentValues.put(HostelFeeEntry.COLUMN_NAME_FOUR_BED, feeStructure.getFourBed());


                    db.insert(

                            HostelFeeEntry.TABLE_NAME,
                            null,
                            feeStructureContentValues
                    );

                    db.close();
                    activity.getSharedPreferences(Constants.APP_PACKAGE, 0)
                            .edit()
                            .putBoolean(Constants.FIRST_TIME, false)
                            .apply();
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                }catch (Exception e){

                    Log.e("Error" + i, e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }
}
