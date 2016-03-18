package com.merohostel.task;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merohostel.R;
import com.merohostel.activity.FullScreenPhotoActivity;
import com.merohostel.adapter.FacilityAdapter;
import com.merohostel.adapter.ImageListAdapter;
import com.merohostel.database.DbHelper;
import com.merohostel.database.HostelEntry;
import com.merohostel.database.HostelFacilityEntry;
import com.merohostel.database.HostelFeeEntry;
import com.merohostel.database.HostelPhotoEntry;
import com.merohostel.database.PhotoEntry;
import com.merohostel.pojo.Facility;
import com.merohostel.pojo.FeeStructure;
import com.merohostel.pojo.Hostel;
import com.merohostel.pojo.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asimsangram on 2/7/16.
 */
public class PullHostelTask extends AsyncTask<String, String, Hostel> {

    public static final String LOG_TAG = "PullHostelTask";
    private Activity activity;

    public PullHostelTask(Activity activity) {

        this.activity = activity;
    }

    @Override
    protected Hostel doInBackground(String... ids) {

        String hostelId = ids[0];

        SQLiteDatabase db = new DbHelper(activity).getReadableDatabase();

        String hostelQuery = "SELECT * FROM " + HostelEntry.TABLE_NAME + " WHERE " +
                HostelEntry.COLUMN_NAME_HID + "=?";

        Cursor hostelC = db.rawQuery(hostelQuery, new String[]{hostelId});

        hostelC.moveToFirst();

        try{

            int hid = hostelC.getInt(hostelC.getColumnIndex(HostelEntry.COLUMN_NAME_HID));
            String hName = hostelC.getString(hostelC.getColumnIndex(HostelEntry.COLUMN_NAME_HNAME));
            String hGender = hostelC.getString(hostelC.getColumnIndex(HostelEntry.COLUMN_NAME_GENDER));
            String hLocation = hostelC.getString(hostelC.getColumnIndex(HostelEntry.COLUMN_NAME_LOCATION));

            String photoQuery = "SELECT * FROM " + PhotoEntry.TABLE_NAME + " p" +
                    " INNER JOIN " + HostelPhotoEntry.TABLE_NAME + " hp ON hp." +
                    HostelPhotoEntry.COLUMN_NAME_PID + "=p." + PhotoEntry.COLUMN_NAME_ID +
                    " WHERE " + HostelPhotoEntry.COLUMN_NAME_HID + "=?";

            Cursor photoC = db.rawQuery(photoQuery, new String[]{hid+""});

            final List<Photo> photoList = new ArrayList<>();
            if(photoC.moveToFirst()) {

                do {
                    int id = photoC.getInt(photoC.getColumnIndex(PhotoEntry.COLUMN_NAME_ID));
                    String url = photoC.getString(photoC.getColumnIndex(PhotoEntry.COLUMN_NAME_PHOTO_URL));
                    boolean main = false;
                    if(photoC.getInt(photoC.getColumnIndex(PhotoEntry.COLUMN_NAME_MAIN)) == 1)
                        main  = true;
                    else if(photoC.getInt(photoC.getColumnIndex(PhotoEntry.COLUMN_NAME_MAIN)) == 0)
                        main = false;
                    photoList.add(new Photo(id, url, main));
                } while (photoC.moveToNext());
            }
            Log.d(LOG_TAG, photoList + String.valueOf(photoC.getCount()));
            photoC.close();

            String facilityQuery = "SELECT * FROM " + HostelFacilityEntry.TABLE_NAME +
                    " WHERE " + HostelFacilityEntry.COLUMN_NAME_HID + "=" + hostelId;

            Cursor facilityC = db.rawQuery(facilityQuery, new String[]{});

            List<Facility> facilities = new ArrayList<>();
            if(facilityC.moveToFirst()){

                do{

                    int id = facilityC.getInt(facilityC.getColumnIndex(HostelFacilityEntry.COLUMN_NAME_ID));
                    String name = facilityC.getString(facilityC.getColumnIndex(HostelFacilityEntry.COLUMN_NAME_FACILITY));

                    facilities.add(new Facility(id, name));
                }while (facilityC.moveToNext());
            }
            facilityC.close();

            String feeStructureQuery = "SELECT * FROM " + HostelFeeEntry.TABLE_NAME +
                    " WHERE " + HostelFeeEntry.COLUMN_NAME_HID + "=" + hostelId;

            Cursor feeC = db.rawQuery(feeStructureQuery, new String[]{});

            FeeStructure feeStructure = null;

            if(feeC.moveToFirst()){

                int id = feeC.getInt(feeC.getColumnIndex(HostelFeeEntry.COLUMN_NAME_ID));
                int admission = feeC.getInt(feeC.getColumnIndex(HostelFeeEntry.COLUMN_NAME_ADMISSION));
                int secDeposit = feeC.getInt(feeC.getColumnIndex(HostelFeeEntry.COLUMN_NAME_SEC_DEPOSIT));
                int oneBed = feeC.getInt(feeC.getColumnIndex(HostelFeeEntry.COLUMN_NAME_ONE_BED));
                int twoBed = feeC.getInt(feeC.getColumnIndex(HostelFeeEntry.COLUMN_NAME_TWO_BED));
                int threeBed = feeC.getInt(feeC.getColumnIndex(HostelFeeEntry.COLUMN_NAME_THREE_BED));
                int fourBed = feeC.getInt(feeC.getColumnIndex(HostelFeeEntry.COLUMN_NAME_FOUR_BED));

                feeStructure = new FeeStructure(id, admission, secDeposit, oneBed, twoBed, threeBed, fourBed);
            }

            return new Hostel(hid, hName, hLocation, hGender, photoList, feeStructure, facilities);
        }catch (Exception e){

            Log.e("Error", e.getMessage());
        }finally {

            hostelC.close();
        }

        return null;
    }

    @Override
    protected void onPostExecute(final Hostel hostel) {
        super.onPostExecute(hostel);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView hostelNameTextView = (TextView) activity.findViewById(R.id.hostel_name);
                hostelNameTextView.setText(hostel.getName());

                TextView hostelLocationTextView = (TextView) activity.findViewById(R.id.hostel_location);
                hostelLocationTextView.setText(hostel.getLocation());

                ImageView hostelImageView = (ImageView) activity.findViewById(R.id.hostelImage);

                try{
                    if(hostel.getMainPhoto() != null) {
                        Picasso.with(activity)
                                .load(hostel.getMainPhoto().getUrl())
                                .transform(new RoundedTransformation(30, 0))
                                .fit()
                                .into(hostelImageView);
                    }

                    final List<Photo> photos = hostel.getPhotoList();
                    final String[] urls = new String[photos.size()];
                    if(photos.size() > 0){

                        for(int i = 0; i < photos.size(); i++){

                            urls[i] = photos.get(i).getUrl();
                        }
                    }

                    if(hostel.getPhotoList() != null) {

                        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.photoList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(new ImageListAdapter(hostel.getPhotoList(), R.layout.photo, new ImageListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Photo photo) {

                                /*Intent intent = new Intent(activity, FullScreenPhotoActivity.class);
                                intent.putExtra("photoUrl", photo.getUrl());
                                activity.startActivity(intent);*/
                                Intent intent = new Intent(activity, FullScreenPhotoActivity.class);
                                intent.putExtra("photoUrls", urls);
                                activity.startActivity(intent);
                            }
                        }));
                        com.merohostel.LinearLayoutManager layoutManager = new com.merohostel.LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true);
                        layoutManager.setReverseLayout(false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                    }
                }catch (NullPointerException nl){
                    Log.d(LOG_TAG, "Hiding photo card view");
                    activity.findViewById(R.id.photos_card_view).setVisibility(View.GONE);
                }

                if(hostel.getFacilities() != null){

                    RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.facilities_recycler_view);
                    recyclerView.setAdapter(new FacilityAdapter(hostel.getFacilities(), R.layout.facility));

                    GridLayoutManager manager = new GridLayoutManager(activity, 2);
                    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return 1;
                        }
                    });
                    recyclerView.setLayoutManager(manager);
                }else{

                    Log.d(LOG_TAG, "Hiding facilities card view");
                    activity.findViewById(R.id.facilities_card_view).setVisibility(View.GONE);
                }

                if(hostel.getFeeStructure() != null){

                    FeeStructure feeStructure = hostel.getFeeStructure();
                    LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.fees);
                    TextView admTextView = new TextView(activity);
                    TextView secTextView = new TextView(activity);
                    TextView oneBedTextView = new TextView(activity);
                    TextView twoBedTextView = new TextView(activity);
                    TextView threeBedTextView = new TextView(activity);
                    TextView fourBedTextView = new TextView(activity);
                    if(feeStructure.getAdmissionFee() != 0) {
                        admTextView.setText("Admission Fee: " + feeStructure.getAdmissionFee());
                        linearLayout.addView(admTextView);
                    }
                    if(feeStructure.getSecurityDeposit() != 0) {
                        secTextView.setText("Security Deposit: " + feeStructure.getSecurityDeposit());
                        linearLayout.addView(secTextView);
                    }
                    if(feeStructure.getOneBed() != 0) {
                        oneBedTextView.setText("One bed per room: " + feeStructure.getOneBed());
                        linearLayout.addView(oneBedTextView);
                    }
                    if(feeStructure.getTwoBed() != 0) {
                        twoBedTextView.setText("Two beds per room: " + feeStructure.getTwoBed());
                        linearLayout.addView(twoBedTextView);
                    }
                    if(feeStructure.getThreeBed() != 0) {
                        threeBedTextView.setText("Three beds per room: " + feeStructure.getThreeBed());
                        linearLayout.addView(threeBedTextView);
                    }
                    if(feeStructure.getFourBed() != 0) {
                        fourBedTextView.setText("Four beds per room: " + feeStructure.getFourBed());
                        linearLayout.addView(fourBedTextView);
                    }

                }else{

                    Log.d(LOG_TAG, "Hiding fee structure card view");
                    activity.findViewById(R.id.feeStructure_card_view).setVisibility(View.GONE);
                }
            }
        });
    }


    public class RoundedTransformation implements
            com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin; // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(),
                    source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
                    - margin, source.getHeight() - margin), radius, radius, paint);

            if (source != output) {
                source.recycle();
            }

            return output;
        }

        @Override
        public String key() {
            return "rounded";
        }
    }
}
