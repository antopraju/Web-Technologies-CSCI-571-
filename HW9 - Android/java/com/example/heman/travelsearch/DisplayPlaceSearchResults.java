package com.example.heman.travelsearch;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static android.widget.GridLayout.HORIZONTAL;

public class DisplayPlaceSearchResults extends AppCompatActivity {

    Bundle extras;
    private ProgressDialog progress;
    private List<List<String>> placesList;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private PlacesSearchAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_place_search_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle( "Search Results");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_srch_results);
        pAdapter = new PlacesSearchAdapter(placesList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView.setAdapter(pAdapter);

        extras = getIntent().getExtras();
        final String jsonStr = extras.getString("jsonresponse");
        constructPlacesData(jsonStr);
    }


    private void constructPlacesData(String jsonStr) {

        Log.d("response: ",jsonStr);


        try{
            placesList = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(jsonStr);
            int length = jsonObj.getInt("length");
            Log.d("Modifi", Integer.toString(length));
            JSONArray eventsResults = jsonObj.getJSONArray("result");

            if(length == 0)
            {
                emptyView.setVisibility(View.VISIBLE);
            }
            else
            {
                emptyView.setVisibility(View.GONE);
            }

            for(int i = 0; i < length; i++){

                JSONObject eve = eventsResults.getJSONObject(i);
                String id = eve.getString("id");
                String name = eve.getString("event");
                String date = eve.getString("date");
                String time = eve.getString("time");
                String venue = eve.getString("venue");
                String segment = eve.getString("segment");
                String icon = "";

                if(segment.equals("Music")){
                    icon = "http://csci571.com/hw/hw9/images/android/music_icon.png";
                }else if(segment.equals("Sports")){
                    icon = "http://csci571.com/hw/hw9/images/android/sport_icon.png";
                }else if(segment.equals("Arts & Theatre")){
                    icon = "http://csci571.com/hw/hw9/images/android/art_icon.png";
                }else if(segment.equals("Film")){
                    icon = "http://csci571.com/hw/hw9/images/android/film_icon.png";
                }else if(segment.equals("Miscellaneous")){
                    icon = "http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png";
                }

                List<String> plListItem = new ArrayList<>();

                plListItem.add(id);
                plListItem.add(name);
                plListItem.add(venue);
                plListItem.add(icon);
                plListItem.add(date);
                plListItem.add(time);
                placesList.add(plListItem);

            }

        }catch (final JSONException e)
        {
            Log.e("JSON Parsing Error :", "Json parsing error: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }

        Log.d("adapter", "I have come to adapter");
        pAdapter = new PlacesSearchAdapter(placesList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);
        pAdapter.setPlacesDet(placesList,this);
        pAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pAdapter.notifyDataSetChanged();
    }
}

