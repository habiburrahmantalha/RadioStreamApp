package com.rrmsense.radiostream.models;

/**
 * Created by Talha on 2/12/2017.
 */

public class Radio {
    private String imageURL;
    private String streamURL;
    private String name;
    private String id;
    private String category;
    private boolean buttonPlaying;
    private boolean imageEqualizer;
    private boolean imageLoading;
    private boolean buttonFavourite;
    private boolean buttonRecent;

    public Radio(){

    }
    public Radio(String imageURL, String streamURL,String name,String id,String category) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
        this.id = id;
        this.category = category;
    }

    public Radio(String imageURL, String streamURL, String name,String id,String category, boolean buttonPlaying, boolean imageEqualizer, boolean imageLoading, boolean buttonFavourite,boolean buttonRecent ) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
        this.id = id;
        this.category = category;
        this.buttonPlaying = buttonPlaying;
        this.imageEqualizer = imageEqualizer;
        this.imageLoading = imageLoading;
        this.buttonFavourite = buttonFavourite;
        this.buttonRecent = buttonRecent;
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

    public boolean isButtonRecent() {
        return buttonRecent;
    }

    public void setButtonRecent(boolean buttonRecent) {
        this.buttonRecent = buttonRecent;
    }

}
