package com.dareu.web.data.entity;

import com.dareu.core.pagination.Page;

import java.util.Date;

/**
 * Created by Alberto Rubalcaba on 4/8/2015.
 */
public class DareResponse extends BaseEntity {
    private String dareName;
    private String dareId;
    private String userId;
    private String creationDate;
    private boolean approved;
    private String approvedDate;
    private String dareVideoUrl;
    private String dareVideoThumbnailUrl;
    private int views;
    private boolean featuredByAdmin;
    private int claps;
    private String userProfileUrl;

    public DareResponse(String dareId, String userId, String creationDate,
                        boolean approved, String approvedDate,
                        String dareVideoUrl, String dareVideoThumbnailUrl,
                        int views, boolean featuredByAdmin, int claps,
                        String userProfileUrl) {
        this.dareId = dareId;
        this.userId = userId;
        this.creationDate = creationDate;
        this.approved = approved;
        this.approvedDate = approvedDate;
        this.dareVideoUrl = dareVideoUrl;
        this.dareVideoThumbnailUrl = dareVideoThumbnailUrl;
        this.views = views;
        this.featuredByAdmin = featuredByAdmin;
        this.claps = claps;
        this.userProfileUrl = userProfileUrl;
    }

    public DareResponse() {
    }

    public String getDareId() {
        return dareId;
    }

    public void setDareId(String dareId) {
        this.dareId = dareId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getDareVideoUrl() {
        return dareVideoUrl;
    }

    public void setDareVideoUrl(String dareVideoUrl) {
        this.dareVideoUrl = dareVideoUrl;
    }

    public String getDareVideoThumbnailUrl() {
        return dareVideoThumbnailUrl;
    }

    public void setDareVideoThumbnailUrl(String dareVideoThumbnailUrl) {
        this.dareVideoThumbnailUrl = dareVideoThumbnailUrl;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isFeaturedByAdmin() {
        return featuredByAdmin;
    }

    public void setFeaturedByAdmin(boolean featuredByAdmin) {
        this.featuredByAdmin = featuredByAdmin;
    }

    public int getClaps() {
        return claps;
    }

    public void setClaps(int claps) {
        this.claps = claps;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public String getDareName() {
        return dareName;
    }

    public void setDareName(String dareName) {
        this.dareName = dareName;
    }
}
