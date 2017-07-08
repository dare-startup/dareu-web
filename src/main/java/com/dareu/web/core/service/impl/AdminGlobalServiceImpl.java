package com.dareu.web.core.service.impl;

import com.dareu.web.core.service.AdminGlobalService;
import com.dareu.web.data.entity.ContactMessage;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.ContactMessageRepository;
import com.dareu.web.dto.response.entity.ContactMessageDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.exception.application.InternalApplicationException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class AdminGlobalServiceImpl implements AdminGlobalService{

    @Inject
    private ContactMessageRepository contactMessageRepository;

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
}
