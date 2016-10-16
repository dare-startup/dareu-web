package com.dareu.web.data.repository.impl;

import javax.persistence.Query;

import com.dareu.web.data.entity.DareUserDare;
import com.dareu.web.data.repository.DareUserDareRepository;
import com.dareu.web.exception.DataAccessException;

public class DareUserDareRepositoryImpl extends AbstractRepository<DareUserDare> implements DareUserDareRepository {

	public DareUserDareRepositoryImpl() {
		super(DareUserDare.class);
	}

	public Long countDaresByChallenger(final String challengerId) throws DataAccessException{
		Long count = 0l;
		
		final Query query = em.createQuery("select count(*) from DareUserDare as dud where dud.challenger.id = :id ");
		query.setParameter("id", challengerId);
		
		try{
			count = (Long)query.getSingleResult();
		}catch(Exception e){
			throw new DataAccessException(e.getMessage(), e);
		}
		return count;
	}
}
