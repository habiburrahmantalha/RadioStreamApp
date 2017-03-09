package com.rrmsense.radiostream.models;

/**
 * Created by Talha on 2/12/2017.
 */


public class Radio {
    public static final int PLAYING = 2;
    public static final int LOADING = 1;
    public static final int STOPPED = 0;
    private String imageURL;
    private String streamURL;
    private String name;
    private String id;
    private String category;
    private boolean favourite;
    private boolean recent;
    private int state;


    public Radio() {

    }

    public Radio(String imageURL, String streamURL, String name, String id, String category) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
        this.id = id;
        this.category = category;
    }

    public Radio(String imageURL, String streamURL, String name, String id, String category, boolean favourite, boolean recent, int state) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
        this.id = id;
        this.category = category;
        this.favourite = favourite;
        this.recent = recent;
        this.state = state;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isRecent() {
        return recent;
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;

    }


}
