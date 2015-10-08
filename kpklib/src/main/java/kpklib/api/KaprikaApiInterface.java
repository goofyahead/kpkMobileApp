package kpklib.api;


import java.util.List;

import kpklib.models.AccessToken;
import kpklib.models.Cart;
import kpklib.models.Category;
import kpklib.models.Dish;
import kpklib.models.PrintableOrder;
import kpklib.models.UserInfo;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;


/**
 * Created by goofyahead on 9/8/14.
 */
public interface KaprikaApiInterface {

    @GET("/api/ordersToPrint")
    void getOrdersToPrint(Callback<PrintableOrder> order);

    @GET("/api/currentmenu")
    void getCurrentMenu(Callback<List<Dish>> dishes);

    @GET("/api/categories")
    void getCategories(Callback<List<Category>> categories);

    @GET("/api/lastUpdate")
    void getLastUpdate(Callback<Integer> updateTime);

    @GET("/payments/client_token")
    void getTokenClient(Callback<AccessToken> token);

    @POST("/payments/payment-methods")
    void notifyCartTransaction(@Body Cart items, Callback<String> response);

    @POST("/api/clients")
    void postUserInfo(@Body UserInfo userInfo, Callback<String> response);
}
