package com.goforer.weatherbox.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.goforer.base.model.event.ResponseEvent;
import com.goforer.weatherbox.R;
import com.goforer.weatherbox.model.data.City;
import com.goforer.weatherbox.model.data.Query;
import com.goforer.weatherbox.model.data.WeatherInfo;
import com.goforer.weatherbox.model.event.WeatherInfoEvent;
import com.goforer.weatherbox.utility.ActivityCaller;
import com.goforer.weatherbox.web.Intermediary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

public class CityWeatherInfoActivity extends AppCompatActivity {
    private TextView mWeatherView;

    private City mCity;

    private WeatherInfo mWeatherInfo;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_city_weather_info);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            mActionBar.setTitle(getResources().getString(R.string.app_name));
            mActionBar.setElevation(0);
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setHomeButtonEnabled(true);

        }

        mWeatherView = (TextView) findViewById(R.id.tv_weather);


        mCity = getIntent().getExtras().getParcelable(ActivityCaller.EXTRA_CITY);

        WeatherInfoEvent event = new WeatherInfoEvent();

        Intermediary.INSTANCE.getWeatherInfo(getApplicationContext(), mCity.getCountry(),
                mCity.getCity(), event);

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(WeatherInfoEvent event) {
        new handleTask(this, event).execute();
    }

    private class handleTask extends AsyncTask<Void, Void, String> {
        private ResponseEvent mEvent;
        private WeakReference<CityWeatherInfoActivity> mActivityWeakRef;

        private handleTask(CityWeatherInfoActivity activity, ResponseEvent event) {
            mEvent = event;

            mActivityWeakRef = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Void... params) {
            return mEvent.getResponseClient().getQuery().toString();
        }

        @Override
        protected void onPostExecute(String query) {
            super.onPostExecute(query);

            CityWeatherInfoActivity activity = mActivityWeakRef.get();
            if (activity != null) {
                mWeatherInfo = WeatherInfo.gson().fromJson(Query.gson()
                        .fromJson(query, Query.class).getResults().toString(), WeatherInfo.class);

                mActionBar.setTitle(mWeatherInfo.getChannel().getTitle());
                mWeatherView.setText(query);

            }
        }
    }
}
