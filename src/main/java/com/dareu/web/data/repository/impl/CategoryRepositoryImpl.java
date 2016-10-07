package com.dareu.web.data.repository.impl;

import javax.ejb.Stateless;

import com.dareu.web.data.entity.Category;
import com.dareu.web.data.repository.CategoryRepository;

@Stateless
public class CategoryRepositoryImpl extends AbstractRepository<Category> implements CategoryRepository{

	public CategoryRepositoryImpl() {
		super(Category.class);
	}

}
