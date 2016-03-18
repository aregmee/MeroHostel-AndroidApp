package com.merohostel.task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.merohostel.adapter.AutoCompleteAdapter;
import com.merohostel.database.DbHelper;
import com.merohostel.database.HostelEntry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by asimsangram on 2/6/16.
 */
public class UniqueAddressesPullTask extends AsyncTask<String, String, Set<String>> {

    private Context context;
    private AutoCompleteTextView autoCompleteTextView;
    private String genderToPull;

    public UniqueAddressesPullTask(Context context, AutoCompleteTextView autoCompleteTextView, String genderToPull) {

        this.context = context;
        this.autoCompleteTextView = autoCompleteTextView;
        this.genderToPull = genderToPull;
    }

    @Override
    protected Set<String> doInBackground(String... strings) {

        SQLiteDatabase db = new DbHelper(context).getReadableDatabase();

        String[] projection = {

                HostelEntry.COLUMN_NAME_GENDER,
                HostelEntry.COLUMN_NAME_LOCATION
        };
        Cursor c = db.query(true, HostelEntry.TABLE_NAME, projection, null, null, null, null, null, null);

        c.moveToFirst();

        Set<String> hostelUniqueAddresses = new HashSet<>();
        try {
            do {
                if(c.getString(c.getColumnIndex(HostelEntry.COLUMN_NAME_GENDER)).equalsIgnoreCase(genderToPull))
                    hostelUniqueAddresses.add(c.getString(c.getColumnIndex(HostelEntry.COLUMN_NAME_LOCATION)));
            } while (c.moveToNext());
        }catch(Exception e){

            Log.e("Error", e.getMessage());
        }finally {

            c.close();
        }

        return hostelUniqueAddresses;
    }

    @Override
    protected void onPostExecute(Set<String> hostelUniqueAddresses) {
        super.onPostExecute(hostelUniqueAddresses);

        if(hostelUniqueAddresses.size() > 0){

            String uniqueAddresses[] = new String[hostelUniqueAddresses.size()];

            Iterator<String> uniqueAddressesIterator = hostelUniqueAddresses.iterator();

            for(int i = 0; uniqueAddressesIterator.hasNext(); i++){

                uniqueAddresses[i] = uniqueAddressesIterator.next();
            }

            AutoCompleteAdapter hostelLocationsAdapter =
                    new AutoCompleteAdapter(context, uniqueAddresses);

            autoCompleteTextView.setAdapter(hostelLocationsAdapter);
        }
    }
}