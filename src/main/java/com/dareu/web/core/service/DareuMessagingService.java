package com.dareu.web.core.service;

import com.dareu.web.data.entity.Dare;

public interface DareuMessagingService {
    
    /**
     * Send a notification to a newly dared user
     * @param dare 
     * @param fcmToken 
     */
    public void sendNewDareNotification(Dare dare, String fcmToken);
}
