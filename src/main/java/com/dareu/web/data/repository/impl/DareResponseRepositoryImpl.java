/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.dto.response.entity.DareResponseDescription;
import com.dareu.web.dto.response.entity.Page;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

/**
 *
 * @author MACARENA
 */
@Stateless
public class DareResponseRepositoryImpl extends AbstractRepository<DareResponse>implements DareResponseRepository{
    
    @Inject
    private DareuAssembler assembler; 
    
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

    public Page<DareResponseDescription> getResponses(String userId, int pageNumber) throws DataAccessException {
        try{
            Query q = em.createQuery("SELECT r FROM DareResponse r WHERE r.user.id = :userId")
                    .setParameter("userId", userId)
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));
            List<DareResponse> responses = q.getResultList(); 
            //get count 
            int count = responsesCount(userId); 
            
        }catch(Exception ex){
            throw new DataAccessException("Could not get responses from user: " + ex.getMessage()); 
        }
    }
}
