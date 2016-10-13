package com.dareu.web.data.repository;

import java.util.List;

import com.dareu.web.data.entity.Friendship;
import com.dareu.web.exception.DataAccessException;

public interface FriendshipRepository extends BaseRepository<Friendship>{
	
	/**
     * Finds a Friendship using a requested and user id 
     * @param userId
     * @param requestedUserId
     * @return
     * @throws DataAccessException
     */
    public Friendship findFriendship(String userId, String requestedUserId)throws DataAccessException;
    
    /**
     * Update the provided friendship id to the value of accepted
     * @param approved
     * @param friendhipId
     * @throws DataAccessException
     */
    public void updateFriendhip(boolean approved, String friendhipId)throws DataAccessException;
    
    /**
     * Get all the friends of a given user
     * @param id
     * @throws DataAccessException
     */
    public List<Friendship> findFriends(final String id) throws DataAccessException;
}
