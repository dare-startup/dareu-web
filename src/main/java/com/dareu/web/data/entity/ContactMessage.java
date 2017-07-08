package com.dareu.web.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Table(name = "contact_message")
@Entity(name = "ContactMessage")
public class ContactMessage extends BaseEntity {

    @Column(name = "name")
    private String name;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "comment")
    private String comment;
    
    @Column(name = "message_date")
    private String datetime;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ContactMessageStatus status; 

    public ContactMessage(String name, String email, String comment, String datetime, ContactMessageStatus status) {
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.datetime = datetime;
        this.status = status;
    }



    public ContactMessage() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public ContactMessageStatus getStatus() {
        return status;
    }

    public void setStatus(ContactMessageStatus status) {
        this.status = status;
    }

    
    public static enum ContactMessageStatus{
        REPLIED("replied"),
        PENDING("pending"),
        DISCARDED("discarded");

        String value;
        ContactMessageStatus(String value){
            this.value = value;
        }
        
    }
}
