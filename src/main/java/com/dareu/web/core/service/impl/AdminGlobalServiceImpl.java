package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.AdminGlobalService;
import com.dareu.web.core.service.MessagingService;
import com.dareu.web.data.entity.ContactMessage;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.ContactMessageRepository;
import com.dareu.web.dto.request.ContactReplyRequest;
import com.dareu.web.dto.response.entity.ContactMessageDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class AdminGlobalServiceImpl implements AdminGlobalService{

    @Inject
    private ContactMessageRepository contactMessageRepository;

    @Inject
    private MessagingService messagingService;

    @Override
    public Response getPendingContactMessages(int pageNumber) throws InternalApplicationException {
        if(pageNumber <= 0)
            pageNumber = 1;

        try{
            Page<ContactMessageDescription> page = contactMessageRepository.getByStatus(pageNumber, ContactMessage.ContactMessageStatus.PENDING);
            return Response.ok(page)
                    .build();
        }catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response replyContactMessage(ContactReplyRequest request) throws InvalidRequestException, InternalApplicationException {
        if(request == null)
            throw new InvalidRequestException("No contact reply body was provided");
        else if(StringUtils.isBlank(request.getContactMessageId()))
            throw new InvalidRequestException("No contact message id was provided");
        else if(StringUtils.isBlank(request.getBody()))
            throw new InvalidRequestException("No contact message body was provided");

        try{
            ContactMessage contactMessage = contactMessageRepository.find(request.getContactMessageId());
            if(contactMessage == null)
                throw new InvalidRequestException("Contact message id is not valid");
            //send email jms message
            //TODO:
        }catch(DataAccessException ex){

        }
    }
}
