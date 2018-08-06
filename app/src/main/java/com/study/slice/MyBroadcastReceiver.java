package com.study.slice;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import static android.app.slice.Slice.EXTRA_TOGGLE_STATE;
import static com.study.slice.MainActivity.sTemperature;
import static com.study.slice.MainActivity.updateTemperature;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static String ACTION_CHANGE_TEMP = "com.android.example.slicecodelab.ACTION_CHANGE_TEMP";
    public static String EXTRA_TEMP_VALUE = "com.android.example.slicecodelab.EXTRA_TEMP_VALUE";

    public static int sReceivedCount = 0;
    public static String EXTRA_MESSAGE = "message";

    private static Uri sliceUri = Uri.parse("content://com.android.example.slicesample/count");

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (ACTION_CHANGE_TEMP.equals(action) && intent.getExtras() != null) {
            int newValue = intent.getExtras().getInt(EXTRA_TEMP_VALUE, sTemperature);
            updateTemperature(context, newValue);
        }
        /*else if (intent.hasExtra(EXTRA_TOGGLE_STATE)) {
            Toast.makeText(context, "Toggled:  " + intent.getBooleanExtra(
                    EXTRA_TOGGLE_STATE, false),
                    Toast.LENGTH_LONG).show();
            sReceivedCount++;
            ContentResolver contentResolver = context.getContentResolver();
            context.getContentResolver().notifyChange(sliceUri, null);
        }*/
    }


}
