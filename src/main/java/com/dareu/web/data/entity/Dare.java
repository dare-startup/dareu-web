package com.dareu.web.data.entity;

import com.dareu.core.pagination.Page;

import java.util.Date;

/**
 * Created by Alberto Rubalcaba on 4/8/2015.
 */
public class Dare extends BaseEntity{

    private String name;
    private String description;
    private String category;
    private int estimatedDareTime;
    private int dareCoinsPrize;
    private boolean approved;
    private boolean accepted;
    private Date creationDate;
    private String userId;
    private String daredUserId;
    private String acceptedDate;
    private boolean active;

    public Dare(String name, String description, String category,
                int estimatedDareTime, int dareCoinsPrize, boolean approved,
                boolean accepted, Date creationDate, String userId,
                String daredUserId, String acceptedDate, boolean active) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.estimatedDareTime = estimatedDareTime;
        this.dareCoinsPrize = dareCoinsPrize;
        this.approved = approved;
        this.accepted = accepted;
        this.creationDate = creationDate;
        this.userId = userId;
        this.daredUserId = daredUserId;
        this.acceptedDate = acceptedDate;
        this.active = active;
    }

    public Dare() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEstimatedDareTime() {
        return estimatedDareTime;
    }

    public void setEstimatedDareTime(int estimatedDareTime) {
        this.estimatedDareTime = estimatedDareTime;
    }

    public int getDareCoinsPrize() {
        return dareCoinsPrize;
    }

    public void setDareCoinsPrize(int dareCoinsPrize) {
        this.dareCoinsPrize = dareCoinsPrize;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getCategory(){
        return this.category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDaredUserId() {
        return daredUserId;
    }

    public void setDaredUserId(String daredUserId) {
        this.daredUserId = daredUserId;
    }

    public String getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(String acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
