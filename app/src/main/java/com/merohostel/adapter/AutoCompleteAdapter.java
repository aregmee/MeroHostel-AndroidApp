package com.merohostel.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.merohostel.R;

/**
 * Created by asimsangram on 2/5/16.
 */
public class AutoCompleteAdapter extends ArrayAdapter<String> {
    public AutoCompleteAdapter(Context context, String[] locations) {
        super(context, 0, locations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String location = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.autocomplete_textview_list, parent, false);
        }
        TextView text = (TextView) convertView.findViewById(R.id.textview);
        if(location != null)
        {
            text.setTextColor(Color.parseColor("#000000"));
            text.setText(location);
        }

        return convertView;
    }
}
