package com.dareu.web.core.service.impl;

import com.dareu.web.core.DareUtils;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.dareu.web.core.service.DareuMessagingService;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.entity.Friendship;
import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.dto.response.message.ConnectionAcceptedMessage;
import com.dareu.web.dto.response.message.ConnectionRequestMessage;
import com.dareu.web.dto.response.message.NewCommentMessage;
import com.dareu.web.dto.response.message.NewDareMessage;
import com.dareu.web.dto.response.message.QueuedDareMessage;
import com.dareu.web.dto.response.message.UploadedResponseMessage;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.enums.PriorityEnum;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.model.topics.Topic;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.topic.TopicUnicastMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;

public class DareuMessagingServiceImpl implements DareuMessagingService {

    private Logger log = Logger.getLogger(DareuMessagingServiceImpl.class.getName());
    
    @Inject
    private FcmClient client; 
    private static final FcmMessageOptions options = FcmMessageOptions.builder()
            .setTimeToLive(Duration.ofHours(12))
            .setDelayWhileIdle(true)
            .setPriorityEnum(PriorityEnum.High)
            .build();

    public DareuMessagingServiceImpl() {
        
    }
    
   
    
    @Override
    public void sendConnectionRequestedNotification(FriendshipRequest friendship, String fcmToken){
        ConnectionRequestMessage message = new ConnectionRequestMessage(); 
        message.setFriendshipId(friendship.getId());
        message.setRequestUserId(friendship.getUser().getId());
        message.setUserName(friendship.getUser().getName());
        
        client.send(new DataUnicastMessage(options, fcmToken, message));
    }

    @Override
    public void sendNewDareNotification(Dare dare, String fcmToken) {
        //create message 
        NewDareMessage message = new NewDareMessage(); 
        message.setChallenger(dare.getChallengerUser().getName());
        message.setDareDescription(dare.getDescription());
        message.setDareId(dare.getId());
        message.setDareName(dare.getName());
        message.setTimer(dare.getEstimatedDareTime());
        client.send(new DataUnicastMessage(options, fcmToken, message)); 
    }

    @Override
    public void sendConnectionAcceptedNotification(String userFcmToken, FriendshipRequest f) {
        ConnectionAcceptedMessage message = new ConnectionAcceptedMessage();
        message.setFriendshipId(f.getId());
        
        client.send(new DataUnicastMessage(options, userFcmToken, message));
    }

    @Override
    public void sendDareResponseUploaded(DareResponse response, String userFcmToken) {
        UploadedResponseMessage message = new UploadedResponseMessage(); 
        message.setDareResponseId(response.getId());
        message.setMessage(response.getUser().getName() + " just uploaded a response to your dare");
        
        client.send(new DataUnicastMessage(options, userFcmToken, message));
    }

    @Override
    public void sendNewCommentNotification(String fcmToken, String commentId, String responseId, String comment) {
        NewCommentMessage newCommentMessage = new NewCommentMessage();
        newCommentMessage.setComment(comment);
        newCommentMessage.setCommentId(commentId);
        newCommentMessage.setResponseId(responseId);
        
        client.send(new DataUnicastMessage(options, fcmToken, newCommentMessage));
    }

    @Override
    public void sendQueuedDareNotification(String dareId, String token, String currentDareStatus) {
        QueuedDareMessage message = new QueuedDareMessage();
        message.setDareId(dareId);
        message.setCreationDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
        message.setCurrentDareStatus(currentDareStatus);
        client.send(new DataUnicastMessage(options, token, message)); 
    }

}
