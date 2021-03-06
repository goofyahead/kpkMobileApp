package kpklib.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Dish implements Serializable{

    @SerializedName("_id")
	private String id;
	private String name;
	private String description;
	private float price;
	private String picture;
	private String video;
	private boolean demo;
    private boolean kitchen;
	private LinkedList<Category> categories;
	private LinkedList<Ingredient> ingredients;
	private LinkedList<Tag> tags;
    private LinkedList<Recommendation> recommendations;
    private LinkedHashMap<String, LinkedList<String>> options;

    public Dish(String id, String name, String description, float price, String picture, String video, boolean demo, LinkedList<Category> categories, LinkedList<Ingredient> ingredients, LinkedList<Tag> tags, LinkedList<Recommendation> recommendations, boolean isKitchen) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.video = video;
        this.demo = demo;
        this.categories = categories;
        this.ingredients = ingredients;
        this.tags = tags;
        this.recommendations = recommendations;
        this.kitchen = isKitchen;
    }

    public Dish(String id, String name, String description, float price, String picture, String video, boolean demo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.video = video;
        this.demo = demo;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public LinkedHashMap<String, LinkedList<String>> getOptions() {
        return options;
    }

    public void setOptions(LinkedHashMap <String, LinkedList<String>> options) {
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getImage() {
        return picture;
    }

    public String getVideo() {
        return video;
    }

    public boolean isDemo() {
        return demo;
    }

    public LinkedList<Category> getCategories() {
        return categories;
    }

    public LinkedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public LinkedList<Tag> getTags() {
        return tags;
    }

    public LinkedList<Recommendation> getRecommendations() {
        return recommendations;
    }
}
