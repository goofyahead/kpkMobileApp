package com.nxtlink.kaprika.models;

import com.google.gson.annotations.SerializedName;

public class Tag {
	@SerializedName("_id")
	private String id;
	private String name;
	private String description;
	
	public Tag(String id, String name, String description) {
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
