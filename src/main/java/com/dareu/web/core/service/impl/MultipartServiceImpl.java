/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.MultipartService;
import com.dareu.web.data.request.SignupRequest;
import com.dareu.web.exception.InvalidRequestException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author MACARENA
 */
@Stateless
public class MultipartServiceImpl implements MultipartService{
    
    @Override
    public SignupRequest getSignupRequest(MultipartFormDataInput input)throws 
            InvalidRequestException, IOException{
        Map<String, List<InputPart>> map = input.getFormDataMap(); 
        String name = map.get("name") .get(0).getBodyAsString(); 
        String email = map.get("email") .get(0).getBodyAsString();
        String username = map.get("username") .get(0).getBodyAsString();
        String password = map.get("password") .get(0).getBodyAsString();
        String birthday = map.get("birthday") .get(0).getBodyAsString();
        
        List<InputPart> list = map.get("file");
        InputStream file = null; 
        
        if(list != null && ! list.isEmpty()){
             file = list.get(0).getBody(InputStream.class, null); 
        }else
            throw new InvalidRequestException("No image attached to request"); 
        
        if(name == null)
            throw new InvalidRequestException("No name field provided"); 
        if(email == null)
            throw new InvalidRequestException("No email field provided"); 
        if(username == null)
            throw new InvalidRequestException("No username field provided"); 
        if(password == null)
            throw new InvalidRequestException("No password field provided"); 
        if(birthday == null)
            throw new InvalidRequestException("No birthday field provided"); 
        if(file == null)
            throw new InvalidRequestException("No file field provided"); 
        return new SignupRequest(name, email, username, password, file, birthday); 
    }
}
