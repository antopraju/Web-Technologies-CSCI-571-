package com.example.heman.travelsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.heman.travelsearch.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {
    private static final String TAG = "PlaceActivity";
    private ViewPager mViewPager;
    private ProgressDialog progress;
    final PlaceActivity pActivity = this;


    Bundle bundle = new Bundle();
    Bundle extras;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress=new ProgressDialog(this);
        progress.setMessage("Getting Event details...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.show();

        Log.d(TAG, "onCreate: Starting.");
        extras = getIntent().getExtras();
        final ArrayList<String> al = extras.getStringArrayList("event_info");
        final String eventID = al.get(0);
        final String eventFavName = al.get(1);
        final String eventFavAddr = al.get(2);
        final String eventFavIcon = al.get(3);
        final String eventFavDate = al.get(4);
        final String eventFavTime = al.get(5);

        final RequestQueue queue = Volley.newRequestQueue(this);

        //String url ="http://10.0.2.2:8081/getEventDetailsJson?eventId=" + eventID;
        String url ="http://alskdj.us-east-2.elasticbeanstalk.com/getEventDetailsJson?eventId=" + eventID;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            setContentView(R.layout.place_details); // With favorite and twitter Button.
                            Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
                            setSupportActionBar(toolbar);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                            String eventname = "";
                            String eventURL = "";
                            String venueName = "";
                            String formattedDate = "";
                            String joinedArtist = "";
                            String Genres = "";
                            String priceRange = "";
                            String status = "";
                            String buyTicket = "";
                            String seatMap = "";
                            String segment = "";
                            ArrayList<String> attrlist = new ArrayList<String>();

                            JSONObject eventResult = new JSONObject(response);

                            eventname = eventResult.getString("eventName");
                            eventURL = eventResult.getString("eventURL");
                            venueName = eventResult.getString("venue");
                            formattedDate = eventResult.getString("formattedDate");
                            joinedArtist = eventResult.getString("joinedArtist");
                            JSONArray attractionList = eventResult.getJSONArray("attractionsList");

                            if (attractionList != null) {
                                int len = attractionList.length();
                                for (int i=0;i<len;i++){
                                    attrlist.add(attractionList.get(i).toString());
                                }
                            }

                            Genres = eventResult.getString("genres");
                            priceRange = eventResult.getString("pricerange");
                            status = eventResult.getString("status");
                            buyTicket = eventResult.getString("buyticket");
                            seatMap = eventResult.getString("seatmap");
                            segment = eventResult.getString("segment");

                            bundle.putString("eventname", eventname);
                            bundle.putString("joinedArtist", joinedArtist );
                            bundle.putString("venueName", venueName);
                            bundle.putString("formattedDate", formattedDate);
                            bundle.putString("Genres", Genres);
                            bundle.putString("priceRange", priceRange);
                            bundle.putString("status", status);
                            bundle.putString("seatMap", seatMap);
                            bundle.putString("buyTicket", buyTicket);
                            bundle.putString("segment", segment);
                            bundle.putStringArrayList("attrList", attrlist);

                            //final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                            //String songkickUrl = "http://10.0.2.2:8081/getUpcomingIdDetailsJson?venueName=" + venueName;
                            String songkickUrl = "http://alskdj.us-east-2.elasticbeanstalk.com/getUpcomingIdDetailsJson?venueName=" + venueName;

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, songkickUrl,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try{

                                                Log.d("Got it 1st", response);

                                                JSONObject songKickResults = new JSONObject(response);
                                                String songKickID = "";

                                                if(songKickResults.has("resultsPage")){
                                                    if(songKickResults.getJSONObject("resultsPage").has("results")){
                                                        if(songKickResults.getJSONObject("resultsPage").getJSONObject("results").has("venue")){
                                                            if(songKickResults.getJSONObject("resultsPage").getJSONObject("results").getJSONArray("venue").getJSONObject(0).has("id")){
                                                                {
                                                                    songKickID = songKickResults.getJSONObject("resultsPage").getJSONObject("results").getJSONArray("venue").getJSONObject(0).getString("id");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                Log.d("songKickID", songKickID);

                                                if (!songKickID.equals("")){

                                                    try{
                                                        //String upcomingUrl = "http://10.0.2.2:8081/getUpcomingDetailsJson?venueId=" + songKickID;
                                                        String upcomingUrl = "http://alskdj.us-east-2.elasticbeanstalk.com/getUpcomingDetailsJson?venueId=" + songKickID;

                                                        StringRequest secondRequest = new StringRequest(Request.Method.GET, upcomingUrl,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        Log.d("gettting upcoming..", response);
                                                                        bundle.putString("upcoming", response);
                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                                Log.d("error :",error.toString());
                                                            }
                                                        });

// Add the request to the RequestQueue.
                                                        queue.add(secondRequest);

                                                    }catch(Exception e){

                                                    }
                                                }
                                            }catch (Exception e){

                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.d("error :",error.toString());
                                }
                            });
                            queue.add(stringRequest);

                            //String venueURL = "http://10.0.2.2:8081/getVenueDetailsJson?venueName=" + venueName;
                            String venueURL = "http://alskdj.us-east-2.elasticbeanstalk.com/getVenueDetailsJson?venueName=" + venueName;

                            StringRequest stringReq = new StringRequest(Request.Method.GET, venueURL,
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("gettting venue..", response);
                                            bundle.putString("venueDetails", response);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.d("error :", error.toString());
                                }
                            });
                            queue.add(stringReq);


                         if(attractionList != null && attractionList.length() > 0)  {

                             boolean twoItems = false;

                             if(attractionList.length() > 1){
                                 twoItems = true;
                                 bundle.putInt("count", 2);
                             }else{
                                 bundle.putInt("count", 1);
                             }

                             if(segment.equals("Music")){

                                 final String[] spotifyResult = new String[2];

                                 //String spotifyUrl = "http://10.0.2.2:8081/getSpotifyDetailsJson?keyword=" + attractionList.getString(0);
                                 String spotifyUrl = "http://alskdj.us-east-2.elasticbeanstalk.com/getSpotifyDetailsJson?keyword=" + attractionList.getString(0);

                                 StringRequest spotifyReq = new StringRequest(Request.Method.GET, spotifyUrl,
                                         new Response.Listener<String>() {

                                             @Override
                                             public void onResponse(String response) {
                                                 Log.d("gettting spotify..", response);
                                                 spotifyResult[0] = response;
                                                 //bundle.putString("spotify1", response);
                                             }
                                         }, new Response.ErrorListener() {
                                     @Override
                                     public void onErrorResponse(VolleyError error) {

                                         Log.d("error :", error.toString());
                                     }
                                 });
                                 queue.add(spotifyReq);

                                 if(twoItems){

                                     //String spotifyUrl1 = "http://10.0.2.2:8081/getSpotifyDetailsJson?keyword=" + attractionList.getString(1);
                                     String spotifyUrl1 = "http://alskdj.us-east-2.elasticbeanstalk.com/getSpotifyDetailsJson?keyword=" + attractionList.getString(1);

                                     StringRequest spotifyReq1 = new StringRequest(Request.Method.GET, spotifyUrl1,
                                             new Response.Listener<String>() {

                                                 @Override
                                                 public void onResponse(String response) {
                                                     Log.d("gettting spotify1..", response);
                                                     spotifyResult[1] = response;
                                                     //bundle.putString("spotify2", response);
                                                 }
                                             }, new Response.ErrorListener() {
                                         @Override
                                         public void onErrorResponse(VolleyError error) {

                                             Log.d("error :", error.toString());
                                         }
                                     });
                                     queue.add(spotifyReq1);
                                 }
                                 bundle.putStringArray("spotifyResult", spotifyResult);
                             }

                             final String[] customResult = new String[2];

                             //String customUrl = "http://10.0.2.2:8081/getCustomGoogleJson?keyword=" + attractionList.getString(0);
                             String customUrl = "http://alskdj.us-east-2.elasticbeanstalk.com/getCustomGoogleJson?keyword=" + attractionList.getString(0);

                             StringRequest customReq = new StringRequest(Request.Method.GET, customUrl,
                                     new Response.Listener<String>() {

                                         @Override
                                         public void onResponse(String response) {
                                             Log.d("gettting spotify..", response);
                                             customResult[0] = response;
                                             //bundle.putString("spotify1", response);
                                         }
                                     }, new Response.ErrorListener() {
                                 @Override
                                 public void onErrorResponse(VolleyError error) {

                                     Log.d("error :", error.toString());
                                 }
                             });
                             queue.add(customReq);

                             if(twoItems){

                                 //String customUrl1 = "http://10.0.2.2:8081/getCustomGoogleJson?keyword=" + attractionList.getString(1);
                                 String customUrl1 = "http://alskdj.us-east-2.elasticbeanstalk.com/getCustomGoogleJson?keyword=" + attractionList.getString(1);

                                 StringRequest customReq1 = new StringRequest(Request.Method.GET, customUrl1,
                                         new Response.Listener<String>() {

                                             @Override
                                             public void onResponse(String response) {
                                                 Log.d("gettting spotify1..", response);
                                                 customResult[1] = response;
                                                 //bundle.putString("spotify2", response);
                                             }
                                         }, new Response.ErrorListener() {
                                     @Override
                                     public void onErrorResponse(VolleyError error) {

                                         Log.d("error :", error.toString());
                                     }
                                 });
                                 queue.add(customReq1);
                             }

                             bundle.putStringArray("customResult", customResult);
                         }


                            SectionsPageAdapter mSectionsPageAdapter;
                            mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
                            mViewPager = (ViewPager) findViewById(R.id.container);
                            //setupViewPager(mViewPager);
                            SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
                            Fragment frag = new Tab1Fragment();
                            frag.setArguments(bundle);
                            adapter.addFragment(frag, "EVENT");
                            frag = new PhotoFragment();
                            frag.setArguments(bundle);
                            adapter.addFragment(frag, "ARTIST(S)");
                            frag = new Tab3Fragment();
                            frag.setArguments(bundle);
                            adapter.addFragment(frag, "VENUE");
                            frag = new Tab4Fragment();
                            frag.setArguments(bundle);
                            adapter.addFragment(frag, "UPCOMING");

                            mViewPager.setAdapter(adapter);
                            mViewPager.setAdapter(adapter);
                            tabLayout = (TabLayout) findViewById(R.id.tabs1);
                            tabLayout.setupWithViewPager(mViewPager);
                            setupTabIcons();

                            ImageView tweetbutton = (ImageView) findViewById(R.id.twitterbutton);
                            final String finalName =  eventname;
                            final String finalVenue = venueName;
                            final String finalWebsite = eventURL;

                            TextView tvTitle = (TextView) findViewById(R.id.detailToolbarText);
                            tvTitle.setText(eventname);
                            tweetbutton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://twitter.com/intent/tweet?text="+"Check out "+ finalName + " at "+ finalVenue + ". Website: " + finalWebsite + "&hashtags=" + "CSCI571EventSearch"));
                                    startActivity(intent);
                                }
                            });


                            final ImageView favButton = (ImageView ) findViewById(R.id.favbutton);
                            final FavouriteHelper favHelper = new FavouriteHelper(pActivity);

                            if(favHelper.checkInFavourites(eventID) >-1){
                                favButton.setImageResource(R.drawable.red);
                            }
                            else{
                                favButton.setImageResource(R.drawable.white_fill_nb);
                            }
                            favButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String event_id = eventID;
                                    int index =favHelper.checkInFavourites(event_id);
                                    if(index>-1)
                                    {
                                        favHelper.deleteFromFavorites(event_id,index);
                                        favButton.setImageResource(R.drawable.white_fill_nb);
                                        Toast toast = Toast.makeText(pActivity,eventFavName+" was removed from Favorites.",Toast.LENGTH_SHORT);
                                        toast.show();

                                    }
                                    else{
                                        List<String> row = new ArrayList<String>();
                                        row.add(event_id);
                                        row.add(eventFavName);
                                        row.add(eventFavAddr);
                                        row.add(eventFavIcon);
                                        row.add(eventFavDate);
                                        row.add(eventFavTime);
                                        favHelper.addtoFavorites(row);

                                        favButton.setImageResource(R.drawable.red);
                                        Toast toast = Toast.makeText(pActivity,row.get(1)+" was added to Favorites.",Toast.LENGTH_SHORT);
                                        toast.show();

                                    }

                                }
                            });

                            progress.hide();
                        } catch (Exception e) {
                            Log.i("error", e.getMessage());
                            Toast toast = Toast.makeText(pActivity, e.getMessage(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bundle.putString("key", "that didn't work");
            }
        });
        queue.add(stringRequest);
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("EVENT");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.info_outline,  0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("ARTIST(S)");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.artist,  0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("VENUE");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.venue,  0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("UPCOMING");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.upcoming, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}