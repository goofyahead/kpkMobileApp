package kpklib.models;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by goofyahead on 10/1/15.
 */
public class PrintableOrder {
    private HashMap<String, CartItem> itemList;
    private UserInfo address;
    private String timestamp;
    private String amount;

    public HashMap<String, CartItem> getItemList() {
        return itemList;
    }

    public void setItemList(HashMap<String, CartItem> itemList) {
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
