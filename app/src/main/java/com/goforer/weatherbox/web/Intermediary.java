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

package com.goforer.weatherbox.web;

import android.content.Context;

import com.goforer.base.model.event.ResponseEvent;
import com.goforer.weatherbox.web.communicator.RequestClient;
import com.goforer.weatherbox.web.communicator.ResponseClient;

import retrofit2.Call;
import retrofit2.Response;

public enum Intermediary {
    INSTANCE;

    public void getWeatherInfoWithPlace(Context context, String country, String city, ResponseEvent event) {
        String query =
        "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text="
                + "\"" + country + "," +  city + "\""+ ")";

        Call<ResponseClient> call = RequestClient.INSTANCE
                .getRequestMethod(context).getWeather(query, "json");
        call.enqueue(new RequestClient.RequestCallback(event) {
            @Override
            public void onResponse(Call<ResponseClient> call, Response<ResponseClient> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseClient> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void getWeatherInfoWithWoeid(Context context, String woeid, ResponseEvent event) {
        String query =
                "select * from weather.forecast where woeid =" + woeid;

        Call<ResponseClient> call = RequestClient.INSTANCE
                .getRequestMethod(context).getWeather(query, "json");
        call.enqueue(new RequestClient.RequestCallback(event) {
            @Override
            public void onResponse(Call<ResponseClient> call, Response<ResponseClient> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseClient> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void getWoeid(Context context, String place, ResponseEvent event) {
        String query =
                "select * from geo.places where text=" + "\"" + place + "\"";

        Call<ResponseClient> call = RequestClient.INSTANCE
                .getRequestMethod(context).getWoeid(query, "json");
        call.enqueue(new RequestClient.RequestCallback(event) {
            @Override
            public void onResponse(Call<ResponseClient> call, Response<ResponseClient> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseClient> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }
}

