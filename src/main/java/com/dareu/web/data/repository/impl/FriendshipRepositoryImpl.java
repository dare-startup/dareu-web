package com.dareu.web.data.repository.impl;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.dareu.web.data.entity.Friendship;
import com.dareu.web.data.repository.FriendshipRepository;
import com.dareu.web.exception.DataAccessException;

public class FriendshipRepositoryImpl extends AbstractRepository<Friendship> implements FriendshipRepository{

	public FriendshipRepositoryImpl() {
		super(Friendship.class);
	}
	
	@Override
	public Friendship findFriendship(String userId, String requestedUserId)
			throws DataAccessException {
		Query q = em.createQuery("SELECT f FROM Friendship f WHERE f.userId = :userId AND f.requestedUserId = :requestedUserId")
				.setParameter("requestedUserId", requestedUserId)
				.setParameter("userId", userId);
		
		
		Friendship f = null; 
		try{
			f = (Friendship)q.getSingleResult();
			return f; 
		}catch(NoResultException ex){
			return null; 
		}
	}

}
