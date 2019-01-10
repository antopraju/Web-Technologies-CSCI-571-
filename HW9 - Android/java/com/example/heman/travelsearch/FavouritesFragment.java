package com.example.heman.travelsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FavouritesFragment  extends Fragment{

    public static final String TAG ="FromFavouritesFragment";
    private RecyclerView recyclerView;
    private TextView emptyFavourites;
    public FavouritesFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favourites_fragment, container, false);
        String fileContents = "Hello world!";
        FileOutputStream outputStream;
        StringBuilder  readInput = new StringBuilder();
        Log.d(TAG, "onCreateView: here before reading from local storage");
        emptyFavourites = (TextView) view.findViewById(R.id.empty_fav);
        FavWrapper[] elements = new FavouriteHelper(getContext()).readFavouritesFromStorage();
        if(elements!=null) {

            ArrayList<FavWrapper> favlist = new ArrayList<FavWrapper> ( Arrays.asList(elements));
            FavouriteAdapter favadapter = new FavouriteAdapter(favlist, getContext());


            recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_favourites);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(favadapter);
            FavouriteObserver observer = new FavouriteObserver(recyclerView, emptyFavourites);
            favadapter.registerAdapterDataObserver(observer);

        }
        return view;
    }
}
