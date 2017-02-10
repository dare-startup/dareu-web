/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author MACARENA
 */
public interface FileService {
    public String saveFile(InputStream stream, FileType fileType, String fileName)throws IOException;
    public InputStream getFile(String fileName, FileType fileType)throws FileNotFoundException, IOException;
    public boolean userHasProfileImage(String userId);
    
    public enum FileType{
    	PROFILE_IMAGE, DARE_VIDEO; 
    }
}
