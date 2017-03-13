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

public class CardViewExpanded {

    public ImageButton previous;
    public ImageButton next;
    public ImageButton play;
    public ImageButton stop;
    public ImageButton favourite;
    public ImageButton headphone;
    public ImageButton volume;
    public ProgressBar progressBar;
    public ImageView image_radio;
    public ImageView equalizer;
    public ImageView circle;


    public CardViewExpanded(ImageButton previous, ImageButton next, ImageButton play, ImageButton stop, ImageButton favourite,ImageButton headphone,ImageButton volume, ProgressBar progressBar, ImageView image_radio,ImageView equalizer) {
        this.previous = previous;
        this.next = next;
        this.play = play;
        this.stop = stop;
        this.favourite = favourite;
        this.headphone = headphone;
        this.volume = volume;
        this.progressBar = progressBar;
        this.image_radio = image_radio;
        this.equalizer = equalizer;

    }

    public void setValue(Radio r, Context context){

        Glide.with(context).load(r.getImageURL()).override(150,150).fitCenter().diskCacheStrategy( DiskCacheStrategy.RESULT ).into(image_radio);
        Glide.with(context).load(R.drawable.music_spectrum).asGif().into(equalizer);

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
    public void headphone(boolean headphone){
        if(headphone){
            this.headphone.setImageResource(R.drawable.headphone_select);
        }
        else{
            this.headphone.setImageResource(R.drawable.headphone_unselect);
        }
    }
    public void volume(boolean volume){
        if(volume){
            this.volume.setImageResource(R.drawable.volume_loud);
        }
        else{
            this.volume.setImageResource(R.drawable.volume_mute);
        }

    }
}
