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
    public InputStream getFile(String fileName, FileType fileType) throws FileNotFoundException, IOException {
        switch (fileType) {
            case DARE_VIDEO:
                log.info("Looking for file " + dareVideosDirectory + fileName);
                return new FileInputStream(new File(dareVideosDirectory + fileName));
            case PROFILE_IMAGE:
                log.info("Looking for file " + profileImagesDirectory + fileName);
                return new FileInputStream(new File(profileImagesDirectory + fileName));
            default:
                return null;
        }
    }

    @Override
    public boolean userHasProfileImage(String userId) {
        InputStream stream; 
        try{
            stream = getFile(userId + ".jpg", FileType.PROFILE_IMAGE); 
            return stream != null; 
        }catch(IOException ex){
            return false; 
        }
    }

    enum DareVideoHostingProvider {

        LOCAL, AMAZON, MEGA, DROPBOX; //local and amazon supported, other two just in case
    }

}
