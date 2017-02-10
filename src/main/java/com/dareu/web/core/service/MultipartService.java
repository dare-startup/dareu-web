/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service;

import com.dareu.web.dto.request.DareUploadRequest;
import com.dareu.web.dto.request.SignupRequest;
import com.dareu.web.exception.InvalidRequestException;
import java.io.IOException;
import java.io.InputStream;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author MACARENA
 */
public interface MultipartService {
    public InputStream getImageProfile(MultipartFormDataInput input)throws IOException; 
    public DareUploadRequest getDareUploadRequest(MultipartFormDataInput input)throws IOException; 
}
