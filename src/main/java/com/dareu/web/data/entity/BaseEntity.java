package com.dareu.web.data.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by Alberto Rubalcaba on 4/7/2015.
 */
@MappedSuperclass
public abstract class BaseEntity {
	
	@Id
	@Column(name = "id")
    private String id;

    public BaseEntity(){
        this.id = UUID.randomUUID().toString();
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }
}
