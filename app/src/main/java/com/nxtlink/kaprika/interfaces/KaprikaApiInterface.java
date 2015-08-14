package com.nxtlink.kaprika.interfaces;

import com.nxtlink.kaprika.models.Category;
import com.nxtlink.kaprika.models.Dish;

import java.util.List;


import retrofit.Callback;
import retrofit.http.GET;


/**
 * Created by goofyahead on 9/8/14.
 */
public interface KaprikaApiInterface {
    @GET("/api/currentmenu")
    void getCurrentMenu(Callback<List<Dish>> dishes);

    @GET("/api/categories")
    void getCategories(Callback<List<Category>> categories);
}
