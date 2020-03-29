package ar.org.fransis.radiustool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import ar.org.fransis.radiustool.model.Result;
import ar.org.fransis.radiustool.model.TestCase;

public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;

    private EditText mEditName = null;
    private EditText mEditAddress = null;
    private EditText mEditAuthPort = null;
    private EditText mEditSecret = null;
    private EditText mEditUserName = null;
    private EditText mEditUserPassword = null;
    private Button mButtonAuth = null;
    private TextView mTextResponse = null;
    private TextView mTextResponseTime = null;
    private TextView mTextReplyMessage = null;
    private Spinner mSpinnerTestCase = null;
    private ArrayAdapter<TestCase> mAdapter = null;
    private ImageView mIcView = null;
    private ProgressBar mPbar = null;
    private AdView mAdView = null;
    private int mNormalTimeResponse = 0;
    private int mHighTimeResponse = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        mEditName = (EditText) view.findViewById(R.id.text_server_name);
        mEditAddress = (EditText) view.findViewById(R.id.text_radius_ip_address);
        mEditAuthPort = (EditText) view.findViewById(R.id.text_radius_auth_port);
        mEditSecret = (EditText) view.findViewById(R.id.text_radius_secret);
        mEditUserName = (EditText) view.findViewById(R.id.text_radius_username);
        mEditUserPassword = (EditText) view.findViewById(R.id.text_radius_password);
        mTextResponse = (TextView) view.findViewById(R.id.text_radius_response);
        mTextResponseTime = (TextView) view.findViewById(R.id.text_time_response);
        mTextReplyMessage = (TextView) view.findViewById(R.id.text_radius_reply_message);
        mSpinnerTestCase = (Spinner)view.findViewById(R.id.list_test_cases);
        mIcView = (ImageView) view.findViewById(R.id.image_response);
        mPbar = (ProgressBar) view.findViewById(R.id.progress_auth);


        mAdapter = new ArrayAdapter<TestCase>(getActivity(), R.layout.support_simple_spinner_dropdown_item, mListener.getTestCaseDAO().getAll());

        mSpinnerTestCase.setAdapter(mAdapter);
        mSpinnerTestCase.setOnItemSelectedListener(this);

        mAdView = (AdView) view.findViewById(R.id.adView);
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                Log.d("Ads", "mAdView onAdFailedToLoad " + i);
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                Log.d("Ads", "mAdView Loaded");
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                Log.d("Ads", "mAdView AdClicked");
                super.onAdClicked();
            }
        });
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                .build();
        mAdView.loadAd(adRequest);

        loadPreferences();

        mButtonAuth = (Button) view.findViewById(R.id.button_auth);
        mButtonAuth.setOnClickListener(new View.OnClickListener() {
            private TestCase mTestCase;
            @Override
            public void onClick(View view) {
                if(AdSingleton.getInstance().showAd()){
                    mListener.onShowAdMain();
                }

                this.mTestCase = new TestCase();
                this.mTestCase.setAddress(mEditAddress.getText().toString());
                this.mTestCase.setAuthPort(Integer.parseInt(mEditAuthPort.getText().toString()));
                this.mTestCase.setSecret(mEditSecret.getText().toString());
                this.mTestCase.setUserName(mEditUserName.getText().toString());
                this.mTestCase.setUserPassword(mEditUserPassword.getText().toString());
                this.mTestCase.setName(mEditName.getText().toString());

                new RadiusAsyncTask(
                        mTextResponse,
                        mTextResponseTime,
                        mTextReplyMessage,
                        mIcView,
                        mPbar,
                        mNormalTimeResponse,
                        mHighTimeResponse,
                        getActivity().getApplicationContext(),
                        mListener,
                        this.mTestCase).execute();

            }
        });
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TestCase selected = (TestCase) adapterView.getItemAtPosition(i);
        mEditName.setText(selected.getName());
        mEditAddress.setText(selected.getAddress());
        mEditAuthPort.setText(Integer.toString(selected.getAuthPort()));
        mEditSecret.setText(selected.getSecret());
        mEditUserName.setText(selected.getUserName());
        mEditUserPassword.setText(selected.getUserPassword());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        long onAdd(TestCase testCase);
        void onRemove(TestCase testCase);
        void onEdit(TestCase testCase);
        ar.org.fransis.radiustool.dao.TestCase getTestCaseDAO();
        void onShowAdMain();
        void onTestCompleted(Result result);

    }


    private void loadPreferences(){
        TestCase tc = null;
        if(mSpinnerTestCase != null){
            tc = (TestCase) mSpinnerTestCase.getSelectedItem();
            if(tc == null){
                tc = new TestCase();
            }
            mEditName.setText(tc.getName());
            mEditAddress.setText(tc.getAddress());
            mEditAuthPort.setText(Integer.toString(tc.getAuthPort()));
            mEditSecret.setText(tc.getSecret());
            mEditUserName.setText(tc.getUserName());
            mEditUserPassword.setText(tc.getUserPassword());
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mNormalTimeResponse = Integer.parseInt(sp.getString("pref_normal_time_response","100"));
        mHighTimeResponse = Integer.parseInt(sp.getString("pref_high_time_response","300"));
    }

    public void remove() {

        TestCase tc = (TestCase) mSpinnerTestCase.getSelectedItem();
        if(tc != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.remove_test_case);
            builder.setMessage(R.string.message_remove_test);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    TestCase tc = (TestCase) mSpinnerTestCase.getSelectedItem();
                    if (tc != null) {
                        mListener.onRemove(tc);
                        mAdapter.remove(tc);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity().getApplicationContext(), "Test case removed.", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity().getApplicationContext(), "There is not test case selected.", Toast.LENGTH_LONG).show();
        }
    }

    public void edit() {
        TestCase tc = (TestCase) mSpinnerTestCase.getSelectedItem();
        if(tc !=null){
            tc.setName(mEditName.getText().toString());
            tc.setAddress(mEditAddress.getText().toString());
            tc.setAuthPort(Integer.parseInt(mEditAuthPort.getText().toString()));
            tc.setSecret(mEditSecret.getText().toString());
            tc.setUserName(mEditUserName.getText().toString());
            tc.setUserPassword(mEditUserPassword.getText().toString());

            mListener.onEdit(tc);
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(), "Test case saved.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "There is not test case selected.", Toast.LENGTH_LONG).show();
        }
    }

    public void add() {
        if(!"".equals(mEditName.getText().toString())){
            TestCase tc = new TestCase(
                    mEditName.getText().toString(),
                    mEditAddress.getText().toString(),
                    Integer.parseInt(mEditAuthPort.getText().toString()),
                    mEditSecret.getText().toString(),
                    mEditUserName.getText().toString(),
                    mEditUserPassword.getText().toString()
            );

            tc.setId(mListener.onAdd(tc));
            mAdapter.add(tc);
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(), "Test case saved.", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity().getApplicationContext(), "Please, complete the test name first.", Toast.LENGTH_LONG).show();
        }
    }

}
