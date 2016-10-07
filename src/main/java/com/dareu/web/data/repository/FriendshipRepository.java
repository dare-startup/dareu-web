package com.dareu.web.data.repository;

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
}
