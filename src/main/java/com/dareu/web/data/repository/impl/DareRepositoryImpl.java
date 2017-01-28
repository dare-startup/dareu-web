/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.Page;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author MACARENA
 */
@Stateless
public class DareRepositoryImpl extends AbstractRepository<Dare> implements DareRepository {

    @Inject
    private DareuAssembler assembler;
    
    public DareRepositoryImpl() {
        super(Dare.class);
    }

    @Override
    public String createDare(Dare dare) throws DataAccessException {
        return persist(dare);
    }

    @Override
    public List<Dare> findUnapprovedDares(int pageNumber) throws DataAccessException {
        try{
            Query q = em.createQuery("SELECT d FROM Dare d WHERE d.approved = false")
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult((pageNumber * DEFAULT_PAGE_NUMBER) - DEFAULT_PAGE_NUMBER);
            return q.getResultList();
        }catch(Exception ex){
            throw new DataAccessException("Could not get unapproved dares: " + ex.getMessage(), ex); 
        }
    }

    public int daresCount(String userId) throws DataAccessException {
        Long count = 0L; 
        try{
            Query q = em.createQuery("SELECT COUNT(d) FROM Dare d WHERE d.challengerUser.id = :userId", Long.class)
                    .setParameter("userId", userId); 
            count = (Long)q.getSingleResult(); 
            
            return count.intValue(); 
        }catch(Exception ex){
            throw new DataAccessException("Could not get dares count: " + ex.getMessage()); 
        }
    }

    public Dare findUnacceptedDare(String userId) throws DataAccessException {
        Dare dare = null;
        List<Dare> list;
        try{
            Query query = em.createQuery("SELECT d FROM Dare d WHERE d.challengedUser.id = :userId AND d.accepted = 0 ORDER BY d.creationDate ASC")
                    .setParameter("userId", userId);
            
            list = query.getResultList();
            if(list.isEmpty())return null;
            return list.get(0);//returns first position to simulate a queue
        }catch(NoResultException ex){
            return null; 
        }catch(Exception ex){
            throw new DataAccessException("Could not get dare: " + ex.getMessage());
        }
            
    }

    public void confirmDareRequest(String dareId, boolean accepted) throws DataAccessException {
        try{
            Query q = em.createQuery("UPDATE Dare d SET d.accepted = :accepted WHERE d.id = :dareId")
                    .setParameter("dareId", dareId)
                    .setParameter("accepted", accepted);
            q.executeUpdate();
        }catch(Exception ex){
            throw new DataAccessException("Could not update dare confirmation: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Page<DareDescription> discoverDares(int pageNumber, String userId) throws DataAccessException {
        Page<DareDescription> daresPage; 
        List<Dare> dares; 
        List<DareDescription> descs;
        Long count;
        try{
            Query q = em.createQuery("SELECT d FROM Dare d WHERE d.challengedUser.id <> :userId AND d.challengerUser.id <> :userId")
                    .setParameter("userId", userId)
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));
            
            dares = q.getResultList();
            
            //get count 
            q = em.createQuery("SELECT COUNT(d.id) FROM Dare d WHERE d.challengedUser.id <> :userId AND d.challengerUser.id <> :userId")
                    .setParameter("userId", userId);
            count = (Long)q.getSingleResult();
            
            daresPage = new Page<DareDescription>();
            daresPage.setPageNumber(pageNumber);
            daresPage.setPageSize(DEFAULT_PAGE_NUMBER);
            daresPage.setPagesAvailable(getPagesAvailable(pageNumber, count.intValue()));
            descs = assembler.assembleDareDescriptions(dares);
            daresPage.setItems(descs);
            return daresPage;
        }catch(Exception ex){
            throw new DataAccessException("Could not get dares: " + ex.getMessage());
        }
    }
    
    
}
