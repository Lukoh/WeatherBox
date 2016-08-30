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
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {

    private static final String TAG = "MainActivity";
    private static final String file_url
            = "https://raw.githubusercontent.com/Lukoh/WeatherBox/master/cities.dat";

    private static final int REQUEST_PERMISSION = 0;
    private static final int REQUEST_LOCATION_UPDATE_PERMISSION = 1;

    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_MY_LOCATION_UPDATE
            = "weatherbox::requesting-location-updates";
    private final static String KEY_LOCATION = "weatherbox::location";
    private final static String KEY_LAST_UPDATED_TIME_STRING
            = "weatherbox::last-updated-time-string";

    public static final String EXTRA_LATITUDE = "weatherbox:latitude";
    public static final String EXTRA_LONGITUDE = "weatherbox:longitude";

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    /**
     * Tracks the status of the location updates request.
     */
    private Boolean mRequestingLocationUpdates;

    private ProgressDialog mDialog;

    private CityAdapter mAdapter;

    private double mLatitude;
    private double mLongitude;

    private String mMyPlace;

    private Button mBtnReadFile;
    private Button mBtnMyWeather;

    private ImageView mIvMyLocation;

    private ListView mListView;

    private List<City> mCityInfo = new ArrayList<>();
    private List<String> mCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

        mRequestingLocationUpdates = false;

        if (!isNetworkAvailable(this)) {
            showMessage(getResources().getString(R.string.not_connected_internet));
            return;
        }

        requestMyLocation(savedInstanceState);

        mBtnReadFile = (Button) findViewById(R.id.btn_read);
        mBtnMyWeather = (Button) findViewById(R.id.btn_view_my_weather);

        mIvMyLocation = (ImageView) findViewById(R.id.iv_view_my_map);

        mListView = (ListView) findViewById(R.id.lv_cities);

        mAdapter = new CityAdapter(this);

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

        mBtnReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListView.getCount() > 0 ) {
                    return;
                }

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
                    cityInfo.setFlagUrl(cities[5]);
                    mCityInfo.add(cityInfo);
                }

                for(City city : mCityInfo) {
                    mAdapter.addItem(city);
                }

                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });

        mBtnMyWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationSettings();

                //TODO:: Before - get my current location information. So refer to below site
                //TODO:: visit to My Location App : https://github.com/Lukoh/MyLocation
                if (mMyPlace != null) {
                    showMyWeather(mMyPlace);
                }
            }
        });

        mIvMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra(EXTRA_LATITUDE, mLatitude);
                intent.putExtra(EXTRA_LONGITUDE, mLongitude);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * Stores activity data in the Bundle.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_MY_LOCATION_UPDATE, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                new DownloadFileTask(this).execute(file_url);
                while (mCurrentLocation == null) {
                    mCurrentLocation = LocationServices.FusedLocationApi
                            .getLastLocation(mGoogleApiClient);
                }

                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                doExtractPlace();
            } else {
                // Permission was denied or request was cancelled
                showMessage(getResources().getString(R.string.toast_permission_denied));
            }
        } else if (requestCode == REQUEST_LOCATION_UPDATE_PERMISSION) {
           if(grantResults.length != 0
                   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // We can now safely use the API we requested access to
               LocationServices.FusedLocationApi.requestLocationUpdates(
                       mGoogleApiClient,
                       mLocationRequest,
                       this
               ).setResultCallback(new ResultCallback<Status>() {
                   @Override
                   public void onResult(@NonNull Status status) {
                       mRequestingLocationUpdates = true;
                   }
               });
           }
       }
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                startLocationUpdates();
                Log.i(TAG, "All location settings are satisfied.");
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        mDialog = ProgressDialog.show(MainActivity.this, "",
                                "Checking your location...", true);
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION);
            } else {
                new DownloadFileTask(this).execute(file_url);
                while (mCurrentLocation == null) {
                    mCurrentLocation = LocationServices.FusedLocationApi
                            .getLastLocation(mGoogleApiClient);
                }

                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                doExtractPlace();
            }
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        doExtractPlace();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(WeatherInfoEvent event) {
        new parseWeatherTask(this, event).execute();
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(PlaceInfoEvent event) {
        new parseWeatherTask(this, event).execute();
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    private synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );

        result.setResultCallback(this);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    REQUEST_LOCATION_UPDATE_PERMISSION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    mRequestingLocationUpdates = true;
                }
            });
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_MY_LOCATION_UPDATE)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_MY_LOCATION_UPDATE);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }

            doExtractPlace();
        }
    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager
                = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void requestMyLocation(Bundle savedInstanceState) {
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
    }

    private void doExtractPlace() {
        mLatitude = mCurrentLocation.getLatitude();
        mLongitude = mCurrentLocation.getLongitude();

        new extractPlaceTask(this).execute();

        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showMyWeather(String place) {
        PlaceInfoEvent event = new PlaceInfoEvent();
        event.setResponseType(ResponseEvent.RESPONSE_TYPE_PLACE_INFO);

        Intermediary.INSTANCE.getWoeid(getApplicationContext(), place, event);
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
        private WeakReference<MainActivity> activityWeakRef;

        private DownloadFileTask(MainActivity activity) {
            activityWeakRef = new WeakReference<>(activity);
        }

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
            MainActivity activity = activityWeakRef.get();
            if (activity != null) {
                //: Todo Nothing
            }
        }
    }

    private class parseWeatherTask extends AsyncTask<Void, Void, String> {
        private ResponseEvent mEvent;
        private WeakReference<MainActivity> activityWeakRef;

        private parseWeatherTask(MainActivity activity, ResponseEvent event) {
            mEvent = event;

            activityWeakRef = new WeakReference<>(activity);
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

            MainActivity activity = activityWeakRef.get();
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

    private class extractPlaceTask extends AsyncTask<Void, Void, List<Address>> {
        private WeakReference<MainActivity> activityWeakRef;

        private extractPlaceTask(MainActivity activity) {
            activityWeakRef = new WeakReference<>(activity);
        }

        @Override
        protected List<Address> doInBackground(Void... params) {
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
                while (addresses.size() == 0) {
                    addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            MainActivity activity = activityWeakRef.get();
            if (activity != null) {
                if (addresses != null && addresses.size() > 0) {
                    mMyPlace = addresses.get(0).getLocality();
                }
            }
        }
    }
}
