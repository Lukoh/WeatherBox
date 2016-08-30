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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goforer.ui.view.BezelImageView;
import com.goforer.weatherbox.R;
import com.goforer.weatherbox.model.data.City;
import com.goforer.weatherbox.ui.activity.MainActivity;
import com.goforer.weatherbox.ui.activity.MapsActivity;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {
    private ArrayList<City> mCityItem = new ArrayList<>() ;

    public CityAdapter() {

    }

    @Override
    public int getCount() {
        return mCityItem.size();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        final City cityItem = mCityItem.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_city_info_item, parent, false);
            holder = new ViewHolder();
            holder.mIvFlag = (BezelImageView) convertView.findViewById(R.id.iv_flag);
            holder.mTvCountry = (TextView) convertView.findViewById(R.id.tv_country) ;
            holder.mTvCity = (TextView) convertView.findViewById(R.id.tv_city) ;
            holder.mTvZip = (TextView) convertView.findViewById(R.id.tv_zip) ;
            holder.mTvLongitude= (TextView) convertView.findViewById(R.id.tv_longitude) ;
            holder.mTvLatitude = (TextView) convertView.findViewById(R.id.tv_latitude) ;
            holder.mIvCityLocation = (ImageView) convertView.findViewById(R.id.iv_view_city_map);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mIvFlag.setImageUrl(cityItem.getFlagUrl());
        holder.mTvCountry.setText(cityItem.getCountry());
        holder.mTvCity.setText(cityItem.getCity());
        holder.mTvZip.setText(cityItem.getZip());
        holder.mTvLongitude.setText(cityItem.getLongitude());
        holder.mTvLatitude.setText(cityItem.getLatitude());
        holder.mIvCityLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        parent.getContext().getApplicationContext(), MapsActivity.class);
                intent.putExtra(MainActivity.EXTRA_LATITUDE,
                        Double.parseDouble(cityItem.getLatitude()));
                intent.putExtra(MainActivity.EXTRA_LONGITUDE,
                        Double.parseDouble(cityItem.getLongitude()));
                parent.getContext()
                        .getApplicationContext().startActivity(intent);
            }
        });

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

    static class ViewHolder {
        TextView mTvCountry;
        TextView mTvCity;
        TextView mTvZip;
        TextView mTvLongitude;
        TextView mTvLatitude;
        BezelImageView mIvFlag;
        ImageView mIvCityLocation;
    }
}