package com.dareu.web.data.repository.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.dareu.web.data.entity.Friendship;
import com.dareu.web.data.repository.FriendshipRepository;
import com.dareu.web.data.response.FriendshipResponse;
import com.dareu.web.exception.DataAccessException;

public class FriendshipRepositoryImpl extends AbstractRepository<Friendship> implements FriendshipRepository{

	
	@Inject
	private Logger log; 
	
	public FriendshipRepositoryImpl() {
		super(Friendship.class);
	}
	
	public Friendship findFriendship(String userId, String requestedUserId)
			throws DataAccessException {
		Query q = em.createQuery("SELECT f FROM Friendship f WHERE f.user.id = :userId AND f.requestedUser.id = :requestedUserId")
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

	public void updateFriendhip(boolean approved, String friendhipId)
			throws DataAccessException {
		//create a query 
		Query q = em.createQuery("UPDATE Friendship SET accepted = :approved WHERE id = :friendshipId")
				.setParameter("approved", approved)
				.setParameter("friendshipId", friendhipId);
		
		q.executeUpdate();
		log.info("Updated friendship " + friendhipId);
	}

	public List<FriendshipResponse> findFriends(final String id) throws DataAccessException{
		final StringBuilder userHql = new StringBuilder("SELECT new com.dareu.web.data.response.FriendshipResponse(f.friendId, f.friendName, f.dareCount) ");
		userHql.append("FROM VFriendship f WHERE f.id = :id ");
		final Query query = em.createQuery(userHql.toString());
		query.setParameter("id", id);
		return query.getResultList();
	}
	
	public List<FriendshipResponse> findFriendsByName(final String id, final String name) throws DataAccessException{
		final StringBuilder userHql = new StringBuilder("SELECT new com.dareu.web.data.response.FriendshipResponse(f.friendId, f.friendName, f.dareCount) ");
		userHql.append("FROM VFriendship f WHERE f.id = :id and  f.friendName like :name ");
		final Query query = em.createQuery(userHql.toString());
		query.setParameter("id", id);
		query.setParameter("name", name+"%");
		return query.getResultList();
	}
}
