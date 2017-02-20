package com.rrmsense.radiostream.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.rrmsense.radiostream.fragments.FavouriteFragment;
import com.rrmsense.radiostream.fragments.RadioFragment;
import com.rrmsense.radiostream.fragments.RecentFragment;
import com.rrmsense.radiostream.models.SelectFragment;

/**
 * Created by Talha on 2/16/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

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
                //return RadioFragment.newInstance(SelectFragment.FRAGMENT_BANGLA_RADIO);

            case 1:
                Fragment fragmentRecent = new RecentFragment();
                Bundle bundleRecent = new Bundle();
                bundleRecent.putInt("ID", SelectFragment.FRAGMENT_RECENT);
                fragmentRecent.setArguments(bundleRecent);
                Log.d("F2", String.valueOf(SelectFragment.FRAGMENT_RECENT));
                return fragmentRecent;
                //return RadioFragment.newInstance(SelectFragment.FRAGMENT_RECENT);
            case 2:
                Fragment fragmentFavourite = new FavouriteFragment();
                Bundle bundleFavourite = new Bundle();
                bundleFavourite.putInt("ID", SelectFragment.FRAGMENT_FAVOURITE);
                fragmentFavourite.setArguments(bundleFavourite);
                Log.d("F3", String.valueOf(SelectFragment.FRAGMENT_FAVOURITE));
                return fragmentFavourite;
                //return RadioFragment.newInstance(SelectFragment.FRAGMENT_FAVOURITE);

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
