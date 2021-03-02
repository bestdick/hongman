package com.example.hongman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.hongman.main_fragments.login_fragment;
import com.example.hongman.server._ServerCommunicator;
import com.example.hongman.until_func.Debug_msg;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static  public String token = "";

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





}