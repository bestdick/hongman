package com.example.hongman.until_func;

import android.util.Log;

public class Debug_msg {

        int debug_mod = 1;

        public void debug_msg( int log_mode  , String log_title, String log_msg) {
            switch( log_mode ){
                case 1:
                    Log.e( log_title, log_msg );
                    break;
            }
        }


}
