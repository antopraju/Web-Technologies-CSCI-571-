package com.example.heman.travelsearch;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    AutoCompleteTextView keywordInput;
    private ProgressDialog progress;
    private String  keyword;
    boolean custom_location=false;
    public static Double longitude, latitude;

    private static final int MY_PERMISSIONS_FINE_LOCATION = 1;

    public SearchFragment() {

    }

    Button buttonSrch,buttonClr;
    private OnFragmentInteractionListener mListener;
    private Spinner categOption;
    private Spinner unitOption;
    private EditText locInput, distanceEntered ;
    private TextView errTextKWord;
    private TextView errTextLoc;
    private RadioButton otherLocRadio,currLoc;
    private String distanceText;
    ArrayAdapter<String> adapter;
    private static final String TAG = "ErorrLogVj";
    ArrayList<String> auto_complete_names;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_FINE_LOCATION);
            Log.d("antoper", "prajuLoc");

            latitude = 34.0266;
            longitude = -118.2831;

        } else {

            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = lm.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Log.d("TAG", "I am checking here");
                Log.d("anto", "prajuLoc");
                Location l = lm.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            if(bestLocation !=null) {
                Log.d("TAG", "HEllo is am inside location");
                Log.d("anto", "prajuLoc");
                longitude = bestLocation.getLongitude();
                latitude = bestLocation.getLatitude();
            }

            latitude = 34.0266;
            longitude = -118.2831;
            Log.d("tagggg :", String.valueOf(longitude) + " " + String.valueOf(latitude));
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.categ_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categ_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.units_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        auto_complete_names = new ArrayList<String>();

        keywordInput = (AutoCompleteTextView) view.findViewById(R.id.EditTextName);
        ((AutoCompleteTextView) keywordInput).setThreshold(0);

        categOption = (Spinner) view.findViewById(R.id.categ_spinner);
        unitOption = (Spinner) view.findViewById(R.id.units_spinner);
        locInput  = (EditText) view.findViewById(R.id.enteredLocation);
        currLoc = (RadioButton) view.findViewById(R.id.currLoc);
        otherLocRadio = (RadioButton) view.findViewById(R.id.otherLoc);
        errTextKWord  = (TextView) view.findViewById(R.id.keywordErrorTxt);
        errTextLoc = (TextView) view.findViewById(R.id.locErrorTxt);
        distanceEntered = (EditText) view.findViewById(R.id.DistanceName);
        buttonSrch = view.findViewById(R.id.searchBttn);
        distanceText = "10";

        buttonSrch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(keywordInput.getText().toString().trim().isEmpty() || otherLocRadio.isChecked() && locInput.getText().toString().trim().isEmpty())
                {
                    if(keywordInput.getText().toString().trim().isEmpty())
                        errTextKWord.setVisibility(View.VISIBLE);
                    if ( otherLocRadio.isChecked() && locInput.getText().toString().trim().isEmpty())
                        errTextLoc.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(getContext(),"Please fix all fields with errors",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {

                    progress=new ProgressDialog(getContext());
                    progress.setMessage("Searching Events...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(false);
                    progress.setProgress(0);
                    progress.show();

                    if(!custom_location) {

                        prepareUrl();

                    }
                    else {
                        Log.d(TAG, "onClick:here for custom location");
                        getCustomLatandLong();

                    }
                }
            }
        });


        final Button buttonClr = view.findViewById(R.id.clearBttn);
        buttonClr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                errTextKWord.setVisibility(View.GONE);
                errTextLoc.setVisibility(View.GONE);
                currLoc.setChecked(true);
                locInput.setText("");
                locInput.setEnabled(false);
                keywordInput.setText("");
                categOption.setSelection(0);
                unitOption.setSelection(0);
                distanceEntered.setText("");

            }
        });

        keywordInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof String){
                    Log.d(TAG, "onItemClick:chosse" + item.toString());
                     keyword = item.toString();

                }
            }
        });

        keywordInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length() <= 25) {
                    auto_complete_names = new ArrayList<String>();
                    getAutoSearchResults(s.toString());
                }

            }
        });

        RadioGroup rg = (RadioGroup) view.findViewById(R.id.fromLocRadio);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.currLoc:
                        locInput.setText("");
                        locInput.setEnabled(false);
                        custom_location = false;
                        break;

                    case R.id.otherLoc:
                        locInput.setEnabled(true);
                        custom_location = true;
                        break;
                }
            }
        });

        return view;
    }

    public void getAutoSearchResults(String text) {


        //Log.d("autoSearchResults", autoCompleteUrl);
        //String autoCompleteUrl = "http://10.0.2.2:8081/autoCompleteTicketMaster?keyword=" + text;
        String autoCompleteUrl = "http://alskdj.us-east-2.elasticbeanstalk.com/autoCompleteTicketMaster?keyword=" + text;
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        //Log.d(TAG, "getSearchResults: Came?");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, autoCompleteUrl,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    Log.d(TAG, "onResponse: Came?");
                    JSONObject json = response.getJSONObject("_embedded");
                    JSONArray attrac = json.getJSONArray("attractions");

                    for (int i = 0; i < attrac.length(); i++) {

                        JSONObject c = attrac.getJSONObject(i);
                        String description = c.getString("name");
                        Log.d("description", description);
                        auto_complete_names.add(description);
                    }

                    adapter = new ArrayAdapter<String>(
                            getActivity().getApplicationContext(),
                            android.R.layout.simple_list_item_1, auto_complete_names) {
                        @Override
                        public View getView(int position,
                                            View convertView, ViewGroup parent) {
                            View view = super.getView(position,
                                    convertView, parent);
                            TextView text = (TextView) view
                                    .findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    keywordInput.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: here" +error.getMessage());
            }
        });
        queue.add(jsonObjReq);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Log.d("debugvj", " not granted");
                    return;
                }
            }
        }


    }

    public void prepareUrl(){
        Log.d(TAG, "I am here...." + String.valueOf(unitOption.getSelectedItem().toString().toLowerCase()));
        keyword = keywordInput.getText().toString().trim();
        String category = categOption.getSelectedItem().toString().toLowerCase();
        Log.d("TAG", "This is before category" + category);
        category = categOption.getSelectedItem().toString().toLowerCase().replace(" ", "");
        Log.d("TAG", "This is category" + category);
        String unit = unitOption.getSelectedItem().toString().toLowerCase();
        Log.d("TAG", "This is units" + unit);

        Log.d("dist..", distanceEntered.getText().toString());
        distanceText = distanceEntered.getText().toString();
        if(distanceText.trim().equals(null) || distanceText.trim().equals(""))   distanceText = "10";

        //Log.d("TAG", "This is distance text" + distanceText);

        String segmentId = "";

        if(category.equals("all")) segmentId = "";
        else if(category.equals("music"))	segmentId = "KZFzniwnSyZfZ7v7nJ";
        else if(category.equals("sports"))	segmentId = "KZFzniwnSyZfZ7v7nE";
        else if(category.equals("artsandtheatre"))	segmentId = "KZFzniwnSyZfZ7v7na";
        else if(category.equals("film"))	segmentId = "KZFzniwnSyZfZ7v7nn";
        else if(category.equals("miscellaneous"))	segmentId = "KZFzniwnSyZfZ7v7n1";

        //String baseURL = "http://cxzewq.us-east-2.elasticbeanstalk.com/searchResults?searchurl=https://app.ticketmaster.com/discovery/v2/events.json?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb&";
        //String baseURL = "http://10.0.2.2:8081/searchResults?searchurl=https://app.ticketmaster.com/discovery/v2/events.json?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb&";
        String baseURL = "http://alskdj.us-east-2.elasticbeanstalk.com/searchResults?searchurl=https://app.ticketmaster.com/discovery/v2/events.json?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb&";
        baseURL += "keyword=" + keyword + "&segmentId=" + segmentId + "&radius=" + distanceText + "&unit=" + unit + "&latitude=" + latitude + "&longitude=" + longitude;
        Log.d(TAG,"I am getting the searchURL...." + String.valueOf(baseURL));
        getSearchResults(baseURL);
    }

    public void getCustomLatandLong(){

        String customURL="";
        String enteredLoc = locInput.getText().toString().trim();
        //customURL ="http://10.0.2.2:8081/getLatandLon?searchLocation="+ enteredLoc;
        customURL ="http://alskdj.us-east-2.elasticbeanstalk.com/getLatandLon?searchLocation="+ enteredLoc;

            RequestQueue queue = Volley.newRequestQueue(getActivity());

            StringRequest stringRequest = new StringRequest(Method.GET, customURL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject js = new JSONObject(response);
                                Log.d(TAG, "onResponse: " +js.toString());
                                JSONArray array =  js.getJSONArray("results");
                                Log.d(TAG, "onResponse: " + array.toString());
                                JSONObject geometry = (JSONObject) array.getJSONObject(0).get("geometry");
                                geometry = (JSONObject) geometry.get("location");

                                latitude = (Double) geometry.get("lat");
                                longitude = (Double) geometry.get("lng");
                                prepareUrl();

                            } catch (Exception e)
                            {
                                Log.d(TAG, "Errrror: " + e.getMessage() + e.toString());
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("error :", error.toString());
                }
            });
            queue.add(stringRequest);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getSearchResults(String url) {
        Log.d("alskdj", "anto");
        Log.d("alskdj", url);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringReq = new StringRequest(Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse:  cameeeee here on response");
                        Log.d("hoho", "praju");
                        Log.d("modified Json", response);
                        progress.hide();
                        Log.d(TAG, "This is the result from the server: " + response.toString());
                        Intent i = new Intent(getActivity(), DisplayPlaceSearchResults.class);
                        i.putExtra("jsonresponse", response.toString());
                        startActivity(i);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error :", error.toString());
            }
        });
        queue.add(stringReq);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

