package com.example.hongman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.hongman.server._ServerCommunicator;
import com.example.hongman.store_fragment.store_inventorylist_fragment;
import com.example.hongman.store_fragment.store_mainlist_fragment;
import com.example.hongman.store_fragment.store_scratchlist_fragment;
import com.example.hongman.until_func.Debug_msg;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

import static com.example.hongman.until_func.static_variable.baseurl;
import static com.example.hongman.until_func.static_variable.token;

public class StoreMain extends AppCompatActivity {
    Debug_msg debug_msg = new Debug_msg();

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    FrameLayout store_main_container ;

    // --- Variable
    int market_idx = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main);


        Intent get_intent = getIntent();
        market_idx = get_intent.getIntExtra( "market_idx" , 0 );
        debug_msg.debug_msg(1 , "STORE INTENT" , String.valueOf( market_idx ) );
        // --- SET store login log ----
        set_store_login_log();
        // --- SET basic element
        fragmentManager = getSupportFragmentManager();
        store_main_container = (FrameLayout) findViewById(R.id.store_main_container);


        // --- SET Tab content mgr
        tab_cont_mgr( );

        // --- SET Initial loader
        Fragment store_mainlist_fragment = new store_mainlist_fragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.store_main_container, store_mainlist_fragment).commitAllowingStateLoss();
    }

    private void tab_cont_mgr(){
        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                debug_msg.debug_msg(1, "TAB SELECT", String.valueOf( tab.getPosition()) ) ;
                switch ( tab.getPosition() ){
                    case 0 :
                        Fragment store_mainlist_fragment = new store_mainlist_fragment( );
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.store_main_container, store_mainlist_fragment).commitAllowingStateLoss();
                        break;
                    case 1:
                        Fragment store_scratchlist_fragment =
                                com.example.hongman.store_fragment.store_scratchlist_fragment.newInstance( market_idx, null );
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.store_main_container, store_scratchlist_fragment).commitAllowingStateLoss();
                        break;
                    case 2:
                        Fragment store_inventorylist_fragment = new store_inventorylist_fragment();
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.store_main_container, store_inventorylist_fragment).commitAllowingStateLoss();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void set_store_login_log(){
        String url = baseurl + "eindex.html" ;
        Map<String, String> params = new HashMap<>();
        params.put( "key", "jhmn");
        params.put( "mode", "protocol_member_ajax");
        params.put( "code", "store_login");
        params.put( "market_idx", String.valueOf( market_idx ) );
        params.put( "token", token);

        _ServerCommunicator serverCommunicator = new _ServerCommunicator( this, url );
        serverCommunicator._Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                debug_msg.debug_msg( 1 , "STORE LOGIN", result );
            }
        } , params);
    }
}