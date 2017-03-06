package com.rrmsense.radiostream.models;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Talha on 3/6/2017.
 */

public class CardViewCollapsed {

    Button previous;
    Button next;
    Button play;
    Button stop;
    Button favourite;
    ProgressBar progressBar;
    ImageView image_radio;

    public CardViewCollapsed(Button previous, Button next, Button play, Button stop, Button favourite, ProgressBar progressBar, ImageView image_radio) {
        this.previous = previous;
        this.next = next;
        this.play = play;
        this.stop = stop;
        this.favourite = favourite;
        this.progressBar = progressBar;
        this.image_radio = image_radio;
    }

    public Button getPrevious() {
        return previous;
    }

    public void setPrevious(Button previous) {
        this.previous = previous;
    }

    public Button getNext() {
        return next;
    }

    public void setNext(Button next) {
        this.next = next;
    }

    public Button getPlay() {
        return play;
    }

    public void setPlay(Button play) {
        this.play = play;
    }

    public Button getStop() {
        return stop;
    }

    public void setStop(Button stop) {
        this.stop = stop;
    }

    public Button getFavourite() {
        return favourite;
    }

    public void setFavourite(Button favourite) {
        this.favourite = favourite;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ImageView getImage_radio() {
        return image_radio;
    }

    public void setImage_radio(ImageView image_radio) {
        this.image_radio = image_radio;
    }

    public void setValue(String id){

    }
}
