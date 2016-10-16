package com.dareu.web.data.repository;

import com.dareu.web.data.entity.DareUserDare;
import com.dareu.web.exception.DataAccessException;

public interface DareUserDareRepository extends BaseRepository<DareUserDare>{

	/**
	 * Return all dares for the given challenger aka dare user
	 * 
	 * @param challengerId
	 * @return count
	 * @throws DataAccessException 
	 * */
	public Long countDaresByChallenger(final String challengerId) throws DataAccessException;
}
