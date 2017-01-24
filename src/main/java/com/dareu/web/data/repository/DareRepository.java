/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.exception.DataAccessException;
import java.util.List;

/**
 *
 * @author MACARENA
 */
public interface DareRepository extends BaseRepository<Dare> {

    public String createDare(Dare dare) throws DataAccessException;
    public List<Dare> findUnapprovedDares(int pageNumber)throws DataAccessException; 
    public int daresCount(String userId)throws DataAccessException; 
}
