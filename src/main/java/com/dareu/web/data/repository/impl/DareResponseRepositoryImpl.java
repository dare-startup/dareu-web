/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.data.entity.Comment;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.dto.response.entity.CommentDescription;
import com.dareu.web.dto.response.entity.DareResponseDescription;
import com.dareu.web.dto.response.entity.Page;
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

    @Override
    public void createResponseComment(Comment comment) throws DataAccessException {
        try{
            em.persist(comment);
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public Page<CommentDescription> findResponseComments(int pageNumber, String responseId) throws DataAccessException {
        try{
            List<Comment> comments = em.createQuery("SELECT c FROM Comment c WHERE c.response.id = :responseId")
                    .setParameter("responseId", responseId)
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber))
                    .getResultList();
            Long count = (Long)em.createQuery("SELECT (c.id) FROM Comment c WHERE c.response.id = :responseId")
                    .setParameter("responseId", responseId)
                    .getSingleResult();
            
            List<CommentDescription> descs = assembler.assembleCommentDescriptions(comments);
            Page<CommentDescription> page = new Page<CommentDescription>();
            page.setItems(descs); 
            page.setPageNumber(pageNumber);
            page.setPageSize(DEFAULT_PAGE_NUMBER);
            page.setPagesAvailable(getPagesAvailable(pageNumber, count.intValue()));
            return page;
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    public Comment findComment(String commentId) throws DataAccessException {
        try{
            return em.find(Comment.class, commentId);
        }catch(NoResultException ex){
            return null;
        }catch(Exception ex){
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }
}
