package com.example.hongman.store_fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongman.R;
import com.example.hongman.StoreMain;
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


    private FrameLayout frameLayout ;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    List<scratchlist_bean> list;
    ScratchAdapter adapter;

    private String shift_action = "in"; // in or out





    // ---- UI elements
    ScrollView each_input_container;

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

        fragmentManager = ( (StoreMain) getContext()  ).getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        frameLayout = (FrameLayout) rootView.findViewById(R.id.each_input_fragment_container);

        // --- SET shift in and out UI elements
        shift_cont_mgr( rootView );
        return rootView;
    }
    private void shift_cont_mgr(View rootView){
        Button shift_in = (Button) rootView.findViewById(R.id.shift_in_btn);
        Button shift_out = (Button) rootView.findViewById(R.id.shift_out_btn);
        LinearLayout shift_in_out_container = (LinearLayout) rootView.findViewById(R.id.shift_in_out_container);
        LinearLayout scratch_list_container = (LinearLayout) rootView.findViewById(R.id.scratch_list_container);
        each_input_container = (ScrollView) rootView.findViewById(R.id.each_input_container);

        shift_in_out_container.setVisibility(View.VISIBLE);
        scratch_list_container.setVisibility(View.GONE);
        each_input_container.setVisibility(View.GONE);

        // ---SET List view items .....
        ListView scratch_listview =(ListView) rootView.findViewById(R.id.scratch_listview);
        list =new ArrayList<>();
        adapter = new ScratchAdapter( getContext(), list );
        scratch_listview.setAdapter( adapter );

        // --- SET list on click listener
        listview_action_mgr( scratch_listview );




        shift_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shift_action = "IN";
                shift_in_out_container.setVisibility(View.GONE);
                scratch_list_container.setVisibility(View.VISIBLE);
                each_input_container.setVisibility(View.GONE);
                // --- GET list and Draw list view
                get_scratch_list( adapter );
            }
        });

        shift_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shift_action = "OUT";
                shift_in_out_container.setVisibility(View.GONE);
                scratch_list_container.setVisibility(View.VISIBLE);
                each_input_container.setVisibility(View.GONE);
                // --- GET list and Draw list view
                get_scratch_list( adapter );
            }
        });
    }

    private void listview_action_mgr( ListView scratch_listview ){
        scratch_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                debug_msg.debug_msg( 1 , "LISTVIEW POS CHK", "psoition : "+String.valueOf( position ) );
                if( list.get(position).getUsage_flag().equals("U") ) {
                    each_input_container.setVisibility(View.VISIBLE);
                    Bundle scratch_info_bundle = new Bundle();
                    scratch_info_bundle.putInt("list_position", position);
                    scratch_info_bundle.putInt("scratch_idx", list.get(position).getScratch_idx());
                    scratch_info_bundle.putInt("seq", list.get(position).getScratch_seq());
                    scratch_info_bundle.putString("scratch_name", list.get(position).getScratch_name());
                    scratch_info_bundle.putInt("price", list.get(position).getPrice());
                    scratch_info_bundle.putInt("per_roll", list.get(position).getPer_roll());


                    Fragment store_scratchlist_sub_fragment = com.example.hongman.store_fragment.store_scratchlist_sub_fragment.newInstance(market_idx, scratch_info_bundle);
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.each_input_fragment_container, store_scratchlist_sub_fragment).commitAllowingStateLoss();
                }else{
                   // Toast.makeText( getContext() , "Not in use..", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
//---- sub fragment access here
    public void remove_sub_fragment( Fragment fragment ){
        transaction = getChildFragmentManager().beginTransaction();
        transaction.remove( fragment ).commitAllowingStateLoss();
        each_input_container.setVisibility(View.GONE);
    }
    public void submit_sub_fragment( Fragment fragment , int list_position, int scratch_idx, int num_of_scratch ){
        //--- close
        transaction = getChildFragmentManager().beginTransaction();
        transaction.remove( fragment ).commitAllowingStateLoss();
        each_input_container.setVisibility(View.GONE);
        debug_msg.debug_msg( 1 , "SUBMIT : " , "list_position : " + list_position + "  ## scratch_idx : " + scratch_idx + " ## num_of_scratch : " + num_of_scratch );
        //--- refresh list view
        refresh_listview( list_position , scratch_idx, num_of_scratch );
        // --- insert to server
        String url = baseurl + "eindex.html" ;
        Map<String, String> params = new HashMap<>();
        params.put( "key", "jhmn");
        params.put( "token", token);
        params.put( "mode", "protocol_contents_ajax");
        if( shift_action.equals("IN")) {
            params.put("code", "20210");
        }else if(shift_action.equals("OUT")){
            params.put("code", "20211");
        }
        params.put( "market_idx", String.valueOf( market_idx ) );
        params.put( "scratch_idx", String.valueOf( scratch_idx ) );
        params.put( "scratch_num", String.valueOf( num_of_scratch ) );

        _ServerCommunicator serverCommunicator = new _ServerCommunicator( getContext(), url);
        serverCommunicator._Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                debug_msg.debug_msg( 1, "INSERT RES", result );
                if( connection.equals( "connection_success")) {
                    try {
                        JSONObject jsonObject =  new JSONObject( result );
                        int res = jsonObject.getInt( "res" );
                        if ( res == 0 ){

                        }else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    debug_msg.debug_msg( 1, "CONN FAIL", "submit_sub_fragment()" );
                }

            }
        },  params);
    }

    private void refresh_listview( int list_position, int scratch_idx, int num_of_scratch ){
        list.get(list_position).setCurrent_seq(num_of_scratch);
        adapter.notifyDataSetChanged();

    }
    private void get_scratch_list( ScratchAdapter adapter){
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
                    debug_msg.debug_msg(1, "SCRATCH INFO", result);

                    try {
                        JSONObject jsonObject =  new JSONObject( result );
                        int res = jsonObject.getInt( "res" );
                        if ( res == 0 ){
                            // ---- container info
                            JSONObject container = jsonObject.getJSONObject("msg").getJSONObject("container");
                            JSONArray scratch_list = jsonObject.getJSONObject("msg").getJSONArray("list");
                            // ---- make list -----
                            String scratch_num_str_param = "scratch_num_so";
                            if( shift_action.equals("IN")){
                                scratch_num_str_param = "scratch_num_si";
                            }
                            for( int i = 0 ; i < scratch_list.length(); i++ ){
                                scratchlist_bean item = new scratchlist_bean(
                                    scratch_list.getJSONObject(i).getInt( "scratch_idx"),scratch_list.getJSONObject(i).getInt( "market_idx"),
                                        scratch_list.getJSONObject(i).getInt( "scratch_seq"), scratch_list.getJSONObject(i).getString( "scratch_name"),
                                        scratch_list.getJSONObject(i).getInt( "price"), scratch_list.getJSONObject(i).getInt( "roll") ,
                                        scratch_list.getJSONObject(i).getString("usage_flag"), scratch_list.getJSONObject(i).getInt( scratch_num_str_param )
                                ) ;
                                list.add( item );

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
class ScratchAdapter extends BaseAdapter{
    Context context;
    List<scratchlist_bean> list;
    public ScratchAdapter(Context context , List<scratchlist_bean> list) {
        this.context = context;
        this.list = list ;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.item_in_listview_scratch, null);
        TextView scratch_seq_text_view = (TextView) v.findViewById(R.id.scratch_seq_text_view);
        TextView scratch_name_text_view = (TextView) v.findViewById(R.id.scratch_name_text_view);
        TextView price_text_view = (TextView) v.findViewById(R.id.price_text_view);
        TextView per_roll_text_view = (TextView) v.findViewById(R.id.per_roll_text_view);
        TextView current_seq_text_view = (TextView) v.findViewById(R.id.current_seq_text_view);


        int scratch_seq = list.get(i).getScratch_seq();
        String scratch_name = list.get(i).getScratch_name();
        int price = list.get(i).getPrice();
        int roll = list.get(i).getPer_roll();

        scratch_seq_text_view.setText( "#"+String.valueOf( scratch_seq ) );
        scratch_name_text_view.setText( scratch_name );
        price_text_view.setText( "$" + String.valueOf(price) );
        per_roll_text_view.setText(  "Max Scratch Num" + String.valueOf( roll )  );

        if ( list.get(i).getCurrent_seq() != 0 ){
            current_seq_text_view.setText("Input Scratch Num : "+ String.valueOf( list.get(i).getCurrent_seq() ) );
        }

        if ( list.get(i).getUsage_flag().equals("N")){
            v.setBackgroundColor(Color.parseColor("#d0d0d0"));
        }
        return v ;
    }
}

class scratchlist_bean{
    int scratch_idx ;
    int market_idx ;
    int scratch_seq ;
    String scratch_name;
    int price;
    int per_roll;
    String usage_flag;
    int current_seq ;

    public int getScratch_idx() {
        return scratch_idx;
    }

    public void setScratch_idx(int scratch_idx) {
        this.scratch_idx = scratch_idx;
    }

    public int getMarket_idx() {
        return market_idx;
    }

    public void setMarket_idx(int market_idx) {
        this.market_idx = market_idx;
    }

    public int getScratch_seq() {
        return scratch_seq;
    }

    public void setScratch_seq(int scratch_seq) {
        this.scratch_seq = scratch_seq;
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

    public int getPer_roll() {
        return per_roll;
    }

    public void setPer_roll(int per_roll) {
        this.per_roll = per_roll;
    }

    public String getUsage_flag() {
        return usage_flag;
    }

    public void setUsage_flag(String usage_flag) {
        this.usage_flag = usage_flag;
    }

    public int getCurrent_seq() {
        return current_seq;
    }

    public void setCurrent_seq(int current_seq) {
        this.current_seq = current_seq;
    }

    public scratchlist_bean(int scratch_idx, int market_idx, int scratch_seq, String scratch_name, int price, int per_roll, String usage_flag, int current_seq) {
        this.scratch_idx = scratch_idx;
        this.market_idx = market_idx;
        this.scratch_seq = scratch_seq;
        this.scratch_name = scratch_name;
        this.price = price;
        this.per_roll = per_roll;
        this.usage_flag = usage_flag;
        this.current_seq = current_seq;
    }
}