package com.example.hongman.store_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hongman.R;
import com.example.hongman.StoreMain;
import com.example.hongman.until_func.Debug_msg;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link store_scratchlist_sub_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class store_scratchlist_sub_fragment extends Fragment {
    Debug_msg debug_msg = new Debug_msg();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "market_idx";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int market_idx;
    private String mParam2;

    public store_scratchlist_sub_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment store_scratchlist_sub_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static store_scratchlist_sub_fragment newInstance(int param1, String param2) {
        store_scratchlist_sub_fragment fragment = new store_scratchlist_sub_fragment();
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
        View rootView = inflater.inflate(R.layout.fragment_store_scratchlist_sub_fragment, container, false);

        debug_msg.debug_msg( 1 , "SUB FRAG M_IDX" , String.valueOf( market_idx ) ) ;
        ui_cont_mgr( rootView );
        return rootView;
    }

    private void ui_cont_mgr( View rootView ){
        Button close_btn = ( Button ) rootView.findViewById(R.id.close_btn);
        Button submit_btn = ( Button ) rootView.findViewById(R.id.submit_btn);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().detach(store_scratchlist_sub_fragment.this).commitAllowingStateLoss();
                        //.remove(store_scratchlist_sub_fragment.this).commit();
            }
        });
    }
}