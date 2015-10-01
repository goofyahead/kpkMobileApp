package com.nxtlink.kaprika.models;

import android.util.Log;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Created by goofyahead on 3/09/15.
 */
public class Cart implements Serializable{

    private static final String TAG = Cart.class.getName();
    private LinkedHashMap<String, CartItem> itemList;
    private String nonce;
    private String fbId;

    public Cart(String nonce, String fbId) {
        this.itemList = new LinkedHashMap<>();
        this.fbId = fbId;
        this.nonce = nonce;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public void addItemToCart(Dish dishToAdd, int quantity){
        if (itemList.containsKey(dishToAdd.getId())){
            Log.d(TAG, "Cart already contains dish augmenting qty");
            CartItem current = itemList.get(dishToAdd.getId());
            current.setQuantity(current.getQuantity() + quantity);
        } else {
            itemList.put(dishToAdd.getId(), new CartItem(dishToAdd, quantity));
        }
    }

    public float getTotal (){
        float total = 0;
        for (CartItem item : itemList.values()) {
            total = total + (item.getItem().getPrice() * item.getQuantity());
        }
        return total;
    }

    public int getItemsCount() {
        int total = 0;
        for (CartItem item : itemList.values()) {
            total = total + item.getQuantity();
        }
        return total;
    }

    public int getItemCount() {
        return itemList.size();
    }

    public CartItem getItem(int position) {
        return (CartItem) itemList.values().toArray()[position];
    }

    public void deleteItem(int position) {
        itemList.remove(getItem(position).getItem().getId());
    }
}
