package com.example.heman.travelsearch;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PhotoFragment extends android.support.v4.app.Fragment {

    private RecyclerView photosRecyclerView;
    private RecyclerView photosRecyclerView1;
    private TextView noResultsTextView;
    private RecyclerView.Adapter adapter;
    private ProgressDialog progress;
    private View photosView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress=new ProgressDialog(getContext());
        progress.setMessage("Getting Photo and spotify details...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.show();

        photosView = inflater.inflate(R.layout.activity_results_list_view, container, false);
        noResultsTextView = (TextView) photosView.findViewById(R.id.noResultsTextView);

        photosRecyclerView = (RecyclerView) photosView.findViewById(R.id.resultsListRecyclerView);
        photosRecyclerView.setHasFixedSize(true);
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(photosView.getContext()));

        photosRecyclerView1 = (RecyclerView) photosView.findViewById(R.id.resultsListRecyclerView2);
        photosRecyclerView1.setHasFixedSize(true);
        photosRecyclerView1.setLayoutManager(new LinearLayoutManager(photosView.getContext()));

        TextView eventHeading = (TextView) photosView.findViewById(R.id.artistHeading);
        TextView eventHeading2 = (TextView) photosView.findViewById(R.id.artistHeading2);

        ArrayList<String> attractionList = getArguments().getStringArrayList("attrList");
        String segment = getArguments().getString("segment");

        if(attractionList != null && attractionList.size() > 0){

            eventHeading.setText(attractionList.get(0));

            RequestQueue queue = Volley.newRequestQueue(getContext());

            //String customUrl = "http://10.0.2.2:8081/getCustomGoogleJson?keyword=" + attractionList.get(0);
            String customUrl = "http://alskdj.us-east-2.elasticbeanstalk.com/getCustomGoogleJson?keyword=" + attractionList.get(0);

            try{
                StringRequest customReq = new StringRequest(Request.Method.GET, customUrl,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d("gettting custab photo..", response);

                                try{
                                    getPhotoDetails(response);
                                }catch(Exception e){
                                    Log.d("hello", "anto");
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("error :", error.toString());
                    }
                });
                queue.add(customReq);

            }catch(Exception e){

            }

            if(attractionList.size() > 1){

                eventHeading2.setText(attractionList.get(1));

                //String customUrl1 = "http://10.0.2.2:8081/getCustomGoogleJson?keyword=" + attractionList.get(1);
                String customUrl1 = "http://alskdj.us-east-2.elasticbeanstalk.com/getCustomGoogleJson?keyword=" + attractionList.get(1);

                try{
                    StringRequest customReq1 = new StringRequest(Request.Method.GET, customUrl1,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.d("gettting custab photo..", response);

                                    try{
                                        getPhotoDetails1(response);
                                    }catch(Exception e){
                                        Log.d("hello", "anto");
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("error :", error.toString());
                        }
                    });
                    queue.add(customReq1);

                }catch(Exception e){

                }
            }

            if(segment.equals("Music")){

                //String spotifyUrl = "http://10.0.2.2:8081/getSpotifyDetailsJson?keyword=" + attractionList.get(0);
                String spotifyUrl = "http://alskdj.us-east-2.elasticbeanstalk.com/getSpotifyDetailsJson?keyword=" + attractionList.get(0);

                try{
                    StringRequest spotifyReq = new StringRequest(Request.Method.GET, spotifyUrl,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.d("gettting custab photo..", response);

                                    try{
                                        getSpotifyDetails(response);
                                    }catch(Exception e){
                                        Log.d("hello", "anto");
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("error :", error.toString());
                        }
                    });
                    queue.add(spotifyReq);

                }catch(Exception e){

                }

                if(attractionList.size() > 1){

                    //String spotifyUrl1 = "http://10.0.2.2:8081/getSpotifyDetailsJson?keyword=" + attractionList.get(1);
                    String spotifyUrl1 = "http://alskdj.us-east-2.elasticbeanstalk.com/getSpotifyDetailsJson?keyword=" + attractionList.get(1);

                    try{
                        StringRequest spotifyReq = new StringRequest(Request.Method.GET, spotifyUrl1,
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("gettting custab photo..", response);

                                        try{
                                            getSpotifyDetails1(response);
                                        }catch(Exception e){
                                            Log.d("hello", "anto");
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.d("error :", error.toString());
                            }
                        });
                        queue.add(spotifyReq);

                    }catch(Exception e){

                    }
                }

            }else{
                TableLayout spotifyMainTab = photosView.findViewById(R.id.spotifyTable);
                spotifyMainTab.setVisibility(photosView.GONE);

                TableLayout spotifyMainTab1 = photosView.findViewById(R.id.spotifyTable2);
                spotifyMainTab1.setVisibility(photosView.GONE);
            }
        }else{
            noResultsTextView.setVisibility(View.VISIBLE);
        }
        progress.hide();
        return photosView;
    }

    private void getSpotifyDetails(String response) throws Exception{

        JSONObject spotifyResult = new JSONObject(response);
        //TableLayout spotifyMainTab = photosView.findViewById(R.id.spotifyTable);
        TableRow tr_name = photosView.findViewById(R.id.nameSpotify);
        TableRow tr_followers = photosView.findViewById(R.id.followersSpotify);
        TableRow tr_popularity = photosView.findViewById(R.id.popularitySpotify);
        TableRow tr_check = photosView.findViewById(R.id.checkAtSpotify);

        TextView name_spotify = (TextView) photosView.findViewById(R.id.nameSpotifyValue);
        TextView followers_spotify = (TextView) photosView.findViewById(R.id.followersSpotifyValue);
        TextView popularity_spotify = (TextView) photosView.findViewById(R.id.popularitySpotifyValue);
        TextView check_spotify = (TextView) photosView.findViewById(R.id.checkAtSpotifyValue);

        name_spotify.setText(spotifyResult.getString("name"));
        tr_name.setVisibility(View.VISIBLE);

        followers_spotify.setText(spotifyResult.getString("followers"));
        tr_followers.setVisibility(View.VISIBLE);

        popularity_spotify.setText(spotifyResult.getString("popularity"));
        tr_popularity.setVisibility(View.VISIBLE);

        //check_spotify.setText(spotifyResult.getString("exUrl"));
        String linkedText = String.format("<a href=\"%s\">Check At</a> ", spotifyResult.getString("exUrl"));
        check_spotify.setText(Html.fromHtml(linkedText));
        check_spotify.setLinkTextColor(Color.GRAY);
        check_spotify.setMovementMethod(LinkMovementMethod.getInstance());
        tr_check.setVisibility(View.VISIBLE);
    }

    private void getSpotifyDetails1(String response) throws Exception{

        JSONObject spotifyResult = new JSONObject(response);
        //TableLayout spotifyMainTab = photosView.findViewById(R.id.spotifyTable);
        TableRow tr_name = photosView.findViewById(R.id.nameSpotify2);
        TableRow tr_followers = photosView.findViewById(R.id.followersSpotify2);
        TableRow tr_popularity = photosView.findViewById(R.id.popularitySpotify2);
        TableRow tr_check = photosView.findViewById(R.id.checkAtSpotify2);

        TextView name_spotify = (TextView) photosView.findViewById(R.id.nameSpotifyValue2);
        TextView followers_spotify = (TextView) photosView.findViewById(R.id.followersSpotifyValue2);
        TextView popularity_spotify = (TextView) photosView.findViewById(R.id.popularitySpotifyValue2);
        TextView check_spotify = (TextView) photosView.findViewById(R.id.checkAtSpotifyValue2);

        name_spotify.setText(spotifyResult.getString("name"));
        tr_name.setVisibility(View.VISIBLE);

        followers_spotify.setText(spotifyResult.getString("followers"));
        tr_followers.setVisibility(View.VISIBLE);

        popularity_spotify.setText(spotifyResult.getString("popularity"));
        tr_popularity.setVisibility(View.VISIBLE);

        //check_spotify.setText(spotifyResult.getString("exUrl"));
        String linkedText = String.format("<a href=\"%s\">Check At</a> ", spotifyResult.getString("exUrl"));
        check_spotify.setText(Html.fromHtml(linkedText));
        check_spotify.setLinkTextColor(Color.GRAY);
        check_spotify.setMovementMethod(LinkMovementMethod.getInstance());
        tr_check.setVisibility(View.VISIBLE);
    }

    private void getPhotoDetails(String response) throws Exception {
        try {

            JSONObject customResult = new JSONObject(response);
            JSONArray customArray = customResult.getJSONArray("items");
            ArrayList<String> imageUrl = new ArrayList<String>();

            if(customArray.length() > 0){
                for(int i = 0; i < customArray.length(); i++){

                    imageUrl.add(customArray.getJSONObject(i).getString("link"));
                }

                Log.d("image checking", imageUrl.toString());

                PhotosAdapter adapter = new PhotosAdapter(photosView.getContext(), imageUrl);
                photosRecyclerView.setAdapter(adapter);

            }else{
                noResultsTextView.setVisibility(View.VISIBLE);
            }

        } catch (Throwable e) {
            e.printStackTrace();
            noResultsTextView.setVisibility(View.VISIBLE);
        }
    }

    private void getPhotoDetails1(String response) throws Exception {
        try {

            JSONObject customResult = new JSONObject(response);
            JSONArray customArray = customResult.getJSONArray("items");
            ArrayList<String> imageUrl = new ArrayList<String>();

            if(customArray.length() > 0){
                for(int i = 0; i < customArray.length(); i++){

                    imageUrl.add(customArray.getJSONObject(i).getString("link"));
                }

                Log.d("image checking", imageUrl.toString());

                PhotosAdapter1 adapter = new PhotosAdapter1(photosView.getContext(), imageUrl);
                photosRecyclerView1.setAdapter(adapter);

            }else{
                noResultsTextView.setVisibility(View.VISIBLE);
            }

        } catch (Throwable e) {
            e.printStackTrace();
            noResultsTextView.setVisibility(View.VISIBLE);
        }
    }
}
