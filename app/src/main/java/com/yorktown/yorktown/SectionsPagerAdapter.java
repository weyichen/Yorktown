package com.yorktown.yorktown;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by Daniel on 1/28/2015.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // TODO: return the appropriate fragments
        if (position == 0) {
            Fragment cardsFragment = CardsFragment.newInstance();
            return cardsFragment;
        }

        if (position == 1) {
            Fragment cardsFragment = CardsFragment.newInstance();
            return cardsFragment;
        }

        return null;

    }

    @Override
    public int getCount() {
        // TODO: Show the correct number of fragments
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "All Trips".toUpperCase(l);
            case 1:
                return "All Trips".toUpperCase(l);
        }
        return null;
    }
}
