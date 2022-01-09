package com.h5190059.iett.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;


import androidx.appcompat.app.AlertDialog;

public class AlertUtil {
    public static void alertGoster(Activity activity, int style, Drawable icon, CharSequence title, CharSequence message, CharSequence negativeButton, CharSequence positiveButton){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity,style);
        builder.setIcon(icon);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(PrefUtil.getStringPref(activity.getApplicationContext(),Constants.PREF_ALERT_SECILEN).equals(Constants.PREF_ALERT_INTERNET)){
                    activity.finish();
                }
                else{
                    activity.finish();
                }
            }
        });
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(PrefUtil.getStringPref(activity.getApplicationContext(),Constants.PREF_ALERT_SECILEN).equals(Constants.PREF_ALERT_INTERNET)){
                    dialog.dismiss();
                    activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    activity.finish();
                }
                else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
}
