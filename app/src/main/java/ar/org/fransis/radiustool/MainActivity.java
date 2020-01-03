package ar.org.fransis.radiustool;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.vorlonsoft.android.rate.AppRate;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ar.org.fransis.radiustool.model.TestCase;
import ar.org.fransis.radiustool.store.TestCaseDB;


public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener,
            AboutMeFragment.OnFragmentInteractionListener,
            SettingsFragment.OnFragmentInteractionListener {

    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAdSettings;
    private MainFragment mMainFragment;
    private SettingsFragment mSettingsFragment;
    private AboutMeFragment mAboutMeFragment;
    private TestCaseDB mDatabase = null;
    private ar.org.fransis.radiustool.dao.TestCase mTestCaseDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        mInterstitialAdSettings = new InterstitialAd(this);
        mInterstitialAdSettings.setAdUnitId(getResources().getString(R.string.interstitial_ad_donation));

        //if(AdSingleton.getInstance().isShowStartUpAd() == true){
        mInterstitialAd.loadAd(new AdRequest.Builder()
                //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                .build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //mInterstitialAd.show();
                //AdSingleton.getInstance().setShowStartUpAd(false);
                Log.d("Ad", "mInterstitialAd Loaded");
            }

            @Override
            public void onAdClosed() {
                Log.d("Ad", "mInterstitialAd AdClosed");
                mInterstitialAd.loadAd(new AdRequest.Builder()
                        //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                        .build());
            }
        });
        //}

        mInterstitialAdSettings.loadAd(new AdRequest.Builder().build());
        final Activity activity = this;
        mInterstitialAdSettings.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                Log.d("Ad", "mInterstitialAdSettings Loaded");
                //super.onAdLoaded();
            }

            @Override
            public void onAdClosed(){
                mInterstitialAdSettings.loadAd(new AdRequest.Builder().build());
                Log.d("Ad", "mInterstitialAdSettings AdClosed");
                Toast.makeText(activity, getString(R.string.pref_gracias_platita), Toast.LENGTH_SHORT).show();
                activity.onBackPressed();
            }

        });

        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        // Load an ad into the AdMob banner view.

        mDatabase = Room.databaseBuilder(this.getApplicationContext(),
                TestCaseDB.class, "Testcase.db").allowMainThreadQueries().build();
        mTestCaseDAO = mDatabase.testCaseDao();

        /*
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        */
        mMainFragment = MainFragment.newInstance("","");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMainFragment);
        fragmentTransaction.commit();

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
                //Intent aboutMe = new Intent(this, AboutMeActivity.class);
                //startActivity(aboutMe);
                if(mAboutMeFragment == null)
                {
                    mAboutMeFragment = AboutMeFragment.newInstance("","");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mAboutMeFragment)
                        .addToBackStack(null).commit();
                break;
            case R.id.action_settings:
                //Intent settings = new Intent(this, SettingsActivity.class);
                //startActivity(settings);
                if(mSettingsFragment == null)
                {
                    mSettingsFragment = SettingsFragment.newInstance();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mSettingsFragment)
                        .addToBackStack(null).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if(mInterstitialAdSettings.isLoaded()){
            mInterstitialAdSettings.show();
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

}