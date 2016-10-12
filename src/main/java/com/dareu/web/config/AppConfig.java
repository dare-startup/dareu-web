package com.dareu.web.config;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rest/")
public class AppConfig extends Application{
	
	public AppConfig(){
		BeanConfig config = new BeanConfig();
		config.setVersion("1.0.0");
		config.setTitle("Dare‹ API documentation");
		config.setContact("Alberto Rubalcaba: albertoruvel@gmail.com");
		
		config.setBasePath("api-docs");
		config.setResourcePackage("com.dareu.web.resource");
		config.setScan(true); 
	}
}
