package com.dareu.web.core.service.impl;

import com.dareu.web.core.DareUtils;

import javax.inject.Inject;

import com.dareu.web.core.service.DareuMessagingService;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.dto.response.message.ClappedResponseMessage;
import com.dareu.web.dto.response.message.ConnectionAcceptedMessage;
import com.dareu.web.dto.response.message.ConnectionRequestMessage;
import com.dareu.web.dto.response.message.NewCommentMessage;
import com.dareu.web.dto.response.message.NewDareMessage;
import com.dareu.web.dto.response.message.QueuedDareMessage;
import com.dareu.web.dto.response.message.UploadedResponseMessage;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.model.enums.PriorityEnum;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.Date;

public class DareuMessagingServiceImpl implements DareuMessagingService {

    @Inject
    private Logger log;

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
        log.info("Sending connection request notification to " + friendship.getRequestedUser().getEmail());
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

        log.info("Sending new dare notification to " + dare.getChallengedUser().getEmail());
        client.send(new DataUnicastMessage(options, fcmToken, message)); 
    }

    @Override
    public void sendConnectionAcceptedNotification(String userFcmToken, FriendshipRequest f) {
        ConnectionAcceptedMessage message = new ConnectionAcceptedMessage();
        message.setFriendshipId(f.getId());

        log.info("Sending connection accepted notification to " + f.getUser().getEmail());
        client.send(new DataUnicastMessage(options, userFcmToken, message));
    }

    @Override
    public void sendDareResponseUploaded(DareResponse response, String userFcmToken) {
        UploadedResponseMessage message = new UploadedResponseMessage(); 
        message.setDareResponseId(response.getId());
        message.setMessage(response.getUser().getName() + " just uploaded a response to your dare");

        log.info("Sending dare response uploaded notification to " + response.getDare().getChallengerUser().getEmail());
        client.send(new DataUnicastMessage(options, userFcmToken, message));
    }

    @Override
    public void sendNewCommentNotification(String fcmToken, String commentId, String responseId, String comment) {
        NewCommentMessage newCommentMessage = new NewCommentMessage();
        newCommentMessage.setComment(comment);
        newCommentMessage.setCommentId(commentId);
        newCommentMessage.setResponseId(responseId);

        log.info("Sending new comment notification");
        client.send(new DataUnicastMessage(options, fcmToken, newCommentMessage));
    }

    @Override
    public void sendQueuedDareNotification(String dareId, String token, String currentDareStatus) {
        QueuedDareMessage message = new QueuedDareMessage();
        message.setDareId(dareId);
        message.setCreationDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
        message.setCurrentDareStatus(currentDareStatus);

        log.info("Sending queued dare notification");
        client.send(new DataUnicastMessage(options, token, message)); 
    }

    @Override
    public void sendClappedResponse(String id, String fcmToken) {
        ClappedResponseMessage message = new ClappedResponseMessage(); 
        message.setResponseId(id);

        log.info("Sending clapped response notification");
        client.send(new DataUnicastMessage(options, fcmToken, message));
    }

}
