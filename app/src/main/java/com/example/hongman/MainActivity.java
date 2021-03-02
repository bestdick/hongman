package com.example.hongman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hongman.server._ServerCommunicator;
import com.example.hongman.until_func.Debug_msg;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Debug_msg debug_msg = new Debug_msg();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login();
    }


    private void login(){
        String url = "http://122.46.245.107:12188/hongs_project/webroot_common/eindex.html";
        Map<String, String> params = new HashMap<>();
        params.put("mode", )
        _ServerCommunicator serverCommunicator = new _ServerCommunicator( this, url );
        serverCommunicator._Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                debug_msg.debug_msg( 1, "LOGIN" , result );
            }
        }, params );
    }


}