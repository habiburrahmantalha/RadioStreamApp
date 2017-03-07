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
    private boolean playing;
    private boolean equalizer;
    private boolean loading;
    private boolean favourite;
    private boolean recent;

    public Radio(){

    }
    public Radio(String imageURL, String streamURL,String name,String id,String category) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
        this.id = id;
        this.category = category;
    }

    public Radio(String imageURL, String streamURL, String name, String id, String category, boolean playing, boolean equalizer, boolean loading, boolean favourite, boolean recent) {
        this.imageURL = imageURL;
        this.streamURL = streamURL;
        this.name = name;
        this.id = id;
        this.category = category;
        this.playing = playing;
        this.equalizer = equalizer;
        this.loading = loading;
        this.favourite = favourite;
        this.recent = recent;
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

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isEqualizer() {
        return equalizer;
    }

    public void setEqualizer(boolean equalizer) {
        this.equalizer = equalizer;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
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

}
