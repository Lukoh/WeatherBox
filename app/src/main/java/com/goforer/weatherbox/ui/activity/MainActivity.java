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

package com.goforer.weatherbox.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.goforer.base.model.event.ResponseEvent;
import com.goforer.customtabsclient.shared.CustomTabsHelper;
import com.goforer.weatherbox.R;
import com.goforer.weatherbox.model.data.City;
import com.goforer.weatherbox.model.data.Error;
import com.goforer.weatherbox.model.data.PlaceInfo;
import com.goforer.weatherbox.model.data.Query;
import com.goforer.weatherbox.model.data.WeatherInfo;
import com.goforer.weatherbox.model.event.PlaceInfoEvent;
import com.goforer.weatherbox.model.event.WeatherInfoEvent;
import com.goforer.weatherbox.ui.adapter.CityAdapter;
import com.goforer.weatherbox.web.Intermediary;
import com.google.gson.JsonElement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final String file_url
            = "https://raw.githubusercontent.com/Lukoh/WeatherBox/master/cities.dat";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Button mReadFileButton;
    private Button mViewMyWeather;

    private ListView mListView;

    private List<City> mCityInfo = new ArrayList<>();
    private List<String> mCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            allowStoragePermissions(this);
        }

        mReadFileButton = (Button) findViewById(R.id.btn_read);
        mViewMyWeather = (Button) findViewById(R.id.btn_view_my_weather);

        mListView = (ListView) findViewById(R.id.lv_cities);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : use item data.
                WeatherInfoEvent event = new WeatherInfoEvent();
                event.setResponseType(ResponseEvent.RESPONSE_TYPE_WEATHER_INFO);

                if (mCityInfo.get(position) != null) {
                    Intermediary.INSTANCE.getWeatherInfoWithPlace(getApplicationContext(),
                            mCityInfo.get(position).getCountry(),
                            mCityInfo.get(position).getCity(), event);
                }
            }
        }) ;

        mReadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCities = new ArrayList<>();
                    Scanner sc = new Scanner(new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS).toString() + "/Cities.dat"));
                    while (sc.hasNextLine())    {
                        mCities.add(sc.nextLine());
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e);
                }

                for(int i = 1; i < mCities.size(); i++) {
                    City cityInfo = new City();
                    String city = mCities.get(i);
                    String[] cities = city.split(",");
                    cityInfo.setCountry(cities[0]);
                    cityInfo.setZip(cities[1]);
                    cityInfo.setCity(cities[2]);
                    cityInfo.setLongitude(cities[3]);
                    cityInfo.setLatitude(cities[4]);
                    mCityInfo.add(cityInfo);
                }

                CityAdapter adapter = new CityAdapter() ;

                mListView.setAdapter(adapter);

                for(City city : mCityInfo) {
                    adapter.addItem(city);
                }
            }
        });

        mViewMyWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:: Before - get my current location information. So refer to below site
                //TODO:: visit to My Location App : https://github.com/Lukoh/MyLocation
                /* Commented temporarily - The below code is on implementing and done by ends of this week
                showMyWeather();
                */
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO:: Download file
                new DownloadFileTask().execute(file_url);
            }
        }
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(WeatherInfoEvent event) {
        new parseTask(this, event).execute();
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(PlaceInfoEvent event) {
        new parseTask(this, event).execute();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private static void allowStoragePermissions(Activity activity) {
        int writePermission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED
                || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void showMyWeather() {
        PlaceInfoEvent event = new PlaceInfoEvent();
        event.setResponseType(ResponseEvent.RESPONSE_TYPE_PLACE_INFO);

        Intermediary.INSTANCE.getWoeid(getApplicationContext(), "", event);
    }

    private void showWeatherInfoWithPlace(JsonElement results, Activity activity) {
        WeatherInfo weatherInfo = WeatherInfo.gson().fromJson(
                results.toString(), WeatherInfo.class);

        String link = weatherInfo.getChannel().getLink();
        final String url = link.substring(link.indexOf("*") + 1, link.length());
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources()
                .getColor(R.color.colorPrimaryDark)).setShowTitle(true);
        builder.setCloseButtonIcon(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
        CustomTabsIntent customTabsIntent = builder.build();
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        CustomTabsHelper.addKeepAliveExtra(activity, customTabsIntent.intent);
        CustomTabsClient.bindCustomTabsService(getApplicationContext(), packageName,
                new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                client.warmup(0);

                CustomTabsSession session = client.newSession(new CustomTabsCallback());
                session.mayLaunchUrl(Uri.parse(url), null, null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        });

        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    private void showWeatherInfoWithWeoid(JsonElement results) {
        WeatherInfoEvent event = new WeatherInfoEvent();
        event.setResponseType(ResponseEvent.RESPONSE_TYPE_WEATHER_INFO);

        Intermediary.INSTANCE.getWeatherInfoWithWoeid(
                getApplicationContext(), getWoeid(results), event);
    }

    private String getWoeid(JsonElement results) {
        PlaceInfo placeInfo = PlaceInfo.gson().fromJson(
                results.toString(), PlaceInfo.class);

        return placeInfo.getPlace().getWoeid();
    }

    /**
     * Background Async Task to download file
     *
     */
    private static class DownloadFileTask extends AsyncTask<String, Integer, String> {
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {
                String root = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).toString();

                System.out.println("Downloading");
                URL url = new URL(fileUrl[0]);

                URLConnection conection = url.openConnection();
                conection.connect();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(root + "/Cities.dat");
                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {

        }
    }

    private class parseTask extends AsyncTask<Void, Void, String> {
        private ResponseEvent mEvent;
        private WeakReference<MainActivity> mActivityWeakRef;

        private parseTask(MainActivity activity, ResponseEvent event) {
            mEvent = event;

            mActivityWeakRef = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Void... params) {
            String query;
            if (mEvent.getResponseClientType() == ResponseEvent.RESPONSE_TYPE_CLIENT_SUCCESS) {
                query = mEvent.getResponseClient().getQuery().toString();
            } else {
                query = mEvent.getResponseErrorClient().toString();
            }
            return query;
        }

        @Override
        protected void onPostExecute(String query) {
            super.onPostExecute(query);

            MainActivity activity = mActivityWeakRef.get();
            if (activity != null) {
                if (mEvent.getResponseClientType() == ResponseEvent.RESPONSE_TYPE_CLIENT_SUCCESS) {
                    JsonElement results = Query.gson().fromJson(query, Query.class).getResults();
                    if (results == null) {
                        showMessage(getResources().getString(R.string.no_result));
                    } else {
                        switch (mEvent.getResponseType()) {
                            case ResponseEvent.RESPONSE_TYPE_WEATHER_INFO:
                                showWeatherInfoWithPlace(results, activity);
                                break;
                            case ResponseEvent.RESPONSE_TYPE_PLACE_INFO:
                                showWeatherInfoWithWeoid(results);
                                break;
                            default:
                        }
                    }

                }
            } else {
                showMessage(Error.gson().fromJson(query, Error.class).getDescription());
            }
        }
    }
}
