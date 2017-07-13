package com.dareu.web.core.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.dareu.web.core.service.MessagingService;
import com.dareu.web.dto.jms.EmailType;
import com.dareu.web.dto.jms.PushNotificationRequest;
import com.dareu.web.dto.jms.EmailRequest;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class MessagingServiceImpl implements MessagingService {

    @Inject
    @ApplicationProperty(name = "com.dareu.web.jms.push.queue", type = ApplicationProperty.Types.SYSTEM)
    private String pushNotificationsDestinationName;

    @Inject
    @ApplicationProperty(name = "com.dareu.web.jms.file.queue", type = ApplicationProperty.Types.SYSTEM)
    private String awsFileUploadDestinationName;

    @Inject
    @ApplicationProperty(name = "server.email.queue.name", type = ApplicationProperty.Types.SYSTEM)
    private String emailRequestDestinationName;

    @Inject
    private AmazonSQS amazonSQS;

    @Override
    public void sendPushNotificationMessage(PushNotificationRequest data, String fcmToken) {
        final String queueUrl = amazonSQS.getQueueUrl(pushNotificationsDestinationName).getQueueUrl();
        amazonSQS.sendMessage(new SendMessageRequest(queueUrl, new Gson().toJson(data)));
    }

    @Override
    public void sendAwsFileUpload(PushNotificationRequest pushNotificationRequest, String uploadType) {
        final String queueUrl = amazonSQS.getQueueUrl(awsFileUploadDestinationName).getQueueUrl();
        amazonSQS.sendMessage(new SendMessageRequest(queueUrl, new Gson().toJson(pushNotificationRequest.getPushNotificationPayload())));
    }

    @Override
    public void sendEmailMessage(EmailRequest emailRequest, EmailType emailType) {
        final String queueUrl = amazonSQS.getQueueUrl(emailRequestDestinationName).getQueueUrl();
        SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, new Gson().toJson(emailRequest));
        Map<String, MessageAttributeValue> messageAttributes = new HashMap();
        messageAttributes.put("emailType", new MessageAttributeValue().withStringValue(emailType.toString()));
        sendMessageRequest.setMessageAttributes(messageAttributes);
        amazonSQS.sendMessage(sendMessageRequest);
    }
}
