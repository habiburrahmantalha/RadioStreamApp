package com.rrmsense.radiostream.models;

/**
 * Created by Talha on 2/12/2017.
 */

public class Radio {
    String imageURL;
    String streamURL;
    String name;
    String id;
    boolean buttonPlaying;
    boolean imageEqualizer;
    boolean imageLoading;
    boolean buttonFavourite;

    public Radio(){

    }
    public Radio(String imageURL, String streamURL,String name,String id) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
        this.id = id;
    }

    public Radio(String imageURL, String streamURL, String name,String id, boolean buttonPlaying, boolean imageEqualizer, boolean imageLoading, boolean buttonFavourite) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
        this.id = id;
        this.buttonPlaying = buttonPlaying;
        this.imageEqualizer = imageEqualizer;
        this.imageLoading = imageLoading;
        this.buttonFavourite = buttonFavourite;
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

    public boolean isButtonPlaying() {
        return buttonPlaying;
    }

    public void setButtonPlaying(boolean buttonPlaying) {
        this.buttonPlaying = buttonPlaying;
    }

    public boolean isImageEqualizer() {
        return imageEqualizer;
    }

    public void setImageEqualizer(boolean imageEqualizer) {
        this.imageEqualizer = imageEqualizer;
    }

    public boolean isImageLoading() {
        return imageLoading;
    }

    public void setImageLoading(boolean imageLoading) {
        this.imageLoading = imageLoading;
    }

    public boolean isButtonFavourite() {
        return buttonFavourite;
    }

    public void setButtonFavourite(boolean buttonFavourite) {
        this.buttonFavourite = buttonFavourite;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
}
