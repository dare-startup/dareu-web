/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.DareUserRepository;
import javax.ejb.Stateless;

/**
 *
 * @author MACARENA
 */
@Stateless
public class DareUserRepositoryImpl extends AbstractRepository<DareUser> implements DareUserRepository{
    public DareUserRepositoryImpl(){
        super(DareUser.class); 
    }
}
