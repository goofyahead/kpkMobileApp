package com.nxtlink.kaprika.models;

import java.util.LinkedList;

public class Dish {
	
	private String id;
	private String name;
	private String description;
	private float price;
	private String image;
	private String video;
	private boolean demo;
	private LinkedList<Category> categories;
	private LinkedList<Ingredient> ingredients;
	private LinkedList<Tag> tags;
    private LinkedList<Recommendation> recommendations;

    public Dish(String id, String name, String description, float price, String image, String video, boolean demo, LinkedList<Category> categories, LinkedList<Ingredient> ingredients, LinkedList<Tag> tags, LinkedList<Recommendation> recommendations) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.video = video;
        this.demo = demo;
        this.categories = categories;
        this.ingredients = ingredients;
        this.tags = tags;
        this.recommendations = recommendations;
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
        return image;
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
