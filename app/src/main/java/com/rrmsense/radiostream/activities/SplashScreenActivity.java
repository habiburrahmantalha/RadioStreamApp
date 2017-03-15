package com.rrmsense.radiostream.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.Services.NetworkStateReceiver;

public class SplashScreenActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private static int SPLASH_TIME_OUT = 6000;
    private NetworkStateReceiver networkStateReceiver;
    RelativeLayout relativeLayout;
    ImageView splashImage;
    ImageView noInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_splash_screen);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));


        splashImage = (ImageView) findViewById(R.id.splashImage);
        noInternet = (ImageView) findViewById(R.id.noInternet);
        Glide.with(SplashScreenActivity.this)
                .load(R.drawable.giphy)
                .asGif()
                .crossFade()
                .fitCenter()
                .into(splashImage);
    }

    @Override
    public void networkAvailable() {
        splashImage.setVisibility(ImageView.VISIBLE);
        noInternet.setVisibility(ImageView.INVISIBLE);
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
        splashImage.setVisibility(ImageView.INVISIBLE);
        noInternet.setVisibility(ImageView.VISIBLE);
        Snackbar snackbar = Snackbar.make(relativeLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(networkStateReceiver);
    }
}
