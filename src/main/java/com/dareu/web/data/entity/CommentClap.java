package com.dareu.web.data.entity;

import javax.persistence.*;

/**
 * Created by jose.rubalcaba on 03/22/2017.
 */
@Table(name = "comment_clap")
@Entity(name = "CommentClap")
public class CommentClap extends BaseEntity{

    @Column(name = "clap_date")
    private String clapDate;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private DareUser user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Comment comment;

    public CommentClap() {
        super();
    }

    public String getClapDate() {
        return clapDate;
    }

    public void setClapDate(String clapDate) {
        this.clapDate = clapDate;
    }

    public DareUser getUser() {
        return user;
    }

    public void setUser(DareUser user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
