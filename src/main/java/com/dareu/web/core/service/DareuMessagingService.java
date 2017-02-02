package com.dareu.web.core.service;

import com.dareu.web.data.entity.Dare;
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
}
