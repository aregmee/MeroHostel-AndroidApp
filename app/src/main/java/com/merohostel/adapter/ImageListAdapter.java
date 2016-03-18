package com.merohostel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.merohostel.pojo.Photo;

import java.util.List;

/**
 * Created by asimsangram on 2/11/16.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    public interface OnItemClickListener {

        void onItemClick(Photo photo);
    }

    private List<Photo> photos;
    private int itemLayout;
    private final OnItemClickListener listener;

    public ImageListAdapter(List<Photo> photos, int itemLayout, OnItemClickListener listener){

        this.photos = photos;
        this.itemLayout = itemLayout;
        this.listener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        holder.bind(photos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
