package com.dareu.web.core.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.dareu.web.core.service.MessagingService;
import com.dareu.web.dto.jms.QueueMessage;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.google.gson.Gson;

import javax.inject.Inject;

public class MessagingServiceImpl implements MessagingService {

    @Inject
    @ApplicationProperty(name = "com.dareu.web.jms.push.queue", type = ApplicationProperty.Types.SYSTEM)
    private String pushNotificationsDestinationName;

    @Inject
    private AmazonSQS amazonSQS;

    @Override
    public void sendPushNotificationMessage(QueueMessage data) {
        final String queueUrl = amazonSQS.getQueueUrl(pushNotificationsDestinationName).getQueueUrl();
        amazonSQS.sendMessage(new SendMessageRequest(queueUrl, new Gson().toJson(data)));
    }
}
