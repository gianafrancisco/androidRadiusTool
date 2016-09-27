package fransis.org.ar.radiustool;

import android.os.AsyncTask;
import android.widget.TextView;

import fransis.org.ar.radiustool.service.RadiusService;
import fransis.org.ar.radiustool.service.RadiusServiceImpl;

/**
 * Created by francisco on 15/09/2016.
 */
public class RadiusAsyncTask extends AsyncTask<Void, Void, String> {

    private RadiusService radiusService;
    private String address;
    private int authPort;
    private String secret;
    private String userName;
    private String userPassword;
    private TextView textResponse = null;

    public RadiusAsyncTask(String address, int authPort, String secret, String userName, String userPassword, TextView textResponse) {
        this.address = address;
        this.authPort = authPort;
        this.secret = secret;
        this.userName = userName;
        this.userPassword = userPassword;
        this.textResponse = textResponse;
    }

    @Override
    protected String doInBackground(Void... voids) {
        radiusService = new RadiusServiceImpl();
        return radiusService.auth(address, authPort, secret, userName, userPassword);
    }

    @Override
    protected void onPostExecute(String s) {
        textResponse.setText(s);
    }
}
