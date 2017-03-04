/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.response.entity.DareResponseDescription;
import com.dareu.web.dto.response.entity.Page;

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
    public Page<DareResponseDescription> getChannelPage(int pageNumber)throws DataAccessException;
    
    
}
