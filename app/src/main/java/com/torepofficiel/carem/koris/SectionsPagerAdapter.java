package com.torepofficiel.carem.koris;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a TabFragment0 (defined as a static inner class below).

        if(position == 0) {
            return new TabFragment0();
        }

        else if(position == 1) {
            return new TabFragment1();
        }

        else if(position == 2) {
            return new TabFragment2();
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ErrorActivity.currentActivity.get().getString(R.string.compte_local);
            case 1:
                return ErrorActivity.currentActivity.get().getString(R.string.compte_ligne);
            case 2:
                return ErrorActivity.currentActivity.get().getString(R.string.abonnement);
        }
        return null;
    }
}
