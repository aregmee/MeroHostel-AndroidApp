package com.merohostel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.merohostel.R;

/**
 * Created by asimsangram on 2/17/16.
 */
public class FacilityViewHolder extends RecyclerView.ViewHolder{

    public TextView facilityTextView;

    public FacilityViewHolder(View itemView){

        super(itemView);
        this.facilityTextView = (TextView) itemView.findViewById(R.id.facility_text_view);
    }
}
