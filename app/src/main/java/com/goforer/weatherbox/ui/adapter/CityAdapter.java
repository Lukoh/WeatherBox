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
import com.goforer.ui.view.BezelImageView;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Context context = parent.getContext();
        City cityItem = mCityItem.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_city_info_item, parent, false);
            holder = new ViewHolder();
            holder.tvCountry = (TextView) convertView.findViewById(R.id.tv_country) ;
            holder.tvCity = (TextView) convertView.findViewById(R.id.tv_city) ;
            holder.tvZip = (TextView) convertView.findViewById(R.id.tv_zip) ;
            holder.tvLongitude= (TextView) convertView.findViewById(R.id.tv_longitude) ;
            holder.tvLatitude = (TextView) convertView.findViewById(R.id.tv_latitude) ;
            holder.ivFlag = (BezelImageView) convertView.findViewById(R.id.iv_flag);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder != null) {
            holder.tvCountry.setText(cityItem.getCountry());
            holder.tvCity.setText(cityItem.getCity());
            holder.tvZip.setText(cityItem.getZip());
            holder.tvLongitude.setText(cityItem.getLongitude());
            holder.tvLatitude.setText(cityItem.getLatitude());
            holder.ivFlag.setImageUrl(cityItem.getFlagUrl());
        }

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
        TextView tvCountry;
        TextView tvCity;
        TextView tvZip;
        TextView tvLongitude;
        TextView tvLatitude;
        BezelImageView ivFlag;
    }
}