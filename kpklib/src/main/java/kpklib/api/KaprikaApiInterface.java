package kpklib.api;


import java.util.LinkedList;
import java.util.List;

import kpklib.models.AccessToken;
import kpklib.models.Cart;
import kpklib.models.Category;
import kpklib.models.Dish;
import kpklib.models.OrderFromKpk;
import kpklib.models.PrintableOrder;
import kpklib.models.UserInfo;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * Created by goofyahead on 9/8/14.
 */
public interface KaprikaApiInterface {

    @GET("/api/clientFavOrders/fb/{fbId}")
    void getClientFavOrders(@Path("fbId") String fbId, Callback<LinkedList<Cart>> orders);

    @GET("/api/clientHistoryOrders/fb/{fbId}")
    void getClientHistoryOrders(@Path("fbId") String fbId, Callback<LinkedList<Cart>> orders);

    @GET("/api/clientActiveOrders/fb/{fbId}")
    void getClientActiveOrders(@Path("fbId") String fbId, Callback<LinkedList<Cart>> orders);

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

    @GET("/api/clients/phone/{phone}")
    void getClientByPhone(@Path("phone") String phone, Callback<UserInfo> user);

    @POST("/api/orderFromKaprika")
    void postOrderFromKpk(@Body OrderFromKpk order, Callback<String> response);
}
