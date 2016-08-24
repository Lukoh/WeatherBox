package com.goforer.weatherbox.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.goforer.weatherbox.R;
import com.goforer.weatherbox.model.data.City;
import com.goforer.weatherbox.ui.adapter.CityAdapter;
import com.goforer.weatherbox.utility.ActivityCaller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static String file_url
            = "https://raw.githubusercontent.com/Lukoh/WeatherBox/master/cities.dat";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Button mReadFileButton;

    private ListView mListView;

    private List<City> mCityInfo = new ArrayList<>();
    private List<String> mCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            allowStoragePermissions(this);
        }

        mReadFileButton = (Button) findViewById(R.id.btn_read) ;
        mListView = (ListView) findViewById(R.id.lv_cities);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : use item data.
                ActivityCaller.INSTANCE.callWeatherInfo(v.getContext(),
                        mCityInfo.get(position));
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
                    String[] cities = city.split(" ");
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO:: Nothing
                new DownloadFileFromURL().execute(file_url);
            }
        }
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

    /**
     * Background Async Task to download file
     *
     */
    private static class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

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
}
