package com.bryanwalsh.sleepcyclewidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class addAlarmActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Intent received", Toast.LENGTH_LONG).show();
    }
}
