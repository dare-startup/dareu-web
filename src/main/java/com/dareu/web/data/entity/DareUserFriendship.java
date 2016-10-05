package com.dareu.web.data.entity;

/**
 * Created by Alberto Rubalcaba on 4/27/2015.
 */
public class DareUserFriendship extends BaseEntity{
    private String userId;
    private String requestedUserId;
    private boolean accepted;
    private String requestDate;
    private String acceptedSince;
    private String requestMessage;
    private String requestTitle;

    public DareUserFriendship(String userId, String requestedUserId, boolean accepted, String requestDate, String acceptedSince, String requestMessage, String requestTitle) {
        this.userId = userId;
        this.requestedUserId = requestedUserId;
        this.accepted = accepted;
        this.requestDate = requestDate;
        this.acceptedSince = acceptedSince;
        this.requestMessage = requestMessage;
        this.requestTitle = requestTitle;
    }

    public DareUserFriendship() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestedUserId() {
        return requestedUserId;
    }

    public void setRequestedUserId(String requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getAcceptedSince() {
        return acceptedSince;
    }

    public void setAcceptedSince(String acceptedSince) {
        this.acceptedSince = acceptedSince;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }
}
