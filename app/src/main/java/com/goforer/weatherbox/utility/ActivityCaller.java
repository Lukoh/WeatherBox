/*
 * Copyright (C) 2016 Lukoh Nam, goForer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
