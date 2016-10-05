package com.dareu.web.data.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Alberto Rubalcaba on 4/8/2015.
 */
@Entity(name = "DareResponse")
@Table(name = "dare_response")
public class DareResponse extends BaseEntity {
    
    @Column(name = "response_date")
    private String responseDate; 
    
    @Column(name = "video_url")
    private String videoPath;
    
    @Column(name = "views_count")
    private int viewsCount; 

    public DareResponse() {
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }
    
    
    
    
}
