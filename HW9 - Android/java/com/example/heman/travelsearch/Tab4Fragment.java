package com.example.heman.travelsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

import static android.view.View.VISIBLE;

public class Tab4Fragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Tab4Fragment";
    RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    public ReviewsAdapter rva;
    public Tab4Fragment() {
        Log.d(TAG, "Tab4Fragment:  created");
    }
    public String selectedType = "Default";
    public String orderType = "Ascending";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final List<Review> upcomingEventList =new ArrayList<Review>();

        final View view = inflater.inflate(R.layout.fragment_tabbed4, container, false);
        String venueName = "";
        String upcomResult = "";

        Log.d("gettting it..", venueName);

        if(getArguments() != null){

            venueName = getArguments().getString("venueName");

            try{
                if(getArguments().containsKey("upcoming")){
                    upcomResult = getArguments().getString("upcoming");
                    Log.d("gettting it. OK.", upcomResult );
                }else{
                    Log.d("nonsense", "android");
                    getView().findViewById(R.id.emptyReviewsAlert).setVisibility(VISIBLE);
                }
            }catch(Exception e){
                Log.d("Noupcoming","Anto");
            }

            Log.d("gettting it. OK.", venueName);

            try {

                Log.d("Got it 2nd", upcomResult);
                JSONObject upcomingResult = new JSONObject(upcomResult);
                int upcomingLength = upcomingResult.getInt("CardeventsLength");

                if(upcomingLength > 0){

                    JSONArray upcomingList = upcomingResult.getJSONArray("result");

                    for(int i = 0; i < upcomingLength; i++){

                        JSONObject jo = upcomingList.getJSONObject(i);
                        Review rev = new Review();
                        rev.setdisplayName(jo.getString("Cardname"));
                        rev.setUri(jo.getString("Carduri"));
                        rev.setartistName(jo.getString("Cardartist"));
                        rev.setdatetime(jo.getString("formattedCardDate"));
                        rev.settype(jo.getString("Cardtype"));
                        upcomingEventList.add(rev);
                    }
                }
            } catch (Exception e){
                    Log.d("nonsense", "praju");
            }

        }


        view.setTag(TAG);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        getArguments();


        Log.d("hello there", String.valueOf(upcomingEventList.size()));
        rva = new ReviewsAdapter(upcomingEventList);
        mRecyclerView.setAdapter(rva);
        Log.d("successfull tab 4", "tab 4");

        final Spinner eventType = view.findViewById(R.id.spinner_type);
        final Spinner filter_type = view.findViewById(R.id.spinner_filter);
        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String eventSelected = eventType.getSelectedItem().toString();
                if(eventSelected.equalsIgnoreCase("Default")){
                    if(upcomingEventList.isEmpty()){
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(VISIBLE);
                    }
                    else{
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.INVISIBLE);
                    }
                    filter_type.setEnabled(false);
                    selectedType = "Default";
                }else if(eventSelected.equalsIgnoreCase("Event Name")){
                    if(upcomingEventList.isEmpty()){
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(VISIBLE);
                    }
                    else{
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.INVISIBLE);
                    }
                    filter_type.setEnabled(true);
                    selectedType = "Event";
                }else if(eventSelected.equalsIgnoreCase("Time")){
                    if(upcomingEventList.isEmpty()){
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(VISIBLE);
                    }
                    else{
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.INVISIBLE);
                    }
                    filter_type.setEnabled(true);
                    selectedType = "Time";
                }else if(eventSelected.equalsIgnoreCase("Artist")){
                    if(upcomingEventList.isEmpty()){
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(VISIBLE);
                    }
                    else{
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.INVISIBLE);
                    }
                    filter_type.setEnabled(true);
                    selectedType = "Artist";
                }else if(eventSelected.equalsIgnoreCase("Type")){
                    if(upcomingEventList.isEmpty()){
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(VISIBLE);
                    }
                    else{
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.INVISIBLE);
                    }
                    filter_type.setEnabled(true);
                    selectedType = "Type";
                }

                setOrder(upcomingEventList);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.Event_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventType.setAdapter(adapter);

        filter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String orderWay = filter_type.getSelectedItem().toString();

                if(orderWay.equalsIgnoreCase("Ascending")){
                    orderType = "Ascending";
                }else{
                    orderType = "Descending";
                }

                setOrder(upcomingEventList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.filter_type, android.R.layout.simple_spinner_item);
        filter_type.setAdapter(adapter);


       return view;
    }

    public void setOrder(List<Review> upcomingEventList) {

        if(selectedType.equalsIgnoreCase("Default")){

            Log.d("Inside..", "Default");
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }
        else if(selectedType.equalsIgnoreCase("Event") && orderType.equalsIgnoreCase("Ascending")){

            Collections.sort(upcomingEventList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return (r1.getdisplayName().compareTo(r2.getdisplayName()));
                }
            });
            Log.d("Inside..", "Event Asc");
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }

        else if(selectedType.equalsIgnoreCase("Event") && orderType.equalsIgnoreCase("Descending")) {
            Collections.sort(upcomingEventList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.getdisplayName().compareTo(r2.getdisplayName());
                }
            });
            Log.d("Inside..", "Event Desc");
            Collections.reverse(upcomingEventList);
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }
        else if(selectedType.equalsIgnoreCase("Artist") && orderType.equalsIgnoreCase("Ascending")){
            Collections.sort(upcomingEventList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.getartistName().compareTo(r2.getartistName());
                }
            });
            Log.d("Inside..", "Artist Asc");
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }

        else if(selectedType.equalsIgnoreCase("Artist") && orderType.equalsIgnoreCase("Descending")){
            Collections.sort(upcomingEventList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.getartistName().compareTo(r2.getartistName());
                }
            });
            Log.d("Inside..", "Artist Des");
            Collections.reverse(upcomingEventList);
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }
        else if(selectedType.equalsIgnoreCase("Time") && orderType.equalsIgnoreCase("Ascending")){
            Collections.sort(upcomingEventList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.getdatetime().compareTo(r2.getdatetime());
                }
            });
            Log.d("Inside..", "Time Asc");
            Collections.reverse(upcomingEventList);
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }
        else if(selectedType.equalsIgnoreCase("Time") && orderType.equalsIgnoreCase("Descending")){
            Collections.sort(upcomingEventList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.getdatetime().compareTo(r2.getdatetime());
                }
            });
            Log.d("Inside..", "Time Desc");
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }
        else if(selectedType.equalsIgnoreCase("Type") && orderType.equalsIgnoreCase("Ascending")){
            Collections.sort(upcomingEventList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.gettype().compareTo(r2.gettype());
                }
            });
            Log.d("Inside..", "Type Asc");
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }
        else if(selectedType.equalsIgnoreCase("Type") && orderType.equalsIgnoreCase("Descending")){
            Collections.sort(upcomingEventList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.gettype().compareTo(r2.gettype());
                }
            });
            Log.d("Inside..", "Type Desc");
            Collections.reverse(upcomingEventList);
            rva = new ReviewsAdapter(upcomingEventList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }
    }

}
