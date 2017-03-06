package com.dareu.web.core.aws.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dareu.web.core.aws.AwsFileService;
import com.dareu.web.core.service.FileService;
import com.github.roar109.syring.annotation.ApplicationProperty;
import java.io.File;
import java.io.InputStream;
import javax.inject.Inject;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public class AwsFileServiceImpl implements AwsFileService{

    @Inject
    @ApplicationProperty(name = "amazon.access.key", type = ApplicationProperty.Types.SYSTEM)
    private String accessKey;
    
    @Inject
    @ApplicationProperty(name = "amazon.secret.key", type = ApplicationProperty.Types.SYSTEM)
    private String secretKey;
    
    private static final String PROFILE_BUCKET = "dareu-profiles";
    private static final String THUMB_BUCKET = "dareu-thumbs";
    private static final String VIDEO_BUCKET = "dareu-uploads";
    private static final String BASE_URL = "https://s3.amazonaws.com/%s/%s";
    
    private AmazonS3 client;
    public AwsFileServiceImpl(){
        client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
    }
    
    @Override
    public String saveFile(File file, FileService.FileType fileType) {
        String url = "";
        PutObjectRequest request = null;
        switch(fileType){
            case PROFILE_IMAGE: 
                request = new PutObjectRequest(PROFILE_BUCKET, file.getName(), file);
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                //generate url
                url = String.format(BASE_URL, PROFILE_BUCKET, file.getName());
                break;
            case VIDEO_THUMBNAIL:
                request = new PutObjectRequest(VIDEO_BUCKET, file.getName(), file);
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                url = String.format(BASE_URL, VIDEO_BUCKET, file.getName());
                break;
            case DARE_VIDEO: 
                request = new PutObjectRequest(THUMB_BUCKET, file.getName(), file);
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                url = String.format(BASE_URL, THUMB_BUCKET, file.getName());
                break;
        }
        return url;
    }

    @Override
    public String saveFile(InputStream stream, FileService.FileType fileType, String fileName) {
        String url = "";
        PutObjectRequest request = null;
        switch(fileType){
            case PROFILE_IMAGE: 
                request = new PutObjectRequest(PROFILE_BUCKET, fileName, stream, new ObjectMetadata());
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                //generate url
                url = String.format(BASE_URL, PROFILE_BUCKET, fileName);
                break;
            case VIDEO_THUMBNAIL:
                request = new PutObjectRequest(VIDEO_BUCKET, fileName, stream, new ObjectMetadata());
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                url = String.format(BASE_URL, VIDEO_BUCKET, fileName);
                break;
            case DARE_VIDEO: 
                request = new PutObjectRequest(THUMB_BUCKET, fileName, stream, new ObjectMetadata());
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                url = String.format(BASE_URL, THUMB_BUCKET, fileName);
                break;
        }
        return url;
    }
    
}
