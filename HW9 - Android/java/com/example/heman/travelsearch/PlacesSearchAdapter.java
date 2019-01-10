package com.example.heman.travelsearch;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PlacesSearchAdapter extends RecyclerView.Adapter<PlacesSearchAdapter.MyViewHolder> {

    private List<List<String>> placeData;
    private DisplayPlaceSearchResults pActivity;
    private FavouritesFragment favFrag;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, addr, date, time;
        public ImageView plIcon, favIcon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.placeTitle);
            addr = (TextView) view.findViewById(R.id.placeAddress);
            plIcon =  (ImageView) view.findViewById(R.id.placeIcon);
            favIcon = (ImageView) view.findViewById(R.id.favIcon);
            time = (TextView) view.findViewById(R.id.placeTime);
            date = (TextView) view.findViewById(R.id.placeDate);
        }
    }

    public PlacesSearchAdapter(List<List<String>> placesDet, DisplayPlaceSearchResults i) {
        this.placeData = placesDet;
        this.pActivity = i;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_list_row, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ArrayList<String> plRow = new ArrayList<String>(placeData.get(position));
        holder.title.setText(plRow.get(1));
        holder.addr.setText(plRow.get(2));

        new DownloadImageFromInternet(holder.plIcon)
                .execute(plRow.get(3));
        holder.date.setText(plRow.get(4));
        holder.time.setText(plRow.get(5));

        holder.favIcon.setImageResource(R.drawable.red);

        final FavouriteHelper favhelper = new FavouriteHelper(pActivity);
        if(favhelper.checkInFavourites(plRow.get(0))>-1){
            holder.favIcon.setImageResource(R.drawable.red);
        }
        else{
            holder.favIcon.setImageResource(R.drawable.white);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(pActivity, PlaceActivity.class);
                i.putStringArrayListExtra("event_info", plRow);
                v.getContext().startActivity(i);

            }
        });

        holder.favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String place_id = plRow.get(0);
                int index =favhelper.checkInFavourites(place_id);
                if(index>-1)
                {
                    favhelper.deleteFromFavorites(place_id,index);
                    holder.favIcon.setImageResource(R.drawable.white);
                    Toast toast = Toast.makeText(pActivity,plRow.get(1)+" was removed from Favorites.",Toast.LENGTH_SHORT);
                    toast.show();

                }
                else{
                    favhelper.addtoFavorites(plRow);
                    holder.favIcon.setImageResource(R.drawable.red);
                    Toast toast = Toast.makeText(pActivity,plRow.get(1)+" was added to Favorites.",Toast.LENGTH_SHORT);
                    toast.show();

                }

            }
        });
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public int getItemCount() {
        return placeData.size();
    }
    public void setPlacesDet(List<List<String>> placesDet, DisplayPlaceSearchResults i) {
        this.placeData = placesDet;
        this.pActivity = i;
    }
}