package fransis.org.ar.radiustool;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";


    private EditText editAddress = null;
    private EditText editAuthPort = null;
    private EditText editSecret = null;
    private EditText editUserName = null;
    private EditText editUserPassword = null;
    private Button buttonAuth = null;
    private TextView textResponse = null;
    private RadiusService radiusService = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        editAddress = (EditText) findViewById(R.id.text_radius_ip_address);
        editAuthPort = (EditText) findViewById(R.id.text_radius_auth_port);
        editSecret = (EditText) findViewById(R.id.text_radius_secret);
        editUserName = (EditText) findViewById(R.id.text_radius_username);
        editUserPassword = (EditText) findViewById(R.id.text_radius_password);
        textResponse = (TextView) findViewById(R.id.text_radius_response);

        loadPreferences();

        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
        Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();

        radiusService = new RadiusServiceMock();

        buttonAuth = (Button) findViewById(R.id.button_auth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
                String response = radiusService.auth(
                        editAddress.getText().toString(),
                        Integer.parseInt(editAuthPort.getText().toString()),
                        editSecret.getText().toString(),
                        editUserName.getText().toString(),
                        editUserPassword.getText().toString()
                        );
                textResponse.setText(response);
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

}
