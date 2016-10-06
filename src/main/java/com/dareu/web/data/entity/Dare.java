package com.dareu.web.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
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
    
    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, 
    		optional = true, targetEntity = Category.class)
    private Category category; 
    
    @Column(name = "estimated_dare_time")
    private int estimatedDareTime;
    
    @Column(name = "approved")
    private boolean approved;
    
    @Column(name = "accepted")
    private boolean accepted;
    
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @ManyToMany
    @JoinTable(name = "dareu_user_dare",
    		   joinColumns = @JoinColumn(name = "challenger_id", referencedColumnName = "id"), 
    		   inverseJoinColumns = @JoinColumn(name = "challenged_id", referencedColumnName = "id"))
    private List<DareUser> users; 

    public Dare(String name, String description, String category,
                int estimatedDareTime, boolean approved,
                boolean accepted, Date creationDate) {
        this.name = name;
        this.description = description;
        this.estimatedDareTime = estimatedDareTime;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
    
    
}
