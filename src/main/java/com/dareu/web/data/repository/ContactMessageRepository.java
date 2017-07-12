package com.dareu.web.data.repository;

import com.dareu.web.data.entity.ContactMessage;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.response.entity.ContactMessageDescription;
import com.dareu.web.dto.response.entity.Page;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public interface ContactMessageRepository extends BaseRepository<ContactMessage>{
    public Page<ContactMessageDescription> getByStatus(int pageNumber, ContactMessage.ContactMessageStatus contactMessageStatus)throws DataAccessException;
    
}
