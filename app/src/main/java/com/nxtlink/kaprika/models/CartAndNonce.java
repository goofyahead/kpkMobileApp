package com.nxtlink.kaprika.models;

/**
 * Created by goofyahead on 9/09/15.
 */
public class CartAndNonce {
    private Cart cart;
    private String nonce;
    private String fbId;

    public CartAndNonce(Cart cart, String nonce, String fbId) {
        this.cart = cart;
        this.nonce = nonce;
        this.fbId = fbId;
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

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }
}
