package com.dareu.web.core.service.impl;

import java.util.logging.Logger;

import javax.inject.Inject;

import com.dareu.web.core.service.DareuFcmService;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;
import com.google.firebase.FirebaseApp;

public class DareuFcmServiceImpl implements DareuFcmService{

	@Inject
	private Logger log; 
	
	public DareuFcmServiceImpl(){
		
	}
	
	public void sendNewDareNotification(Dare dare, DareUser user) {
		// TODO Auto-generated method stub
		
	}

}
