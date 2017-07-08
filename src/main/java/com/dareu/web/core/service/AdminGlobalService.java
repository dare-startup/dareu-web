package com.dareu.web.core.service;

import com.dareu.web.exception.application.InternalApplicationException;

import javax.ws.rs.core.Response;

public interface AdminGlobalService {

    public Response getPendingContactMessages(int pageNumber)throws InternalApplicationException;
}
