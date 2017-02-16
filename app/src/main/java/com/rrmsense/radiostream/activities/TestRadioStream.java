package com.rrmsense.radiostream.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rrmsense.radiostream.R;

public class TestRadioStream extends AppCompatActivity implements View.OnClickListener{

    MediaPlayer mediaPlayer;
    Button play;
    Button pause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_radio_stream);

        Uri uri = Uri.parse("http://202.126.122.43:9898/;stream.mp3");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepare();
        }catch(Exception e){

        }
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        pause.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                mediaPlayer.start();
                play.setEnabled(false);
                pause.setEnabled(true);
                break;
            case R.id.pause:
                mediaPlayer.pause();
                play.setEnabled(true);
                pause.setEnabled(false);

                break;
        }
    }
}
