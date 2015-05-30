package com.yixinzhu.philzmobile;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public class NetworkClient {
    private static NetworkClient INSTANCE = new NetworkClient();
    public static NetworkClient getInstance() {
        return INSTANCE;
    }
    private NetworkClient() {}

    final static PhilzNetworkService mNetworkService = new RestAdapter.Builder()
            .setEndpoint("https://ajax.googleapis.com").build().create(PhilzNetworkService.class);

    //https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=fuzzy%20monkey

    private interface PhilzNetworkService {
        @GET("/ajax/services/search/images?v=1.0")
        Observable<Object> getImagesBySearchTerm(@Query("q") String search_term,
                @Query("start") int index);

    }

    public Observable<Object> getImageBySearchTerm(String search_term, int index) {
        return mNetworkService.getImagesBySearchTerm(search_term, index);
    }


}
