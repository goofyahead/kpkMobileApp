package com.nxtlink.kaprika.models;

/**
 * Created by goofyahead on 9/09/15.
 */
public class CartAndNonce {
    private Cart cart;
    private String nonce;

    public CartAndNonce(Cart cart, String nonce) {
        this.cart = cart;
        this.nonce = nonce;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
