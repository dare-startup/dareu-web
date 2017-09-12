package com.dareu.web.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "DareFlag")
@Table(name = "dare_flag")
public class DareFlag extends BaseEntity{
    
    @Column(name = "comment")
    private String comment;
    
    
    @Column(name = "flag_date")
    private String flagDate; 

    public DareFlag(String comment, String flagDate) {
        this.comment = comment;
        this.flagDate = flagDate;
    }

    public DareFlag() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFlagDate() {
        return flagDate;
    }

    public void setFlagDate(String flagDate) {
        this.flagDate = flagDate;
    }
    
    
}
