package ar.org.fransis.radiustool;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.List;
import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter
{
        private List<Fragment> mList;

        static final int NUM_ITEMS = 5;
        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mList = new ArrayList<Fragment>();
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mList.get(position);
            if(fragment == null)
            {
                switch(position)
                {
                    case 0:
                        fragment = MainFragment.newInstance("","");
                        break;
                    case 1:
                        fragment = ItemFragment.newInstance(0);
                        break;
                    case 2:
                        fragment = ItemFragment.newInstance(0);
                        break;
                    case 3:
                        fragment = AboutMeFragment.newInstance("","");
                        break;
                    case 4:
                        fragment = SettingsFragment.newInstance();
                        break;
                }
            }
            return null;
        }
}