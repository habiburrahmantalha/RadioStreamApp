package com.rrmsense.radiostream.models;

/**
 * Created by Talha on 2/12/2017.
 */

public class Radio {
    String imageURL;
    String streamURL;
    String name;
    String buttonText;
    boolean imageGif;
    boolean imageLoading;


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

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
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
}
