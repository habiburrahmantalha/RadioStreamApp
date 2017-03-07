package com.rrmsense.radiostream.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.fragments.RadioFragment;
import com.rrmsense.radiostream.interfaces.OnPreparedCallback;
import com.rrmsense.radiostream.models.CardViewCollapsed;
import com.rrmsense.radiostream.models.Radio;
import com.rrmsense.radiostream.models.SelectFragment;
import com.rrmsense.radiostream.models.Storage;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, SharedPreferences.OnSharedPreferenceChangeListener, MediaPlayer.OnErrorListener, View.OnClickListener {

    public MediaPlayer mediaPlayer;
    public ArrayList<String> banglaRadios = new ArrayList<>();
    public ArrayList<String> internationalRadios = new ArrayList<>();
    public ArrayList<String> newsRadios = new ArrayList<>();
    public ArrayList<String> musicRadios = new ArrayList<>();
    public ArrayList<String> recentRadios = new ArrayList<>();
    public ArrayList<String> favouriteRadios = new ArrayList<>();
    public int FRAGMENT = SelectFragment.FRAGMENT_BANGLA_RADIO;
    OnPreparedCallback onPreparedCallback;
    int position;
    Deque<String> history = new ArrayDeque<>();
    View layout_collapsed;
    View layout_expanded;
    LinearLayout cardView_holder;
    CardViewCollapsed cardViewCollapsed;
    Radio playingNow;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    public static int dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        LayoutInflater li = LayoutInflater.from(this);
        layout_collapsed = li.inflate(R.layout.cardview_radio_main_collapsed, null, false);
        layout_expanded = li.inflate(R.layout.cardview_radio_main_expanded, null, false);
        cardView_holder = (LinearLayout) findViewById(R.id.cardView_holder);
        cardView_holder.addView(layout_collapsed);
        CardView cardview_collapsed = (CardView) findViewById(R.id.cardview_collapsed);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        // slidingUpPanelLayout.setPanelHeight(dipToPixels(getApplicationContext(), cardview_collapsed.getHeight()));

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Toast.makeText(MainActivity.this, "" +slideOffset,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Toast.makeText(MainActivity.this, previousState.toString() + " " + newState.toString(), Toast.LENGTH_SHORT).show();

                if (previousState == SlidingUpPanelLayout.PanelState.DRAGGING) {

                    if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        cardView_holder.addView(layout_collapsed);
                        slidingUpPanelLayout.setTouchEnabled(true);
                    } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        cardView_holder.addView(layout_expanded);
                        slidingUpPanelLayout.setTouchEnabled(false);
                    }
                } else if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED && newState == SlidingUpPanelLayout.PanelState.DRAGGING) {

                    cardView_holder.removeView(layout_expanded);

                } else if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED && newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    cardView_holder.removeView(layout_collapsed);
                }
            }


        });

        cardViewCollapsed = new CardViewCollapsed((ImageButton) layout_collapsed.findViewById(R.id.previous), (ImageButton) layout_collapsed.findViewById(R.id.next), (ImageButton) layout_collapsed.findViewById(R.id.play), (ImageButton) layout_collapsed.findViewById(R.id.stop), (ImageButton) layout_collapsed.findViewById(R.id.favourite), (ProgressBar) layout_collapsed.findViewById(R.id.progressBar), (ImageView) layout_collapsed.findViewById(R.id.image_radio));
        cardViewCollapsed.previous.setOnClickListener(this);
        cardViewCollapsed.next.setOnClickListener(this);
        cardViewCollapsed.play.setOnClickListener(this);
        cardViewCollapsed.stop.setOnClickListener(this);
        cardViewCollapsed.favourite.setOnClickListener(this);
        loadRadioStation();

        String id = Storage.getRadioStationSingleValueString("playing","id",getApplicationContext());
        if(id!=""){
            playingNow = Storage.getRadioStation(id,getApplicationContext());
            cardViewCollapsed.setValue(playingNow,getApplicationContext());
            cardViewCollapsed.favourite(playingNow.isFavourite());
        }
    }

    public void loadRadioStation() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://www.rrmelectronics.com/appserver/RadioStreamLink.php", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Log.d("JSON",response.toString());
                for (int i = 0; i < response.length(); i++) {
                    //Log.d("JSON",response.get(i).toString());
                    Gson gson = new Gson();
                    Radio r = null;
                    try {
                        r = gson.fromJson(response.get(i).toString(), Radio.class);
                        r.setPlaying(false);
                        r.setEqualizer(false);
                        r.setLoading(false);
                        r.setFavourite(false);
                        r.setRecent(false);
                        if (!Storage.isRadioStationSaved(r.getId(), getApplication())) {
                            Storage.saveRadioStation(r, getApplicationContext());
                            //Log.d("NO",r.getId());
                        } else {
                            //Log.d("YES",r.getId());
                            Storage.setRadioStationSingleValueBoolean(r.getId(), "playing", false, getApplication());
                            Storage.setRadioStationSingleValueBoolean(r.getId(), "loading", false, getApplication());
                            Storage.setRadioStationSingleValueBoolean(r.getId(), "equalizer", false, getApplication());
                        }
                        switch (r.getCategory()) {
                            case "bangla":
                                banglaRadios.add(r.getId());
                                break;
                            case "news":
                                newsRadios.add(r.getId());
                                break;
                            case "music":
                                musicRadios.add(r.getId());
                                break;

                            case "international":
                                internationalRadios.add(r.getId());
                                break;
                        }

                        //Log.d("JSON",r.getName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //openFragment(SelectFragment.FRAGMENT_BANGLA_RADIO);
                //loadRecentRadioStation();
                loadFavouriteRadioStation();
                //loadViewPager();
                openFragment(SelectFragment.FRAGMENT_BANGLA_RADIO);
            }
        });
        pd.hide();
    }

    private void loadFavouriteRadioStation() {
        favouriteRadios = Storage.getFavourite(getApplicationContext());
    }

    public void openFragment(int fragmentID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new RadioFragment();
        Bundle bundle = new Bundle();
        switch (fragmentID) {
            case SelectFragment.FRAGMENT_BANGLA_RADIO:
                //getSupportActionBar().setTitle("Bangla Online Radio");
                bundle.putInt("ID", SelectFragment.FRAGMENT_BANGLA_RADIO);
                break;
            case SelectFragment.FRAGMENT_FAVOURITE:
                bundle.putInt("ID", SelectFragment.FRAGMENT_FAVOURITE);
                //getSupportActionBar().setTitle("Favourites");
                break;
            case SelectFragment.FRAGMENT_RECENT:
                bundle.putInt("ID", SelectFragment.FRAGMENT_RECENT);
                //getSupportActionBar().setTitle("Recently Played");
                break;
            case SelectFragment.FRAGMENT_NEWS_RADIO:
                bundle.putInt("ID", SelectFragment.FRAGMENT_NEWS_RADIO);
                //getSupportActionBar().setTitle("Recently Played");
                break;
            case SelectFragment.FRAGMENT_MUSIC_RADIO:
                bundle.putInt("ID", SelectFragment.FRAGMENT_MUSIC_RADIO);
                //getSupportActionBar().setTitle("Recently Played");
                break;
            case SelectFragment.FRAGMENT_INTERNATIONAL_RADIO:
                bundle.putInt("ID", SelectFragment.FRAGMENT_INTERNATIONAL_RADIO);
                //getSupportActionBar().setTitle("Recently Played");
                break;
        }
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
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
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadRecentRadioStation() {
        recentRadios = Storage.getRecent(getApplicationContext());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_music) {
            openFragment(SelectFragment.FRAGMENT_MUSIC_RADIO);

            FRAGMENT = SelectFragment.FRAGMENT_MUSIC_RADIO;


        } else if (id == R.id.nav_news) {


            FRAGMENT = SelectFragment.FRAGMENT_NEWS_RADIO;

            openFragment(SelectFragment.FRAGMENT_NEWS_RADIO);
        } else if (id == R.id.nav_international) {

            FRAGMENT = SelectFragment.FRAGMENT_INTERNATIONAL_RADIO;

            openFragment(SelectFragment.FRAGMENT_INTERNATIONAL_RADIO);
        } else if (id == R.id.nav_bangla) {

            FRAGMENT = SelectFragment.FRAGMENT_BANGLA_RADIO;
            openFragment(SelectFragment.FRAGMENT_BANGLA_RADIO);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

            startActivity(new Intent(this, AboutActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "playing_id":
                playingNow = Storage.getRadioStation(Storage.getValue(key, getApplicationContext()), getApplicationContext());
                cardViewCollapsed.setValue(playingNow, getApplicationContext());
                cardViewCollapsed.loading();
                cardViewCollapsed.favourite(playingNow.isFavourite());
                playRadio();
                break;
            case "playing_favourite":

                boolean f = Storage.getRadioStationSingleValueBoolean("playing","favourite",getApplicationContext());
                cardViewCollapsed.favourite(f);

                break;
        }
    }

    public void playRadio() {
        stopRadio();
        Uri uri = Uri.parse(playingNow.getStreamURL());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:

                break;
            case R.id.stop:
                cardViewCollapsed.stop();
                stopRadio();
                break;

            case R.id.previous:

                break;

            case R.id.play:
                cardViewCollapsed.loading();
                playRadio();

                break;

            case R.id.favourite:
                boolean f = Storage.getRadioStationSingleValueBoolean("playing","favourite",getApplicationContext());
                Storage.setRadioStationSingleValueBoolean("playing","favourite",!f,getApplicationContext());
                cardViewCollapsed.favourite(!f);

                break;
        }
    }

    public void stopRadio() {

        //resetRadio();
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
        }
    }

    public void playRadio(String id, String url, int position, OnPreparedCallback onPreparedCallback) {

        resetRadio();
        history.push(id);
        this.position = position;
        this.onPreparedCallback = onPreparedCallback;
        stopRadio();
        Uri uri = Uri.parse(url);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
        }

    }

    public void resetRadio() {
        while (!history.isEmpty()) {
            Storage.setRadioStationSingleValueBoolean(history.peek(), "playing", false, getApplication());
            //Storage.setRadioStationSingleValueBoolean(history.peek(),"loading",false,getApplication());
            Storage.setRadioStationSingleValueBoolean(history.peek(), "equalizer", false, getApplication());
            history.pop();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //((ImageView) radioView.findViewById(R.id.image_gif)).setVisibility(ProgressBar.VISIBLE);
        //((ProgressBar) radioView.findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
        cardViewCollapsed.play();
        mediaPlayer.start();
        //onPreparedCallback.OnPreparedCallback(position);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("Progress", String.valueOf(percent));
        Storage.T(getApplicationContext(), String.valueOf(percent));
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Storage.si
        return false;
    }
}

