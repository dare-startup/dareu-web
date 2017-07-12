package com.dareu.web.core.service;

import com.dareu.web.dto.jms.QueueMessage;
import com.dareu.web.dto.request.email.EmailRequest;

public interface MessagingService {
    public void sendPushNotificationMessage(QueueMessage data);
    public void sendAwsFileUpload(QueueMessage queueMessage);
    public void sendEmailMessage(EmailRequest emailRequest);
}
