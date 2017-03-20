package com.dareu.web.data.entity;

import javax.persistence.*;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Table(name = "response_anchor")
@Entity(name = "AnchoredContent")
public class AnchoredContent extends BaseEntity {
    
    @Column(name = "creation_date")
    private String creationDate; 
    
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(referencedColumnName = "id", updatable = false)
    private DareUser user;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(referencedColumnName = "id", updatable = false)
    private DareResponse response; 

    public AnchoredContent() {
        super(); 
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public DareUser getUser() {
        return user;
    }

    public void setUser(DareUser user) {
        this.user = user;
    }

    public DareResponse getResponse() {
        return response;
    }

    public void setResponse(DareResponse response) {
        this.response = response;
    }
    
    
}
