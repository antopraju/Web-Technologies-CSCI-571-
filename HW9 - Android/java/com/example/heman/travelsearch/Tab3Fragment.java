package com.example.heman.travelsearch;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.android.volley.toolbox.Volley.newRequestQueue;

public class Tab3Fragment extends Fragment implements  OnMapReadyCallback {

    String venuename = "";
    String address = "";
    String city = "";
    String phoneNumber = "";
    String openHours = "";
    String generalRule = "";
    String childRule = "";
    String venueDetails = "";
    String destLat = "";
    String destLon = "";


    private GoogleMap mMap;

    public static Tab3Fragment newInstance() {
        Tab3Fragment fragment = new Tab3Fragment();
        return fragment;
    }

    public Tab3Fragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_layout, container, false);

        if(getArguments() != null){

            try{
                if(getArguments().containsKey("venueDetails")){
                    venueDetails = getArguments().getString("venueDetails");
                }else{
                    Log.d("nonsense", "android");
                    //NO venue Details
                }
            }catch(Exception e){
                Log.d("NoVenueDetails","Anto");

            }

            try{

                JSONObject venueResult = new JSONObject(venueDetails);
                venuename = venueResult.getString("venueName");
                address = venueResult.getString("address");
                city = venueResult.getString("city");
                phoneNumber = venueResult.getString("phoneNumber");
                openHours = venueResult.getString("openHours");
                generalRule = venueResult.getString("generalRule");
                childRule = venueResult.getString("childRule");
                childRule = venueResult.getString("childRule");
                destLat = venueResult.getString("venueLat");
                destLon = venueResult.getString("venueLon");


            }catch(Exception e){
                Log.d("I am Anot", "praju");
            }
        }
        else
        {
            Log.d("Map arguments not found", "onCreateView:getArguments is returning null" );
        }

        TableRow tr_name = view.findViewById(R.id.venueName);
        TableRow tr_address = view.findViewById(R.id.venueAddress);
        TableRow tr_phone = view.findViewById(R.id.venuePhone);
        TableRow tr_general = view.findViewById(R.id.venueGeneral);
        TableRow tr_child = view.findViewById(R.id.venueChild);
        TableRow tr_city = view.findViewById(R.id.venueCity);
        TableRow tr_open = view.findViewById(R.id.venueOpenhours);


        if(!venuename.isEmpty()){
            TextView nameTextView = (TextView) view.findViewById(R.id.venueNameValue);
            nameTextView.setText(venuename);
            tr_name.setVisibility(View.VISIBLE);
        }
        else{
            tr_name.setVisibility(View.GONE);
        }

        if(!address.isEmpty()){

            TextView venueTextView = (TextView) view.findViewById(R.id.venueaddressValue);
            venueTextView.setText(address);
            tr_address.setVisibility(View.VISIBLE);
        }
        else{
            tr_address.setVisibility(View.GONE);
        }

        if(!city.isEmpty()){

            TextView timeView = (TextView) view.findViewById(R.id.venuecityValue);
            timeView.setText(city);
            tr_city.setVisibility(View.VISIBLE);
        }
        else{
            tr_city.setVisibility(View.GONE);
        }

        if(!phoneNumber.isEmpty()){

            TextView textView = view.findViewById(R.id.venuephoneValue);
            textView.setText(phoneNumber);
            tr_phone.setVisibility(View.VISIBLE);
        }
        else{
            tr_phone.setVisibility(View.GONE);
        }

        if(!openHours.isEmpty()){

            TextView textView = view.findViewById(R.id.venueopenhoursValue);
            textView.setText(openHours);
            tr_open.setVisibility(View.VISIBLE);
        }
        else{
            tr_open.setVisibility(View.GONE);
        }

        if(!generalRule.isEmpty()){

            TextView textView = view.findViewById(R.id.venuegeneralValue);
            textView.setText(generalRule);
            tr_general.setVisibility(View.VISIBLE);
        }
        else{
            tr_general.setVisibility(View.GONE);
        }
        //textView.setText(strtext);

        if(!childRule.isEmpty()){

            TextView textView = view.findViewById(R.id.venuechildValue);
            textView.setText(childRule);
            tr_child.setVisibility(View.VISIBLE);
        }
        else{
            tr_child.setVisibility(View.GONE);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // New try for fourth tab api's
        Log.i("text", "value");
        Log.d("successful", "tab3");
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng destLoc = new LatLng(Double.parseDouble(destLat),Double.parseDouble(destLon));
        mMap.addMarker(new MarkerOptions().position(destLoc).title(venuename));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destLoc));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(destLoc)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}