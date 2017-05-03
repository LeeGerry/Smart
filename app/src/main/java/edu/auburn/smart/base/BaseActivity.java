package edu.auburn.smart.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Author: Gary
 * Time: 17/1/10
 */

public class BaseActivity extends Activity {
    protected static String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getName();
    }

    protected void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
    }

    protected void log(String message){
        Log.i(TAG, message);
    }
}
