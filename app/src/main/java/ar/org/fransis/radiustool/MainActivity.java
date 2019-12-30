package ar.org.fransis.radiustool;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.vorlonsoft.android.rate.AppRate;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ar.org.fransis.radiustool.model.TestCase;
import ar.org.fransis.radiustool.store.TestCaseDB;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editName = null;
    private EditText editAddress = null;
    private EditText editAuthPort = null;
    private EditText editSecret = null;
    private EditText editUserName = null;
    private EditText editUserPassword = null;
    private Button buttonAuth = null;
    private TextView textResponse = null;
    private TextView textResponseTime = null;
    private TextView textReplyMessage = null;
    private ar.org.fransis.radiustool.dao.TestCase dao;
    private Spinner spinnerTestCase = null;
    private ArrayAdapter<TestCase> adapter = null;
    private ImageView icView = null;
    private ProgressBar pbar = null;
    private int normalTimeResponse = 0;
    private int highTimeResponse = 0;
    private TestCaseDB db = null;

    private InterstitialAd mInterstitialAd;


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

        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);


        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                .build();
        adView.loadAd(adRequest);



        /*
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        */


        db = Room.databaseBuilder(getApplicationContext(),
                TestCaseDB.class, "Testcase.db").allowMainThreadQueries().build();

        dao = db.testCaseDao();

        editName = (EditText) findViewById(R.id.text_server_name);
        editAddress = (EditText) findViewById(R.id.text_radius_ip_address);
        editAuthPort = (EditText) findViewById(R.id.text_radius_auth_port);
        editSecret = (EditText) findViewById(R.id.text_radius_secret);
        editUserName = (EditText) findViewById(R.id.text_radius_username);
        editUserPassword = (EditText) findViewById(R.id.text_radius_password);
        textResponse = (TextView) findViewById(R.id.text_radius_response);
        textResponseTime = (TextView) findViewById(R.id.text_time_response);
        textReplyMessage = (TextView) findViewById(R.id.text_radius_reply_message);
        spinnerTestCase = (Spinner)findViewById(R.id.list_test_cases);
        icView = (ImageView) findViewById(R.id.image_response);
        pbar = (ProgressBar) findViewById(R.id.progress_auth);

        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dao.getAll());

        spinnerTestCase.setAdapter(adapter);
        spinnerTestCase.setOnItemSelectedListener(this);

        loadPreferences();

        buttonAuth = (Button) findViewById(R.id.button_auth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AdSingleton.getInstance().showAd()){

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }

                    /*mInterstitialAd.loadAd(new AdRequest.Builder()
                            //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                            .build());

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            mInterstitialAd.show();
                        }
                    });*/
                }

                new RadiusAsyncTask(
                        getApplicationContext(),
                        Integer.parseInt(editAuthPort.getText().toString()),
                        editSecret.getText().toString(),
                        editUserName.getText().toString(),
                        editUserPassword.getText().toString(),
                        textResponse,
                        icView,
                        pbar,
                        textResponseTime,
                        normalTimeResponse,
                        highTimeResponse,
                        editAddress.getText().toString(),
                        textReplyMessage).execute();

            }
        });
    }

    private void remove() {

        TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
        if(tc != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.remove_test_case);
            builder.setMessage(R.string.message_remove_test);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
                    if (tc != null) {
                        dao.delete(tc);
                        adapter.remove(tc);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Test case removed.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(getApplicationContext(), "There is not test case selected.", Toast.LENGTH_LONG).show();
        }
    }

    private void edit() {
        TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
        if(tc !=null){
            tc.setName(editName.getText().toString());
            tc.setAddress(editAddress.getText().toString());
            tc.setAuthPort(Integer.parseInt(editAuthPort.getText().toString()));
            tc.setSecret(editSecret.getText().toString());
            tc.setUserName(editUserName.getText().toString());
            tc.setUserPassword(editUserPassword.getText().toString());

            dao.update(tc);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Test case saved.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "There is not test case selected.", Toast.LENGTH_LONG).show();
        }
    }

    private void add() {
        if(!"".equals(editName.getText().toString())){
            TestCase tc = new TestCase(
                    editName.getText().toString(),
                    editAddress.getText().toString(),
                    Integer.parseInt(editAuthPort.getText().toString()),
                    editSecret.getText().toString(),
                    editUserName.getText().toString(),
                    editUserPassword.getText().toString()
            );

            tc.setId(dao.insert(tc));
            adapter.add(tc);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Test case saved.", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(), "Please, complete the test name first.", Toast.LENGTH_LONG).show();
        }
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
                add();
                break;
            case R.id.action_edit:
                edit();
                break;
            case R.id.action_delete:
                remove();
                break;
            case R.id.action_about_me:
                Intent aboutMe = new Intent(this, AboutMeActivity.class);
                startActivity(aboutMe);
                break;
            case R.id.action_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadPreferences(){
        TestCase tc = null;
        if(spinnerTestCase != null){
            tc = (TestCase) spinnerTestCase.getSelectedItem();
            if(tc == null){
                tc = new TestCase();
            }
            editName.setText(tc.getName());
            editAddress.setText(tc.getAddress());
            editAuthPort.setText(Integer.toString(tc.getAuthPort()));
            editSecret.setText(tc.getSecret());
            editUserName.setText(tc.getUserName());
            editUserPassword.setText(tc.getUserPassword());
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        normalTimeResponse = Integer.parseInt(sp.getString("pref_normal_time_response","100"));
        highTimeResponse = Integer.parseInt(sp.getString("pref_high_time_response","300"));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TestCase selected = (TestCase) adapterView.getItemAtPosition(i);
        editName.setText(selected.getName());
        editAddress.setText(selected.getAddress());
        editAuthPort.setText(Integer.toString(selected.getAuthPort()));
        editSecret.setText(selected.getSecret());
        editUserName.setText(selected.getUserName());
        editUserPassword.setText(selected.getUserPassword());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        loadPreferences();
        super.onResume();
    }
}
