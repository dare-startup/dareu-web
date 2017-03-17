/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import com.dareu.web.core.aws.AwsFileService;
import com.dareu.web.core.service.FileService;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.github.roar109.syring.annotation.ApplicationProperty.Types;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    @ApplicationProperty(name = "dareu.multipart.tmp.directory", type = Types.SYSTEM)
    private String tmpDirectory; 

    @Inject
    private Logger log;
    
    @Inject
    private AwsFileService awsFileService;

    //current hosting provider
    private static final DareVideoHostingProvider currentHostingProvider = 
            DareVideoHostingProvider.AMAZON;

    public FileServiceImpl() {

    }

    @Override
    public String saveFile(InputStream stream, FileType fileType, String fileName) throws IOException {
        //create output directory name 
        String outputDirectory = "";
        log.info("Current hosting provider: " + currentHostingProvider); 
        log.info("FileType " + fileType);
        switch (fileType) {
            case PROFILE_IMAGE:
                switch(currentHostingProvider){
                    case AMAZON: 
                        return awsFileService.saveFile(stream, FileType.PROFILE_IMAGE, fileName);
                    case LOCAL:
                        outputDirectory = profileImagesDirectory + fileName;
                        writeLocalFile(outputDirectory, stream);
                        break; 
                }
                break;
            case DARE_VIDEO:
                outputDirectory = dareVideosDirectory + fileName;
                switch (currentHostingProvider) {
                    case LOCAL:
                        writeLocalFile(outputDirectory, stream); 
                        break;
                    case AMAZON:
                        return awsFileService.saveFile(stream, FileType.DARE_VIDEO, fileName);
                }
                break;
            case VIDEO_THUMBNAIL: 
                outputDirectory = dareVideoThumbDirectory + fileName; 
                switch(currentHostingProvider){
                    case LOCAL: 
                        writeLocalFile(outputDirectory, stream); 
                        break; 
                    case AMAZON: 
                        return awsFileService.saveFile(stream, FileType.VIDEO_THUMBNAIL, fileName);
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

    @Override
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

    @Override
    public String saveTemporalfile(InputStream is, String name, FileType type) throws IOException {
        String ext = ""; 
        switch(type){
            case DARE_VIDEO: 
                ext = ".mp4"; 
                break; 
            case PROFILE_IMAGE:
                ext = ".jpg";
                break; 
            case VIDEO_THUMBNAIL: 
                ext = ".jpg";
                break; 
        }
        File file = new File(tmpDirectory + name + ext); 
        file.createNewFile(); 
        
        FileOutputStream out = new FileOutputStream(file); 
        byte[] buff = new byte[1024];
        
        while((is.read(buff)) != -1)
            out.write(buff);
        
        out.close();
        return file.getAbsolutePath(); 
    }

    @Override
    public String saveFile(String filePath, FileType fileType, String fileName) throws IOException {
        //create output directory name 
        String outputDirectory = "";
        
        switch (fileType) {
            case PROFILE_IMAGE:
                switch(currentHostingProvider){
                    case AMAZON: 
                        return awsFileService.saveFile(new File(filePath), FileType.PROFILE_IMAGE);
                    case LOCAL:
                        outputDirectory = profileImagesDirectory + fileName;
                        writeLocalFile(outputDirectory, new FileInputStream(new File(filePath)));
                        break; 
                }
                break;
            case DARE_VIDEO:
                outputDirectory = dareVideosDirectory + fileName;
                switch (currentHostingProvider) {
                    case LOCAL:
                        writeLocalFile(outputDirectory, new FileInputStream(new File(filePath))); 
                        break;
                    case AMAZON:
                        return awsFileService.saveFile(new File(filePath), FileType.DARE_VIDEO);
                }
                break;
            case VIDEO_THUMBNAIL: 
                outputDirectory = dareVideoThumbDirectory + fileName; 
                switch(currentHostingProvider){
                    case LOCAL: 
                        writeLocalFile(outputDirectory, new FileInputStream(new File(filePath))); 
                        break; 
                    case AMAZON: 
                        return awsFileService.saveFile(new File(filePath), FileType.VIDEO_THUMBNAIL);
                }
                break; 
            default:
                throw new AssertionError();
        }
        return outputDirectory;
    }

    @Override
    public void deleteTemporalFile(String tmpFile) throws IOException {
        File file = new File(tmpFile);
        if(file.exists())
            file.delete(); 
    }

    enum DareVideoHostingProvider {

        LOCAL, AMAZON, MEGA, DROPBOX; //local and amazon supported, other two just in case
    }

}
