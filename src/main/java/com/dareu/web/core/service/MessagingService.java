package com.dareu.web.core.service;

import com.dareu.web.dto.jms.PushNotificationRequest;
import com.dareu.web.dto.jms.EmailRequest;

public interface MessagingService {
    public void sendPushNotificationMessage(PushNotificationRequest data);
    public void sendAwsFileUpload(PushNotificationRequest pushNotificationRequest);
    public void sendEmailMessage(EmailRequest emailRequest);
}
