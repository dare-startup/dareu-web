/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.FileService;
import com.github.roar109.syring.annotation.ApplicationProperty;
import com.github.roar109.syring.annotation.ApplicationProperty.Types;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author MACARENA
 */
@Stateless
public class FileServiceImpl implements FileService {
	
	@Inject
	@ApplicationProperty(name = "profile.images.directory", type = Types.FILE)
	private String profileImagesDirectory; 
	
	@Inject
	@ApplicationProperty(name = "dare.videos.directory", type = Types.FILE)
	private String dareVideosDirectory; 

    public FileServiceImpl(){
        
    }

	@Override
	public String saveFile(InputStream stream, FileType fileType, String fileName) throws IOException{
		//create output directory name 
		String outputDirectory = "";
		if(fileType == FileType.PROFILE_IMAGE)
			outputDirectory = profileImagesDirectory + fileName; 
		else if(fileType == FileType.DARE_VIDEO)
			outputDirectory = dareVideosDirectory + fileName; 
		
		//create a new file 
		DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(outputDirectory))); 
		//read the input stream 
		byte[] buff = null; 
		while(stream.read(buff) != -1)
			out.write(buff);
		
		//close stream
		out.close();
		return outputDirectory;
	}

	@Override
	public InputStream getFile(String fileName, FileType fileType) throws FileNotFoundException, IOException{
		// 
		return null;
	}
    
}
