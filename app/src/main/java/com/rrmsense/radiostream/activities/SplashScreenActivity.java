package com.rrmsense.radiostream.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.rrmsense.radiostream.R;

import static com.rrmsense.radiostream.R.id.imageView;
import static com.rrmsense.radiostream.R.id.progressBar;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 0;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        image = (ImageView) findViewById(R.id.image);

        Glide.with(SplashScreenActivity.this)
                .load(R.drawable.musical)
                .asGif()
                .crossFade()
                .into(image);



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
