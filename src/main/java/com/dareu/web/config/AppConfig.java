package com.dareu.web.config;

import com.dareu.web.dto.security.PasswordEncryptor;
import com.dareu.web.dto.security.impl.PasswordEncryptorImpl;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.client.settings.PropertiesBasedSettings;
import de.bytefish.fcmjava.model.enums.PriorityEnum;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import io.swagger.jaxrs.config.BeanConfig;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rest/")
public class AppConfig extends Application {

    private static final Logger log = Logger.getLogger(AppConfig.class.getName());

    @Inject
    @ApplicationProperty(name = "com.dareu.web.message.properties", type = ApplicationProperty.Types.SYSTEM) 
    private String propertiesFile; 

    @Inject
    @ApplicationProperty(name = "com.dareu.web.message.config", type = ApplicationProperty.Types.SYSTEM) 
    private String configurationFile;
    
    @Inject
    @ApplicationProperty(name = "com.dareu.web.message.database.url", type = ApplicationProperty.Types.SYSTEM) 
    private String databaseUrl;
    
    public AppConfig() {
        
        //swagger
        BeanConfig config = new BeanConfig();
        config.setVersion("1.0.0");
        config.setTitle("DareU API documentation");
        config.setContact("Alberto Rubalcaba: albertoruvel@gmail.com, Hector Mendoza: roar109@gmail.com");

        config.setBasePath("dareu-services/rest");
        config.setResourcePackage("com.dareu.web.resource");
        config.setScan(true);
    }
    
    @Produces
    public FcmClient fcmClient(){
        PropertiesBasedSettings settings = PropertiesBasedSettings.createFromFile(Paths.get(propertiesFile), Charset.forName("UTF-8"));
        return new FcmClient(settings); 
    }

    @Produces
    public PasswordEncryptor encryptor() {
        return new PasswordEncryptorImpl();
    }
}
