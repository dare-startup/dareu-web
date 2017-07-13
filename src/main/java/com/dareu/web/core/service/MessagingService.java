package com.dareu.web.core.service;

import com.messaging.dto.email.EmailRequest;
import com.messaging.dto.email.EmailType;
import com.messaging.dto.upload.FileUploadRequest;
import com.messaging.dto.push.PushNotificationRequest;

public interface MessagingService {
    public void sendPushNotificationMessage(PushNotificationRequest data);
    public void sendAwsFileUpload(FileUploadRequest fileUploadRequest);
    public void sendEmailMessage(EmailRequest emailRequest, EmailType emailType);
}
