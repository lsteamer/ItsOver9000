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
     * Getting all of the BitCoin information.
     * Mayhaps not all the info is used, but like this
     * only one call is needed.
     */
    @GET("charts/market-price?timespan=all")
    Call<Feed> getData();
}
