package com.rrmsense.radiostream.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rrmsense.radiostream.fragments.BanglaRadioFragment;
import com.rrmsense.radiostream.fragments.FavouriteFragment;
import com.rrmsense.radiostream.fragments.RecentActivityFragment;
import com.rrmsense.radiostream.fragments.ViewPagerFragment;

/**
 * Created by Talha on 2/16/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOffTabs;

    public ViewPagerAdapter(FragmentManager fm,int numberOffTabs) {
        super(fm);
        this.numberOffTabs = numberOffTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BanglaRadioFragment();
            case 1:
                return new RecentActivityFragment();
            case 2:
                return new FavouriteFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOffTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
