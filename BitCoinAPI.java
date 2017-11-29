package com.example.android.itsover9000;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import com.example.android.itsover9000.model.Feed;

/**
 * Interface to retrieve the JSON
 */

public interface BitCoinAPI {

    @Headers("Content-Type: application/json")

    /*
    Getting all of the BitCoin information.
    The weight of the info is rather small so there's no point
    Getting it in parts.
     */
    @GET("charts/market-price?timespan=all")
    Call<Feed> getData();
}
