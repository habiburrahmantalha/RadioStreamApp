package com.rrmsense.radiostream.interfaces;

/**
 * Created by Talha on 2/15/2017.
 */

public interface NotifyItem {
    void onItemChanged(String id);
    void playNext(String id);
    void playPrevious(String id);
}
