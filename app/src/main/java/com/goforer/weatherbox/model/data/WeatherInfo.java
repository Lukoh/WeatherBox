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
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherInfo extends BaseModel implements Parcelable {
    @SerializedName("channel")
    private Channel mChannel;

    public Channel getChannel() {
        return mChannel;
    }

    protected WeatherInfo(Parcel in) {
        mChannel = in.readParcelable(Channel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mChannel, flags);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeatherInfo> CREATOR
            = new Parcelable.Creator<WeatherInfo>() {
        @Override
        public WeatherInfo createFromParcel(Parcel in) {
            return new WeatherInfo(in);
        }

        @Override
        public WeatherInfo[] newArray(int size) {
            return new WeatherInfo[size];
        }
    };

    public final static class Channel implements Parcelable {
        @SerializedName("units")
        private Units mUnits;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("link")
        private String mLink;
        @SerializedName("description")
        private String mDescription;
        @SerializedName("language")
        private String mLanguage;
        @SerializedName("lastBuildDate")
        private String mLastBuildDate;
        @SerializedName("ttl")
        private String mTTL;
        @SerializedName("location")
        private Location mLocation;
        @SerializedName("wind")
        private Wind mWind;
        @SerializedName("atmosphere")
        private Atmosphere mAtmosphere;
        @SerializedName("astronomy")
        private Astronomy mAstronomy;
        @SerializedName("image")
        private Image mImage;
        @SerializedName("item")
        private Item mItem;

        public Units getUnits() {
            return mUnits;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getLink() {
            return mLink;
        }

        public String getDescription() {
            return mDescription;
        }

        public String getLanguage() {
            return mLanguage;
        }

        public String getLastBuildDate() {
            return mLastBuildDate;
        }

        public String getTTL() {
            return mTTL;
        }

        public Location getLocation() {
            return mLocation;
        }

        public Wind getWind() {
            return mWind;
        }

        public Atmosphere getAtmosphere() {
            return mAtmosphere;
        }

        public Astronomy getAstronomy() {
            return mAstronomy;
        }

        public Image getImage() {
            return mImage;
        }

        public Item getItem() {
            return mItem;
        }

        protected Channel(Parcel in) {
            mUnits = in.readParcelable(Units.class.getClassLoader());
            mTitle = in.readString();
            mLink = in.readString();
            mDescription = in.readString();
            mLanguage = in.readString();
            mLastBuildDate = in.readString();
            mTTL = in.readString();
            mLocation = in.readParcelable(Location.class.getClassLoader());
            mWind = in.readParcelable(Wind.class.getClassLoader());
            mAtmosphere = in.readParcelable(Atmosphere.class.getClassLoader());
            mAstronomy = in.readParcelable(Astronomy.class.getClassLoader());
            mImage = in.readParcelable(Image.class.getClassLoader());
            mItem = in.readParcelable(Item.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(mUnits, flags);
            dest.writeString(mTitle);
            dest.writeString(mLink);
            dest.writeString(mDescription);
            dest.writeString(mLanguage);
            dest.writeString(mLastBuildDate);
            dest.writeString(mTTL);
            dest.writeParcelable(mLocation, flags);
            dest.writeParcelable(mWind, flags);
            dest.writeParcelable(mAtmosphere, flags);
            dest.writeParcelable(mAstronomy, flags);
            dest.writeParcelable(mImage, flags);
            dest.writeParcelable(mItem, flags);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Channel> CREATOR
                = new Parcelable.Creator<Channel>() {
            @Override
            public Channel createFromParcel(Parcel in) {
                return new Channel(in);
            }

            @Override
            public Channel[] newArray(int size) {
                return new Channel[size];
            }
        };

        public final static class Units implements Parcelable {
            @SerializedName("distance")
            private String mDistance;
            @SerializedName("pressure")
            private String mPressure;
            @SerializedName("speed")
            private String mSpeed;
            @SerializedName("temperature")
            private String mTemperature;

            public String getDistance() {
                return mDistance;
            }

            public String getPressure() {
                return mPressure;
            }

            public String getSpeed() {
                return mSpeed;
            }

            public String getTemperature() {
                return mTemperature;
            }

            protected Units(Parcel in) {
                mDistance = in.readString();
                mPressure = in.readString();
                mSpeed = in.readString();
                mTemperature = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mDistance);
                dest.writeString(mPressure);
                dest.writeString(mSpeed);
                dest.writeString(mTemperature);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Units> CREATOR
                    = new Parcelable.Creator<Units>() {
                @Override
                public Units createFromParcel(Parcel in) {
                    return new Units(in);
                }

                @Override
                public Units[] newArray(int size) {
                    return new Units[size];
                }
            };
        }

        public final static class Location implements Parcelable {
            @SerializedName("city")
            private String mCity;
            @SerializedName("country")
            private String mCountry;
            @SerializedName("region")
            private String mRegion;

            public String getCity() {
                return mCity;
            }

            public String getCountry() {
                return mCountry;
            }

            public String getRegion() {
                return mRegion;
            }

            protected Location(Parcel in) {
                mCity = in.readString();
                mCountry = in.readString();
                mRegion = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mCity);
                dest.writeString(mCountry);
                dest.writeString(mRegion);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Location> CREATOR
                    = new Parcelable.Creator<Location>() {
                @Override
                public Location createFromParcel(Parcel in) {
                    return new Location(in);
                }

                @Override
                public Location[] newArray(int size) {
                    return new Location[size];
                }
            };
        }

        public final static class Wind implements Parcelable {
            @SerializedName("chill")
            private String mChill;
            @SerializedName("direction")
            private String mDirection;
            @SerializedName("speed")
            private String mSpeed;

            public String getChill() {
                return mChill;
            }

            public String getDirection() {
                return mDirection;
            }

            public String getSpeed() {
                return mSpeed;
            }

            protected Wind(Parcel in) {
                mChill = in.readString();
                mDirection = in.readString();
                mSpeed = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mChill);
                dest.writeString(mDirection);
                dest.writeString(mSpeed);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Wind> CREATOR
                    = new Parcelable.Creator<Wind>() {
                @Override
                public Wind createFromParcel(Parcel in) {
                    return new Wind(in);
                }

                @Override
                public Wind[] newArray(int size) {
                    return new Wind[size];
                }
            };
        }

        public final static class Atmosphere implements Parcelable {
            @SerializedName("humidity")
            private String mHumidity;
            @SerializedName("pressure")
            private String mPressure;
            @SerializedName("rising")
            private String mRising;
            @SerializedName("visibility")
            private String mVisibility;

            public String getHumidity() {
                return mHumidity;
            }

            public String getPressure() {
                return mPressure;
            }

            public String getRising() {
                return mRising;
            }

            public String getVisibility() {
                return mVisibility;
            }

            protected Atmosphere(Parcel in) {
                mHumidity = in.readString();
                mPressure = in.readString();
                mRising = in.readString();
                mVisibility = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mHumidity);
                dest.writeString(mPressure);
                dest.writeString(mRising);
                dest.writeString(mVisibility);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Atmosphere> CREATOR
                    = new Parcelable.Creator<Atmosphere>() {
                @Override
                public Atmosphere createFromParcel(Parcel in) {
                    return new Atmosphere(in);
                }

                @Override
                public Atmosphere[] newArray(int size) {
                    return new Atmosphere[size];
                }
            };
        }

        public final static class Astronomy implements Parcelable {
            @SerializedName("sunrise")
            private String mSunriseTime;
            @SerializedName("sunset")
            private String mSunsetTime;

            public String getSunriseTime() {
                return mSunriseTime;
            }

            public String getmSunsetTime() {
                return mSunsetTime;
            }

            protected Astronomy(Parcel in) {
                mSunriseTime = in.readString();
                mSunsetTime = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mSunriseTime);
                dest.writeString(mSunsetTime);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Astronomy> CREATOR
                    = new Parcelable.Creator<Astronomy>() {
                @Override
                public Astronomy createFromParcel(Parcel in) {
                    return new Astronomy(in);
                }

                @Override
                public Astronomy[] newArray(int size) {
                    return new Astronomy[size];
                }
            };
        }

        public final static class Image implements Parcelable {
            @SerializedName("title")
            private String mTitle;
            @SerializedName("width")
            private String mWidth;
            @SerializedName("height")
            private String mHeight;
            @SerializedName("link")
            private String mLink;
            @SerializedName("url")
            private String mUrl;

            public String getTitle() {
                return mTitle;
            }

            public String getWidth() {
                return mWidth;
            }

            public String getHeight() {
                return mHeight;
            }

            public String getLink() {
                return mLink;
            }

            public String getUrl() {
                return mUrl;
            }

            protected Image(Parcel in) {
                mTitle = in.readString();
                mWidth = in.readString();
                mHeight = in.readString();
                mLink = in.readString();
                mUrl = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mTitle);
                dest.writeString(mWidth);
                dest.writeString(mHeight);
                dest.writeString(mLink);
                dest.writeString(mUrl);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Image> CREATOR
                    = new Parcelable.Creator<Image>() {
                @Override
                public Image createFromParcel(Parcel in) {
                    return new Image(in);
                }

                @Override
                public Image[] newArray(int size) {
                    return new Image[size];
                }
            };
        }

        public final static class Item implements Parcelable {
            @SerializedName("title")
            private String mTitle;
            @SerializedName("lat")
            private String mLatitude;
            @SerializedName("long")
            private String mLongitude;
            @SerializedName("link")
            private String mLink;
            @SerializedName("pubDate")
            private String mPublishDate;
            @SerializedName("condition")
            private Condition mCondition;
            @SerializedName("forecast")
            private List<Forecast> mForecasts;
            @SerializedName("description")
            private String mDescription;

            public String getTitle() {
                return mTitle;
            }

            public String getLatitude() {
                return mLatitude;
            }

            public String getLongitude() {
                return mLongitude;
            }

            public String getLink() {
                return mLink;
            }

            public String getPublishDate() {
                return mPublishDate;
            }

            public Condition getCondition() {
                return mCondition;
            }

            public List<Forecast> getForecasts() {
                return mForecasts;
            }

            public String getDescription() {
                return mDescription;
            }

            protected Item(Parcel in) {
                mTitle = in.readString();
                mLatitude = in.readString();
                mLongitude = in.readString();
                mLink = in.readString();
                mPublishDate = in.readString();
                mCondition = in.readParcelable(Condition.class.getClassLoader());
                mForecasts = new ArrayList<>();
                in.readTypedList(mForecasts, Forecast.CREATOR);
                mDescription = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mTitle);
                dest.writeString(mLatitude);
                dest.writeString(mLongitude);
                dest.writeString(mLink);
                dest.writeString(mPublishDate);
                dest.writeParcelable(mCondition, flags);
                dest.writeTypedList(mForecasts);
                dest.writeString(mDescription);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Item> CREATOR
                    = new Parcelable.Creator<Item>() {
                @Override
                public Item createFromParcel(Parcel in) {
                    return new Item(in);
                }

                @Override
                public Item[] newArray(int size) {
                    return new Item[size];
                }
            };

            public final static class Condition implements Parcelable {
                @SerializedName("code")
                private String mCode;
                @SerializedName("date")
                private String mDate;
                @SerializedName("temp")
                private String mTemp;
                @SerializedName("text")
                private String mText;

                public String getCode() {
                    return mCode;
                }

                public String getDate() {
                    return mDate;
                }

                public String getTemp() {
                    return mTemp;
                }

                public String getText() {
                    return mText;
                }

                protected Condition(Parcel in) {
                    mCode = in.readString();
                    mDate = in.readString();
                    mTemp = in.readString();
                    mText = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(mCode);
                    dest.writeString(mDate);
                    dest.writeString(mTemp);
                    dest.writeString(mText);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Condition> CREATOR
                        = new Parcelable.Creator<Condition>() {
                    @Override
                    public Condition createFromParcel(Parcel in) {
                        return new Condition(in);
                    }

                    @Override
                    public Condition[] newArray(int size) {
                        return new Condition[size];
                    }
                };
            }

            public final static class Forecast implements Parcelable {
                @SerializedName("code")
                private String mCode;
                @SerializedName("date")
                private String mDate;
                @SerializedName("day")
                private String mDay;
                @SerializedName("high")
                private String mHigh;
                @SerializedName("low")
                private String mLow;
                @SerializedName("text")
                private String mCondition;

                public String getCode() {
                    return mCode;
                }

                public String getDate() {
                    return mDate;
                }

                public String getDay() {
                    return mDay;
                }

                public String getHighTemperature() {
                    return mHigh;
                }

                public String getLowTemerature() {
                    return mLow;
                }

                public String getCondition() {
                    return mCondition;
                }

                protected Forecast(Parcel in) {
                    mCode = in.readString();
                    mDate = in.readString();
                    mDay = in.readString();
                    mHigh = in.readString();
                    mLow = in.readString();
                    mCondition = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(mCode);
                    dest.writeString(mDate);
                    dest.writeString(mDay);
                    dest.writeString(mHigh);
                    dest.writeString(mLow);
                    dest.writeString(mCondition);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Forecast> CREATOR
                        = new Parcelable.Creator<Forecast>() {
                    @Override
                    public Forecast createFromParcel(Parcel in) {
                        return new Forecast(in);
                    }

                    @Override
                    public Forecast[] newArray(int size) {
                        return new Forecast[size];
                    }
                };
            }
        }
    }
}
