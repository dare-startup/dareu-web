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
    
    private FcmClient client; 
    
    private static final FcmMessageOptions options = FcmMessageOptions.builder()
                .setTimeToLive(Duration.ofHours(6))
                .setDelayWhileIdle(true)
                .setPriorityEnum(PriorityEnum.High)
                .build();

    @Inject
    public DareuMessagingServiceImpl(
            @ApplicationProperty(name = "com.dareu.web.message.config", type = ApplicationProperty.Types.SYSTEM) String configurationFile, 
            @ApplicationProperty(name = "com.dareu.web.message.database.url", type = ApplicationProperty.Types.SYSTEM) String databaseUrl, 
            @ApplicationProperty(name = "com.dareu.web.message.properties", type = ApplicationProperty.Types.SYSTEM) String propertiesFile) {
        try{
            //log properties 
            log.info("Json Config File: " + configurationFile);
            log.info("Firebase Database URL: " + databaseUrl); 
            log.info("Firebase Messaging Properties: " + propertiesFile);
            
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream(configurationFile))
                .setDatabaseUrl(databaseUrl)
                .build(); 
            FirebaseApp.initializeApp(options); 
            
            //read properties file 
            PropertiesBasedSettings settings = PropertiesBasedSettings.createFromFile(Paths.get(propertiesFile), Charset.forName("UTF-8"));
            client = new FcmClient(settings); 
        }catch(FileNotFoundException ex){
            log.severe("Could not find Firebase configuration file: " + ex.getMessage());
        } catch(Exception ex){
            log.severe("Could not create Firebase configuration: " + ex.getMessage()); 
        }
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

}
