package com.dareu.web.data.entity;

import java.security.Principal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.dareu.web.dto.security.SecurityRole;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Created by Alberto Rubalcaba on 4/7/2015.
 */
@Entity(name = "User")
@Table(name = "dareu_user")
public class DareUser extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "user_since_date")
    private String userSince;

    @Column(name = "gcm_reg_id")
    private String GCM;

    @Column(name = "coins")
    private int coins;

    @Column(name = "uscore")
    private int uScore;

    @Column(name = "verified")
    private boolean verified;

    @Column(name = "g_id")
	private String googleId;

    @Enumerated
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "role")
    @Enumerated(EnumType.ORDINAL)
    private SecurityRole role;

    @Column(name = "birthday")
    private String birthday;
    
    @Column(name = "security_token")
    private String securityToken;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Transient
    @OneToMany(mappedBy = "requestedUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<FriendshipRequest> receivedFriendshipRequests; 
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Transient
    private Set<FriendshipRequest> sentFriendshipRequests; 

    public DareUser() {
    	super(); 
    }

	public DareUser(String name, String email, String password, String userSince, String gCM, int coins,
			int uScore, boolean verified, SecurityRole role, String imagePath,
			String birthday, String securityToken) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.userSince = userSince;
		GCM = gCM;
		this.coins = coins;
		this.uScore = uScore;
		this.verified = verified;
		this.role = role;
		this.birthday = birthday;
		this.securityToken = securityToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserSince() {
		return userSince;
	}

	public void setUserSince(String userSince) {
		this.userSince = userSince;
	}

	public String getGCM() {
		return GCM;
	}

	public void setGCM(String gCM) {
		GCM = gCM;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getuScore() {
		return uScore;
	}

	public void setuScore(int uScore) {
		this.uScore = uScore;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public SecurityRole getRole() {
		return role;
	}

	public void setRole(SecurityRole role) {
		this.role = role;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

    public Set<FriendshipRequest> getReceivedFriendshipRequests() {
        return receivedFriendshipRequests;
    }

    public void setReceivedFriendshipRequests(Set<FriendshipRequest> receivedFriendshipRequests) {
        this.receivedFriendshipRequests = receivedFriendshipRequests;
    }

    public Set<FriendshipRequest> getSentFriendshipRequests() {
        return sentFriendshipRequests;
    }

    public void setSentFriendshipRequests(Set<FriendshipRequest> sentFriendshipRequests) {
        this.sentFriendshipRequests = sentFriendshipRequests;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public enum AccountType{
        LOCAL, G_PLUS
    }
}
