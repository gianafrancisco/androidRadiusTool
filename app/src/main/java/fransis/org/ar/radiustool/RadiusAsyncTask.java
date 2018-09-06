package fransis.org.ar.radiustool;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private ImageView icView = null;
    private ProgressBar pbar = null;

    public RadiusAsyncTask(String address, int authPort, String secret, String userName, String userPassword, TextView textResponse, ImageView icView, ProgressBar pbar) {
        this.address = address;
        this.authPort = authPort;
        this.secret = secret;
        this.userName = userName;
        this.userPassword = userPassword;
        this.textResponse = textResponse;
        this.icView = icView;
        this.pbar = pbar;
    }

    @Override
    protected String doInBackground(Void... voids) {
        radiusService = new RadiusServiceImpl();
        return radiusService.auth(address, authPort, secret, userName, userPassword);
    }

    @Override
    protected void onPreExecute() {
        icView.setVisibility(View.GONE);
        pbar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        textResponse.setText(s);
        switch (s){
            case RadiusService.ACCESS_ACCEPT:
                icView.setImageResource(R.mipmap.ic_success);
                break;
            case RadiusService.ACCESS_REJECT:
                icView.setImageResource(R.mipmap.ic_reject);
                break;
            default:
                icView.setImageResource(R.mipmap.ic_timeout);
        }
        pbar.setVisibility(View.GONE);
        icView.setVisibility(View.VISIBLE);
    }
}
