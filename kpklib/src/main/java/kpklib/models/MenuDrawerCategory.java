package kpklib.models;

import android.graphics.drawable.Drawable;

/**
 * Created by goofyahead on 9/13/14.
 */
public class MenuDrawerCategory {
    private String categoryName;
    private int badgeCount;
    private Drawable img;

    public MenuDrawerCategory(String categoryName, int badgeCount, Drawable img) {
        this.categoryName = categoryName;
        this.badgeCount = badgeCount;
        this.img = img;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}
