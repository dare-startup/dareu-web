/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import com.dareu.web.data.entity.AnchoredContent;
import com.dareu.web.data.entity.Comment;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.entity.ResponseClap;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.response.entity.AnchoredDescription;
import com.dareu.web.dto.response.entity.CommentDescription;
import com.dareu.web.dto.response.entity.DareResponseDescription;
import com.dareu.web.dto.response.entity.Page;
import com.google.api.client.util.Data;

/**
 *
 * @author MACARENA
 */
public interface DareResponseRepository extends BaseRepository<DareResponse>{
    /**
     * Gets the number of responses that a user has uploaded
     * @param userId
     * @return
     * @throws DataAccessException 
     */
    public int responsesCount(String userId)throws DataAccessException; 
    
    /**
     * Get a page of responses from a user
     * @param userId
     * @param pageNumber
     * @return
     * @throws DataAccessException 
     */
    public Page<DareResponseDescription> getResponses(String userId, int pageNumber)throws DataAccessException; 

    /**
     * Get a channel of dare responses 
     * @param pageNumber
     * @return
     * @throws DataAccessException 
     */
    public Page<DareResponseDescription> getChannelPage(int pageNumber, String userId)throws DataAccessException;
    
    /**
     * Creates a new response comment
     * @param comment
     * @throws DataAccessException 
     */
    public void createResponseComment(Comment comment)throws DataAccessException;  

    /**
     * Returns a page of response comments
     * @param pageNumber
     * @param responseId
     * @return
     * @throws DataAccessException 
     */
    public Page<CommentDescription> findResponseComments(int pageNumber, String responseId, String userId) throws DataAccessException;
    
    /**
     * 
     * @param commentId
     * @return
     * @throws DataAccessException 
     */
    public Comment findComment(String commentId)throws DataAccessException;

    /**
     * Get the number of comments a response has
     * @param responseId
     * @return
     * @throws DataAccessException 
     */
    public int getResponseCommentsCount(String responseId) throws DataAccessException;

    /**
     * Creates a clap response
     * @param clap
     * @throws DataAccessException 
     */
    public void clapResponse(ResponseClap clap) throws DataAccessException;

    /**
     * Un clap a dare response
     * @param responseId
     * @param userId
     * @throws DataAccessException 
     */
    public void unclapResponse(String responseId, String userId)throws DataAccessException;
    
    
    /**
     * anchor a dare response to a user 
     * @param content
     * @throws DataAccessException 
     */
    public void anchorContent(AnchoredContent content) throws DataAccessException; 
    
    /**
     * Get a user anchored content
     * @param pageNumber
     * @param token
     * @return
     * @throws DataAccessException 
     */
    public Page<AnchoredDescription> getAnchoredContent(int pageNumber, String token) throws DataAccessException;

    /**
     * 
     * @param responseId
     * @param token
     * @return
     * @throws DataAccessException 
     */
    public AnchoredContent findAnchoredContent(String responseId, String token) throws DataAccessException;

    /**
     * unpin anchored content
     * @param anchoredContentId
     * @throws DataAccessException 
     */
    public void unpinContent(String anchoredContentId) throws DataAccessException;

    /**
     * Checks if a user has already clapped a dare response
     * @param userId
     * @return
     * @throws DataAccessException
     */
    public boolean isResponseClapped(String userId, String responseId)throws DataAccessException;

    /**
     *
     * @param userId
     * @param responseId
     * @return
     * @throws DataAccessException
     */
    public boolean isResponseAnchored(String userId, String responseId)throws DataAccessException;

    /**
     * Finds an anchored content using an id
     * @param id
     * @return
     * @throws DataAccessException
     */
    public AnchoredContent findAnchoredContent(String id)throws DataAccessException;
}
