package com.nxtlink.kaprika.models;

/**
 * Created by goofyahead on 13/08/15.
 */
public class Recommendation {
    private String id;
    private String name;

    public Recommendation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
