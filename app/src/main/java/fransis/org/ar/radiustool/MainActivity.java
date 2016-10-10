package fransis.org.ar.radiustool;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import fransis.org.ar.radiustool.model.TestCase;
import fransis.org.ar.radiustool.store.TestCaseDbHelper;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editAddress = null;
    private EditText editAuthPort = null;
    private EditText editSecret = null;
    private EditText editUserName = null;
    private EditText editUserPassword = null;
    private Button buttonAuth = null;
    private Button buttonSave = null;
    private Button buttonRemove = null;
    private TextView textResponse = null;
    private TestCaseDbHelper dbHelper = null;
    private Spinner spinnerTestCase = null;
    private ArrayAdapter<TestCase> adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TestCaseDbHelper(getApplicationContext());

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        /*
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        */
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        editAddress = (EditText) findViewById(R.id.text_radius_ip_address);
        editAuthPort = (EditText) findViewById(R.id.text_radius_auth_port);
        editSecret = (EditText) findViewById(R.id.text_radius_secret);
        editUserName = (EditText) findViewById(R.id.text_radius_username);
        editUserPassword = (EditText) findViewById(R.id.text_radius_password);
        textResponse = (TextView) findViewById(R.id.text_radius_response);
        spinnerTestCase = (Spinner)findViewById(R.id.list_test_cases);

        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dbHelper.getAll());

        spinnerTestCase.setAdapter(adapter);
        spinnerTestCase.setOnItemSelectedListener(this);

        loadPreferences();

        buttonAuth = (Button) findViewById(R.id.button_auth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
                new RadiusAsyncTask(
                        editAddress.getText().toString(),
                        Integer.parseInt(editAuthPort.getText().toString()),
                        editSecret.getText().toString(),
                        editUserName.getText().toString(),
                        editUserPassword.getText().toString(),
                        textResponse
                ).execute();
            }
        });

        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestCase tc = dbHelper.insert(
                        editAddress.getText().toString(),
                        editAddress.getText().toString(),
                        Integer.parseInt(editAuthPort.getText().toString()),
                        editSecret.getText().toString(),
                        editUserName.getText().toString(),
                        editUserPassword.getText().toString());
                adapter.add(tc);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Test case saved.", Toast.LENGTH_LONG).show();
            }
        });

        buttonRemove = (Button) findViewById(R.id.button_delete);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
                if(tc != null){
                    dbHelper.remove(tc);
                    adapter.remove(tc);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Test case removed.", Toast.LENGTH_LONG).show();
                }
            }
        });

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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent aboutMe = new Intent(this, AboutMeActivity.class);
            startActivity(aboutMe);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePreferences(){
        SharedPreferences.Editor editor = getSharedPreferences("latest_parameters", MODE_PRIVATE).edit();
        editor.putString("radius_address", editAddress.getText().toString());
        editor.putString("radius_auth_port", editAuthPort.getText().toString());
        editor.putString("radius_secret", editSecret.getText().toString());
        editor.putString("radius_user_name", editUserName.getText().toString());
        editor.putString("radius_user_password", editUserPassword.getText().toString());
        editor.commit();
    }

    private void loadPreferences(){
        SharedPreferences sp = getSharedPreferences("latest_parameters", MODE_PRIVATE);
        editAddress.setText(sp.getString("radius_address",""));
        editAuthPort.setText(sp.getString("radius_auth_port","1812"));
        editSecret.setText(sp.getString("radius_secret",""));
        editUserName.setText(sp.getString("radius_user_name",""));
        editUserPassword.setText(sp.getString("radius_user_password",""));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TestCase selected = (TestCase) adapterView.getItemAtPosition(i);
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
