package com.rrmsense.radiostream.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Talha on 3/20/2017.
 */

public class MusicIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    context.sendBroadcast(new Intent("HEADPHONE_UNPLUGGED"));

                    break;
                case 1:
                    context.sendBroadcast(new Intent("HEADPHONE_PLUGGED"));
                    break;
                default:

            }
        }
    }
}
