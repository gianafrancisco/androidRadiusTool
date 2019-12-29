package ar.org.fransis.radiustool;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import ar.org.fransis.radiustool.service.RadiusService;
import ar.org.fransis.radiustool.service.RadiusServiceImpl;

import static ar.org.fransis.radiustool.service.RadiusService.RADIUS_EXCEPTION;
import static ar.org.fransis.radiustool.service.RadiusService.REPLY_MESSAGE;
import static ar.org.fransis.radiustool.service.RadiusService.RESPONSE_TYPE;

/**
 * Created by francisco on 15/09/2016.
 */
public class RadiusAsyncTask extends AsyncTask<Void, Void, HashMap<Integer, String>> {

    public static final String MS = "";
    private RadiusService radiusService;
    private String address;
    private int authPort;
    private String secret;
    private String userName;
    private String userPassword;
    private TextView textResponse = null;
    private TextView textResponseTime = null;
    private TextView textReplyMessage = null;
    private ImageView icView = null;
    private ProgressBar pbar = null;
    private long responseTime = 0L;
    private int normalTime = 0;
    private int highTime = 0;
    private Context context = null;

    public RadiusAsyncTask(Context context, int authPort, String secret, String userName, String userPassword, TextView textResponse, ImageView icView, ProgressBar pbar, TextView responseTime, int normalTime, int highTime, String address, TextView textReplyMessage) {
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
        this.context = context;
        this.textReplyMessage = textReplyMessage;

    }

    @Override
    protected HashMap<Integer, String> doInBackground(Void... voids) {
        radiusService = new RadiusServiceImpl();
        long start = System.currentTimeMillis();
        HashMap<Integer, String> ret = radiusService.auth(address, authPort, secret, userName, userPassword);
        responseTime = System.currentTimeMillis() - start;
        return ret;
    }

    @Override
    protected void onPreExecute() {
        icView.setVisibility(View.GONE);
        pbar.setVisibility(View.VISIBLE);
        textResponse.setText("");
        textResponseTime.setText("");
        textReplyMessage.setText("");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(HashMap<Integer, String> ret) {
        StringBuilder message = new StringBuilder();
        StringBuilder reply_message = new StringBuilder();
        if(ret.containsKey(RADIUS_EXCEPTION))
        {
            message.append(ret.get(RADIUS_EXCEPTION));
        }else if(ret.containsKey(RESPONSE_TYPE))
        {
            message.append(ret.get(RESPONSE_TYPE));
        }
        textResponse.setTextColor(context.getResources().getColor(R.color.level_0_time_response));
        textResponseTime.setTextColor(context.getResources().getColor(R.color.level_0_time_response));
        textResponse.setText(message);
        textResponseTime.setText(responseTime + MS);
        if(responseTime > normalTime){
            textResponse.setTextColor(context.getResources().getColor(R.color.level_1_time_response));
            textResponseTime.setTextColor(context.getResources().getColor(R.color.level_1_time_response));
        }
        if(responseTime > highTime){
            textResponse.setTextColor(context.getResources().getColor(R.color.level_2_time_response));
            textResponseTime.setTextColor(context.getResources().getColor(R.color.level_2_time_response));
        }
        switch (message.toString()){
            case RadiusService.ACCESS_ACCEPT:
                icView.setImageResource(R.drawable.ic_success);
                break;
            case RadiusService.ACCESS_REJECT:
                icView.setImageResource(R.drawable.ic_reject);
                textResponse.setTextColor(context.getResources().getColor(R.color.level_2_time_response));
                break;
            default:
                icView.setImageResource(R.drawable.ic_timeout);
        }
        pbar.setVisibility(View.GONE);
        icView.setVisibility(View.VISIBLE);
        textReplyMessage.setText(ret.get(REPLY_MESSAGE));
    }
}
