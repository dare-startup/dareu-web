package com.dareu.web.data.entity;

import javax.persistence.*;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Entity(name = "ResponseClap")
@Table(name = "response_clap")
public class ResponseClap extends BaseEntity{

    @Column(name = "clap_date")
    private String clapDate;
    
    @OneToOne(fetch = FetchType.EAGER,  cascade = CascadeType.REFRESH)
    @JoinColumn(name = "response_id")
    private DareResponse response; 
    
    @OneToOne(fetch = FetchType.EAGER,  cascade = CascadeType.REFRESH)
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
