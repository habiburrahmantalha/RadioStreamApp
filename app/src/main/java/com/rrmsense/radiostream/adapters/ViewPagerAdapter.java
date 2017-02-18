package com.rrmsense.radiostream.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.rrmsense.radiostream.fragments.RadioFragment;
import com.rrmsense.radiostream.models.SelectFragment;

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
                Fragment fragmentBangla = new RadioFragment();
                Bundle bundleBangla = new Bundle();
                bundleBangla.putInt("ID", SelectFragment.FRAGMENT_BANGLA_RADIO);
                fragmentBangla.setArguments(bundleBangla);
                Log.d("F1", String.valueOf(SelectFragment.FRAGMENT_BANGLA_RADIO));
                return fragmentBangla;

            case 1:
                Fragment fragmentRecent = new RadioFragment();
                Bundle bundleRecent = new Bundle();
                bundleRecent.putInt("ID", SelectFragment.FRAGMENT_RECENT);
                fragmentRecent.setArguments(bundleRecent);
                Log.d("F2", String.valueOf(SelectFragment.FRAGMENT_RECENT));
                return fragmentRecent;
            case 2:
                Fragment fragmentFavourite = new RadioFragment();
                Bundle bundleFavourite = new Bundle();
                bundleFavourite.putInt("ID", SelectFragment.FRAGMENT_FAVOURITE);
                fragmentFavourite.setArguments(bundleFavourite);
                Log.d("F3", String.valueOf(SelectFragment.FRAGMENT_FAVOURITE));
                return fragmentFavourite;

            default:
                /*
                Fragment fragmentDef = new RadioFragment();
                Bundle bundleDef = new Bundle();
                bundleDef.putInt("ID", SelectFragment.FRAGMENT_BANGLA_RADIO);
                fragmentDef.setArguments(bundleDef);
                return fragmentDef;
                */
                Log.d("FD", "Default");
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