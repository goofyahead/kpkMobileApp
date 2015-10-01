package kpklib.models;

/**
 * Created by goofyahead on 17/09/15.
 */
public class UserInfo {
    private String name;
    private String fbId;
    private String email;
    private String street;
    private String floor;
    private String extraDeliver;
    private String postalCode;
    private String phone;

    public UserInfo(String name, String fbId, String email, String street, String floor, String extraDeliver, String postalCode, String phone) {
        this.name = name;
        this.fbId = fbId;
        this.email = email;
        this.street = street;
        this.floor = floor;
        this.extraDeliver = extraDeliver;
        this.postalCode = postalCode;
        this.phone = phone;
    }
}
