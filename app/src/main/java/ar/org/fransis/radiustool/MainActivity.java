package ar.org.fransis.radiustool;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import com.vorlonsoft.android.rate.AppRate;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.org.fransis.radiustool.model.Result;
import ar.org.fransis.radiustool.model.TestCase;
import ar.org.fransis.radiustool.store.TestCaseDB;


public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener,
            AboutMeFragment.OnFragmentInteractionListener,
            SettingsFragment.OnFragmentInteractionListener,
            ItemFragment.OnListFragmentInteractionListener,
            DetailsFragment.OnFragmentInteractionListener {

    public static final String LOG_ADS_TAG = "Ads";
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAdPreference;
    private MainFragment mMainFragment;
    private SettingsFragment mSettingsFragment;
    private AboutMeFragment mAboutMeFragment;
    private ItemFragment mItemFragment;
    private TestCaseDB mDatabase = null;
    private ar.org.fransis.radiustool.dao.TestCase mTestCaseDAO;
    private TabLayout mTab;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private List<Result> mResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        mInterstitialAdPreference = new InterstitialAd(this);
        mInterstitialAdPreference.setAdUnitId(getResources().getString(R.string.interstitial_ad_donation));

        //if(AdSingleton.getInstance().isShowStartUpAd() == true){
        mInterstitialAd.loadAd(new AdRequest.Builder()
                //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                .build());

        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
                Log.d("Ads", "mInterstitialAd onAdFailedToLoad " + i);
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //mInterstitialAd.show();
                //AdSingleton.getInstance().setShowStartUpAd(false);
                Log.d("Ads", "mInterstitialAd Loaded");
            }

            @Override
            public void onAdClosed() {
                Log.d("Ads", "mInterstitialAd AdClosed");
                mInterstitialAd.loadAd(new AdRequest.Builder()
                        //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                        .build());
            }
        });
        //}

        mInterstitialAdPreference.loadAd(new AdRequest.Builder().build());
        final Activity activity = this;
        mInterstitialAdPreference.setAdListener(new AdListener()
        {

            @Override
            public void onAdFailedToLoad(int i) {
                Log.d("Ads", "mInterstitialAdPreference onAdFailedToLoad " + i);
            }

            @Override
            public void onAdLoaded() {
                Log.d("Ads", "mInterstitialAdPreference Loaded");
                //super.onAdLoaded();
            }

            @Override
            public void onAdClosed(){
                mInterstitialAdPreference.loadAd(new AdRequest.Builder().build());
                Log.d(LOG_ADS_TAG, "mInterstitialAdPreference AdClosed");
                Toast.makeText(activity, getString(R.string.pref_gracias_platita), Toast.LENGTH_SHORT).show();
                activity.onBackPressed();
            }

        });

        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        mTab = (TabLayout) findViewById(R.id.tab_layout);
        /*
        mTab.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                        openRadius();
                        break;
                    case 1:
                        openList();
                        break;
                    case 2:
                        break;
                    case 3:
                        openAboutMe();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        */
        // Load an ad into the AdMob banner view.

        mDatabase = Room.databaseBuilder(this.getApplicationContext(),
                TestCaseDB.class, "Testcase.db").allowMainThreadQueries().build();
        mTestCaseDAO = mDatabase.testCaseDao();

        /*
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        */
        mPagerAdapter = new PagerAdapter( getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        mResults = new ArrayList<>();

        mMainFragment = (MainFragment) mPagerAdapter.getItem(Tab.RADIUS.value);
/*
        mMainFragment = MainFragment.newInstance("","");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMainFragment);
        fragmentTransaction.commit();
*/
        AppRate.with(this)
                .setInstallDays((byte) 0)                  // default is 10, 0 means install day, 10 means app is launched 10 or more days later than installation
                .setLaunchTimes((byte) 5)                  // default is 10, 3 means app is launched 3 or more times
                .setRemindInterval((byte) 3)               // default is 1, 1 means app is launched 1 or more days after neutral button clicked
                .setSelectedAppLaunches((byte) 1)         // default is 0, 1 means app is launched 1 or more times after neutral button clicked
                .setTitle(R.string.new_rate_dialog_title)
                .setTextLater(R.string.new_rate_dialog_later)
                /* comment to use library strings instead app strings - end */
                /* uncomment to use app string instead library string */
                .setMessage(R.string.new_rate_dialog_message)
                /* comment to use library strings instead app strings - start */
                .setTextNever(R.string.new_rate_dialog_never)
                .setTextRateNow(R.string.new_rate_dialog_ok)
                .monitor();                                // Monitors the app launch times
        AppRate.showRateDialogIfMeetsConditions(this); // Shows the Rate Dialog when conditions are met

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_add:
                //add();
                mMainFragment.add();
                break;
            case R.id.action_edit:
                //edit();
                mMainFragment.edit();
                break;
            case R.id.action_delete:
                //remove();
                mMainFragment.remove();
                break;
            case R.id.action_about_me:
                openAboutMe();
                break;
            case R.id.action_settings:
                openPreferences();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferences() {
        mViewPager.setCurrentItem(Tab.PREFERENCE.value);
        /*
        if(mSettingsFragment == null)
        {
            mSettingsFragment = SettingsFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mSettingsFragment)
                .addToBackStack(null).commit();
        */
    }

    private void openAboutMe() {
        mViewPager.setCurrentItem(Tab.ABOUT.value);
        /*
        if(mAboutMeFragment == null)
        {
            mAboutMeFragment = AboutMeFragment.newInstance("","");
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mAboutMeFragment)
                .addToBackStack(null).commit();
        */
    }

    private void openRadius() {
        mViewPager.setCurrentItem(Tab.RADIUS.value);
        /*
        if(mMainFragment == null)
        {
            mMainFragment = MainFragment.newInstance("","");
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mMainFragment)
                .addToBackStack(null).commit();
        */
    }

    private void openList() {
        mViewPager.setCurrentItem(Tab.RESULTS.value);
        /*
        if(mItemFragment == null)
        {
            mItemFragment = ItemFragment.newInstance(0);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mItemFragment)
                .addToBackStack(null).commit();
        */
    }

    @Override
    protected void onResume() {
        //loadPreferences();
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onShowAdPreference() {
        if(mInterstitialAdPreference.isLoaded()){
            mInterstitialAdPreference.show();
        }
    }

    @Override
    public long onAdd(TestCase testCase) {
        if(testCase != null)
        {
            return mTestCaseDAO.insert(testCase);
        }
        return -1;
    }

    @Override
    public void onRemove(TestCase testCase) {
        if(testCase != null)
        {
            mTestCaseDAO.delete(testCase);
        }
    }

    @Override
    public void onEdit(TestCase testCase) {
        if(testCase != null)
        {
            mTestCaseDAO.update(testCase);
        }
    }

    @Override
    public ar.org.fransis.radiustool.dao.TestCase getTestCaseDAO() {
        return mTestCaseDAO;
    }

    @Override
    public void onShowAdMain() {
        /*
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        */
    }

    @Override
    public void onTestCompleted(Result result) {
        mResults.add(result);
    }

    @Override
    public void onListFragmentInteraction(Result item) {

    }

    @Override
    public List<Result> getResults() {
        return mResults;
    }
}