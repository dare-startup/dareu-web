package com.dareu.web.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "DareUserDare")
@Table(name = "dareu_user_dare")
public class DareUserDare extends BaseEntity {

	@Transient
	private static final long serialVersionUID = 1L;

	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER, 
			optional = false, targetEntity = DareUser.class)
	@JoinColumn(name = "challenger_id")
	private DareUser challenger;
	
	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER, 
			optional = false, targetEntity = DareUser.class)
	@JoinColumn(name = "challenged_id")
	private DareUser challenged;
	
	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER, 
			optional = false, targetEntity = Dare.class)
	@JoinColumn(name = "dare_id")
	private Dare dare;

	public DareUser getChallenger() {
		return challenger;
	}

	public void setChallenger(DareUser challenger) {
		this.challenger = challenger;
	}

	public DareUser getChallenged() {
		return challenged;
	}

	public void setChallenged(DareUser challenged) {
		this.challenged = challenged;
	}

	public Dare getDare() {
		return dare;
	}

	public void setDare(Dare dare) {
		this.dare = dare;
	}
	
	
}
