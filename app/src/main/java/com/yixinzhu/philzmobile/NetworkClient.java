package com.yixinzhu.philzmobile;

import com.yixinzhu.philzmobile.Network.JSLandingPageData;

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

    //https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=fuzzy%20monkey

    private interface PhilzNetworkService {
        @GET("/landingPageData.json")
        Observable<JSLandingPageData> getLandingPageData();

    }

    public Observable<JSLandingPageData> getLandingPageData() {
        return mNetworkService.getLandingPageData();
    }


}
