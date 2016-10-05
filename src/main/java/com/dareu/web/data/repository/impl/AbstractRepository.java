/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import com.dareu.web.data.entity.BaseEntity;
import com.dareu.web.exception.DataAccessException;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author MACARENA
 */
public class AbstractRepository<T extends BaseEntity>{
    private final Class<T> type;
    
    @Inject 
    private transient Logger LOG;

    @PersistenceContext
    protected EntityManager em;

    @Resource
    protected UserTransaction utx;

    
    public AbstractRepository(Class<T> type) {
        this.type = type;
    }

    public T find(String id) throws DataAccessException {
        T t = null;
        try {
            t = em.find(type, id);
            return t;
        } catch (Exception ex) {
            throw new DataAccessException("Could not find entity: " + ex.getMessage()); 
        }
    }

    public List<T> list() throws DataAccessException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> root = query.from(type);
            query.select(root);
            TypedQuery<T> typedQuery = em.createQuery(query);
            List<T> resultList = typedQuery.getResultList();
            return resultList;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get entities list: "+ ex.getMessage()); 
        }

    }

    public void persist(T entity) throws DataAccessException {
        try{
            utx.begin();
            em.persist(entity);
            utx.commit();
        }catch(IllegalStateException ex){
            throw new DataAccessException("Could not persist entity: " + ex.getMessage()); 
        }catch(Exception ex){
            throw new DataAccessException("Exception persisting entity: " + ex.getMessage()); 
        }
    }

    public void remove(T entity) throws DataAccessException {
        try{
            T t = find(entity.getId()); 
            if(t != null){
                utx.begin();
                em.remove(entity);
                utx.commit();
            }
        }catch(Exception ex){
            throw new DataAccessException("Could not remove entity: " + ex.getMessage()); 
        }
    }
    
    public List<T> getPage(int pageNumber, int pageSize)throws DataAccessException{
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> root = query.from(type);
            query.select(root);
            TypedQuery<T> typedQuery = em.createQuery(query);
            typedQuery.setMaxResults(pageSize);
            typedQuery.setFirstResult(pageSize * pageNumber); 
            List<T> resultList = typedQuery.getResultList();
            return resultList;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get entities list: "+ ex.getMessage()); 
        }
    }
}
