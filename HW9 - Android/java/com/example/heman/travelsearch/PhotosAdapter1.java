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

public class PhotosAdapter1 extends RecyclerView.Adapter<PhotosAdapter1.CustomViewHolder1> {

    private ArrayList<String> imageUrl;
    private Context context;

    public PhotosAdapter1(Context context, ArrayList<String> imageUrl) {

        this.context = context;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public CustomViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.photos_layout1, null);
        return new CustomViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder1 holder, int position) {

        Log.d("YOu you", "anto");
        Uri uri = Uri.parse(imageUrl.get(position));
        Glide.with(context)
                .load(uri)
                .into(holder.placePhotoImageView);
    }

    @Override
    public int getItemCount() {
        return imageUrl.size();
    }

    public class CustomViewHolder1 extends RecyclerView.ViewHolder {
        public ImageView placePhotoImageView;
        public CustomViewHolder1 (View view) {
            super(view);
            placePhotoImageView =  (ImageView) view.findViewById(R.id.placePhotoImageView1);
        }
    }
}
