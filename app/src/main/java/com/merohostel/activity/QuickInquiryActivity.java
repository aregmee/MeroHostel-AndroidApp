package com.merohostel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.merohostel.Constants;
import com.merohostel.R;
import com.merohostel.adapter.HostelAdapter;
import com.merohostel.pojo.Hostel;
import com.merohostel.service.NetworkService;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class QuickInquiryActivity extends AppCompatActivity {
    HttpURLConnection urlConnection = null;
    URL url = null;
    JSONObject object = null;
    InputStream inStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_inquiry);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_white_hostel_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent callingIntent = getIntent();
        String hostelName = callingIntent.getStringExtra(Constants.HOSTEL_NAME);
        TextView hostelNameView = (TextView)findViewById(R.id.hostelName);
        hostelNameView.setText(hostelName);

    }

    public void sendInquiryEmail(final View view) {
            if (new NetworkService(getApplicationContext()).isNetworkAvailable()) {
                TextView nameView = (TextView) findViewById(R.id.userName);
                TextView emailAddressView = (TextView) findViewById(R.id.email_address);
                TextView contactNumberView = (TextView) findViewById(R.id.contactNumber);
                TextView hostelNameView = (TextView) findViewById(R.id.hostelName);
                TextView messageNameView = (TextView) findViewById(R.id.message);

                RadioGroup roomGroup = (RadioGroup) findViewById(R.id.roomNumber);
                int selectedRadio = roomGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadio);

                List<TextView> textViews = new ArrayList<>();
                textViews.add(nameView);
                textViews.add(emailAddressView);
                textViews.add(contactNumberView);
                textViews.add(messageNameView);

                if(!validateField(textViews)){
                    return;
                }

                String name = nameView.getText().toString();
                String hostelName = hostelNameView.getText().toString();
                String bedType = selectedRadioButton.getText().toString();
                String message = messageNameView.getText().toString();
                String emailAddress = emailAddressView.getText().toString();
                String contactNumber = contactNumberView.getText().toString();
                new SendEmail(this).execute(name, emailAddress, contactNumber, bedType,message,hostelName);
                Toast.makeText(QuickInquiryActivity.this, "Inquiry is submitted successfully.", Toast.LENGTH_SHORT).show();
            }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection!");

            TextView hostelNameView = (TextView) findViewById(R.id.hostelName);
            final Intent inquiry = new Intent(this, QuickInquiryActivity.class);
            inquiry.putExtra(Constants.HOSTEL_NAME, hostelNameView.getText().toString());

            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    sendInquiryEmail(view);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(inquiry);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
    }

    public class SendEmail extends AsyncTask<String, String, Boolean> {

        private Activity activity;

        public SendEmail(QuickInquiryActivity quickInquiryActivity) {
            activity=quickInquiryActivity;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String comments = "Hostel Name: "+params[5]+"\nBed Type: "+params[3]+"\nMessage: "+params[4];
            try {
                String uri = Uri.parse(Constants.SERVER_URL_SEND_EMAIL)
                        .buildUpon()
                        .appendQueryParameter("first_name", params[0])
                        .appendQueryParameter("last_name", "")
                        .appendQueryParameter("email", params[1])
                        .appendQueryParameter("telephone", params[2])
                        .appendQueryParameter("comments", comments)
                        .appendQueryParameter("emailType", "Hostel Inquiry")
                        .build().toString();
                url = new URL(uri);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(QuickInquiryActivity.this, "Failed to submit the Inquiry.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean validateField(List<TextView> textViews){
        for(TextView textView:textViews){
            if(textView.getText().toString().equals("")){
                textView.setError("This is a required field.");
                return false;
            }
        }
        for(TextView textView:textViews){
            if(textView.getId()==R.id.email_address){
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(textView.getText().toString()).matches()){
                    textView.setError("Email Address invalid.");
                    return false;
                }
            }else if(textView.getId()==R.id.userName){
                Pattern namePattern = Pattern.compile(Constants.NAME_PATTERN);
                if(!namePattern.matcher(textView.getText().toString()).matches()){
                    textView.setError("Name should only contain letter and space only.");
                    return false;
                }
            }else if(textView.getId()==R.id.contactNumber){
                Pattern phoneNumberPattern = Pattern.compile(Constants.PHONE_PATTERN);
                if(!phoneNumberPattern.matcher(textView.getText().toString()).matches()){
                    textView.setError("Phone number should only contain numbers or phone number length is too short.");
                    return false;
                }
            }
        }
        return true;
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
