package com.dareu.web.core.service;

import com.dareu.web.dto.jms.QueueMessage;

public interface MessagingService {
    public void sendPushNotificationMessage(QueueMessage data);
}
