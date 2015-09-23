package com.nxtlink.kaprika.api;

import com.nxtlink.kaprika.models.AccessToken;
import com.nxtlink.kaprika.models.CartAndNonce;
import com.nxtlink.kaprika.models.Category;
import com.nxtlink.kaprika.models.Dish;
import com.nxtlink.kaprika.models.UserInfo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;


/**
 * Created by goofyahead on 9/8/14.
 */
public interface KaprikaApiInterface {
    @GET("/api/currentmenu")
    void getCurrentMenu(Callback<List<Dish>> dishes);

    @GET("/api/categories")
    void getCategories(Callback<List<Category>> categories);

    @GET("/api/lastUpdate")
    void getLastUpdate(Callback<Integer> updateTime);

    @GET("/payments/client_token")
    void getTokenClient (Callback<AccessToken> token);

    @POST("/payments/payment-methods")
    void notifyCartTransaction(@Body CartAndNonce cart, Callback<String> response);

    @POST("/api/clients")
    void postUserInfo(@Body UserInfo userInfo, Callback<String> response);
}
