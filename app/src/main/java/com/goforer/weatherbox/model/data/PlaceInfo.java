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

public class PlaceInfo extends BaseModel implements Parcelable {
    @SerializedName("place")
    private Place mPlace;
    @SerializedName("name")
    private String mName;
    @SerializedName("country")
    private Country mCountry;
    @SerializedName("admin1")
    private Admin1 mAdmin1;
    @SerializedName("admin2")
    private Admin2 mAdmin2;
    @SerializedName("admin3")
    private Admin3 mAdmin3;
    @SerializedName("locality1")
    private Locality1 mLocality1;
    @SerializedName("locality2")
    private Locality2 mLocality2;
    @SerializedName("centroid")
    private Centroid mCentroid;
    @SerializedName("boundingBox")
    private BoundingBox mBoundingBox;
    @SerializedName("areaRank")
    private String mAreaRank;
    @SerializedName("popRank")
    private String mPopRank;
    @SerializedName("timezone")
    private Timezone mTimezone;

    public Place getPlace() {
        return mPlace;
    }

    public String getName() {
        return mName;
    }

    public Country getCountry() {
        return mCountry;
    }

    public Admin1 getAdmin1() {
        return mAdmin1;
    }

    public Admin2 getAdmin2() {
        return mAdmin2;
    }

    public Admin3 getAdmin3() {
        return mAdmin3;
    }

    public Locality1 getLocality1() {
        return mLocality1;
    }

    public Locality2 getLocality2() {
        return mLocality2;
    }

    public Centroid getCentroid() {
        return mCentroid;
    }

    public BoundingBox getBoundingBox() {
        return mBoundingBox;
    }

    public String getAreaRank() {
        return mAreaRank;
    }

    public String getPopRank() {
        return mPopRank;
    }

    public Timezone getTimezone() {
        return mTimezone;
    }

    protected PlaceInfo(Parcel in) {
        mPlace = in.readParcelable(Place.class.getClassLoader());
        mName = in.readString();
        mCountry = in.readParcelable(Country.class.getClassLoader());
        mAdmin1 = in.readParcelable(Admin1.class.getClassLoader());
        mAdmin2 = in.readParcelable(Admin2.class.getClassLoader());
        mAdmin3 = in.readParcelable(Admin3.class.getClassLoader());
        mLocality1 = in.readParcelable(Locality1.class.getClassLoader());
        mLocality2 = in.readParcelable(Locality2.class.getClassLoader());
        mCentroid = in.readParcelable(Centroid.class.getClassLoader());
        mBoundingBox = in.readParcelable(BoundingBox.class.getClassLoader());
        mAreaRank = in.readString();
        mPopRank = in.readString();
        mTimezone = in.readParcelable(Timezone.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mPlace, flags);
        dest.writeString(mName);
        dest.writeParcelable(mCountry, flags);
        dest.writeParcelable(mAdmin1, flags);
        dest.writeParcelable(mAdmin2, flags);
        dest.writeParcelable(mAdmin3, flags);
        dest.writeParcelable(mLocality1, flags);
        dest.writeParcelable(mLocality2, flags);
        dest.writeParcelable(mCentroid, flags);
        dest.writeParcelable(mBoundingBox, flags);
        dest.writeString(mAreaRank);
        dest.writeString(mPopRank);
        dest.writeParcelable(mTimezone, flags);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PlaceInfo> CREATOR
            = new Parcelable.Creator<PlaceInfo>() {
        @Override
        public PlaceInfo createFromParcel(Parcel in) {
            return new PlaceInfo(in);
        }

        @Override
        public PlaceInfo[] newArray(int size) {
            return new PlaceInfo[size];
        }
    };

    public final static class Place implements Parcelable {
        @SerializedName("lang")
        private String mLanguage;
        @SerializedName("xmlns")
        private String mXmlns;
        @SerializedName("yahoo")
        private String mYahoo;
        @SerializedName("uri")
        private String mUri;
        @SerializedName("woeid")
        private String mWoeid;
        @SerializedName("placeTypeName")
        private PlaceTypeName mPlaceTypeName;

        public String getLanguage() {
            return mLanguage;
        }

        public String getXmlns() {
            return mXmlns;
        }

        public String getYahoo() {
            return mYahoo;
        }

        public String getUri() {
            return mUri;
        }

        public String getWoeid() {
            return mWoeid;
        }

        public PlaceTypeName getPlaceTypeName() {
            return mPlaceTypeName;
        }

        protected Place(Parcel in) {
            mLanguage = in.readString();
            mXmlns = in.readString();
            mYahoo = in.readString();
            mLanguage = in.readString();
            mUri = in.readString();
            mWoeid = in.readString();
            mPlaceTypeName = in.readParcelable(PlaceTypeName.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mLanguage);
            dest.writeString(mXmlns);
            dest.writeString(mYahoo);
            dest.writeString(mLanguage);
            dest.writeString(mUri);
            dest.writeString(mWoeid);
            dest.writeParcelable(mPlaceTypeName, flags);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Place> CREATOR
                = new Parcelable.Creator<Place>() {
            @Override
            public Place createFromParcel(Parcel in) {
                return new Place(in);
            }

            @Override
            public Place[] newArray(int size) {
                return new Place[size];
            }
        };

        public final static class PlaceTypeName implements Parcelable {
            @SerializedName("code")
            private String mCode;
            @SerializedName("content")
            private String mContent;

            public String getCode() {
                return mCode;
            }

            public String getContent() {
                return mContent;
            }

            protected PlaceTypeName(Parcel in) {
                mCode = in.readString();
                mContent = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mCode);
                dest.writeString(mContent);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<PlaceTypeName> CREATOR
                    = new Parcelable.Creator<PlaceTypeName>() {
                @Override
                public PlaceTypeName createFromParcel(Parcel in) {
                    return new PlaceTypeName(in);
                }

                @Override
                public PlaceTypeName[] newArray(int size) {
                    return new PlaceTypeName[size];
                }
            };
        }
    }

    public final static class Country implements Parcelable {
        @SerializedName("code")
        private String mCode;
        @SerializedName("type")
        private String mType;
        @SerializedName("woeid")
        private String mWoeid;
        @SerializedName("content")
        private String mContent;

        public String getCode() {
            return mCode;
        }

        public String getType() {
            return mType;
        }

        public String getWoeid() {
            return mWoeid;
        }

        public String getContent() {
            return mContent;
        }

        protected Country(Parcel in) {
            mCode = in.readString();
            mType = in.readString();
            mWoeid = in.readString();
            mContent = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mCode);
            dest.writeString(mType);
            dest.writeString(mWoeid);
            dest.writeString(mContent);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Country> CREATOR
                = new Parcelable.Creator<Country>() {
            @Override
            public Country createFromParcel(Parcel in) {
                return new Country(in);
            }

            @Override
            public Country[] newArray(int size) {
                return new Country[size];
            }
        };
    }

    public final static class Admin1 implements Parcelable {
        @SerializedName("code")
        private String mCode;
        @SerializedName("type")
        private String mType;
        @SerializedName("woeid")
        private String mWoeid;
        @SerializedName("content")
        private String mContent;

        public String getCode() {
            return mCode;
        }

        public String getType() {
            return mType;
        }

        public String getWoeid() {
            return mWoeid;
        }

        public String getContent() {
            return mContent;
        }

        protected Admin1(Parcel in) {
            mCode = in.readString();
            mType = in.readString();
            mWoeid = in.readString();
            mContent = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mCode);
            dest.writeString(mType);
            dest.writeString(mWoeid);
            dest.writeString(mContent);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Admin1> CREATOR
                = new Parcelable.Creator<Admin1>() {
            @Override
            public Admin1 createFromParcel(Parcel in) {
                return new Admin1(in);
            }

            @Override
            public Admin1[] newArray(int size) {
                return new Admin1[size];
            }
        };
    }

    public final static class Admin2 implements Parcelable {
        @SerializedName("code")
        private String mCode;
        @SerializedName("type")
        private String mType;
        @SerializedName("woeid")
        private String mWoeid;
        @SerializedName("content")
        private String mContent;

        public String getCode() {
            return mCode;
        }

        public String getType() {
            return mType;
        }

        public String getWoeid() {
            return mWoeid;
        }

        public String getContent() {
            return mContent;
        }

        protected Admin2(Parcel in) {
            mCode = in.readString();
            mType = in.readString();
            mWoeid = in.readString();
            mContent = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mCode);
            dest.writeString(mType);
            dest.writeString(mWoeid);
            dest.writeString(mContent);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Admin2> CREATOR
                = new Parcelable.Creator<Admin2>() {
            @Override
            public Admin2 createFromParcel(Parcel in) {
                return new Admin2(in);
            }

            @Override
            public Admin2[] newArray(int size) {
                return new Admin2[size];
            }
        };
    }

    public final static class Admin3 implements Parcelable {
        @SerializedName("code")
        private String mCode;
        @SerializedName("type")
        private String mType;
        @SerializedName("woeid")
        private String mWoeid;
        @SerializedName("content")
        private String mContent;

        public String getCode() {
            return mCode;
        }

        public String getType() {
            return mType;
        }

        public String getWoeid() {
            return mWoeid;
        }

        public String getContent() {
            return mContent;
        }

        protected Admin3(Parcel in) {
            mCode = in.readString();
            mType = in.readString();
            mWoeid = in.readString();
            mContent = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mCode);
            dest.writeString(mType);
            dest.writeString(mWoeid);
            dest.writeString(mContent);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Admin3> CREATOR
                = new Parcelable.Creator<Admin3>() {
            @Override
            public Admin3 createFromParcel(Parcel in) {
                return new Admin3(in);
            }

            @Override
            public Admin3[] newArray(int size) {
                return new Admin3[size];
            }
        };
    }

    public final static class Locality1 implements Parcelable {
        @SerializedName("type")
        private String mType;
        @SerializedName("woeid")
        private String mWoeid;
        @SerializedName("content")
        private String mContent;

        public String getType() {
            return mType;
        }

        public String getWoeid() {
            return mWoeid;
        }

        public String getContent() {
            return mContent;
        }

        protected Locality1(Parcel in) {
            mType = in.readString();
            mWoeid = in.readString();
            mContent = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mType);
            dest.writeString(mWoeid);
            dest.writeString(mContent);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Locality1> CREATOR
                = new Parcelable.Creator<Locality1>() {
            @Override
            public Locality1 createFromParcel(Parcel in) {
                return new Locality1(in);
            }

            @Override
            public Locality1[] newArray(int size) {
                return new Locality1[size];
            }
        };
    }

    public final static class Locality2 implements Parcelable {
        @SerializedName("type")
        private String mType;
        @SerializedName("woeid")
        private String mWoeid;
        @SerializedName("content")
        private String mContent;

        public String getType() {
            return mType;
        }

        public String getWoeid() {
            return mWoeid;
        }

        public String getContent() {
            return mContent;
        }

        protected Locality2(Parcel in) {
            mType = in.readString();
            mWoeid = in.readString();
            mContent = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mType);
            dest.writeString(mWoeid);
            dest.writeString(mContent);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Locality2> CREATOR
                = new Parcelable.Creator<Locality2>() {
            @Override
            public Locality2 createFromParcel(Parcel in) {
                return new Locality2(in);
            }

            @Override
            public Locality2[] newArray(int size) {
                return new Locality2[size];
            }
        };
    }

    public final static class Centroid implements Parcelable {
        @SerializedName("latitude")
        private String mLatitude;
        @SerializedName("longitude")
        private String mLongitude;

        public String getLatitude() {
            return mLatitude;
        }

        public String getLongitude() {
            return mLongitude;
        }


        protected Centroid(Parcel in) {
            mLatitude = in.readString();
            mLongitude = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mLatitude);
            dest.writeString(mLongitude);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Centroid> CREATOR
                = new Parcelable.Creator<Centroid>() {
            @Override
            public Centroid createFromParcel(Parcel in) {
                return new Centroid(in);
            }

            @Override
            public Centroid[] newArray(int size) {
                return new Centroid[size];
            }
        };
    }

    public final static class BoundingBox implements Parcelable {
        @SerializedName("southWest")
        private SouthWest mSouthWest;
        @SerializedName("northEast")
        private NorthEast mNorthEast;

        public SouthWest getSouthWest() {
            return mSouthWest;
        }

        public NorthEast getNorthEast() {
            return mNorthEast;
        }

        protected BoundingBox(Parcel in) {
            mSouthWest = in.readParcelable(SouthWest.class.getClassLoader());
            mNorthEast = in.readParcelable(NorthEast.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(mSouthWest, flags);
            dest.writeParcelable(mNorthEast, flags);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<BoundingBox> CREATOR
                = new Parcelable.Creator<BoundingBox>() {
            @Override
            public BoundingBox createFromParcel(Parcel in) {
                return new BoundingBox(in);
            }

            @Override
            public BoundingBox[] newArray(int size) {
                return new BoundingBox[size];
            }
        };

        public final static class SouthWest implements Parcelable {
            @SerializedName("latitude")
            private String mLatitude;
            @SerializedName("longitude")
            private String mLongitude;

            public String getLatitude() {
                return mLatitude;
            }

            public String getLongitude() {
                return mLongitude;
            }


            protected SouthWest(Parcel in) {
                mLatitude = in.readString();
                mLongitude = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mLatitude);
                dest.writeString(mLongitude);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<SouthWest> CREATOR
                    = new Parcelable.Creator<SouthWest>() {
                @Override
                public SouthWest createFromParcel(Parcel in) {
                    return new SouthWest(in);
                }

                @Override
                public SouthWest[] newArray(int size) {
                    return new SouthWest[size];
                }
            };
        }

        public final static class NorthEast implements Parcelable {
            @SerializedName("latitude")
            private String mLatitude;
            @SerializedName("longitude")
            private String mLongitude;

            public String getLatitude() {
                return mLatitude;
            }

            public String getLongitude() {
                return mLongitude;
            }


            protected NorthEast(Parcel in) {
                mLatitude = in.readString();
                mLongitude = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mLatitude);
                dest.writeString(mLongitude);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<NorthEast> CREATOR
                    = new Parcelable.Creator<NorthEast>() {
                @Override
                public NorthEast createFromParcel(Parcel in) {
                    return new NorthEast(in);
                }

                @Override
                public NorthEast[] newArray(int size) {
                    return new NorthEast[size];
                }
            };
        }
    }

    public final static class Timezone implements Parcelable {
        @SerializedName("type")
        private String mType;
        @SerializedName("woeid")
        private String mWoeid;
        @SerializedName("content")
        private String mContent;

        public String getType() {
            return mType;
        }

        public String getWoeid() {
            return mWoeid;
        }

        public String getContent() {
            return mContent;
        }

        protected Timezone(Parcel in) {
            mType = in.readString();
            mWoeid = in.readString();
            mContent = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mType);
            dest.writeString(mWoeid);
            dest.writeString(mContent);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Timezone> CREATOR
                = new Parcelable.Creator<Timezone>() {
            @Override
            public Timezone createFromParcel(Parcel in) {
                return new Timezone(in);
            }

            @Override
            public Timezone[] newArray(int size) {
                return new Timezone[size];
            }
        };
    }
}