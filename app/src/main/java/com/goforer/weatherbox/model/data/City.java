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

package com.goforer.weatherbox.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.goforer.base.model.BaseModel;

public class City extends BaseModel implements Parcelable {
    private String mCountry;
    private String mZip;
    private String mCity;
    private long mDistance;
    private String mLongitude;
    private String mLatitude;
    private String mDescription;
    private String mFlagUrl;

    public City() {

    }

    public String getCountry() {
        return this.mCountry;
    }

    public String getZip() {
        return this.mZip;
    }

    public String getCity() {
        return this.mCity;
    }

    public String getLongitude() {
        return this.mLongitude;
    }

    public String getLatitude() {
        return this.mLatitude;
    }

    public String getDescrption() {
        return this.mDescription;
    }

    public String getFlagUrl() {
        return this.mFlagUrl;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public void setZip(String zip) {
        this.mZip = zip;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public void setLongitude(String longitude) {
        this.mLongitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.mLatitude = latitude;
    }

    public void setDescrption(String descrption) {
        this.mDescription = descrption;
    }

    public void setFlagUrl(String flagUrl) {
        this.mFlagUrl = flagUrl;
    }

    protected City(Parcel in) {
        mCountry = in.readString();
        mZip = in.readString();
        mCity = in.readString();
        mDistance = in.readLong();
        mLongitude = in.readString();
        mLatitude = in.readString();
        mDescription = in.readString();
        mFlagUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCountry);
        dest.writeString(mZip);
        dest.writeString(mCity);
        dest.writeLong(mDistance);
        dest.writeString(mLongitude);
        dest.writeString(mLatitude);
        dest.writeString(mDescription);
        dest.writeString(mFlagUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
