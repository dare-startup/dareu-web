package com.dareu.web.core.service;

import com.dareu.web.data.entity.*;
import com.dareu.web.dto.jms.EmailRequest;
import com.dareu.web.dto.request.GoogleSignupRequest;
import com.dareu.web.dto.request.SignupRequest;
import com.dareu.web.dto.response.entity.*;

import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author jose.rubalcaba
 */
public interface DareuAssembler {
    /**
     * transform a dare user entity into a user account DTO
     * @param user
     * @return 
     */
    public UserAccount assembleUserAccount(DareUser user); 
    
    /**
     * transform a list of users into a page DTO
     * @param users
     * @param pageNumber
     * @return 
     */
    public Page<UserAccount> assembleUserAccountPage(List<DareUser> users, int pageNumber); 
    
    /**
     * Creates a page of categories DTO
     * @param categories
     * @param pageNumber
     * @return 
     */
    public Page<CategoryDescription> assembleCategories(List<Category> categories, int pageNumber); 
    
    /**
     * creates a page of dare descriptions
     * @param dares
     * @param pageNumber
     * @return 
     */
    public Page<DareDescription> assembleDareDescriptions(List<Dare> dares, int pageNumber);

    /**
     * creates a list
     * @param list
     * @param users
     * @return 
     */
    public Page<DiscoverUserAccount> assembleDiscoverUserAccounts(List<DiscoverUserAccount> list, Page<DareUser> users);

    /**
     * assemble a discover user account from a dare user
     * @param user
     * @return 
     */
    public DiscoverUserAccount assembleDiscoverUserAccount(DareUser user);

    /**
     * transforms friendship requests into friend search descriptions 
     * @param friendships
     * @param count
     * @return 
     */
    public List<FriendSearchDescription> transformFriendRequests(List<DareUser> friendships, Long count);

    /**
     * Assembles a list of dare descriptions
     * @param dares
     * @return 
     */
    public List<DareDescription> assembleDareDescriptions(List<Dare> dares);
    
    /**
     * Assembles a new user description from a dare user entity
     * @param user
     * @return 
     */
    public UserDescription assembleUserDescription(DareUser user);
    
    /**
     * Assembles a new dare description
     * @param dare
     * @return 
     */
    public DareDescription assembleDareDescription(Dare dare); 
    
    /**
     * Assembles a connection details object
     * @param request
     * @return 
     */
    public ConnectionDetails assembleConnectionDetails(FriendshipRequest request); 

    /**
     * 
     * @param dares
     * @return 
     */
    public List<CreatedDare> assembleCreatedDares(List<Dare> dares);
    
    /**
     * Assembles a new active dare from a dare entity
     * @param dare
     * @return 
     */
    public ActiveDare assembleActiveDare(Dare dare);

    /**
     * Assembles a list of dare response descriptions
     * @param responses
     * @return 
     */
    public List<DareResponseDescription> assembleDareResponseDescriptions(List<DareResponse> responses, String userId);

    /**
     * Get a list of dare response descriptions
     * @param list
     * @return 
     */
    public List<DareResponseDescription> getResponseDescriptions(List<DareResponse> list, String userId);

    /**
     * assembles a new response description
     * @param resp
     * @return 
     */
    public DareResponseDescription assembleDareResponseDescription(DareResponse resp, String userId);

    /**
     * Assembles a new comment description list
     * @param comments
     * @return 
     */
    public List<CommentDescription> assembleCommentDescriptions(List<Comment> comments, String userId);
    
    /**
     * Assemble a new comment description
     * @param comment
     * @return 
     */
    public CommentDescription assembleCommentDescription(Comment comment, String userId);
    
    /**
     * Creates a new account profile
     * @param user
     * @param createdDares
     * @param responses
     * @return 
     */
    public AccountProfile getAccountProfile(DareUser user, Page<CreatedDare> createdDares, Page<DareResponseDescription> responses, Page<FriendSearchDescription> contacts);
    
    /**
     * Get an unaccepted dare
     * @param dare
     * @return 
     */
    public UnacceptedDare getUnacceptedDare(Dare dare);

    /**
     * assemble a list of connection requests
     * @param list
     * @param sent
     * @return 
     */
    public List<ConnectionRequest> assembleConnectionRequests(List<FriendshipRequest> list, boolean sent);
    
    /**
     * Assembles a list of anchored content description
     * @param list
     * @return 
     */
    public List<AnchoredDescription> assembleAnchoredContent(List<AnchoredContent> list, String userId);

    /**
     * Creates a new dare user from a google sign up request
     * @param request
     * @return
     */
    public DareUser getDareUser(GoogleSignupRequest request);

    /**
     *
     * @param messages
     * @return
     */
    List<ContactMessageDescription> assembleContactMessageDescriptions(List<ContactMessage> messages);

    /**
     * assembles an email request to send a welcome email to userId
     * @param userId
     * @return
     */
    public EmailRequest assembleWelcomeEmailRequest(DareUser userId);
}
