package com.goforer.weatherbox.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.goforer.weatherbox.ui.activity.MainActivity;
import com.goforer.weatherbox.ui.activity.MapsActivity;

public enum ActivityCaller {
    INSTANCE;

    public Intent createIntent(Context context, Class<?> cls, boolean isNewTask) {
        Intent intent = new Intent(context, cls);

        if (isNewTask && !(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        return intent;
    }

    public void callViewMap(Context context, double latitude, double longitude) {
        Intent intent = createIntent(context, MapsActivity.class, true);

        intent.putExtra(MainActivity.EXTRA_LATITUDE, latitude);
        intent.putExtra(MainActivity.EXTRA_LONGITUDE, longitude);

        context.startActivity(intent);
    }
}
