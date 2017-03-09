package com.dareu.web.core.aws.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.dareu.web.core.aws.AwsFileService;
import com.dareu.web.core.service.FileService;
import com.github.roar109.syring.annotation.ApplicationProperty;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public class AwsFileServiceImpl implements AwsFileService {

    private static final String PROFILE_BUCKET = "dareu-profiles";
    private static final String THUMB_BUCKET = "dareu-thumbs";
    private static final String VIDEO_BUCKET = "dareu-uploads";
    private static final String BASE_URL = "https://%s.s3.amazonaws.com/%s";

    private AmazonS3 client;

    @Inject
    public AwsFileServiceImpl(
            @ApplicationProperty(name = "amazon.access.key", type = ApplicationProperty.Types.SYSTEM) String accessKey,
            @ApplicationProperty(name = "amazon.secret.key", type = ApplicationProperty.Types.SYSTEM) String secretKey) {
        client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Override
    public String saveFile(File file, FileService.FileType fileType) {
        String url = "";
        PutObjectRequest request = null;
        switch (fileType) {
            case PROFILE_IMAGE:
                request = new PutObjectRequest(PROFILE_BUCKET, file.getName(), file);
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                //generate url
                url = String.format(BASE_URL, PROFILE_BUCKET, file.getName());
                break;
            case VIDEO_THUMBNAIL:
                request = new PutObjectRequest(THUMB_BUCKET, file.getName(), file);
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                url = String.format(BASE_URL, THUMB_BUCKET, file.getName());
                break;
            case DARE_VIDEO:
                request = new PutObjectRequest(VIDEO_BUCKET, file.getName(), file);
                request.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(request);
                url = String.format(BASE_URL, VIDEO_BUCKET, file.getName());
                break;
        }
        return url;
    }

    @Override
    public String saveFile(InputStream stream, FileService.FileType fileType, String fileName) {
        String url = "";
        PutObjectRequest request = null;
        ObjectMetadata meta = new ObjectMetadata();
        //get bytes 
        try {
            byte[] bytes = IOUtils.toByteArray(stream);
            meta.setContentLength(bytes.length);
            switch (fileType) {
                case PROFILE_IMAGE:
                    request = new PutObjectRequest(PROFILE_BUCKET, fileName, stream, meta);
                    request.setCannedAcl(CannedAccessControlList.PublicRead);
                    client.putObject(request);
                    //generate url
                    url = String.format(BASE_URL, PROFILE_BUCKET, fileName);
                    break;
                case VIDEO_THUMBNAIL:
                    request = new PutObjectRequest(THUMB_BUCKET, fileName, stream, meta);
                    request.setCannedAcl(CannedAccessControlList.PublicRead);
                    client.putObject(request);
                    url = String.format(BASE_URL, THUMB_BUCKET, fileName);
                    break;
                case DARE_VIDEO:
                    request = new PutObjectRequest(VIDEO_BUCKET, fileName, stream, meta);
                    request.setCannedAcl(CannedAccessControlList.PublicRead);
                    client.putObject(request);
                    url = String.format(BASE_URL, VIDEO_BUCKET, fileName);
                    break;
            }
        } catch (IOException ex) {
            
        }
        return url;
    }

}
