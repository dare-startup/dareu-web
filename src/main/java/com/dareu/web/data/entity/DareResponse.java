package com.dareu.web.data.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by Alberto Rubalcaba on 4/8/2015.
 */
@Entity(name = "DareResponse")
@Table(name = "dare_response")
public class DareResponse extends BaseEntity {

    @Column(name = "response_date")
    private String responseDate;

    @Column(name = "views_count")
    private int viewsCount;

    @Column(name = "comment")
    private String comment;

    @OneToOne(fetch = FetchType.EAGER,  cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private DareUser user;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "dare_id")
    private Dare dare;
    
    @OneToMany(cascade = CascadeType.REFRESH, 
            fetch = FetchType.EAGER, mappedBy = "response")
    private List<ResponseClap> claps; 
    
    @Column(name = "last_update")
    private String lastUpdate; 
    
    @Column(name = "thumb_url")
    private String thumbUrl;
    
    @Column(name = "video_url")
    private String videoUrl;

    public DareResponse() {
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public DareUser getUser() {
        return user;
    }

    public void setUser(DareUser user) {
        this.user = user;
    }

    public Dare getDare() {
        return dare;
    }

    public void setDare(Dare dare) {
        this.dare = dare;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<ResponseClap> getClaps() {
        return claps;
    }

    public void setClaps(List<ResponseClap> claps) {
        this.claps = claps;
    }

    
    
}
