package com.example.heman.travelsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.CustomViewHolder> {

    private ArrayList<String> imageUrl;
    private Context context;

    public PhotosAdapter(Context context, ArrayList<String> imageUrl) {

        this.context = context;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.photos_layout, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Uri uri = Uri.parse(imageUrl.get(position));
        Glide.with(context)
                .load(uri)
                .into(holder.placePhotoImageView);

    }

    @Override
    public int getItemCount() {
        return imageUrl.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView placePhotoImageView;
        public CustomViewHolder (View view) {
            super(view);
            placePhotoImageView =  (ImageView) view.findViewById(R.id.placePhotoImageView);
        }
    }
}
