package com.dareu.web.data.repository;

import com.dareu.web.data.entity.Category;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.request.EditCategoryRequest;
import com.dareu.web.dto.response.entity.Page;
import java.util.List;

public interface CategoryRepository extends BaseRepository<Category>{
    
    /**
     * Find a list of categories
     * @param pageNumber
     * @return
     * @throws DataAccessException 
     */
    public List<Category> findCategoriesByPage(int pageNumber)throws DataAccessException;

    /**
     * Updates a category
     * @param request
     * @throws DataAccessException
     */
    public void update(EditCategoryRequest request)throws DataAccessException;
}
