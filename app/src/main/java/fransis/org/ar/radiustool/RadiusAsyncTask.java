package fransis.org.ar.radiustool;

import android.graphics.Color;
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

    public static final String MS = "";
    private RadiusService radiusService;
    private String address;
    private int authPort;
    private String secret;
    private String userName;
    private String userPassword;
    private TextView textResponse = null;
    private TextView textResponseTime = null;
    private ImageView icView = null;
    private ProgressBar pbar = null;
    private long responseTime = 0L;
    private int normalTime = 0;
    private int highTime = 0;

    public RadiusAsyncTask(String address, int authPort, String secret, String userName, String userPassword, TextView textResponse, ImageView icView, ProgressBar pbar, TextView responseTime, int normalTime, int highTime) {
        this.address = address;
        this.authPort = authPort;
        this.secret = secret;
        this.userName = userName;
        this.userPassword = userPassword;
        this.textResponse = textResponse;
        this.icView = icView;
        this.pbar = pbar;
        this.textResponseTime = responseTime;
        this.normalTime = normalTime;
        this.highTime = highTime;

    }

    @Override
    protected String doInBackground(Void... voids) {
        radiusService = new RadiusServiceImpl();
        long start = System.currentTimeMillis();
        String ret = radiusService.auth(address, authPort, secret, userName, userPassword);
        responseTime = System.currentTimeMillis() - start;
        return ret;
    }

    @Override
    protected void onPreExecute() {
        icView.setVisibility(View.GONE);
        pbar.setVisibility(View.VISIBLE);
        textResponse.setText("");
        textResponseTime.setText("");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        textResponse.setTextColor(Color.GREEN);
        textResponseTime.setTextColor(Color.GREEN);
        textResponse.setText(s);
        textResponseTime.setText(responseTime + MS);
        if(responseTime > normalTime){
            textResponse.setTextColor(Color.MAGENTA);
            textResponseTime.setTextColor(Color.MAGENTA);
        }
        if(responseTime > highTime){
            textResponse.setTextColor(Color.RED);
            textResponseTime.setTextColor(Color.RED);
        }
        switch (s){
            case RadiusService.ACCESS_ACCEPT:
                icView.setImageResource(R.mipmap.ic_success);
                break;
            case RadiusService.ACCESS_REJECT:
                icView.setImageResource(R.mipmap.ic_reject);
                textResponse.setTextColor(Color.RED);
                break;
            default:
                icView.setImageResource(R.mipmap.ic_timeout);
        }
        pbar.setVisibility(View.GONE);
        icView.setVisibility(View.VISIBLE);
    }
}
