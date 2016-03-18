package com.merohostel.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.merohostel.Constants;
import com.merohostel.R;
import com.merohostel.adapter.HostelAdapter;
import com.merohostel.database.DbHelper;
import com.merohostel.database.HostelEntry;
import com.merohostel.database.HostelPhotoEntry;
import com.merohostel.database.PhotoEntry;
import com.merohostel.pojo.Hostel;
import com.merohostel.pojo.Photo;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_white_hostel_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent callingIntent = getIntent();
        String hostelLocation = callingIntent.getStringExtra(Constants.HOSTEL_LOCATION);
        String gender = callingIntent.getStringExtra(Constants.HOSTEL_GENDER);
        TextView searchResult = (TextView)findViewById(R.id.searchListHeader);
        String searchHeaderFirstPart = gender+" hostels nearby ";
        String searchHeader = searchHeaderFirstPart + hostelLocation;

        SpannableString spanString = new SpannableString(searchHeader);
        spanString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, gender.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), searchHeaderFirstPart.length(), searchHeader.length(), 0);
        searchResult.setText(spanString);

        new SearchHostelTask(this).execute(hostelLocation, gender);

    }
    class SearchHostelTask extends AsyncTask<String, String, List<Hostel>> {
        private Activity activity;

        public SearchHostelTask(SearchActivity searchActivity) {
            activity = searchActivity;
        }

        @Override
        protected List<Hostel> doInBackground(String... searchInput) {
            String hostelLocation = searchInput[0];
            String hostelGender = searchInput[1].toUpperCase();
            List<Hostel> hostelList = new ArrayList<>();
            SQLiteDatabase db = new DbHelper(getApplicationContext()).getReadableDatabase();

            String searchQuery = "SELECT * FROM " + HostelEntry.TABLE_NAME + " WHERE " +
                    HostelEntry.COLUMN_NAME_GENDER + "=? AND "+HostelEntry.COLUMN_NAME_LOCATION+" LIKE ?";

            Cursor hostelSearch = db.rawQuery(searchQuery, new String[]{hostelGender,"%" + hostelLocation + "%"});
            hostelSearch.moveToFirst();
            try {
                do {
                    int hid = hostelSearch.getInt(hostelSearch.getColumnIndex(HostelEntry.COLUMN_NAME_HID));
                    String hName = hostelSearch.getString(hostelSearch.getColumnIndex(HostelEntry.COLUMN_NAME_HNAME));
                    String hGender = hostelSearch.getString(hostelSearch.getColumnIndex(HostelEntry.COLUMN_NAME_GENDER));
                    String hLocation = hostelSearch.getString(hostelSearch.getColumnIndex(HostelEntry.COLUMN_NAME_LOCATION));

                    String photoQuery = "SELECT * FROM " + PhotoEntry.TABLE_NAME + " p" +
                            " INNER JOIN " + HostelPhotoEntry.TABLE_NAME + " hp ON hp." +
                            HostelPhotoEntry.COLUMN_NAME_PID + "=p." + PhotoEntry.COLUMN_NAME_ID +
                            " WHERE " + HostelPhotoEntry.COLUMN_NAME_HID + "=?";

                    Cursor photoC = db.rawQuery(photoQuery, new String[]{hid + ""});

                    List<Photo> photoList = new ArrayList<>();
                    if (photoC.moveToFirst()) {

                        do {
                            int id = photoC.getInt(photoC.getColumnIndex(PhotoEntry.COLUMN_NAME_ID));
                            String url = photoC.getString(photoC.getColumnIndex(PhotoEntry.COLUMN_NAME_PHOTO_URL));
                            boolean main = false;
                            if (photoC.getInt(photoC.getColumnIndex(PhotoEntry.COLUMN_NAME_MAIN)) == 1)
                                main = true;
                            else if (photoC.getInt(photoC.getColumnIndex(PhotoEntry.COLUMN_NAME_MAIN)) == 0)
                                main = false;
                            photoList.add(new Photo(id, url, main));
                        } while (photoC.moveToNext());
                    }
                    photoC.close();
                    hostelList.add(new Hostel(hid, hName, hLocation, hGender, photoList));
                }while (hostelSearch.moveToNext());
            } catch (Exception e) {

                Log.e("Error", e.getMessage());

            } finally {

                hostelSearch.close();
            }
            return hostelList;

        }
        @Override
        protected void onPostExecute(final List<Hostel> hostels) {
            super.onPostExecute(hostels);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (hostels.size() != 0) {
                        HostelAdapter hostelAdapter = new HostelAdapter(activity, hostels);
                        ListView hostelsListView = (ListView) findViewById(R.id.hostels_list_view);
                        hostelsListView.setAdapter(hostelAdapter);
                    } else {
                        TextView noResult = (TextView)findViewById(R.id.noResult);
                        noResult.setText("No Result found");
                    }

                }
            });
        }
    }

    public void viewDetail(View view){

        Intent intent = new Intent(this, HostelDetailActivity.class);
        TextView hostelIdTextView = (TextView) view.findViewById(R.id.hostel_id);
        intent.putExtra(Constants.HOSTEL_ID, hostelIdTextView.getText().toString());

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return false;
    }
}
