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

package com.goforer.weatherbox.web.communicator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.goforer.base.model.event.ResponseEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public enum RequestClient {
    INSTANCE;

    private static final long READ_TIME_OUT = 5;
    private static final long WRITE_TIME_OUT = 5;
    private static final long CONNECT_TIME_OUT = 5;

    private Context mContext;
    private RequestMethod mRequestor;

    private String mRawResponseBody;

    public RequestMethod getRequestMethod(Context context) {
        mContext = context;

        if (mRequestor == null) {
            final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);

                    mRawResponseBody = response.body().string();

                    return response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(),
                                    mRawResponseBody)).build();
                }
            });

            OkHttpClient client = httpClient.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://query.yahooapis.com")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mRequestor = retrofit.create(RequestMethod.class);
        }

        return mRequestor;
    }

    /**
     * Communicates responses from Server or offline requests.
     * One and only one method will be invoked in response to a given request.
     */
    static public class RequestCallback implements Callback<ResponseClient> {
        private ResponseEvent mEvent;

        public RequestCallback(ResponseEvent event) {
            mEvent = event;
        }

        @Override
        public void onResponse(Call<ResponseClient> call,
                               retrofit2.Response<ResponseClient> response) {
            if (mEvent != null) {
                mEvent.setResponseClient(response.body());
                mEvent.parseInResponse();
                EventBus.getDefault().post(mEvent);
            }
        }

        @Override
        public void onFailure(Call<ResponseClient> call, Throwable t) {

            if (!RequestClient.INSTANCE.isOnline()) {
                //: Todo
            }

            if (mEvent != null) {
                mEvent.setResponseClient(new ResponseClient());

                EventBus.getDefault().post(mEvent);
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            if (connectivityManager != null) {
                @SuppressWarnings("deprecation")
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public interface RequestMethod {
        @GET("v1/public/yql")
        Call<ResponseClient> getWeather(
                @Query("q") String query,
                @Query("format") String json);
    }
}

