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

import android.content.Context;
import android.content.Intent;

import com.goforer.weatherbox.model.data.City;
import com.goforer.weatherbox.ui.activity.CityWeatherInfoActivity;

public enum  ActivityCaller {
    INSTANCE;

    public static final String EXTRA_CITY= "weather:city";


    public Intent createIntent(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return intent;
    }

    public void callWeatherInfo(Context context, City city) {
        Intent intent = createIntent(context, CityWeatherInfoActivity.class);

        intent.putExtra(EXTRA_CITY, city);
        context.startActivity(intent);
    }


}

