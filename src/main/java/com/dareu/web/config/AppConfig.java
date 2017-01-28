package com.dareu.web.config;

import com.dareu.web.dto.security.PasswordEncryptor;
import com.dareu.web.dto.security.impl.PasswordEncryptorImpl;
import io.swagger.jaxrs.config.BeanConfig;
import javax.enterprise.inject.Produces;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rest/")
public class AppConfig extends Application{
	
	public AppConfig(){
		BeanConfig config = new BeanConfig();
		config.setVersion("1.0.0");
		config.setTitle("DareU API documentation");
		config.setContact("Alberto Rubalcaba: albertoruvel@gmail.com, Hector Mendoza: roar109@gmail.com");
		
		config.setBasePath("dareu-services/rest");
		config.setResourcePackage("com.dareu.web.resource");
		config.setScan(true); 
	}
        
        @Produces
        public PasswordEncryptor encryptor(){
            return new PasswordEncryptorImpl(); 
        }
}
