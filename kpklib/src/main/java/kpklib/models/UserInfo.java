package kpklib.models;

import java.io.Serializable;

/**
 * Created by goofyahead on 17/09/15.
 */
public class UserInfo implements Serializable {
    private String name;
    private String fbId;
    private String email;
    private String street;
    private String floor;
    private String extraDeliver;
    private String postalCode;
    private String phone;
    private String gcmId;

    public UserInfo(String name, String fbId, String email, String street, String floor, String extraDeliver, String postalCode, String phone, String gcmId) {
        this.name = name;
        this.fbId = fbId;
        this.email = email;
        this.street = street;
        this.floor = floor;
        this.extraDeliver = extraDeliver;
        this.postalCode = postalCode;
        this.phone = phone;
        this.gcmId = gcmId;
    }

    public String getName() {
        return name;
    }

    public String getFbId() {
        return fbId;
    }

    public String getEmail() {
        return email;
    }

    public String getStreet() {
        return street;
    }

    public String getFloor() {
        return floor;
    }

    public String getExtraDeliver() {
        return extraDeliver;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getGcmId() {
        return gcmId;
    }
}
