package com.dareu.web.data.entity;

import javax.persistence.*;

@Entity(name = "ResponseClap")
@Table(name = "response_clap")
public class ResponseClap extends BaseEntity{

    @Column(name = "clap_date")
    private String clapDate;

    @ManyToOne
    @JoinColumn(name = "response_id")
    private DareResponse response; 
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private DareUser user; 
    
    public ResponseClap(){
        super(); 
    }

    public DareResponse getResponse() {
        return response;
    }

    public void setResponse(DareResponse response) {
        this.response = response;
    }

    public DareUser getUser() {
        return user;
    }

    public void setUser(DareUser user) {
        this.user = user;
    }

    public String getClapDate() {
        return clapDate;
    }

    public void setClapDate(String clapDate) {
        this.clapDate = clapDate;
    }
}
