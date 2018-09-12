package fransis.org.ar.radiustool;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import fransis.org.ar.radiustool.model.TestCase;
import fransis.org.ar.radiustool.store.TestCaseDB;

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
    private fransis.org.ar.radiustool.dao.TestCase dao;
    private Spinner spinnerTestCase = null;
    private ArrayAdapter<TestCase> adapter = null;
    private ImageView icView = null;
    private ProgressBar pbar = null;
    private int normalTimeResponse = 0;
    private int highTimeResponse = 0;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        if(AdSingleton.getInstance().isShowStartUpAd() == true){
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    mInterstitialAd.show();
                    AdSingleton.getInstance().setShowStartUpAd(false);
                }
            });
        }

        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id));

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



        /*
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        */


        TestCaseDB db = Room.databaseBuilder(getApplicationContext(),
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
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            mInterstitialAd.show();
                        }
                    });
                }

                savePreferences();

                new RadiusAsyncTask(
                        editAddress.getText().toString(),
                        Integer.parseInt(editAuthPort.getText().toString()),
                        editSecret.getText().toString(),
                        editUserName.getText().toString(),
                        editUserPassword.getText().toString(),
                        textResponse,
                        icView,
                        pbar,
                        textResponseTime, normalTimeResponse, highTimeResponse).execute();

            }
        });
    }

    private void remove() {
        TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
        if(tc != null){
            dao.delete(tc);
            adapter.remove(tc);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Test case removed.", Toast.LENGTH_LONG).show();
        }
    }

    private void edit() {
        TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
        tc.setName(editName.getText().toString());
        tc.setAddress(editAddress.getText().toString());
        tc.setAuthPort(Integer.parseInt(editAuthPort.getText().toString()));
        tc.setSecret(editSecret.getText().toString());
        tc.setUserName(editUserName.getText().toString());
        tc.setUserPassword(editUserPassword.getText().toString());

        dao.update(tc);
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Test case saved.", Toast.LENGTH_LONG).show();
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

    private void savePreferences(){
        SharedPreferences.Editor editor = getSharedPreferences("latest_parameters", MODE_PRIVATE).edit();

        editor.putString("radius_name", editName.getText().toString());
        editor.putString("radius_address", editAddress.getText().toString());
        editor.putString("radius_auth_port", editAuthPort.getText().toString());
        editor.putString("radius_secret", editSecret.getText().toString());
        editor.putString("radius_user_name", editUserName.getText().toString());
        editor.putString("radius_user_password", editUserPassword.getText().toString());
        editor.commit();
    }

    private void loadPreferences(){
        SharedPreferences sp = getSharedPreferences("latest_parameters", MODE_PRIVATE);
        editName.setText(sp.getString("radius_name",""));
        editAddress.setText(sp.getString("radius_address",""));
        editAuthPort.setText(sp.getString("radius_auth_port","1812"));
        editSecret.setText(sp.getString("radius_secret",""));
        editUserName.setText(sp.getString("radius_user_name",""));
        editUserPassword.setText(sp.getString("radius_user_password",""));
        normalTimeResponse = sp.getInt("pref_normal_time_response",100);
        highTimeResponse = sp.getInt("pref_high_time_response",300);
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
}
