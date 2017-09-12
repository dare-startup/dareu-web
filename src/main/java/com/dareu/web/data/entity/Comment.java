package com.dareu.web.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "response_comment")
@Entity(name = "Comment")
public class Comment extends BaseEntity{
    
    @Column(name = "comment_date")
    private String commentDate; 
    
    @Column(name = "comment")
    private String comment;
    
    @OneToOne(fetch = FetchType.EAGER,  cascade = CascadeType.REFRESH)
    @JoinColumn(name = "response_id")
    private DareResponse response;
    
    @OneToOne(fetch = FetchType.EAGER,  cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private DareUser user;

    public Comment() {
        super();
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
    
    
    
    
}
