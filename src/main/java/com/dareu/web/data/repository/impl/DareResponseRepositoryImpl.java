/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.DareResponseRepository;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author MACARENA
 */
@Stateless
public class DareResponseRepositoryImpl extends AbstractRepository<DareResponse>implements DareResponseRepository{
    public DareResponseRepositoryImpl(){
        super(DareResponse.class);
    }

    public int responsesCount(String userId) throws DataAccessException {
        Long count = 0L; 
        try{
            Query q = em.createQuery("SELECT COUNT(r) FROM DareResponse r WHERE r.user.id = :userId", Long.class)
                    .setParameter("userId", userId); 
            count = (Long)q.getSingleResult(); 
            return count.intValue(); 
        }catch(Exception e){
            throw new DataAccessException("Could not get responses count: " + e.getMessage()); 
        }
    }
}
