package com.nxtlink.kaprika.models;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by goofyahead on 3/09/15.
 */
public class Cart implements Serializable{

    private static final String TAG = Cart.class.getName();
    private LinkedHashMap<String, CartItem> cart;

    public Cart() {
        this.cart = new LinkedHashMap<>();
    }

    public Cart(LinkedHashMap<String, CartItem> dishes) {
        this.cart = dishes;
    }

    public void addItemToCart(Dish dishToAdd, int quantity){
        if (cart.containsKey(dishToAdd.getId())){
            Log.d(TAG, "Cart already contains dish augmenting qty");
            CartItem current = cart.get(dishToAdd.getId());
            current.setQuantity(current.getQuantity() + quantity);
        } else {
            cart.put(dishToAdd.getId(), new CartItem(dishToAdd, quantity));
        }
    }

    public float getTotal (){
        float total = 0;
        for (CartItem item : cart.values()) {
            total = total + (item.getItem().getPrice() * item.getQuantity());
        }
        return total;
    }

    public int getItemsCount() {
        int total = 0;
        for (CartItem item : cart.values()) {
            total = total + item.getQuantity();
        }
        return total;
    }

    public int getItemCount() {
        return cart.size();
    }

    public CartItem getItem(int position) {
        return (CartItem) cart.values().toArray()[position];
    }

    public void deleteItem(int position) {
        cart.remove(getItem(position).getItem().getId());
    }
}
