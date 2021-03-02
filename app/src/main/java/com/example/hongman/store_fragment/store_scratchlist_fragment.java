package com.example.hongman.store_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongman.R;
import com.example.hongman.server._ServerCommunicator;
import com.example.hongman.until_func.Debug_msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hongman.until_func.static_variable.baseurl;
import static com.example.hongman.until_func.static_variable.token;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link store_scratchlist_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class store_scratchlist_fragment extends Fragment {
    Debug_msg debug_msg = new Debug_msg();
    private String shift_action = "in"; // in or out
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "market_idx";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int market_idx;
    private String mParam2;

    public store_scratchlist_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment store_scratchlist_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static store_scratchlist_fragment newInstance(int param1, String param2) {
        store_scratchlist_fragment fragment = new store_scratchlist_fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            market_idx = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_store_scratchlist_fragment, container, false);

        // --- SET shift in and out UI elements
        shift_cont_mgr( rootView );
        return rootView;
    }
    private void shift_cont_mgr(View rootView){
        Button shift_in = (Button) rootView.findViewById(R.id.shift_in_btn);
        Button shift_out = (Button) rootView.findViewById(R.id.shift_out_btn);
        LinearLayout shift_in_out_container = (LinearLayout) rootView.findViewById(R.id.shift_in_out_container);
        shift_in_out_container.setVisibility(View.VISIBLE);

        // ---SET LISTVIEW
        ListView scratch_listview =(ListView) rootView.findViewById(R.id.scratch_listview);
        scratch_listview.setVisibility(View.INVISIBLE);

        //List<scratchlist_bean> list =new ArrayList<>();
        List<String> list =new ArrayList<>();
        //ArrayAdapter<scratchlist_bean> adapter = new ArrayAdapter<scratchlist_bean>( getContext(), android.R.layout.simple_list_item_1 );
        ArrayAdapter<String> adapter = new ArrayAdapter<>( getContext(), android.R.layout.simple_list_item_1 );
        scratch_listview.setAdapter( adapter );
        get_scratch_list( adapter,  list );


        shift_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shift_action = "IN";
                shift_in_out_container.setVisibility(View.INVISIBLE);
                scratch_listview.setVisibility(View.VISIBLE);
            }
        });

        shift_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shift_action = "OUT";
                shift_in_out_container.setVisibility(View.INVISIBLE);
                scratch_listview.setVisibility(View.VISIBLE);
            }
        });
    }



    private void get_scratch_list( ArrayAdapter<String> adapter, List<String> list ){
        String url = baseurl + "eindex.html" ;
        Map<String, String> params = new HashMap<>();
        params.put( "key", "jhmn");
        params.put( "mode", "protocol_contents_ajax");
        params.put( "code", "20200");
        params.put( "market_idx", String.valueOf( market_idx ) );
        params.put( "token", token);

        _ServerCommunicator serverCommunicator = new _ServerCommunicator( getContext(), url );
        serverCommunicator._Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                if( connection.equals("connection_success")) {
                    debug_msg.debug_msg(1, "SCRATCH LIST", result);

                    try {
                        JSONObject jsonObject =  new JSONObject( result );
                        int res = jsonObject.getInt( "res" );
                        if ( res == 0 ){
                            // ---- make list ------
                            JSONArray scratch_list = jsonObject.getJSONArray("msg");
                            for( int i = 0 ; i < scratch_list.length(); i++ ){
                                scratchlist_bean item = new scratchlist_bean(
                                    scratch_list.getJSONObject(i).getInt( "scratch_idx"),scratch_list.getJSONObject(i).getInt( "market_idx"),
                                        scratch_list.getJSONObject(i).getString( "scratch_name"), scratch_list.getJSONObject(i).getInt( "price")

                                ) ;
                                list.add( scratch_list.getJSONObject(i).getString( "scratch_name") );

                            }
                            adapter.notifyDataSetChanged();
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



}
class scratch_adapter extends BaseAdapter{
    Context context;
    List<scratchlist_bean> list;
    public scratch_adapter(Context context , List<scratchlist_bean> list) {
        this.context = context;
        this.list = list ;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
class scratchlist_bean{
    int scratch_idx ;
    int market_idx ;
    String scratch_name;
    int price;

    public scratchlist_bean(int scratch_idx, int market_idx, String scratch_name, int price) {
        this.scratch_idx = scratch_idx;
        this.market_idx = market_idx;
        this.scratch_name = scratch_name;
        this.price = price;
    }

    public int getScratch_list() {
        return scratch_idx;
    }

    public void setScratch_list(int scratch_list) {
        this.scratch_idx = scratch_list;
    }

    public int getMarket_idx() {
        return market_idx;
    }

    public void setMarket_idx(int market_idx) {
        this.market_idx = market_idx;
    }

    public String getScratch_name() {
        return scratch_name;
    }

    public void setScratch_name(String scratch_name) {
        this.scratch_name = scratch_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}