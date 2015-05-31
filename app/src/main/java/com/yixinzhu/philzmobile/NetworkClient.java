package com.yixinzhu.philzmobile;

import com.yixinzhu.philzmobile.Network.JSLandingPageData;
import com.yixinzhu.philzmobile.Network.JSLoginData;

import retrofit.RestAdapter;
import retrofit.http.GET;
import rx.Observable;

public class NetworkClient {
    private static NetworkClient INSTANCE = new NetworkClient();
    public static NetworkClient getInstance() {
        return INSTANCE;
    }
    private NetworkClient() {}

    final static PhilzNetworkService mNetworkService = new RestAdapter.Builder()
            .setEndpoint("https://philzapp.firebaseio.com").build().create(PhilzNetworkService.class);

    private interface PhilzNetworkService {
        @GET("/landingPageData.json")
        Observable<JSLandingPageData> getLandingPageData();

        @GET("/loginData.json")
        Observable<JSLoginData> getLoginData();
    }

    public Observable<JSLandingPageData> getLandingPageData() {
        return mNetworkService.getLandingPageData();
    }

    public Observable<JSLoginData> getLoginData() {
        return mNetworkService.getLoginData();
    }
}
