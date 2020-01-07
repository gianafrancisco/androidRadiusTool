package ar.org.fransis.radiustool;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter
{
        private HashMap<Integer,Fragment> mMap;

        static final int NUM_ITEMS = 5;
        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mMap = new HashMap<>();
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mMap.get(position);
            if(fragment == null)
            {
                if(position == Tab.RADIUS.value)
                {
                    fragment = MainFragment.newInstance("","");
                }
                else if(position == Tab.DETAILS.value) {
                    fragment = DetailsFragment.newInstance("","");
                }
                else if(position == Tab.RESULTS.value) {
                    fragment = ItemFragment.newInstance(0);
                }
                else if(position == Tab.ABOUT.value) {
                    fragment = AboutMeFragment.newInstance("","");
                }
                else if(position == Tab.PREFERENCE.value) {
                    fragment = SettingsFragment.newInstance();
                }
                mMap.put(position, fragment);
            }
            return fragment;
        }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == Tab.RADIUS.value)
        {
            return Tab.RADIUS.title;
        }
        else if(position == Tab.DETAILS.value) {
            return Tab.DETAILS.title;
        }
        else if(position == Tab.RESULTS.value) {
            return Tab.RESULTS.title;
        }
        else if(position == Tab.ABOUT.value) {
            return Tab.ABOUT.title;
        }
        else if(position == Tab.PREFERENCE.value) {
            return Tab.PREFERENCE.title;
        }
        return super.getPageTitle(position);
    }
}