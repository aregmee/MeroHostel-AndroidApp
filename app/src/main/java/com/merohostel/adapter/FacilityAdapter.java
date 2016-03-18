package com.merohostel.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.merohostel.Constants;
import com.merohostel.R;
import com.merohostel.pojo.Facility;

import java.util.List;

/**
 * Created by asimsangram on 2/16/16.
 */
public class FacilityAdapter extends RecyclerView.Adapter<FacilityViewHolder> {

    private List<Facility> facilities;
    private int itemLayout;

    public FacilityAdapter(List<Facility> facilities, int itemLayout){

        this.facilities = facilities;
        this.itemLayout = itemLayout;

    }

    @Override
    public FacilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

        return new FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FacilityViewHolder holder, int position) {

        Facility facility = facilities.get(position);
        holder.itemView.setTag(facility);
        holder.facilityTextView.setText(facility.getName());

        int resId = 0;
        String facilityName = facility.getName();
        if(facilityName.equalsIgnoreCase(Constants.ELECTRICITY))
            resId = R.drawable.ic_electricity_icon;
        else if(facilityName.equalsIgnoreCase(Constants.TELEVISION))
            resId = R.drawable.ic_television_icon;
        else if(facilityName.equalsIgnoreCase(Constants.WIFI))
            resId = R.drawable.ic_wifi_icon;
        else if(facilityName.equalsIgnoreCase(Constants.HOSTEL_WARDEN))
            resId = R.drawable.ic_warden_icon;
        else if(facilityName.equalsIgnoreCase(Constants.HOT_WATER))
            resId = R.drawable.ic_hot_water_icon;
        else if(facilityName.equalsIgnoreCase(Constants.GYM))
            resId = R.drawable.ic_gym_icon;
        else if(facilityName.equalsIgnoreCase(Constants.LAUNDRY))
            resId = R.drawable.ic_laundry_icon;
        else if(facilityName.equalsIgnoreCase(Constants.LOCKER))
            resId = R.drawable.ic_locker_icon;

        holder.facilityTextView.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }
}
