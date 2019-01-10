package com.example.heman.travelsearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.SimpleViewHolder>{
    ArrayList<FavWrapper> favouritesList;
    private TextView emptyFavourites;
    public static final String TAG = "FavouritesAdapterDebug";
//    FavouritesFragment favFrag ;
    Context savedContext;
    public FavouriteAdapter(ArrayList<FavWrapper> favlist, Context context) {

        favouritesList = favlist;
        Log.d(TAG, "FavouriteAdapter: "+favlist);
        savedContext = context;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView nameOfPlace, addr, datef, timef;
        public ImageView plIcon, favIcon;

        public SimpleViewHolder(View view) {
            super(view);
            nameOfPlace = (TextView) view.findViewById(R.id.FavplaceTitle);
            addr = (TextView) view.findViewById(R.id.FavplaceAddress);
            datef = (TextView) view.findViewById(R.id.FavplaceDate);
            timef = (TextView) view.findViewById(R.id.FavplaceTime);
            plIcon =  (ImageView) view.findViewById(R.id.FavplaceIcon);
            favIcon = (ImageView) view.findViewById(R.id.FavfavIcon);

        }
    }
    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_list_favourites, parent, false);


        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleViewHolder holder, final int position) {
        final FavWrapper favRow = favouritesList.get(position);
        Log.d(TAG, "onBindViewHolder: " + favRow);
        holder.nameOfPlace.setText(favRow.fav.getName());
        holder.addr.setText(favRow.fav.getAddress());
        holder.datef.setText(favRow.fav.getDate());
        holder.timef.setText(favRow.fav.getTime());
        new FavouriteAdapter.DownloadImageFromInternet(holder.plIcon).execute(favRow.fav.getIcon());

        final FavouriteHelper favhelper = new FavouriteHelper(savedContext);
        if (favhelper.checkInFavourites(favRow.place_id) > -1) {
            holder.favIcon.setImageResource(R.drawable.red);
        } else {
            holder.favIcon.setImageResource(R.drawable.white);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(savedContext, PlaceActivity.class);
                ArrayList<String> favcall = new ArrayList<String>();
                favcall.add(favRow.place_id);
                favcall.add(favRow.fav.getName());
                favcall.add(favRow.fav.getAddress());
                favcall.add(favRow.fav.getIcon());
                favcall.add(favRow.fav.getDate());
                favcall.add(favRow.fav.getTime());

                i.putStringArrayListExtra("event_info", favcall);
                v.getContext().startActivity(i);
                Log.d("egfgggggggggggggggggggg","asbjkdajjjjjjjjjjjjjjjjjjj");
            }
        });

        holder.favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String place_id = favRow.place_id;
                int index = favhelper.checkInFavourites(place_id);
                if (index > -1) {
                    favhelper.deleteFromFavorites(place_id, index);
                    favouritesList.remove(position);

                    holder.favIcon.setImageResource(R.drawable.white);
                    Toast toast = Toast.makeText(savedContext,favRow.fav.getName()+" was removed from Favorites.",Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    List<String> row = new ArrayList<String>();
                    row.add(favRow.place_id);
                    row.add(favRow.fav.getName());
                    row.add(favRow.fav.getAddress());
                    row.add(favRow.fav.getIcon());
                    row.add(favRow.fav.getDate());
                    row.add(favRow.fav.getTime());
                    favhelper.addtoFavorites(row);
                    holder.favIcon.setImageResource(R.drawable.red);
                    Toast toast = Toast.makeText(savedContext,favRow.fav.getName()+" was removed from Favorites.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                notifyDataSetChanged();


            }
        });
    }
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            //  Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
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
        return favouritesList.size();
    }

}
