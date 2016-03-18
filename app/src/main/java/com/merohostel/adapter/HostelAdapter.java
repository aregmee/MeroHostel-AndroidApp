package com.merohostel.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.merohostel.R;
import com.merohostel.pojo.Hostel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.HashMap;
import java.util.List;

/**
 * Created by asimsangram on 2/4/16.
 */
public class HostelAdapter extends ArrayAdapter<Hostel>{

    List<Hostel> hostels;

    public HostelAdapter(Context context, List<Hostel> hostels) {
        super(context, 0, hostels);
        this.hostels = hostels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Hostel hostel = hostels.get(position);
        ListImageViewHolder imageViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hostels_list, parent, false);
            imageViewHolder  = new ListImageViewHolder();


            imageViewHolder.hostelImage = (ImageView) convertView.findViewById(R.id.hostelImage);
            imageViewHolder.hostelId = (TextView) convertView.findViewById(R.id.hostel_id);
            imageViewHolder.hostelName = (TextView) convertView.findViewById(R.id.hostel_name);
            imageViewHolder.hostelLocation = (TextView) convertView.findViewById(R.id.hostel_location);
            convertView.setTag(imageViewHolder);
        }else
            imageViewHolder = (ListImageViewHolder) convertView.getTag();

        try{
            Picasso.with(this.getContext()).cancelRequest(imageViewHolder.hostelImage);
            Picasso.with(this.getContext()).load(hostel.getMainPhoto().getUrl()).into(imageViewHolder.hostelImage);
            Log.d("HostelAdapter", "Photo id (" + hostel.getMainPhoto().getId() + " assigned to hostel (" + hostel.getName() + ")");
        }catch (NullPointerException nl){

            Log.e("HostelAdapter", "No main photo for hostel " + hostel.getName());
            imageViewHolder.hostelImage.setImageResource(R.drawable.ic_hostel_icon);
        }
        imageViewHolder.hostelId.setText(hostel.getHid()+"");
        imageViewHolder.hostelName.setText(hostel.getName());
        imageViewHolder.hostelLocation.setText(hostel.getLocation());

        return convertView;
    }

    public class ListImageViewHolder {

        public ImageView hostelImage;
        public TextView hostelId;
        public TextView hostelName;
        public TextView hostelLocation;
    }
}
