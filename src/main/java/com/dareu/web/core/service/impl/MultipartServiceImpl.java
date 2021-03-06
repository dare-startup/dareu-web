/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.MultipartService;
import com.dareu.web.dto.request.DareUploadRequest;
import com.dareu.web.dto.request.SignupRequest;
import com.dareu.web.exception.application.InvalidRequestException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ws.rs.core.MultivaluedMap;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author MACARENA
 */
public class MultipartServiceImpl implements MultipartService {

    @Override
    public InputStream getImageProfile(MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> map = input.getFormDataMap();
        List<InputPart> inputParts = map.get("image");
        String fileName;
        InputStream stream = null;
        for (InputPart part : inputParts) {
            MultivaluedMap<String, String> header = part.getHeaders();
            fileName = getFileName(header);
            stream = part.getBody(InputStream.class, null);
            break;
        }

        return stream;
    }

    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    public DareUploadRequest getDareUploadRequest(MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> map = input.getFormDataMap();
        List<InputPart> inputParts = map.get("file");
        List<InputPart> dareIdParts = map.get("dareId");
        List<InputPart> commentParts = map.get("comment");
        List<InputPart> thumbParts = map.get("thumb");
        String fileName;
        String dareId = "";
        String comment = "";
        InputStream stream = null;
        InputStream thumb = null;
        if (inputParts != null) {
            for (InputPart part : inputParts) {
                MultivaluedMap<String, String> header = part.getHeaders();
                fileName = getFileName(header);
                stream = part.getBody(InputStream.class, null);
                break;
            }
        }

        if (dareIdParts != null) {
            //get dare Id 
            for (InputPart part : dareIdParts) {
                dareId = part.getBodyAsString();
                break;
            }
        }

        if (commentParts != null) {
            //get dare Id 
            for (InputPart part : commentParts) {
                comment = part.getBodyAsString();
                break;
            }
        }

        if (thumbParts != null) {
            for (InputPart part : thumbParts) {
                thumb = part.getBody(InputStream.class, null);
                break;
            }
        }

        return new DareUploadRequest(dareId, stream, comment, thumb);
    }

}
