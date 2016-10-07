/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import java.util.List;

import com.dareu.web.data.entity.Category;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.exception.DataAccessException;

/**
 *
 * @author MACARENA
 */
public interface DareRepository extends BaseRepository<Dare>{
	
	public String createDare(Dare dare)throws DataAccessException; 
	
	
}
