/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service;

import java.io.InputStream;

/**
 *
 * @author MACARENA
 */
public interface FileService {
    public String saveFile(InputStream stream, String filePath);
    public InputStream getFile(String filePath);
}
