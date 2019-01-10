package com.example.heman.travelsearch;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Tab1Fragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Tab1Fragment";
    RequestQueue queue;
    String url;


    private Button btnTest1;

    public Tab1Fragment() {
        Log.d(TAG, "Tab1Fragment:  created");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateView:  reached the oncreate for Info");
        View view = inflater.inflate(R.layout.fragment_tabbed, container, false);

        String eventname = "";
        String artistName = "";
        String venueName = "";
        String Time = "";
        String category = "";
        String priceRange = "";
        String status = "";
        String buyTicket = "";
        String seatMap = "";

        if(getArguments() != null){
            Log.d(TAG, "onCreateView:getArguments is not returning null" );
            eventname = getArguments().getString("eventname");
            artistName = getArguments().getString("joinedArtist");
            venueName = getArguments().getString("venueName");
            Time = getArguments().getString("formattedDate");
            category = getArguments().getString("Genres");
            priceRange = getArguments().getString("priceRange");
            status = getArguments().getString("status");
            buyTicket = getArguments().getString("buyTicket");
            seatMap = getArguments().getString("seatMap");
        }
        else
        {
            Log.d(TAG, "onCreateView:getArguments is returning null" );
        }

        TableRow tr_artist = view.findViewById(R.id.artistEvent);
        TableRow tr_venue = view.findViewById(R.id.venueEvent);
        TableRow tr_time = view.findViewById(R.id.timeEvent);
        TableRow tr_category = view.findViewById(R.id.categoryEvent);
        TableRow tr_price = view.findViewById(R.id.priceEvent);
        TableRow tr_status = view.findViewById(R.id.statusEvent);
        TableRow tr_ticket = view.findViewById(R.id.buyTicketEvent);
        TableRow tr_seatmap = view.findViewById(R.id.seatmapEvent);

        if(!artistName.isEmpty()){
            TextView artistTextView = (TextView) view.findViewById(R.id.artistValue);
            artistTextView.setText(artistName);
            tr_artist.setVisibility(View.VISIBLE);
        }
        else{
            tr_artist.setVisibility(View.GONE);
        }

        if(!venueName.isEmpty()){

            TextView venueTextView = (TextView) view.findViewById(R.id.venueValue);
            venueTextView.setText(venueName);
            tr_venue.setVisibility(View.VISIBLE);
        }
        else{
            tr_venue.setVisibility(View.GONE);
        }

        if(!Time.isEmpty()){

            TextView timeView = (TextView) view.findViewById(R.id.timeValue);
            timeView.setText(Time);
            tr_time.setVisibility(View.VISIBLE);
        }
        else{
            tr_time.setVisibility(View.GONE);
        }

        if(!category.isEmpty()){

            TextView textView = view.findViewById(R.id.categoryValue);
            textView.setText(category);
            tr_category.setVisibility(View.VISIBLE);
        }
        else{
            tr_category.setVisibility(View.GONE);
        }

        if(!priceRange.isEmpty()){

            TextView textView = view.findViewById(R.id.priceValue);
            textView.setText(priceRange);
            tr_price.setVisibility(View.VISIBLE);
        }
        else{
            tr_price.setVisibility(View.GONE);
        }

        if(!status.isEmpty()){

            TextView textView = view.findViewById(R.id.statusValue);
            textView.setText(status);
            tr_status.setVisibility(View.VISIBLE);
        }
        else{
            tr_status.setVisibility(View.GONE);
        }
        //textView.setText(strtext);

        if(!buyTicket.isEmpty()){

            TextView textView = view.findViewById(R.id.buyTicketValue);
            //textView.setText(buyTicket);
            tr_ticket.setVisibility(View.VISIBLE);

            //String dynamicUrl = "http://www.google.com"; // or whatever you want, it's dynamic
            String linkedText = String.format("<a href=\"%s\">Ticketmaster</a> ", buyTicket);
            textView.setText(Html.fromHtml(linkedText));
            textView.setLinkTextColor(Color.GRAY);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else{
            tr_ticket.setVisibility(View.GONE);
        }

        if(!seatMap.isEmpty()){

            TextView textView = view.findViewById(R.id.seatmapValue);
            //textView.setText(seatMap);
            tr_seatmap.setVisibility(View.VISIBLE);

            String linkedText = String.format("<a href=\"%s\">View Here</a> ", seatMap);
            textView.setText(Html.fromHtml(linkedText));
            textView.setLinkTextColor(Color.GRAY);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else{
            tr_seatmap.setVisibility(View.GONE);
        }

        Log.i("text", "value");
        Log.d("successful", "tab1");
        return view;
    }
}
