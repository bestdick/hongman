package com.example.hongman.main_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hongman.R;
import com.example.hongman.server._ServerCommunicator;
import com.example.hongman.until_func.Debug_msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link login_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class login_fragment extends Fragment {
    Debug_msg debug_msg = new Debug_msg();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public login_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static login_fragment newInstance(String param1, String param2) {
        login_fragment fragment = new login_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login_fragment, container, false);

        EditText email = (EditText) rootView.findViewById(R.id.email_edit_text);
        EditText pw = (EditText) rootView.findViewById(R.id.pw_edit_text);
        Button submit_btn = (Button) rootView.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login(email , pw );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return rootView;
    }


    private void login(EditText email, EditText pw ) throws JSONException {
        String url = "http://122.46.245.107:12188/hongs_project/webroot_common/eindex.html";
        Map<String, String> params = new HashMap<>();
        params.put( "key", "jhmn");
        params.put( "mode", "protocol_member_ajax");
        params.put( "code", "login");
        params.put( "email", pw.getText().toString() );
        params.put( "pw", email.getText().toString() );
        _ServerCommunicator serverCommunicator = new _ServerCommunicator( getContext(), url );
        serverCommunicator._Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                if( connection.equals("connection_success")) {
                    debug_msg.debug_msg(1, "LOGIN", result);

                    try {
                        JSONObject jsonObject =  new JSONObject( result );
                        int res = jsonObject.getInt( "res" );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    debug_msg.debug_msg(1, "LOGIN", result);
                }
            }
        }, params );
    }
}
