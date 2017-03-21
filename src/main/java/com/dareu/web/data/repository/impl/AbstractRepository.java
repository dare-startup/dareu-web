/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import com.dareu.web.data.entity.BaseEntity;
import com.dareu.web.data.exception.DataAccessException;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author MACARENA
 */
public class AbstractRepository<T extends BaseEntity> {

    private final Class<T> type;
    
    public static final Integer DEFAULT_PAGE_NUMBER = 10; 

    @PersistenceContext
    protected EntityManager em;

    public AbstractRepository(Class<T> type) {
        this.type = type;
    }
    
    protected int getFirstResult(int pageNumber){
        return (pageNumber - 1) * DEFAULT_PAGE_NUMBER; 
    }
    
    protected int getPagesAvailable(int pageNumber, int totalCount){
        //int start = getFirstResult(pageNumber); 
        return (int) ((totalCount / DEFAULT_PAGE_NUMBER) + 1);
    }

    public T find(String id) throws DataAccessException {
        T t = null;
        try {
            t = em.find(type, id);

            return t;
        } catch (Exception ex) {
            throw new DataAccessException("Could not find entity: "
                    + ex.getMessage());
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
            throw new DataAccessException("Could not get entities list: "
                    + ex.getMessage());
        }

    }

    public String persist(T entity) throws DataAccessException {
        try {
            em.persist(entity);
            return entity.getId();
            //utx.commit();
        } catch (IllegalStateException ex) {
            throw new DataAccessException("Could not persist entity: "
                    + ex.getMessage());
        } catch (Exception ex) {
            throw new DataAccessException("Exception persisting entity: "
                    + ex.getMessage());
        }
    }

    @Transactional
    public String persist(Class<? extends BaseEntity> entity) throws DataAccessException {
        return persist(entity);
    }

    @Transactional
    public void remove(T entity) throws DataAccessException {
        try {
            T t = find(entity.getId());
            if (t != null) {
                //utx.begin();
                em.remove(em.contains(entity) ? entity : em.merge(entity));
                //utx.commit();
            }
        } catch (Exception ex) {
            throw new DataAccessException("Could not remove entity: "
                    + ex.getMessage());
        }
    }

    public List<T> getPage(int pageNumber, int pageSize)
            throws DataAccessException {
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
            throw new DataAccessException("Could not get entities list: "
                    + ex.getMessage());
        }
    }
}
