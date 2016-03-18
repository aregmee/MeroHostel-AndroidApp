package com.merohostel.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.merohostel.R;
import com.merohostel.pojo.Photo;
import com.squareup.picasso.Picasso;

/**
 * Created by asimsangram on 2/11/16.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    public void bind(final Photo photo, final ImageListAdapter.OnItemClickListener listener){

        itemView.setTag(photo);
        imageView.setImageBitmap(null);
        Picasso.with(imageView.getContext()).cancelRequest(imageView);
        Picasso.with(imageView.getContext()).load(photo.getUrl()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClick(photo);
            }
        });
    }
}
