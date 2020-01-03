package ar.org.fransis.radiustool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import ar.org.fransis.radiustool.model.TestCase;

public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;

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
    private Spinner spinnerTestCase = null;
    private ArrayAdapter<TestCase> adapter = null;
    private ImageView icView = null;
    private ProgressBar pbar = null;
    private int normalTimeResponse = 0;
    private int highTimeResponse = 0;

    private ar.org.fransis.radiustool.dao.TestCase mTestCaseDAO;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
        mTestCaseDAO = mListener.getTestCaseDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        editName = (EditText) view.findViewById(R.id.text_server_name);
        editAddress = (EditText) view.findViewById(R.id.text_radius_ip_address);
        editAuthPort = (EditText) view.findViewById(R.id.text_radius_auth_port);
        editSecret = (EditText) view.findViewById(R.id.text_radius_secret);
        editUserName = (EditText) view.findViewById(R.id.text_radius_username);
        editUserPassword = (EditText) view.findViewById(R.id.text_radius_password);
        textResponse = (TextView) view.findViewById(R.id.text_radius_response);
        textResponseTime = (TextView) view.findViewById(R.id.text_time_response);
        textReplyMessage = (TextView) view.findViewById(R.id.text_radius_reply_message);
        spinnerTestCase = (Spinner)view.findViewById(R.id.list_test_cases);
        icView = (ImageView) view.findViewById(R.id.image_response);
        pbar = (ProgressBar) view.findViewById(R.id.progress_auth);


        adapter = new ArrayAdapter<TestCase>(getActivity(), R.layout.support_simple_spinner_dropdown_item, mTestCaseDAO.getAll());

        spinnerTestCase.setAdapter(adapter);
        spinnerTestCase.setOnItemSelectedListener(this);

        AdView adView = (AdView) view.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("9B75E357FEC4150DBD2350B1A0A6E908")
                .build();
        adView.loadAd(adRequest);

        loadPreferences();

        buttonAuth = (Button) view.findViewById(R.id.button_auth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AdSingleton.getInstance().showAd()){
                    mListener.onShowAdMain();
                }

                new RadiusAsyncTask(
                        getActivity().getApplicationContext(),
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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        normalTimeResponse = Integer.parseInt(sp.getString("pref_normal_time_response","100"));
        highTimeResponse = Integer.parseInt(sp.getString("pref_high_time_response","300"));
    }

    public void remove() {

        TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
        if(tc != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.remove_test_case);
            builder.setMessage(R.string.message_remove_test);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
                    if (tc != null) {
                        //mTestCaseDAO.delete(tc);
                        mListener.onRemove(tc);
                        adapter.remove(tc);
                        adapter.notifyDataSetChanged();
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
        TestCase tc = (TestCase) spinnerTestCase.getSelectedItem();
        if(tc !=null){
            tc.setName(editName.getText().toString());
            tc.setAddress(editAddress.getText().toString());
            tc.setAuthPort(Integer.parseInt(editAuthPort.getText().toString()));
            tc.setSecret(editSecret.getText().toString());
            tc.setUserName(editUserName.getText().toString());
            tc.setUserPassword(editUserPassword.getText().toString());

            mListener.onEdit(tc);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(), "Test case saved.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "There is not test case selected.", Toast.LENGTH_LONG).show();
        }
    }

    public void add() {
        if(!"".equals(editName.getText().toString())){
            TestCase tc = new TestCase(
                    editName.getText().toString(),
                    editAddress.getText().toString(),
                    Integer.parseInt(editAuthPort.getText().toString()),
                    editSecret.getText().toString(),
                    editUserName.getText().toString(),
                    editUserPassword.getText().toString()
            );

            //tc.setId(mTestCaseDAO.insert(tc));
            tc.setId(mListener.onAdd(tc));
            adapter.add(tc);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(), "Test case saved.", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity().getApplicationContext(), "Please, complete the test name first.", Toast.LENGTH_LONG).show();
        }
    }

}
