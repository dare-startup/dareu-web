/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.FileService;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.github.roar109.syring.annotation.ApplicationProperty.Types;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author MACARENA
 */
public class FileServiceImpl implements FileService {

    @Inject
    @ApplicationProperty(name = "profile.images.directory", type = Types.SYSTEM)
    private String profileImagesDirectory;

    @Inject
    @ApplicationProperty(name = "dare.videos.directory", type = Types.SYSTEM)
    private String dareVideosDirectory;
    
    @Inject
    @ApplicationProperty(name = "com.dareu.web.video.thumb.directory", type = Types.SYSTEM)
    private String dareVideoThumbDirectory; 

    @Inject
    private Logger log;

    //current hosting provider
    private static final DareVideoHostingProvider currentHostingProvider = DareVideoHostingProvider.LOCAL;

    public FileServiceImpl() {

    }

    @Override
    public String saveFile(InputStream stream, FileType fileType, String fileName) throws IOException {
        //create output directory name 
        String outputDirectory = "";

        switch (fileType) {
            case PROFILE_IMAGE:
                outputDirectory = profileImagesDirectory + fileName;
                writeLocalFile(outputDirectory, stream);
                break;
            case DARE_VIDEO:
                outputDirectory = dareVideosDirectory + fileName;
                switch (currentHostingProvider) {
                    case LOCAL:
                        writeLocalFile(outputDirectory, stream); 
                        break;
                    case AMAZON:
                        //TODO: change to bigger servers here
                        break;
                }
                break;
            case VIDEO_THUMBNAIL: 
                outputDirectory = dareVideoThumbDirectory + fileName; 
                switch(currentHostingProvider){
                    case LOCAL: 
                        writeLocalFile(outputDirectory, stream); 
                        break; 
                    case AMAZON: 
                        //TODO: save to amazon here
                        break; 
                }
                break; 
            default:
                throw new AssertionError();
        }
        return outputDirectory;
    }

    
    private void writeLocalFile(String fileName, InputStream stream) throws IOException {
        //create a new file 
        DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(fileName)));
        DataInputStream in = new DataInputStream(stream);
        //read the input stream
        byte[] buff = new byte[1024];
        int i = 0;
        while ((i = in.read(buff)) != -1) {
            out.write(buff);
        }
        //close stream
        out.close();
    }

    @Override
    public InputStream getFileStream(String fileName, FileType fileType) throws FileNotFoundException, IOException {
        File file = null; 
        switch (fileType) {
            case DARE_VIDEO:
                file = new File(dareVideosDirectory + fileName + ".mp4"); 
                break; 
            case PROFILE_IMAGE:
                file = new File(profileImagesDirectory + fileName + ".jpg");
                break; 
            case VIDEO_THUMBNAIL: 
                file = new File(dareVideoThumbDirectory + fileName + ".jpg"); 
                break; 
            default:
                return null;
        }
        log.info("File stream name " + file.getAbsolutePath()); 
        if(file.exists())
            return new FileInputStream(file);
        else throw new FileNotFoundException("No file " + file.getAbsolutePath() + " found"); 
    }

    public boolean fileExists(FileType fileType, String id) {
        InputStream stream = null; 
        try{
            switch(fileType){
                case DARE_VIDEO: 
                    stream = getFileStream(id + ".mp4", FileType.DARE_VIDEO); 
                    break; 
                case PROFILE_IMAGE: 
                    stream = getFileStream(id + ".jpg", FileType.PROFILE_IMAGE);
                    break; 
                case VIDEO_THUMBNAIL:
                    stream = getFileStream(id + ".jpg", FileType.VIDEO_THUMBNAIL); 
                    break; 
            }
            
            return stream != null; 
        }catch(IOException ex){
            return false; 
        }
    }
    
    private File getFile(FileType fileType, String fileName)throws FileNotFoundException{
        switch (fileType) {
            case DARE_VIDEO:
                return new File(dareVideosDirectory + fileName);
            case PROFILE_IMAGE:
                return new File(profileImagesDirectory + fileName);
            case VIDEO_THUMBNAIL: 
                return new File(dareVideoThumbDirectory + fileName); 
            default:
                return null;
        }
    }

    @Override
    public File getFile(String fileName, FileType fileType) throws FileNotFoundException {
        return getFile(fileType, fileName); 
    }

    enum DareVideoHostingProvider {

        LOCAL, AMAZON, MEGA, DROPBOX; //local and amazon supported, other two just in case
    }

}
