package com.rrmsense.radiostream.models;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.rrmsense.radiostream.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by Talha on 2/18/2017.
 */

public class Storage {
    public static ArrayList getRecent(Context context) {
        ArrayList<String> radios = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String recent = sharedPreferences.getString("recent", "");
        String[] recents = recent.split(",");
        int i=0;
        for (String f : recents) {
            if(f.length()>0){
                if(i<15){
                radios.add(f);
                    editor.putBoolean(f + "_recent", true);
                }
                else{
                    editor.putBoolean(f + "_recent", false);
                }
            }
            i++;
        }
        editor.apply();
        return radios;
    }
    public static void RemoveAllRecent(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String recent = sharedPreferences.getString("recent", "");
        String[] recents = recent.split(",");
        for (String f : recents) {
            if(f.length()>0){
                editor.putBoolean(f + "_recent", false);
            }
        }
        editor.apply();

    }
    public static ArrayList getFavourite(Context context) {
        ArrayList<String> radios = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favourite = sharedPreferences.getString("favourite", "");
        String[] favourites = favourite.split(",");
        for (String f : favourites) {
            if(f.length()>0){
                radios.add(f);
                Log.d("FAV",f + "_favourite");
                editor.putBoolean(f + "_favourite", true);
            }
        }
        editor.apply();
        return radios;
    }
    public static void removeAllFavourite(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favourite = sharedPreferences.getString("favourite", "");
        String[] favourites = favourite.split(",");
        for (String f : favourites) {
            if(f.length()>0){
                editor.putBoolean(f + "_favourite", false);
            }
        }
        editor.apply();
    }
    public static void removeAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    public static void saveRecent(String s, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String recent = sharedPreferences.getString("recent", "");
        recent =  recent.replace(s+",","");
        recent = s + "," + recent;
        editor.putBoolean(s+"_recent",true);
        editor.putString("recent", recent);
        editor.apply();
        ((MainActivity) context).recentRadios.add(s);
        //recent = recent.replace(s+",","");

        //int i;
//        if (sharedPreferences.getInt("recentCount", 0) > 10) {
//            i = recent.lastIndexOf(',');
//            recent = recent.substring(0, i);
//        } else {
//            editor.putInt("recentCount", sharedPreferences.getInt("recentCount", 0) + 1);
//        }


    }
    public static void saveFavourite(String s, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favourite = sharedPreferences.getString("favourite", "");
        if (favourite.contains(s)) {
            return;
        }
        favourite = s + "," + favourite;
        editor.putString("favourite", favourite).apply();
        ((MainActivity) context).favouriteRadios.add(s);
    }
    public static void removeFavourite(String s, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favourite = sharedPreferences.getString("favourite", "");
        favourite = favourite.replace(s + ",", "");
        editor.putString("favourite", favourite).apply();
        ((MainActivity)context).favouriteRadios.remove(s);
    }
    public static void saveRadioStation(Radio r, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(r.getId() + "_name", r.getName());
        editor.putString(r.getId() + "_stream", r.getStreamURL());
        editor.putString(r.getId() + "_image", r.getImageURL());
        editor.putString(r.getId() + "_id", r.getId());
        editor.putString(r.getId() + "_category", r.getCategory());
        editor.putBoolean(r.getId() + "_playing", r.isPlaying());
        editor.putBoolean(r.getId() + "_equalizer", r.isEqualizer());
        editor.putBoolean(r.getId() + "_loading", r.isLoading());
        editor.putBoolean(r.getId() + "_recent", r.isRecent());
        editor.putBoolean(r.getId() + "_favourite", r.isFavourite());
        editor.apply();
    }
    public static Radio getRadioStation(String s, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(s + "_name", "");
        String streamURL = sharedPreferences.getString(s + "_stream", "");
        String imageURL = sharedPreferences.getString(s + "_image", "");
        String id = sharedPreferences.getString(s + "_id", "");
        String category = sharedPreferences.getString(s + "_category", "");
        boolean playing = sharedPreferences.getBoolean(s + "_playing", false);
        boolean equalizer = sharedPreferences.getBoolean(s + "_equalizer", false);
        boolean loading = sharedPreferences.getBoolean(s + "_loading", false);
        boolean recent = sharedPreferences.getBoolean(s + "_recent", false);
        boolean favourite = sharedPreferences.getBoolean(s + "_favourite", false);
        return new Radio(imageURL, streamURL, name, id, category, playing, equalizer, loading, favourite,recent);

    }
    public static boolean isRadioStationSaved(String s,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.contains(s + "_id");
    }
    public static void setRadioStationSingleValueBoolean(String s, String k, Boolean b, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(s + "_" + k, b).apply();
    }
    public static void setRadioStationSingleValueString(String s, String k, String b, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(s + "_" + k, b).apply();
    }
    public static boolean getRadioStationSingleValueBoolean(String s, String k, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(s + "_" + k, false);

    }
    public static String getRadioStationSingleValueString(String s, String k, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString(s + "_" + k, "");
    }
    public static void saveStack(String s,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String stack = sharedPreferences.getString("stack", "");
        String[] stacks = stack.split(",");
        for (String f : stacks) {
            if(f.length()>0){
                editor.putBoolean(f + "_playing", false);
                editor.putBoolean(f + "_equalizer", false);
                editor.putBoolean(f + "_loading", false);
            }
        }
        editor.putString("stack",s);
        editor.apply();

    }
    public static void T(Context context, String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
    public static String getValue(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}
