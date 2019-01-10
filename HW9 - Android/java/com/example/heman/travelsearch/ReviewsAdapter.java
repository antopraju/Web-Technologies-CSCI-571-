package com.example.heman.travelsearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>{

    List<Review> reviews;

    List<Review> yelp_reviews;
    String url ;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView display_tv, artist_tv, date_tv, type_tv;

        public MyViewHolder(View view) {
            super(view);
            display_tv = (TextView) view.findViewById(R.id.display_name);
            artist_tv = (TextView) view.findViewById(R.id.artist_name);
            date_tv = (TextView) view.findViewById(R.id.date_name);
            type_tv = (TextView) view.findViewById(R.id.type_name);
        }
    }

    public ReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_list_row, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        final Review review = reviews.get(position);
        holder.display_tv.setText(review.getdisplayName());
        holder.artist_tv.setText(review.getartistName());
        holder.date_tv.setText(review.getdatetime());
        holder.type_tv.setText("Type: " + review.gettype());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!review.getUri().isEmpty()){
                    String url= review.getUri();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    v.getContext().startActivity(i);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
