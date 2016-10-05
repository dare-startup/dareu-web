package com.dareu.web.data.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Created by Alberto Rubalcaba on 4/8/2015.
 */
@Entity(name = "Dare")
@Table(name = "dare")
public class Dare extends BaseEntity{

    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "category_id")
    private String category;
    
    @Column(name = "estimated_dare_time")
    private int estimatedDareTime;
    
    @Column(name = "coins_prize")
    private int dareCoinsPrize;
    
    @Column(name = "approved")
    private boolean approved;
    
    @Column(name = "accepted")
    private boolean accepted;
    
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    

    public Dare(String name, String description, String category,
                int estimatedDareTime, int dareCoinsPrize, boolean approved,
                boolean accepted, Date creationDate) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.estimatedDareTime = estimatedDareTime;
        this.dareCoinsPrize = dareCoinsPrize;
        this.approved = approved;
        this.accepted = accepted;
        this.creationDate = creationDate;
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
    
    
}
