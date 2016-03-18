package com.merohostel.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.merohostel.Constants;
import com.merohostel.R;
import com.merohostel.adapter.HostelAdapter;
import com.merohostel.pojo.Hostel;
import com.merohostel.service.CheckDbUpdateService;
import com.merohostel.task.FirstTimeHostelsPullTask;
import com.merohostel.task.UniqueAddressesPullTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "DemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_white_hostel_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent checkDbUpdateServiceIntent = new Intent(this, CheckDbUpdateService.class);
        startService(checkDbUpdateServiceIntent);

        List<Hostel> hostelList = new ArrayList<>();

        HostelAdapter hostelAdapter = new HostelAdapter(this, hostelList);

        ListView hostelsListView = (ListView) findViewById(R.id.hostels_list_view);
        hostelsListView.setAdapter(hostelAdapter);

        //TODO: Pull images from main site

        try {
            hostelAdapter.add(new Hostel(67, "Aayush Boys Hostel", "Katyayani, Baneshwor", Hostel.Gender.BOYS.toString(), null));
            hostelAdapter.add(new Hostel(264, "Kanya Chhatrabas (Girls Hostel)", "White House College, Mid Baneshwor", Hostel.Gender.GIRLS.toString(), null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO call CheckDbUpdateService
        Spinner spinner = (Spinner) findViewById(R.id.genderSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

                new UniqueAddressesPullTask(getApplicationContext(), autoCompleteTextView, (String) parentView.getItemAtPosition(position)).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void viewDetail(View view){

        Intent intent = new Intent(this, HostelDetailActivity.class);
        TextView hostelIdTextView = (TextView) view.findViewById(R.id.hostel_id);
        intent.putExtra(Constants.HOSTEL_ID, hostelIdTextView.getText().toString());

        startActivity(intent);
    }

    public void search(View view){
        Intent intent = new Intent(this,SearchActivity.class);
        Spinner mySpinner=(Spinner) findViewById(R.id.genderSpinner);
        AutoCompleteTextView hostelLocationTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        if(hostelLocationTextView.getText().toString().equals("")){
            hostelLocationTextView.setError("This is required field.");
            return;
        }
        intent.putExtra(Constants.HOSTEL_LOCATION, hostelLocationTextView.getText().toString());
        intent.putExtra(Constants.HOSTEL_GENDER, mySpinner.getSelectedItem().toString());
        startActivity(intent);
    }
}
