package kpklib.models;

/**
 * Created by goofyahead on 9/13/14.
 */
public class MenuCategory {
    private String categoryName;
    private int resourceImg;
    private int badgeCount;
    private String id;

    public MenuCategory(String categoryName, int resourceImg, String id) {
        this.categoryName = categoryName;
        this.resourceImg = resourceImg;
        this.id = id;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
