/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.AccountService;
import com.dareu.web.data.repository.DareUserRepository;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author MACARENA
 */
@Stateless
public class AccountServiceImpl implements AccountService{
    
    @Inject
    private DareUserRepository dareUserRepository; 
    
    public AccountServiceImpl(){
        
    }
}
