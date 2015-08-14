package com.nxtlink.kaprika.models;

/**
 * Created by goofyahead on 9/13/14.
 */
public class MenuCategory {
    private String categoryName;
    private int resourceImg;

    public MenuCategory(String categoryName, int resourceImg) {
        this.categoryName = categoryName;
        this.resourceImg = resourceImg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(int resourceImg) {
        this.resourceImg = resourceImg;
    }
}
