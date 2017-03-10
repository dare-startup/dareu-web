/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import com.dareu.web.data.entity.BaseEntity;
import com.dareu.web.data.exception.DataAccessException;

import java.util.List;

/**
 *
 * @author MACARENA
 * @param <T>
 */
public interface BaseRepository<T extends BaseEntity> {
    
    /**
     * Finds an entity using an id 
     * @param id
     * @return
     * @throws DataAccessException 
     */
    public  T find(String id)throws DataAccessException; 
    
    /**
     * Finds a list of entities
     * @return
     * @throws DataAccessException 
     */
    public  List<T> list()throws DataAccessException; 
    
    /**
     * Persists an entity
     * @param entity
     * @return
     * @throws DataAccessException 
     */
    public  String persist(T entity)throws DataAccessException; 
    
    /**
     * Persists an entity
     * @param entity
     * @return
     * @throws DataAccessException 
     */
    public String persist(Class<?extends BaseEntity> entity)throws DataAccessException;
    
    /**
     * Deletes an entity 
     * @param entity
     * @throws DataAccessException 
     */
    public  void remove(T entity)throws DataAccessException; 
    
    /**
     * Gets a page of entities
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws DataAccessException 
     */
    public List<T> getPage(int pageNumber, int pageSize)throws DataAccessException;
}
