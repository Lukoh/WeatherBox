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

package com.goforer.base.model.event;

import com.goforer.weatherbox.web.communicator.ResponseClient;

import okhttp3.ResponseBody;

public class ResponseEvent {
    public static final int RESPONSE_TYPE_WEATHER_INFO = 0;
    public static final int RESPONSE_TYPE_PLACE_INFO = 1;

    public static final int RESPONSE_TYPE_CLIENT_ERROR = 1000;
    public static final int RESPONSE_TYPE_CLIENT_SUCCESS = 1001;

    protected int mResponseClientType;
    protected int mResponseType;
    protected ResponseClient mResponseClient;
    protected ResponseBody mResponseErrorClient;

    protected String mTag;

    public boolean isMine(String tag){
        return tag == null || tag.equals(mTag);
    }

    public ResponseClient getResponseClient() { return mResponseClient; }

    public ResponseBody getResponseErrorClient() {
        return mResponseErrorClient;
    }

    public String getTag() { return mTag; }

    public int getResponseType() {
        return mResponseType;
    }

    public int getResponseClientType() {
        return mResponseClientType;
    }

    public void setResponseType(int type) {
        mResponseType = type;
    }

    public void setResponseClientType(int type) {
        mResponseClientType = type;
    }

    public void setResponseClient(ResponseClient responseClient) { mResponseClient = responseClient; }

    public void setResponseErrorClient(ResponseBody responseBody) {
        mResponseErrorClient = responseBody;
    }

    public void setTag(String tag) { mTag = tag; }
}
