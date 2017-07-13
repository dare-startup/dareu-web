package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.FileService;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.github.roar109.syring.annotation.ApplicationProperty.Types;
import org.apache.log4j.Logger;

import java.io.File;
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

    private final Logger log = Logger.getLogger(getClass());

    @Override
    public String saveTemporalFile(InputStream is, String name, FileType type) throws IOException {
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


}
