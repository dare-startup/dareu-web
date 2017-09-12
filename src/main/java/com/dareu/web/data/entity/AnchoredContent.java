package com.dareu.web.data.entity;

import javax.persistence.*;

@Table(name = "response_anchor")
@Entity(name = "AnchoredContent")
public class AnchoredContent extends BaseEntity {
    
    @Column(name = "creation_date")
    private String creationDate; 
    
    @ManyToOne()
    @JoinColumn(referencedColumnName = "id")
    private DareUser user;

    @ManyToOne()
    @JoinColumn(referencedColumnName = "id")
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
