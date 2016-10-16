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

	public List<FriendshipResponse> findFriends(final String id, final boolean aceptedOnly) throws DataAccessException{
		//TODO Found a better way of fetching all together
		final StringBuilder userHql = new StringBuilder("SELECT new com.dareu.web.data.response.FriendshipResponse(f.user.id, f.user.name) ");
		userHql.append("FROM Friendship f WHERE f.requestedUser.id = :userId ");
		userHql.append("and f.accepted = :acepted ");
		
		final StringBuilder reqUserHql = new StringBuilder("SELECT new com.dareu.web.data.response.FriendshipResponse(f.requestedUser.id, f.requestedUser.name) ");
		reqUserHql.append("FROM Friendship f WHERE f.user.id = :userId ");
		reqUserHql.append("and f.accepted = :acepted ");
		
		final List<FriendshipResponse> userList = findFriends(id, aceptedOnly, userHql.toString());
		final List<FriendshipResponse> userReqList = findFriends(id, aceptedOnly, reqUserHql.toString());
		userList.addAll(userReqList);
		
		return userList;
	}
	
	private List<FriendshipResponse> findFriends(final String id, final boolean aceptedOnly, final String hql){
		final Query q = em.createQuery(hql)
				.setParameter("userId", id)
				.setParameter("acepted", aceptedOnly);
		return q.getResultList();
	}
}
