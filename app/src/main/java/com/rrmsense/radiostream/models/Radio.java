package com.rrmsense.radiostream.models;

/**
 * Created by Talha on 2/12/2017.
 */

public class Radio {
    String imageURL;
    String streamURL;
    String name;
    boolean buttonPlaying;
    boolean imageGif;
    boolean imageLoading;
    boolean buttonFavourite;


    public Radio(String imageURL, String streamURL,String name) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
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

    public boolean isImageGif() {
        return imageGif;
    }

    public void setImageGif(boolean imageGif) {
        this.imageGif = imageGif;
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
}
