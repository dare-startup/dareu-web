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

    @Override
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

    @Override
    public Page<DareResponseDescription> getResponses(String userId, int pageNumber) throws DataAccessException {
        try{
            Query q = em.createQuery("SELECT r FROM DareResponse r WHERE r.user.id = :userId")
                    .setParameter("userId", userId)
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));
            List<DareResponse> responses = q.getResultList(); 
            //get count 
            int count = responsesCount(userId); 
            Page<DareResponseDescription> page = new Page<DareResponseDescription>(); 
            List<DareResponseDescription> descriptions = assembler.assembleDareResponseDescriptions(responses); 
            page.setItems(descriptions);
            page.setPageNumber(pageNumber);
            page.setPageSize(DEFAULT_PAGE_NUMBER);
            page.setPagesAvailable(getPagesAvailable(pageNumber, count));
            return page; 
        }catch(Exception ex){
            throw new DataAccessException("Could not get responses from user: " + ex.getMessage()); 
        }
    }

    @Override
    public Page<DareResponseDescription> getChannelPage(int pageNumber) throws DataAccessException {
        try{
            Query query = em.createQuery("SELECT r FROM DareResponse r ORDER BY r.lastUpdate DESC")
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber)); 
            List<DareResponse> list = query.getResultList(); 
            
            Long count = (Long)em.createQuery("SELECT COUNT(r.id) FROM DareResponse r ORDER BY r.lastUpdate DESC")
                    .getSingleResult(); 
            List<DareResponseDescription> descs = assembler.getResponseDescriptions(list);
            Page<DareResponseDescription> page = new Page<DareResponseDescription>(); 
            page.setItems(descs);
            page.setPageNumber(pageNumber);
            page.setPageSize(DEFAULT_PAGE_NUMBER);
            page.setPagesAvailable(getPagesAvailable(pageNumber, count.intValue()));
            return page; 
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage()); 
        }
    }
}
