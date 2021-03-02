package com.example.hongman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.hongman.main_fragments.login_fragment;
import com.example.hongman.main_fragments.storelist_fragment;
import com.example.hongman.server._ServerCommunicator;
import com.example.hongman.until_func.Debug_msg;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //static public String token = "";


    Debug_msg debug_msg = new Debug_msg();

    private FrameLayout frameLayout ;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        Fragment login_fragment = new login_fragment();



        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, login_fragment ).commitAllowingStateLoss();

    }

    public void fragment_manager( String input) {
        Fragment storelist_fragment = new storelist_fragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, storelist_fragment).commitAllowingStateLoss();
    }


    public void change_activity( int market_idx , String store_name ){
        Intent intent = new Intent( this , StoreMain.class);
        intent.putExtra( "market_idx", market_idx );
        startActivity( intent );
        finish();
    }




}