package kpklib.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by goofyahead on 10/1/15.
 */

public class PrintableOrder implements Serializable{
    private LinkedList<CartItem> itemList;
    private UserInfo address;
    private String timestamp;
    private String amount;
    private String deliveryOption;
    private String nonce;

    public String getNonce() {
        return nonce;
    }

    public PrintableOrder(){}

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public LinkedList<CartItem> getItemList() {
        return itemList;
    }

    public void setItemList(LinkedList<CartItem> itemList) {
        this.itemList = itemList;
    }

    public UserInfo getAddress() {
        return address;
    }

    public void setAddress(UserInfo address) {
        this.address = address;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
