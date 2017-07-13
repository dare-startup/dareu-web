/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author MACARENA
 */
public interface FileService {
    
    public String saveTemporalFile(InputStream is, String name, FileType type)throws IOException;
    
    public enum FileType{
    	PROFILE_IMAGE, DARE_VIDEO, VIDEO_THUMBNAIL; 
    }
}
