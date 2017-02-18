package com.rrmsense.radiostream.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Talha on 2/18/2017.
 */

public class Storage {

    public static void saveRecent(String s,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String recent = sharedPreferences.getString("recent","");
        recent = s+","+recent;
        int i;
        if(sharedPreferences.getInt("recentCount",0)>10){
            i = recent.lastIndexOf(',');
            recent = recent.substring(0,i);
        }else{
            editor.putInt("recentCount",sharedPreferences.getInt("recentCount",0)+1);
        }
        editor.putString("recent",recent).apply();

    }
    public static ArrayList getRecent(Context context){
        ArrayList<Radio> radios = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
        String recent = sharedPreferences.getString("recent","");
        String[] recents = recent.split(",");
        for(String f : recents){
            radios.add(getRadioStation(f,context));
        }

        return radios;
    }
    public static ArrayList getFavourite(Context context){
        ArrayList<Radio> radios = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
        String favourite = sharedPreferences.getString("favourite","");
        String[] favourites = favourite.split(",");
        for(String f : favourites){
            radios.add(getRadioStation(f,context));
        }

        return radios;
    }
    public static void saveFavourite(String s,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favourite = sharedPreferences.getString("favourite","");
        favourite = s+","+favourite;
        int i;
        if(sharedPreferences.getInt("favouriteCount",0)>10){
            i = favourite.lastIndexOf(',');
            favourite = favourite.substring(0,i);
        }else{
            editor.putInt("favouriteCount",sharedPreferences.getInt("favouriteCount",0)+1);
        }
        editor.putString("favourite",favourite).apply();


    }
    public static void saveRadioStation(Radio r,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(r.getID()+"_name",r.getName());
        editor.putString(r.getID()+"_stream",r.getStreamURL());
        editor.putString(r.getID()+"_image",r.getImageURL());
        editor.putString(r.getID()+"_id",r.getID());
        editor.apply();
    }

    public static Radio getRadioStation(String s, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(s+"_name","");
        String streamURL = sharedPreferences.getString(s+"_stream","");
        String imageURL = sharedPreferences.getString(s+"_image","");
        String id = sharedPreferences.getString(s+"_id","");;
        Radio radio = new Radio(imageURL,streamURL,name,id,false,false,false,false);
        return radio;
    }
}
