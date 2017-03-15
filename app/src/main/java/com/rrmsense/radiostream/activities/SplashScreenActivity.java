package com.rrmsense.radiostream.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.Services.NetworkStateReceiver;

public class SplashScreenActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private static int SPLASH_TIME_OUT = 2000;
    private NetworkStateReceiver networkStateReceiver;
    RelativeLayout relativeLayout;
    RelativeLayout internet;
    RelativeLayout noInternet;
    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_splash_screen);
        internet = (RelativeLayout) findViewById(R.id.internet);
        noInternet = (RelativeLayout) findViewById(R.id.no_internet);
        snackbar = Snackbar.make(relativeLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void networkAvailable() {
        if(snackbar.isShown())
            snackbar.dismiss();
        internet.setVisibility(RelativeLayout.VISIBLE);
        noInternet.setVisibility(RelativeLayout.INVISIBLE);

        TextView tx = (TextView)findViewById(R.id.loading_text);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/zektonBold.ttf");
        tx.setTypeface(custom_font);
        ShimmerFrameLayout container = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.setBaseAlpha(0.25f);
        container.setDuration(1600);
        //container.setDropoff(0.1f);
        //container.useDefaults();
        container.setRepeatMode(ObjectAnimator.REVERSE);
        //container.setRepeatCount(6);
        //container.setTilt(0);
        //container.setIntensity(0.1f);
        //container.setMaskShape(ShimmerFrameLayout.MaskShape.RADIAL);
        container.startShimmerAnimation();

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

    @Override
    public void networkUnavailable() {
        //Toast.makeText(this,"No internet connection!",Toast.LENGTH_SHORT).show();
        internet.setVisibility(RelativeLayout.INVISIBLE);
        noInternet.setVisibility(RelativeLayout.VISIBLE);


        snackbar.show();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(networkStateReceiver);
    }
}
