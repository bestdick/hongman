package com.example.hongman.main_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hongman.MainActivity;
import com.example.hongman.R;
import com.example.hongman.server._ServerCommunicator;
import com.example.hongman.until_func.Debug_msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.hongman.until_func.static_variable.baseurl;
import static com.example.hongman.until_func.static_variable.token;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link storelist_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class storelist_fragment extends Fragment {

    Debug_msg debug_msg = new Debug_msg();
    LinearLayout storelist_container ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public storelist_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment storelist_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static storelist_fragment newInstance(String param1, String param2) {
        storelist_fragment fragment = new storelist_fragment();
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
        View rootView = inflater.inflate(R.layout.fragment_storelist_fragment, container, false);
        storelist_container = (LinearLayout) rootView.findViewById(R.id.storelist_container);
        // --- when the page first initialized
        try {
            get_store_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void get_store_list() throws JSONException {
        String url = baseurl + "eindex.html" ;
        Map<String, String> params = new HashMap<>();
        params.put( "key", "jhmn");
        params.put( "mode", "protocol_contents_ajax");
        params.put( "code", "20000");
        params.put( "token", token);

        _ServerCommunicator serverCommunicator = new _ServerCommunicator( getContext(), url );
        serverCommunicator._Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                if( connection.equals("connection_success")) {
                    debug_msg.debug_msg(1, "STORE LIST", result);

                    try {
                        JSONObject jsonObject =  new JSONObject( result );
                        int res = jsonObject.getInt( "res" );
                        if ( res == 0 ){
                            // ---- make list ------
                            JSONArray storelist = jsonObject.getJSONArray("msg");
                            for( int i = 0 ; i < storelist.length(); i++ ){
                                String store_name = storelist.getJSONObject(i).getString("market_name");
                                int market_idx = storelist.getJSONObject(i).getInt("market_idx");
                                TextView tv = new TextView(getContext());
                                tv.setText( store_name );
                                storelist_container.addView( tv );
                                storelist_click( market_idx, store_name,  tv );
                            }
                        }else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    debug_msg.debug_msg(1, "LOGIN", result);
                }
            }
        }, params );
    }

    private void storelist_click( int market_idx , String store_name , TextView textView ){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ( (MainActivity) getContext() ).change_activity( market_idx , store_name);
            }
        });
    }
}

