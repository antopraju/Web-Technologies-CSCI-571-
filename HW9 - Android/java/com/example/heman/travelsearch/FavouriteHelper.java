package com.example.heman.travelsearch;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavouriteHelper {
    String filename = "favourites.json";
    public final static String TAG ="FavouritesHelper";
    public Context fContext ;

    
    public FavouriteHelper(Context context)
    {
        fContext = context;
    }

    public int checkInFavourites(String placeid)
    {
        FavWrapper[] favouritesList = readFavouritesFromStorage();
        if(favouritesList!=null) {
            for (int i = 0; i < favouritesList.length; i++) {
                if (favouritesList[i].place_id.equals(placeid)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public FavWrapper[] readFavouritesFromStorage()
    {

        FavWrapper[] element =null;
        try{
            Log.d(TAG, "readFavouritesFromStorage: ad ");
            FileInputStream inputStream = fContext.openFileInput(filename)  ;  // getActivity().openFileInput(filename);
            StringBuilder readInput = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(inputStream );
            BufferedReader bufferedReader = new BufferedReader(isr);
            readInput = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                readInput.append(line);
            }
            Log.d(" StringFromFile" , readInput.toString());
            if(readInput.toString().length()>0) {
                element = new Gson().fromJson(readInput.toString(), FavWrapper[].class);

                Log.d(" FavWrapper", Arrays.toString(element));
            }
            inputStream.close();
        }
    catch(FileNotFoundException fe)
    {
        try {
            FileOutputStream outputStream;
            outputStream = fContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write("".getBytes()); // fileContents.getBytes()
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        catch(Exception e){
            e.printStackTrace();
        }
        return element;

    }

    public void deleteFromFavorites(String place_id,int index) {

        FavWrapper[] elements = readFavouritesFromStorage();
        Log.d(TAG, "deleteFromFavorites: " +elements);
        if(elements!=null) {
            ArrayList<FavWrapper> arrayFav = new ArrayList<FavWrapper> (Arrays.asList(elements));
            arrayFav.remove(index);
            elements = arrayFav.toArray(new FavWrapper[arrayFav.size()]);
            writeFavouritesToStorage(elements);
        }


    }

    private void writeFavouritesToStorage(FavWrapper[] elements) {
        FileOutputStream outputStream;
        StringBuilder  readInput = new StringBuilder();

        try {

            outputStream = fContext.openFileOutput(filename, Context.MODE_PRIVATE);
            Gson nGson = new Gson();
            String  jsonString = nGson.toJson(elements);


            outputStream.write(jsonString.getBytes()); // fileContents.getBytes()
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addtoFavorites(List<String> plRow) {
        FavWrapper[] elements = readFavouritesFromStorage();
        Favourite obj = new Favourite();
        obj.name= plRow.get(1);
        obj.address = plRow.get(2);
        obj.icon= plRow.get(3);
        obj.date = plRow.get(4);
        obj.time = plRow.get(5);

        FavWrapper favwrapper  = new FavWrapper(obj, plRow.get(0));
        ArrayList<FavWrapper> arrayFav = new ArrayList<>();
        if(elements!=null) {
            arrayFav = new ArrayList<FavWrapper> (Arrays.asList(elements));
            arrayFav.add(favwrapper);
        }
        else
        {

            arrayFav.add(favwrapper);
        }
        elements =  arrayFav.toArray(new FavWrapper[arrayFav.size()]);

        writeFavouritesToStorage(elements);

    }
}
