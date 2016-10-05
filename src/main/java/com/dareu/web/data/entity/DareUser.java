package com.dareu.web.data.entity;

import java.security.Principal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    @Column(name = "username")
    private String nickname;

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

    @Column(name = "role")
    private String role;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "birthday")
    private String birthday;

    public DareUser() {
    }

    public DareUser(String name, String email, String password,
            String nickname, String userSince, String gCM, int coins,
            int uScore, boolean verified, String role, String imagePath,
            String birthday) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userSince = userSince;
        GCM = gCM;
        this.coins = coins;
        this.uScore = uScore;
        this.verified = verified;
        this.role = role;
        this.imagePath = imagePath;
        this.birthday = birthday;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
