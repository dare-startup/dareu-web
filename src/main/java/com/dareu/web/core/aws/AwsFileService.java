package com.dareu.web.core.aws;

import com.dareu.web.core.service.FileService;
import com.dareu.web.core.service.FileService.FileType;
import java.io.File;
import java.io.InputStream;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public interface AwsFileService {
    
    /**
     * Creates a new file on Amazon S3
     * @param file
     * @param fileType
     * @return the complete URL of the file
     */
    public String saveFile(File file, FileService.FileType fileType);

    /**
     * Creates a new file on Amazon S3 
     * @param stream
     * @param fileType
     * @param fileName 
     * @return  
     */
    public String saveFile(InputStream stream, FileType fileType, String fileName);
}
