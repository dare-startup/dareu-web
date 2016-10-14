package com.dareu.web.core.service;

import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;

public interface DareuFcmService {
	public void sendNewDareNotification(Dare dare, DareUser user); 
}
