package com.nxtlink.kaprika.models;

public class Ingredient {
	private String id;
	private String name;
	private String description;
	
	public Ingredient(String id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
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

}
