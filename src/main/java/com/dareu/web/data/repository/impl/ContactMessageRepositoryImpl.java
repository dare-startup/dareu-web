package com.dareu.web.data.repository.impl;

import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.data.entity.ContactMessage;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.ContactMessageRepository;
import com.dareu.web.dto.response.entity.ContactMessageDescription;
import com.dareu.web.dto.response.entity.Page;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Stateless
public class ContactMessageRepositoryImpl extends AbstractRepository 
implements ContactMessageRepository{

    @Inject
    private DareuAssembler dareuAssembler;

    public ContactMessageRepositoryImpl() {
        super(ContactMessage.class);
    }

    @Override
    public Page<ContactMessageDescription> getByStatus(int pageNumber, ContactMessage.ContactMessageStatus contactMessageStatus) throws DataAccessException {
        Page<ContactMessageDescription> messagesPage;
        List<ContactMessage> messages;
        List<ContactMessageDescription> descs;
        Long count;
        try {
            Query q = em.createQuery("SELECT m FROM ContactMessage m WHERE m.status = :status")
                    .setParameter("status", contactMessageStatus)
                    .setMaxResults(DEFAULT_PAGE_NUMBER)
                    .setFirstResult(getFirstResult(pageNumber));

            messages = q.getResultList();

            //get count
            q = em.createQuery("SELECT COUNT(m.id) FROM ContactMessage m WHERE m.status = :status")
                    .setParameter("status", contactMessageStatus);
            count = (Long) q.getSingleResult();

            messagesPage = new Page<>();
            messagesPage.setPageNumber(pageNumber);
            messagesPage.setPageSize(DEFAULT_PAGE_NUMBER);
            messagesPage.setPagesAvailable(getPagesAvailable(pageNumber, count.intValue()));
            descs = dareuAssembler.assembleContactMessageDescriptions(messages);
            messagesPage.setItems(descs);
            return messagesPage;
        } catch (Exception ex) {
            throw new DataAccessException("Could not get contact messages: " + ex.getMessage());
        }
    }
}
