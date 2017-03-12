package com.rrmsense.radiostream.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
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
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.fragments.RadioFragment;
import com.rrmsense.radiostream.interfaces.OnPreparedCallback;
import com.rrmsense.radiostream.models.CardViewCollapsed;
import com.rrmsense.radiostream.models.CardViewExpanded;
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
    CardViewExpanded cardViewExpanded;
    Radio playingNew;
    Radio playingOld;
    private MusicIntentReceiver musicIntentReceiver;
    private SlidingUpPanelLayout slidingUpPanelLayout;


    public static int dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicIntentReceiver = new MusicIntentReceiver();
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
        //slidingUpPanelLayout.setPanelHeight(dipToPixels(getApplicationContext(), 100));
        //slidingUpPanelLayout.setAnchorPoint(0.3f);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Toast.makeText(MainActivity.this, "" +slideOffset,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //Toast.makeText(MainActivity.this, previousState.toString() + " " + newState.toString(), Toast.LENGTH_SHORT).show();

                if (previousState == SlidingUpPanelLayout.PanelState.DRAGGING) {

                    if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        cardView_holder.addView(layout_collapsed);
                        //slidingUpPanelLayout.setTouchEnabled(true);
                        onPanelCollapsed();
                    } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        cardView_holder.addView(layout_expanded);
                        //slidingUpPanelLayout.setTouchEnabled(false);
                        onPanelExpanded();
                    }
                } else if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED && newState == SlidingUpPanelLayout.PanelState.DRAGGING) {

                    cardView_holder.removeView(layout_expanded);

                } else if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED && newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    cardView_holder.removeView(layout_collapsed);
                }
            }


        });

        CardViewInit();

        loadRadioStation();

        String id = Storage.getRadioStationSingleValueString("playing", "id", getApplicationContext());
        if (id != "") {
            playingNew = Storage.getRadioStation(id, getApplicationContext());
            cardViewCollapsed.setValue(playingNew, getApplicationContext());
            //cardViewCollapsed.favourite(playingNew.isFavourite());
        }
        playingNew = Storage.getRadioStation(Storage.getValue("playing_id", getApplicationContext()), getApplicationContext());
        playingNew.setState(Radio.STOPPED);
        setNotification();
        registerReceiver(broadcastReceiver,new IntentFilter("CLOSE"));
        registerReceiver(broadcastReceiver,new IntentFilter("CONTROLLER"));



    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case "CLOSE":
                    notificationManager.cancel(1);
                    finish();
                    break;
                case "CONTROLLER":
                    if(Radio.PLAYING == playingNew.getState()){
                        stopRadio();
                        notificationView.setImageViewResource(R.id.controller, R.drawable.play);


                    }
                    else if(Radio.STOPPED == playingNew.getState()) {
                        playRadio();
                        notificationView.setImageViewResource(R.id.controller, R.drawable.stop);
                    }
                    notificationManager.notify(1,notificationBuilder.build());

                    break;

            }
        }
    };

    private void onPanelCollapsed() {
    }

    private void onPanelExpanded() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        cardViewExpanded.headphone(!audioManager.isSpeakerphoneOn());
        cardViewExpanded.setValue(playingNew, getApplicationContext());
        playingNew.setFavourite(Storage.getRadioStationSingleValueBoolean(playingNew.getId(), "favourite", getApplicationContext()));
        cardViewExpanded.favourite(playingNew.isFavourite());
        if (playingNew.getState() == Radio.STOPPED) {
            cardViewExpanded.stop();
        }
        new MusicIntentReceiver();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                cardViewExpanded.volume(false);
            } else {
                cardViewExpanded.volume(true);
            }
        } else {
            if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {

                cardViewExpanded.volume(false);
            } else {

                cardViewExpanded.volume(true);
            }
        }


    }

    private void CardViewInit() {
        cardViewCollapsed = new CardViewCollapsed((ImageButton) layout_collapsed.findViewById(R.id.previous), (ImageButton) layout_collapsed.findViewById(R.id.next), (ImageButton) layout_collapsed.findViewById(R.id.play), (ImageButton) layout_collapsed.findViewById(R.id.stop), /*(ImageButton) layout_collapsed.findViewById(R.id.favourite),*/ (ProgressBar) layout_collapsed.findViewById(R.id.progressBar), (ImageView) layout_collapsed.findViewById(R.id.image_radio));
        cardViewCollapsed.previous.setOnClickListener(this);
        cardViewCollapsed.next.setOnClickListener(this);
        cardViewCollapsed.play.setOnClickListener(this);
        cardViewCollapsed.stop.setOnClickListener(this);
        //cardViewCollapsed.favourite.setOnClickListener(this);

        cardViewExpanded = new CardViewExpanded((ImageButton) layout_expanded.findViewById(R.id.previous), (ImageButton) layout_expanded.findViewById(R.id.next), (ImageButton) layout_expanded.findViewById(R.id.play), (ImageButton) layout_expanded.findViewById(R.id.stop), (ImageButton) layout_expanded.findViewById(R.id.favourite), (ImageButton) layout_expanded.findViewById(R.id.headphone), (ImageButton) layout_expanded.findViewById(R.id.volume), (ProgressBar) layout_expanded.findViewById(R.id.progressBar), (ImageView) layout_expanded.findViewById(R.id.image_radio), (ImageView) layout_expanded.findViewById(R.id.equalizer));
        cardViewExpanded.previous.setOnClickListener(this);
        cardViewExpanded.next.setOnClickListener(this);
        cardViewExpanded.play.setOnClickListener(this);
        cardViewExpanded.stop.setOnClickListener(this);
        cardViewExpanded.favourite.setOnClickListener(this);
        cardViewExpanded.volume.setOnClickListener(this);
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
                        r.setState(Radio.STOPPED);
                        r.setFavourite(false);
                        r.setRecent(false);
                        if (!Storage.isRadioStationSaved(r.getId(), getApplication())) {
                            Storage.saveRadioStation(r, getApplicationContext());
                            //Log.d("NO",r.getId());
                        } else {
                            //Log.d("YES",r.getId());

                            Storage.saveState(r.getId(), Radio.STOPPED, getApplicationContext());
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
                loadRecentRadioStation();
                loadFavouriteRadioStation();
                //loadViewPager();
                openFragment(SelectFragment.FRAGMENT_BANGLA_RADIO);
            }
        });
        pd.hide();
    }

    private void loadRecentRadioStation() {
        recentRadios = Storage.getRecent(getApplicationContext());
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
    protected void onDestroy() {
        stopRadio();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        } else {
            //super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(musicIntentReceiver);
        this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(musicIntentReceiver, filter);
        this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
        super.onResume();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_music:
                openFragment(SelectFragment.FRAGMENT_MUSIC_RADIO);

                FRAGMENT = SelectFragment.FRAGMENT_MUSIC_RADIO;
                break;
            case R.id.nav_news:
                FRAGMENT = SelectFragment.FRAGMENT_NEWS_RADIO;

                openFragment(SelectFragment.FRAGMENT_NEWS_RADIO);
                break;
            case R.id.nav_international:
                FRAGMENT = SelectFragment.FRAGMENT_INTERNATIONAL_RADIO;

                openFragment(SelectFragment.FRAGMENT_INTERNATIONAL_RADIO);
                break;
            case R.id.nav_bangla:
                FRAGMENT = SelectFragment.FRAGMENT_BANGLA_RADIO;
                openFragment(SelectFragment.FRAGMENT_BANGLA_RADIO);
                break;

            case R.id.nav_favourite:
                FRAGMENT = SelectFragment.FRAGMENT_FAVOURITE;
                openFragment(SelectFragment.FRAGMENT_FAVOURITE);
                break;
            case R.id.nav_recent:
                FRAGMENT = SelectFragment.FRAGMENT_RECENT;
                openFragment(SelectFragment.FRAGMENT_RECENT);
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "playing_id":
                playingOld = playingNew;
                Storage.saveState(playingOld.getId(), Radio.STOPPED, getApplicationContext());
                playingNew = Storage.getRadioStation(Storage.getValue(key, getApplicationContext()), getApplicationContext());
                playRadio();
                break;
            case "New_Favourite_Added":
                String fa = Storage.getValue(key, getApplicationContext());
                favouriteRadios.add(fa);
                break;
            case "New_Favourite_Removed":
                String fr = Storage.getValue(key, getApplicationContext());
                favouriteRadios.remove(fr);
                break;
        }
    }

    public void playRadio() {
        stopRadio();
        playingNew.setState(Radio.LOADING);
        onStateChanged();
        Uri uri = Uri.parse(playingNew.getStreamURL());
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

    public void stopRadio() {

        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
                playingNew.setState(Radio.STOPPED);
                Storage.saveState(playingNew.getId(), Radio.STOPPED, getApplicationContext());
                onStateChanged();
            }
        } catch (Exception e) {
        }
    }

    public void onStateChanged() {
        switch (playingNew.getState()) {
            case Radio.PLAYING:
                //toast("PLAying");
                cardViewCollapsed.play();
                cardViewExpanded.play();
                break;

            case Radio.LOADING:

                cardViewCollapsed.setValue(playingNew, getApplicationContext());
                cardViewCollapsed.loading();
                //cardViewCollapsed.favourite(playingNew.isFavourite());

                cardViewExpanded.setValue(playingNew, getApplicationContext());
                cardViewExpanded.loading();
                cardViewExpanded.favourite(playingNew.isFavourite());
                //toast("loading");
                break;

            case Radio.STOPPED:
                cardViewCollapsed.stop();
                cardViewExpanded.stop();
                //toast("Stopped");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:

                break;
            case R.id.stop:

                stopRadio();
                break;

            case R.id.previous:

                break;

            case R.id.play:
                playRadio();

                break;

            case R.id.favourite:
                boolean f = Storage.getRadioStationSingleValueBoolean(playingNew.getId(), "favourite", getApplicationContext());
                if (f) {
                    Storage.removeFavourite(playingNew.getId(), getApplicationContext());
                } else {
                    Storage.saveFavourite(playingNew.getId(), getApplicationContext());
                }
                Storage.setRadioStationSingleValueBoolean(playingNew.getId(), "favourite", !f, getApplicationContext());
                playingNew.setFavourite(!f);

                cardViewExpanded.favourite(!f);
                break;
            case R.id.volume:
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_TOGGLE_MUTE, 0);
                    if (audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                        cardViewExpanded.volume(false);
                    } else {
                        cardViewExpanded.volume(true);
                    }
                } else {
                    if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                        cardViewExpanded.volume(true);
                    } else {
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                        cardViewExpanded.volume(false);
                    }
                }


                break;
            case R.id.cardview_collapsed:
                toast("c");
                break;
            case R.id.cardview_expanded:
                toast("e");
                break;
        }
    }

    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void callBack(int position, OnPreparedCallback onPreparedCallback) {
        this.position = position;
        this.onPreparedCallback = onPreparedCallback;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Storage.saveState(playingNew.getId(), Radio.PLAYING, getApplicationContext());
        playingNew.setState(Radio.PLAYING);
        onStateChanged();

        mediaPlayer.start();
        if (onPreparedCallback != null)
            onPreparedCallback.OnPreparedCallback(position);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("Progress", String.valueOf(percent));
        toast(String.valueOf(percent));
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        toast("Playing Error!");
        return false;
    }

    void log(String s) {
        Log.d("LOG", s);
    }

    public void resumePlay() {

        if (playingNew.getState() == Radio.STOPPED)
            playRadio();
    }
    NotificationManager notificationManager;
    RemoteViews notificationView;
    NotificationCompat.Builder notificationBuilder;
    public void setNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


       // @SuppressWarnings("deprecation")
       // Notification notification = new Notification(R.drawable.radio_background, null, System.currentTimeMillis());

        notificationView = new RemoteViews(getPackageName(), R.layout.notification_drawer_controller);

        if(playingNew.getState()==Radio.STOPPED)
            notificationView.setImageViewResource(R.id.controller, R.drawable.play);
        else if(playingNew.getState()==Radio.PLAYING)
            notificationView.setImageViewResource(R.id.controller, R.drawable.stop);


        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //notification.contentView = notificationView;
       // notification.contentIntent = pendingNotificationIntent;
       // notification.flags |= Notification.FLAG_NO_CLEAR;

        notificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.app_icon_2).setTicker("Radio Sration").setContent(notificationView).setContentIntent(pendingNotificationIntent);


        //this is the intent that is supposed to be called when the button is clicked
        Intent switchIntentController = new Intent("com.rrmsense.radiostream.ACTION_CONTROLLER");
        Intent switchIntentClose = new Intent("com.rrmsense.radiostream.ACTION_CLOSE");


        PendingIntent pendingSwitchIntentController = PendingIntent.getBroadcast(this, 0, switchIntentController, 0);
        PendingIntent pendingSwitchIntentClose = PendingIntent.getBroadcast(this, 0, switchIntentClose, 0);

        notificationView.setOnClickPendingIntent(R.id.close, pendingSwitchIntentClose);
        notificationView.setOnClickPendingIntent(R.id.controller, pendingSwitchIntentController);
        notificationManager.notify(1, notificationBuilder.build());


    }

    public class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        log("Headset is unplugged");
                        cardViewExpanded.headphone(false);
                        break;
                    case 1:
                        cardViewExpanded.headphone(true);
                        break;
                    default:

                }
            }
        }
    }

}

