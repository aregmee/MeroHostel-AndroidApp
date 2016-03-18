package com.merohostel.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.merohostel.R;
import com.squareup.picasso.Picasso;

public class FullScreenPhotoActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String[] photoUrls;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);

        Intent callingIntent = getIntent();
        photoUrls = callingIntent.getStringArrayExtra("photoUrls");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), photoUrls);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public static class PlaceholderFragment extends Fragment {

        private String photoUrl;

        public PlaceholderFragment() {

        }

        public static PlaceholderFragment newInstance(String photoUrl) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.photoUrl = photoUrl;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_full_screen_photo, container, false);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
                    Picasso.with(getActivity()).load(photoUrl).into(imageView);
                }
            });

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        String[] photoUrls;

        public SectionsPagerAdapter(FragmentManager fm, String[] photoUrls) {
            super(fm);
            this.photoUrls = photoUrls;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(photoUrls[position]);
        }

        @Override
        public int getCount() {
            return photoUrls.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
