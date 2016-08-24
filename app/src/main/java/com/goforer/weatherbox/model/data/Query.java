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
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class Query extends BaseModel implements Parcelable {
    @SerializedName("count")
    private int mCount;
    @SerializedName("created")
    private String mCreatedTime;
    @SerializedName("lang")
    private String mLanguage;
    @SerializedName("results")
    private JsonElement mResults;

    public Query() {

    }

    public int getCount() {
        return mCount;
    }

    public String getCreatedTime() {
        return mCreatedTime;
    }

    public JsonElement getResults() {
        return mResults;
    }

    public String getLanguage() {
        return mLanguage;
    }

    protected Query(Parcel in) {
        mCount = in.readInt();
        mCreatedTime = in.readString();
        mLanguage = in.readString();
        mResults = in.readParcelable(JsonElement.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCount);
        dest.writeString(mCreatedTime);
        dest.writeString(mLanguage);
        dest.writeParcelable((Parcelable) mResults, flags);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator<Query>() {
        @Override
        public Query createFromParcel(Parcel in) {
            return new Query(in);
        }

        @Override
        public Query[] newArray(int size) {
            return new Query[size];
        }
    };
}
