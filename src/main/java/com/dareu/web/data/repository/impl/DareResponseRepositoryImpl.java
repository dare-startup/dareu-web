/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository.impl;

import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.repository.DareResponseRepository;

import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author MACARENA
 */
public class DareResponseRepositoryImpl extends AbstractRepository<DareResponse>implements DareResponseRepository{
    public DareResponseRepositoryImpl(){
        super(DareResponse.class);
    }
}
