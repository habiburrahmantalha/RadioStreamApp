package com.rrmsense.radiostream.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rrmsense.radiostream.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;
    //ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        TextView tx = (TextView)findViewById(R.id.loading_text);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/zektonBold.ttf");

        tx.setTypeface(custom_font);

        ShimmerFrameLayout container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.setBaseAlpha(0.25f);
        container.setDuration(1600);
        //container.setDropoff(0.1f);
        //container.useDefaults();
        container.setRepeatMode(ObjectAnimator.REVERSE);
        //container.setRepeatCount(6);
        //container.setTilt(0);/////
        //container.setIntensity(0.1f);
        //container.setMaskShape(ShimmerFrameLayout.MaskShape.RADIAL);
        container.startShimmerAnimation();

        //image = (ImageView) findViewById(R.id.image);

       /* Glide.with(SplashScreenActivity.this)
                .load(R.drawable.giphy)
                .asGif()
                .crossFade()
                .into(image);*/



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
