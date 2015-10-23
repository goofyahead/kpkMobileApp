package kpklib.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by goofyahead on 3/09/15.
 */
public class CartItem implements Serializable{
    private Dish item;
    private int quantity;
    private HashMap<String, String> optionSelected = new HashMap<>();

    public CartItem(Dish item, int quantity, HashMap<String, String> options) {
        this.item = item;
        this.quantity = quantity;
        this.optionSelected = options;
    }

    public HashMap<String, String> getOptions() {
        return optionSelected;
    }

    public void setOptions(HashMap<String, String> options) {
        this.optionSelected = options;
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
