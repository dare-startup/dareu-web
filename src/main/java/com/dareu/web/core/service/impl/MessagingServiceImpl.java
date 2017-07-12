package com.dareu.web.core.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.dareu.web.core.service.MessagingService;
import com.dareu.web.dto.jms.QueueMessage;
import com.dareu.web.dto.request.email.EmailRequest;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.google.gson.Gson;

import javax.inject.Inject;

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
    public void sendPushNotificationMessage(QueueMessage data) {
        final String queueUrl = amazonSQS.getQueueUrl(pushNotificationsDestinationName).getQueueUrl();
        amazonSQS.sendMessage(new SendMessageRequest(queueUrl, new Gson().toJson(data)));
    }

    @Override
    public void sendAwsFileUpload(QueueMessage queueMessage) {
        final String queueUrl = amazonSQS.getQueueUrl(awsFileUploadDestinationName).getQueueUrl();
        amazonSQS.sendMessage(new SendMessageRequest(queueUrl, new Gson().toJson(queueMessage.getPayloadMessage())));
    }

    @Override
    public void sendEmailMessage(EmailRequest emailRequest) {
        final String queueUrl = amazonSQS.getQueueUrl(emailRequestDestinationName).getQueueUrl();
        amazonSQS.sendMessage(new SendMessageRequest(queueUrl, new Gson().toJson(emailRequest)));
    }
}
