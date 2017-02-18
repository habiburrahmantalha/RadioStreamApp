package com.rrmsense.radiostream.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.adapters.ViewPagerAdapter;
import com.rrmsense.radiostream.fragments.BanglaRadioFragment;
import com.rrmsense.radiostream.fragments.ViewPagerFragment;
import com.rrmsense.radiostream.interfaces.OnPreparedCallback;
import com.rrmsense.radiostream.models.Radio;
import com.rrmsense.radiostream.models.RadioPlayerSetting;
import com.rrmsense.radiostream.models.SelectFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {



    public MediaPlayer mediaPlayer;
    OnPreparedCallback onPreparedCallback;
    int position;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    public ArrayList<Radio> radios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.hide();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadRadioStation();
        //openFragment(SelectFragment.FRAGMENT_VIEW_PAGER);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(int fragmentID){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (fragmentID){
            case SelectFragment.FRAGMENT_VIEW_PAGER:
                fragment = new ViewPagerFragment();
                break;
            case SelectFragment.FRAGMENT_BANGLA_RADIO:
                fragment = new BanglaRadioFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_holder,fragment)
                .commit();
    }

    public void loadViewPager(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Bangla Radio Station"));
        tabLayout.addTab(tabLayout.newTab().setText("Recent Activity"));
        tabLayout.addTab(tabLayout.newTab().setText("Favourites"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void loadRadioStation(){
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://www.rrmelectronics.com/appserver/RadioStreamLink.php", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Log.d("JSON",response.toString());
                for(int i=0;i < response.length();i++){
                    //Log.d("JSON",response.get(i).toString());
                    Gson gson = new Gson();
                    Radio r = null;
                    try {
                        r = gson.fromJson(response.get(i).toString(), Radio.class);
                        r.setButtonPlaying(false);
                        r.setImageEqualizer(false);
                        r.setImageLoading(false);
                        r.setButtonFavourite(false);
                        radios.add(r);
                        Log.d("IMAGE_URL", response.get(i).toString());
                        Log.d("JSON",r.getName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                loadViewPager();
            }
        });
    }

    public void playRadio(String url, int position, OnPreparedCallback onPreparedCallback){
        this.position = position;
        this.onPreparedCallback = onPreparedCallback;
        stopRadio();
        Uri uri = Uri.parse(url);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();

        try{
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.prepareAsync();
        }catch(Exception e){
        }
    }
    public  void stopRadio(){
        try {
            if(mediaPlayer!=null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

            }
        } catch(Exception e){

        }
    }
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //((ImageView) radioView.findViewById(R.id.image_gif)).setVisibility(ProgressBar.VISIBLE);
        //((ProgressBar) radioView.findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
        mediaPlayer.start();
        onPreparedCallback.OnPreparedCallback(position);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("Progress", String.valueOf(percent));
    }

}

