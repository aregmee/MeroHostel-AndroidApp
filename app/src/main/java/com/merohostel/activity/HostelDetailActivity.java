package com.merohostel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.merohostel.Constants;
import com.merohostel.R;
import com.merohostel.adapter.FacilityAdapter;
import com.merohostel.task.PullHostelTask;

public class HostelDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_white_hostel_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent callingIntent = getIntent();

        String hostelId = callingIntent.getStringExtra(Constants.HOSTEL_ID);

        new PullHostelTask(this).execute(hostelId);
    }

    public void QuickInquiry(View view){
        TextView hostelName = (TextView)findViewById(R.id.hostel_name);
        Intent inquiry = new Intent(this,QuickInquiryActivity.class);
        inquiry.putExtra(Constants.HOSTEL_NAME,hostelName.getText().toString());
        startActivity(inquiry);
        };

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
