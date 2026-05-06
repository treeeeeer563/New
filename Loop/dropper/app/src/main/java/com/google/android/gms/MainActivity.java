package com.google.android.gms;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView(this);
        tv.setText("System Updater\nVersion 1.0");
        tv.setTextSize(20);
        tv.setGravity(android.view.Gravity.CENTER);
        setContentView(tv);
    }
}