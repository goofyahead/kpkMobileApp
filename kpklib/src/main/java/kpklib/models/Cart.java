package kpklib.models;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by goofyahead on 3/09/15.
 */
public class Cart implements Serializable{

    private static final String TAG = Cart.class.getName();
    private LinkedList<CartItem> itemList;
    private String nonce;
    private String fbId;
    private String deliveryOption;
    private String status;
    private String timestamp;
    private String amount;

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public Cart(String nonce, String fbId) {
        this.itemList = new LinkedList<>();
        this.fbId = fbId;
        this.nonce = nonce;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
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

    public void addItemToCart(Dish dishToAdd, int quantity, HashMap<String, String> options){
        String key = dishToAdd.getId();
        for (String option : options.keySet()){
            key = key + option + ":" + options.get(option).replace(" ", "");
        }

//        if (itemList.containsKey(dishToAdd.getId())){
//            Log.d(TAG, "Cart already contains dish augmenting qty");
//            CartItem current = itemList.get(dishToAdd.getId());
//            current.setQuantity(current.getQuantity() + quantity);
//            current.setOptions(options);
//        } else {
            itemList.add(new CartItem(dishToAdd, quantity, options));
//        }
        Log.d(TAG, "checking key " + key);
    }

    public float getTotal (){
        float total = 0;
        for (CartItem item : itemList) {
            total = total + (item.getItem().getPrice() * item.getQuantity());
        }
        return total;
    }

    public int getItemsCount() {
        int total = 0;
        for (CartItem item : itemList) {
            total = total + item.getQuantity();
        }
        return total;
    }

    public int getItemCount() {
        return itemList.size();
    }

    public CartItem getItem(int position) {
        return (CartItem) itemList.toArray()[position];
    }

    public void deleteItem(int position) {
        itemList.remove(getItem(position).getItem().getId());
    }
}
