/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareFlag;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.response.entity.ActiveDare;
import com.dareu.web.dto.response.entity.CreatedDare;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.Page;
import java.util.List;

/**
 *
 * @author MACARENA
 */
public interface DareRepository extends BaseRepository<Dare> {

    /**
     * Creates a new dare
     * @param dare
     * @return
     * @throws DataAccessException 
     */
    public String createDare(Dare dare) throws DataAccessException;
    
    /**
     * Find unapproved dares using pagination
     * @param pageNumber
     * @return
     * @throws DataAccessException 
     */
    public List<Dare> findUnapprovedDares(int pageNumber)throws DataAccessException; 
    
    /**
     * Get dares from a user id 
     * @param userId
     * @return
     * @throws DataAccessException 
     */
    public int daresCount(String userId)throws DataAccessException; 
    
    /**
     * 
     * @param userId
     * @return
     * @throws DataAccessException 
     */
    public Dare findUnacceptedDare(String userId)throws DataAccessException;
    
    /**
     * confirms a dare to the accepted value 
     * @param dareId
     * @param accepted
     * @throws DataAccessException 
     */
    public void confirmDareRequest(String dareId, boolean accepted)throws DataAccessException;
    
    /**
     * 
     * @param pageNumber
     * @param userId
     * @return
     * @throws DataAccessException 
     */
    public Page<DareDescription> discoverDares(int pageNumber, String userId)throws DataAccessException; 

    /**
     * Returns a page of created dares by a user
     * @param id
     * @param pageNumber
     * @return
     * @throws DataAccessException 
     */
    public Page<CreatedDare> findCreatedDares(String id, int pageNumber) throws DataAccessException;
    
    /**
     * 
     * @param userId
     * @return
     * @throws DataAccessException 
     */
    public ActiveDare getCurrentActiveDare(String userId)throws DataAccessException; 
    
    /**
     * Flags a dare request
     * @param dareFlag
     * @throws DataAccessException 
     */
    public void flagDare(DareFlag dareFlag) throws DataAccessException; 
    
    /**
     * Set a dare to expired
     * @param dareId
     * @throws DataAccessException 
     */
    public void setDareExpiration(String dareId) throws DataAccessException; 
}
