package com.nxtlink.kaprika.models;

import java.io.Serializable;

/**
 * Created by goofyahead on 3/09/15.
 */
public class CartItem implements Serializable{
    private Dish item;
    private int quantity;

    public CartItem(Dish item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Dish getItem() {
        return item;
    }

    public void setItem(Dish item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
