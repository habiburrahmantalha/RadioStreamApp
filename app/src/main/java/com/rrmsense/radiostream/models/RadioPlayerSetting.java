package com.rrmsense.radiostream.models;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Talha on 2/14/2017.
 */

public class RadioPlayerSetting {
    ProgressBar progressBar;
    ImageView imageView;
    Button button;

    public RadioPlayerSetting(ProgressBar progressBar, ImageView imageView, Button button) {
        this.progressBar = progressBar;
        this.imageView = imageView;
        this.button = button;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
