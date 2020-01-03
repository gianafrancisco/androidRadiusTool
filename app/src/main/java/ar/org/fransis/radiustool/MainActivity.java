package ar.org.fransis.radiustool;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.vorlonsoft.android.rate.AppRate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ar.org.fransis.radiustool.model.TestCase;


public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener,
            AboutMeFragment.OnFragmentInteractionListener,
            SettingsFragment.OnFragmentInteractionListener {

    private InterstitialAd mInterstitialAd;
    private MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        //if(AdSingleton.getInstance().isShowStartUpAd() == true){
        mInterstitialAd.loadAd(new AdRequest.Builder()
                //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                .build());

        mInterstitialAd.setAdListener(new AdListener() {
                /*@Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    mInterstitialAd.show();
                    AdSingleton.getInstance().setShowStartUpAd(false);
                }*/

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder()
                        //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                        .build());
            }
        });
        //}

        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

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

        // Load an ad into the AdMob banner view.


        /*
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        */
        mainFragment = MainFragment.newInstance("","");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mainFragment);
        fragmentTransaction.commit();


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
                mainFragment.add();
                break;
            case R.id.action_edit:
                //edit();
                mainFragment.edit();
                break;
            case R.id.action_delete:
                //remove();
                mainFragment.remove();
                break;
            case R.id.action_about_me:
                //Intent aboutMe = new Intent(this, AboutMeActivity.class);
                //startActivity(aboutMe);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AboutMeFragment.newInstance("","")).addToBackStack(null).commit();
                break;
            case R.id.action_settings:
                //Intent settings = new Intent(this, SettingsActivity.class);
                //startActivity(settings);
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container, SettingsFragment.newInstance("","")).addToBackStack(null).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SettingsFragment.newInstance("","")).addToBackStack(null).commit();
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
    public void onAdd(TestCase testCase) {

    }

    @Override
    public void onRemove(TestCase testCase) {

    }

    @Override
    public void onEdit(TestCase testCase) {

    }

}
