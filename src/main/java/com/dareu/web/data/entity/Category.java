package com.dareu.web.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Category")
@Table(name = "category")
public class Category extends BaseEntity {
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	public Category() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
