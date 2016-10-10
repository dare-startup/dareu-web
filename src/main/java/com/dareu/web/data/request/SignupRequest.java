/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.request;

import java.io.InputStream;

/**
 *
 * @author MACARENA
 */
public class SignupRequest {
    private String name; 
    private String email; 
    private String username; 
    private String password; 
    private InputStream image; 
    private String birthday; 

    public SignupRequest() {
    }

    public SignupRequest(String name, String email, String username, String password, InputStream image, String birthday) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
        this.birthday = birthday;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    
    
}
