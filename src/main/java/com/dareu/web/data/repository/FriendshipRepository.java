package com.dareu.web.data.repository;

import com.dareu.web.data.entity.DareUser;
import java.util.List;

import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.response.entity.ConnectionRequest;
import com.dareu.web.dto.response.entity.FriendSearchDescription;
import com.dareu.web.dto.response.entity.Page;

public interface FriendshipRepository extends BaseRepository<FriendshipRequest>{
	
	/**
     * Finds a Friendship using a requested and user id 
     * @param userId
     * @param requestedUserId
     * @return
     * @throws DataAccessException
     */
    public FriendshipRequest findFriendship(String userId, String requestedUserId)throws DataAccessException;
    
    /**
     * Update the provided friendship id to the value of accepted
     * @param approved
     * @param friendhipId
     * @throws DataAccessException
     */
    public void updateFriendhip(boolean approved, String friendhipId)throws DataAccessException;
    
    
    /**
     * Get a list of friendships that are not registered with the provided user
     * @param id
     * @param pageNumber
     * @return
     * @throws DataAccessException 
     */
    public List<DareUser> discoverUsers(String id, int pageNumber)throws DataAccessException; 
    
    /**
     * checks if two users are already friends
     * @param userId
     * @param requestedUserId
     * @return
     * @throws DataAccessException 
     */
    public boolean areUsersFriends(String userId, String requestedUserId)throws DataAccessException; 

    /**
     * Check is a user already sent a request to another user
     * @param userId
     * @param requestedUserId
     * @return
     * @throws DataAccessException 
     */
    public boolean isRequestSent(String userId, String requestedUserId) throws DataAccessException; 
    
    /**
     * Gets a page of friend descriptions from a user id
     * @param userId
     * @param pageNumber
     * @return
     * @throws DataAccessException 
     */
    public Page<FriendSearchDescription> findFriendDescriptions(String userId, int pageNumber)throws DataAccessException;
    
    /**
     * Find a page of friends from a user id using a query name
     * @param userId
     * @param pageNumber
     * @param query
     * @return
     * @throws DataAccessException 
     */
    public Page<FriendSearchDescription> findFriendDescriptions(String userId, int pageNumber, String query)throws DataAccessException;
    
    /**
     * Checks if a user has received a friendship request from another user
     * @param userId
     * @param requestedUserId
     * @return
     * @throws DataAccessException 
     */
    public boolean isRequestReceived(String userId, String requestedUserId) throws DataAccessException; 

    /**
     * Get received notifications
     * @param pageNumber
     * @param id
     * @return
     * @throws DataAccessException 
     */
    public Page<ConnectionRequest> getReceivedPendingRequests(int pageNumber, String id)throws DataAccessException;
    
    
    /**
     * get sent requests
     * @param pageNumber
     * @param id
     * @return
     * @throws DataAccessException 
     */
    public Page<ConnectionRequest> getSentPendingRequests(int pageNumber, String id) throws DataAccessException; 
}
