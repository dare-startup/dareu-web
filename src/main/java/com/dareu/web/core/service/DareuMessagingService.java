package com.dareu.web.core.service;

import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.entity.Friendship;
import com.dareu.web.data.entity.FriendshipRequest;

public interface DareuMessagingService {
    
    /**
     * Send a notification to a newly dared user
     * @param dare 
     * @param fcmToken 
     */
    public void sendNewDareNotification(Dare dare, String fcmToken);
    
    /**
     * Connection request notification
     * @param friendship
     * @param fcmToken 
     */
    public void sendConnectionRequestedNotification(FriendshipRequest friendship, String fcmToken); 

    /**
     * Send a connection accepted to the friendship creator
     * @param userFcmToken
     * @param f 
     */
    public void sendConnectionAcceptedNotification(String userFcmToken, FriendshipRequest f);
    
    /**
     * 
     * @param response
     * @param userFcmToken 
     */
    public void sendDareResponseUploaded(DareResponse response, String userFcmToken); 

    /**
     *  Send a new comment notification to the dare response creator
     * @param videoCreatorId
     * @param commentId
     * @param responseId 
     * @param comment 
     */
    public void sendNewCommentNotification(String videoCreatorId, String commentId, String responseId, String comment);
    
    /**
     * Sends a new queued dare notification
     * @param dareId 
     * @param token 
     * @param currentDareStatus 
     */
    public void sendQueuedDareNotification(String dareId, String token, String currentDareStatus); 
    
}
