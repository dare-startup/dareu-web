package com.dareu.web.data.repository.impl;

import javax.ejb.Stateless;

import com.dareu.web.data.entity.Category;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.CategoryRepository;
import com.dareu.web.dto.request.EditCategoryRequest;
import com.dareu.web.dto.response.entity.Page;
import java.util.List;
import javax.persistence.Query;

@Stateless
public class CategoryRepositoryImpl extends AbstractRepository<Category> implements CategoryRepository{

	public CategoryRepositoryImpl() {
		super(Category.class);
	}

    @Override
    public List<Category> findCategoriesByPage(int pageNumber) throws DataAccessException{
        
        List<Category> categories = null;
        try{
            Query q = em.createQuery("SELECT c FROM Category c")
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult((pageNumber * DEFAULT_PAGE_NUMBER) - DEFAULT_PAGE_NUMBER);
            categories = q.getResultList();
            return categories;
        }catch(Exception ex){
            throw new DataAccessException("Could not get categories: " + ex.getMessage(), ex);
        }
        
    }

    @Override
    public void update(EditCategoryRequest request) throws DataAccessException {
        Category category = em.find(Category.class, request.getId());
        if(category == null)
            throw new DataAccessException("Invalid category ID");
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        try{
            em.merge(category);
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage(), ex);
        }

    }

}
