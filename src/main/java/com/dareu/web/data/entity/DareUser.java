package com.dareu.web.data.entity;


import java.security.Principal;

import javax.persistence.Entity;

/**
 * Created by Alberto Rubalcaba on 4/7/2015.
 */
@Entity(name = "User")
public class DareUser extends BaseEntity{
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String userSince;
    private String GCM;
    private int coins;
    private int uScore;
    private boolean verified;
    private String role;
    private String imagePath;
    private String gender;
    private String birthday;

    public DareUser() {
    }

    public DareUser(String email, String name,
                    String password, String nickname,
                    String userSince, String GCM,
                    int coins, int uScore, boolean verified,
                    String role, String imagePath,
                    String gender, String birthday) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userSince = userSince;
        this.GCM = GCM;
        this.coins = coins;
        this.uScore = uScore;
        this.verified = verified;
        this.role = role;
        this.name = name;
        this.imagePath = imagePath;
        this.gender = gender;
        this.birthday = birthday;
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

    public void setGCM(String GMC) {
        this.GCM = GMC;
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

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
