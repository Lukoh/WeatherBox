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

package com.goforer.weatherbox.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.goforer.weatherbox.R;
import com.goforer.weatherbox.model.data.City;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {
    private ArrayList<City> mCityItem = new ArrayList<>() ;

    public CityAdapter() {

    }

    @Override
    public int getCount() {
        return mCityItem.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_city_info_item, parent, false);
        }

        TextView tvCountry = (TextView) convertView.findViewById(R.id.tv_country) ;
        TextView tvCity = (TextView) convertView.findViewById(R.id.tv_city) ;
        TextView tvZip = (TextView) convertView.findViewById(R.id.tv_zip) ;
        TextView tvlongitude = (TextView) convertView.findViewById(R.id.tv_longitude) ;
        TextView tvlatitude = (TextView) convertView.findViewById(R.id.tv_latitude) ;

        City cityItem = mCityItem.get(position);

        tvCountry.setText(cityItem.getCountry());
        tvCity.setText(cityItem.getCity());
        tvZip.setText(cityItem.getZip());
        tvlongitude.setText(cityItem.getLongitude());
        tvlatitude.setText(cityItem.getLatitude());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return mCityItem.get(position) ;
    }

    public void addItem(City city) {
        mCityItem.add(city);
    }
}