package com.rrmsense.radiostream.models;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rrmsense.radiostream.R;

/**
 * Created by Talha on 3/6/2017.
 */

public class CardViewCollapsed {

    public ImageButton previous;
    public ImageButton next;
    public ImageButton play;
    public ImageButton stop;
    public ImageButton favourite;
    public ProgressBar progressBar;
    public ImageView image_radio;

    public CardViewCollapsed(ImageButton previous, ImageButton next, ImageButton play, ImageButton stop, ImageButton favourite, ProgressBar progressBar, ImageView image_radio) {
        this.previous = previous;
        this.next = next;
        this.play = play;
        this.stop = stop;
        this.favourite = favourite;
        this.progressBar = progressBar;
        this.image_radio = image_radio;

        this.previous.setVisibility(Button.INVISIBLE);
        this.next.setVisibility(Button.INVISIBLE);
        this.play.setVisibility(Button.INVISIBLE);
        this.stop.setVisibility(Button.INVISIBLE);
        this.favourite.setVisibility(Button.INVISIBLE);

        this.previous.setVisibility(ImageView.INVISIBLE);
        this.previous.setVisibility(ImageView.INVISIBLE);
        this.progressBar.setVisibility(ProgressBar.INVISIBLE);
    }



    public void setValue(Radio r, Context context){

        Glide.with(context).load(r.getImageURL()).override(150,150).fitCenter().diskCacheStrategy( DiskCacheStrategy.RESULT ).into(image_radio);
        this.play.setVisibility(Button.VISIBLE);
        this.previous.setVisibility(Button.VISIBLE);
        this.next.setVisibility(Button.VISIBLE);
        this.favourite.setVisibility(Button.VISIBLE);

    }


    public void loading(){
        this.play.setVisibility(Button.INVISIBLE);
        this.stop.setVisibility(Button.VISIBLE);
        this.progressBar.setVisibility(Button.VISIBLE);

    }
    public void play(){
        this.stop.setVisibility(Button.VISIBLE);
        this.progressBar.setVisibility(Button.INVISIBLE);

    }
    public void stop(){
        this.play.setVisibility(Button.VISIBLE);
        this.stop.setVisibility(Button.INVISIBLE);
        this.progressBar.setVisibility(Button.INVISIBLE);
    }
    public void favourite(boolean favourite){
        if(favourite){
            this.favourite.setImageResource(R.drawable.favourite_select);
        }
        else{
            this.favourite.setImageResource(R.drawable.favourite_unselect);
        }

    }
}
