package com.vm.mserver.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by VanManh on 05-Dec-17.
 */

public class Config {
    private static final String NAME = "s_config";
    private static final String IS_RUNNING = "is_running";
    private static final String IS_FIRST = "is_first";

    public static boolean isServerRun(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        if (preferences.contains(IS_RUNNING)) {
            boolean rs = preferences.getBoolean(IS_RUNNING, false);
            return rs;
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(IS_RUNNING, false);
            editor.commit();
        }
        return false;
    }

    public static boolean setServerStatus(Context context, boolean isRunning) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(IS_RUNNING, isRunning);
            editor.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
