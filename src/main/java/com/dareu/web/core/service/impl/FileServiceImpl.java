/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.FileService;
import java.io.InputStream;
import javax.ejb.Stateless;

/**
 *
 * @author MACARENA
 */
@Stateless
public class FileServiceImpl implements FileService {

    public FileServiceImpl(){
        
    }
    
    public String saveFile(InputStream stream, String filePath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InputStream getFile(String filePath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
