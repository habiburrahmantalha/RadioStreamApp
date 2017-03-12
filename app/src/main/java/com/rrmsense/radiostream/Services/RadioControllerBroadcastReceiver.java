package com.rrmsense.radiostream.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Talha on 3/12/2017.
 */

public class RadioControllerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println("intent action = " + action);
        long id = intent.getLongExtra("id", -1);

        /*if(Constant.PLAY_ALBUM.equals(action)) {
            //playAlbum(id);
        } else if(Constant.QUEUE_ALBUM.equals(action)) {
            //queueAlbum(id);
        } else if(Constant.PLAY_TRACK.equals(action)) {
            //playTrack(id);
        } else if(Constant.QUEUE_TRACK.equals(action)) {
            //queueTrack(id);
        } else if(Constant.PLAY_PAUSE_TRACK.equals(action)) {
            //                playPauseTrack();
            System.out.println("press play");
        } else if(Constant.HIDE_PLAYER.equals(action)) {
            //                hideNotification();
            System.out.println("press next");
        }
        else {
        }*/
    }

}
