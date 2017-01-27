package com.dareu.web.core.service;

import com.dareu.web.data.entity.Category;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.dto.response.entity.CategoryDescription;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.DiscoverUserAccount;
import com.dareu.web.dto.response.entity.FriendSearchDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.response.entity.UserAccount;
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
     * @param pageNumber
     * @return 
     */
    public Page<DiscoverUserAccount> assembleDiscoverUserAccounts(List<DiscoverUserAccount> list, int pageNumber);

    /**
     * assemble a discover user account from a dare user
     * @param user
     * @return 
     */
    public DiscoverUserAccount assembleDiscoverUserAccount(DareUser user);

    /**
     * transforms friendship requests into friend search descriptions 
     * @param userId
     * @param friendships
     * @param count
     * @return 
     */
    public List<FriendSearchDescription> transformFriendRequests(List<DareUser> friendships, Long count);

    
}