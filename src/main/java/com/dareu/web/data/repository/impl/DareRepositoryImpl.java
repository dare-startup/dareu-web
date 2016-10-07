/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import java.util.List;

import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.Category;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.exception.DataAccessException;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author MACARENA
 */
public class DareRepositoryImpl extends AbstractRepository<Dare> implements DareRepository {
    public DareRepositoryImpl(){
        super(Dare.class); 
    }

	@Override
	public String createDare(Dare dare) throws DataAccessException{
		return persist(dare); 
	}
}
