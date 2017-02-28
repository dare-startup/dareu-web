package com.dareu.web.data.repository.impl;

import com.dareu.web.data.entity.ContactMessage;
import com.dareu.web.data.repository.ContactMessageRepository;
import javax.ejb.Stateless;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Stateless
public class ContactMessageRepositoryImpl extends AbstractRepository 
implements ContactMessageRepository{

    public ContactMessageRepositoryImpl() {
        super(ContactMessage.class);
    }
    
}
