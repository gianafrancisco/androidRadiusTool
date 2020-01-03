package ar.org.fransis.radiustool;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class SettingsActivity extends AppCompatPreferenceActivity {

    private static InterstitialAd mInterstitialAd;
    private AdView mAdView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_donation));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener()
            {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdClosed(){
                    Toast.makeText(getActivity(), getString(R.string.pref_gracias_platita), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }

            });

            // Publicidad preference click listener
            Preference myPrefPlatitaVieja = findPreference(getString(R.string.key_platita_vieja));
            myPrefPlatitaVieja.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    if(mInterstitialAd.isLoaded()){
                        mInterstitialAd.show();
                    }
                    return true;
                }
            });

            Preference pref_Compartir = (Preference) findPreference("pref_compartir");
            pref_Compartir.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Simple Radius Test Tool");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.share_the_app));
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    return true;
                }
            });

        }
    }
}