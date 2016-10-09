package com.dareu.web.data.repository.impl;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.dareu.web.data.entity.Friendship;
import com.dareu.web.data.repository.FriendshipRepository;
import com.dareu.web.exception.DataAccessException;

public class FriendshipRepositoryImpl extends AbstractRepository<Friendship> implements FriendshipRepository{

	
	@Inject
	private Logger log; 
	
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

	@Override
	public void updateFriendhip(boolean approved, String friendhipId)
			throws DataAccessException {
		//create a query 
		Query q = em.createQuery("UPDATE u Friendship SET u.approved = :approved WHERE u.friendshipId = :friendshipId")
				.setParameter("approved", approved)
				.setParameter("friendshipId", friendhipId);
		
		q.executeUpdate();
		log.info("Updated friendship " + friendhipId);
	}

}
