/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author MACARENA
 */
public interface FileService {
    public String saveFile(InputStream stream, FileType fileType, String fileName)throws IOException;
    public InputStream getFileStream(String fileName, FileType fileType)throws FileNotFoundException, IOException;
    public File getFile(String fileName, FileType fileType)throws FileNotFoundException; 
    public boolean fileExists(FileType fileType, String id); 
    
    public enum FileType{
    	PROFILE_IMAGE, DARE_VIDEO, VIDEO_THUMBNAIL; 
    }
}
