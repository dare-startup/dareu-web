package com.dareu.web.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "Friendship")
@Table(name = "friendship")
public class FriendshipRequest extends BaseEntity{
	
	@OneToOne(cascade = { CascadeType.REFRESH}, fetch = FetchType.EAGER, 
			optional = false, targetEntity = DareUser.class)
	@JoinColumn(name = "user_id")
	private DareUser user; 
	
	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER, 
			optional = false, targetEntity = DareUser.class)
	@JoinColumn(name = "requested_user_id")
	private DareUser requestedUser; 
	
	@Column(name = "request_date")
	private String requestDate; 
	
	@Column(name = "accepted")
	private boolean accepted; 

	public FriendshipRequest() {
	}

	public DareUser getUser() {
		return user;
	}

	public void setUser(DareUser user) {
		this.user = user;
	}

	public DareUser getRequestedUser() {
		return requestedUser;
	}

	public void setRequestedUser(DareUser requestedUser) {
		this.requestedUser = requestedUser;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	
	
}
