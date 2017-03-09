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
    
    /**
     * Saves a file depending on the current associated host 
     * @param stream
     * @param fileType
     * @param fileName
     * @return
     * @throws IOException 
     */
    public String saveFile(InputStream stream, FileType fileType, String fileName)throws IOException;
    
    /**
     * Saves a file 
     * @param filePath
     * @param fileType
     * @param fileName
     * @return
     * @throws IOException 
     */
    public String saveFile(String filePath, FileType fileType, String fileName)throws IOException;
    
    /**
     * Get a file stream from a local system
     * @param fileName
     * @param fileType
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public InputStream getFileStream(String fileName, FileType fileType)throws FileNotFoundException, IOException;
    
    /**
     * Get a file from a local system
     * @param fileName
     * @param fileType
     * @return
     * @throws FileNotFoundException 
     */
    public File getFile(String fileName, FileType fileType)throws FileNotFoundException; 
    
    /**
     * Checks if a file exist in the local system
     * @param fileType
     * @param id
     * @return 
     */
    public boolean fileExists(FileType fileType, String id); 
    
    
    public String saveTemporalfile(InputStream is, String name, FileType type)throws IOException; 

    public void deleteTemporalFile(String videoPath) throws IOException;
    
    public enum FileType{
    	PROFILE_IMAGE, DARE_VIDEO, VIDEO_THUMBNAIL; 
    }
}
