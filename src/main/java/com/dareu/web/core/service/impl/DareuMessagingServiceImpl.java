package com.dareu.web.core.service.impl;

import java.util.logging.Logger;

import javax.inject.Inject;

import com.dareu.web.core.service.DareuMessagingService;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.dto.response.message.NewDareMessage;
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

public class DareuMessagingServiceImpl implements DareuMessagingService {

    private Logger log = Logger.getLogger(DareuMessagingServiceImpl.class.getName());
    
    @Inject
    private FcmClient client; 
    
    
    
    private static final FcmMessageOptions options = FcmMessageOptions.builder()
            .setTimeToLive(Duration.ofHours(6))
            .setDelayWhileIdle(true)
            .setPriorityEnum(PriorityEnum.High)
            .build();

    public DareuMessagingServiceImpl() {
        
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
        client.send(new DataUnicastMessage(options, fcmToken, new Gson().toJson(message))); 
    }

}
