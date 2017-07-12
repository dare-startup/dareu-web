package com.dareu.web.core.service;

import com.dareu.web.dto.request.ContactReplyRequest;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;

import javax.ws.rs.core.Response;

public interface AdminGlobalService {
    public Response getPendingContactMessages(int pageNumber)throws InternalApplicationException;
    public Response replyContactMessage(ContactReplyRequest request)throws InvalidRequestException, InternalApplicationException;
}
