package ar.org.fransis.radiustool;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ar.org.fransis.radiustool.model.Result;
import ar.org.fransis.radiustool.model.TestCase;
import ar.org.fransis.radiustool.service.RadiusService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment implements FragmentLifecycle {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private TextView mTextName = null;
    private TextView mTextAddress = null;
    private TextView mTestAuthPort = null;
    private TextView mTestSecret = null;
    private TextView mTextUsername = null;
    private TextView mTextPassword = null;
    private TextView mTextResponse = null;
    private TextView mTextResponseTime = null;
    private TextView mTextReplyMessage = null;
    private ImageView mIconView = null;
    private int normalTimeResponse = 0;
    private int highTimeResponse = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Result mResult;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        mTextName = view.findViewById(R.id.text_server_name);
        mTextAddress = view.findViewById(R.id.text_radius_ip_address);
        mTestAuthPort = view.findViewById(R.id.text_radius_auth_port);
        mTestSecret = view.findViewById(R.id.text_radius_secret);
        mTextUsername = view.findViewById(R.id.text_radius_username);
        mTextPassword = view.findViewById(R.id.text_radius_password);
        mTextResponse = view.findViewById(R.id.text_radius_response);
        mTextResponseTime = view.findViewById(R.id.text_time_response);
        mTextReplyMessage = view.findViewById(R.id.text_radius_reply_message);
        mIconView = view.findViewById(R.id.image_response);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        normalTimeResponse = Integer.parseInt(sp.getString("pref_normal_time_response","100"));
        highTimeResponse = Integer.parseInt(sp.getString("pref_high_time_response","300"));

        loadResult();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        Result getResultSelected();
    }

    private void loadResult(){
        Result result = mListener.getResultSelected();
        if(result != null){
            TestCase tc = result.getTestCase();
            long responseTime = result.getResponseTime();
            String message = result.getResponseType();

            mTextName.setText(tc.getName());
            mTextAddress.setText(tc.getAddress());
            mTestAuthPort.setText(Integer.toString(tc.getAuthPort()));
            mTestSecret.setText(tc.getSecret());
            mTextUsername.setText(tc.getUserName());
            mTextPassword.setText(tc.getUserPassword());

            mTextResponse.setText(message);
            mTextResponseTime.setText(responseTime + "ms");
            mTextReplyMessage.setText(result.getReplyMessage());

            mTextResponse.setTextColor(getContext().getResources().getColor(R.color.level_0_time_response));
            mTextResponseTime.setTextColor(getContext().getResources().getColor(R.color.level_0_time_response));

            if(responseTime > normalTimeResponse){
                mTextResponse.setTextColor(getContext().getResources().getColor(R.color.level_1_time_response));
                mTextResponseTime.setTextColor(getContext().getResources().getColor(R.color.level_1_time_response));
            }
            if(responseTime > highTimeResponse){
                mTextResponse.setTextColor(getContext().getResources().getColor(R.color.level_2_time_response));
                mTextResponseTime.setTextColor(getContext().getResources().getColor(R.color.level_2_time_response));
            }
            switch (message){
                case RadiusService.ACCESS_ACCEPT:
                    mIconView.setImageResource(R.drawable.ic_success);
                    break;
                case RadiusService.ACCESS_REJECT:
                    mIconView.setImageResource(R.drawable.ic_reject);
                    mTextResponse.setTextColor(getContext().getResources().getColor(R.color.level_2_time_response));
                    break;
                default:
                    mIconView.setImageResource(R.drawable.ic_timeout);
            }

        }
    }
}
