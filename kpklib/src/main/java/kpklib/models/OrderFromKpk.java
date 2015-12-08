package kpklib.models;

/**
 * Created by goofyahead on 11/3/15.
 */
public class OrderFromKpk {
    private Cart order;
    private UserInfo client;

    public OrderFromKpk(Cart order, UserInfo client) {
        this.order = order;
        this.client = client;
    }
}
