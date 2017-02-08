package com.dareu.web.core.service.impl;

import com.dareu.web.core.DareUtils;
import com.dareu.web.data.entity.Category;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.dto.response.entity.ActiveDare;
import com.dareu.web.dto.response.entity.CategoryDescription;
import com.dareu.web.dto.response.entity.ConnectionDetails;
import com.dareu.web.dto.response.entity.CreatedDare;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.DiscoverUserAccount;
import com.dareu.web.dto.response.entity.FriendSearchDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.response.entity.UserAccount;
import com.dareu.web.dto.response.entity.UserDescription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jose.rubalcaba
 */
public class DareuAssemblerImpl implements DareuAssembler{

    public DareuAssemblerImpl() {
    }

    @Override
    public Page<CategoryDescription> assembleCategories(List<Category> categories, int pageNumber) {
        Page<CategoryDescription> cats = new Page<CategoryDescription>(); 
        List<CategoryDescription> list = new ArrayList(); 
        for(Category category : categories)
            list.add(new CategoryDescription(category.getId(), category.getName(), category.getDescription())); 
        cats.setItems(list);
        cats.setPageNumber(pageNumber);
        cats.setPageSize(20);
        return cats; 
    }
    
    @Override
    public UserAccount assembleUserAccount(DareUser user) {
        UserAccount account = new UserAccount(user.getId(), user.getName(),
                        user.getEmail(), user.getSecurityToken(), user.isVerified(),
                        user.getPassword(), user.getRole().getValue(), user.getuScore(), 
                        user.getCoins(), user.getUserSince());
        return account; 
    }

    @Override
    public Page<UserAccount> assembleUserAccountPage(List<DareUser> users, int pageNumber) {
        List<UserAccount> accounts = new ArrayList(); 
        for(DareUser user : users)
            accounts.add(assembleUserAccount(user)); 
            
        return new Page<UserAccount>(accounts, pageNumber, 20); 
    }

    @Override
    public Page<DareDescription> assembleDareDescriptions(List<Dare> dares, int pageNumber) {
        Page<DareDescription> page = new Page<DareDescription>(); 
        
        List<DareDescription> list = new ArrayList(); 
        DareDescription desc; 
        for(Dare dare : dares){
            desc = new DareDescription(dare.getId(), dare.getName(), dare.getDescription(), 
                    dare.getCategory().getName(), String.format("%d hours", dare.getEstimatedDareTime()), 
                    DareUtils.DATE_FORMAT.format(dare.getCreationDate()));
            desc.setCompleted(dare.isCompleted());
            desc.setChallenger(assembleUserDescription(dare.getChallengerUser()));
            list.add(desc); 
        }
            
        
        page.setItems(list);
        page.setPageNumber(pageNumber);
        page.setPageSize(20);
        return page; 
    }
    
    @Override 
    public DareDescription assembleDareDescription(Dare dare){
        DareDescription desc = new DareDescription(dare.getId(), dare.getName(), dare.getDescription(), 
                    dare.getCategory().getName(), String.format("%d hours", dare.getEstimatedDareTime()), 
                    DareUtils.DATE_FORMAT.format(dare.getCreationDate()));
            desc.setChallenger(assembleUserDescription(dare.getChallengerUser()));
            desc.setCompleted(dare.isCompleted());
            return desc; 
    }

    @Override
    public Page<DiscoverUserAccount> assembleDiscoverUserAccounts(List<DiscoverUserAccount> list, Page<DareUser> users) {
        Page<DiscoverUserAccount> page = new Page<DiscoverUserAccount>(); 
        
        page.setPageNumber(users.getPageNumber());
        page.setPageSize(users.getPageSize());
        page.setPagesAvailable(users.getPagesAvailable());
        page.setItems(list);
        
        return page; 
    }

    @Override
    public DiscoverUserAccount assembleDiscoverUserAccount(DareUser user) {
        return new DiscoverUserAccount(user.getId(), user.getName(), 
                    user.getCoins(), user.getuScore());
    }

    @Override
    public List<FriendSearchDescription> transformFriendRequests(List<DareUser> users, Long count) { 
        List<FriendSearchDescription> list = new ArrayList(); 
        for(DareUser user : users)
            list.add(new FriendSearchDescription(user.getId(), user.getName())); 
        return list; 
    }
    
    

    public List<DareDescription> assembleDareDescriptions(List<Dare> dares) {
        List<DareDescription> descs = new ArrayList();
        DareDescription desc; 
        for (Dare dare : dares) {
            desc = new DareDescription(dare.getId(), dare.getName(), dare.getDescription(), 
                        dare.getCategory().getName(), String.valueOf(dare.getEstimatedDareTime()), 
                        DareUtils.DATE_FORMAT.format(dare.getCreationDate()));
            desc.setChallenger(assembleUserDescription(dare.getChallengerUser()));
            descs.add(desc);
        }
            
        return descs;
    }

    @Override
    public UserDescription assembleUserDescription(DareUser user) {
        return new UserDescription(user.getId(), user.getName(), user.getUserSince());
    }

    public ConnectionDetails assembleConnectionDetails(FriendshipRequest request) {
        ConnectionDetails details = new ConnectionDetails(); 
        
        details.setAccepted(request.isAccepted());
        details.setCreationDate(request.getRequestDate());
        details.setRequestedUserId(request.getRequestedUser().getId());
        details.setRequestedUserName(request.getRequestedUser().getName());
        details.setUserId(request.getUser().getId());
        details.setUserName(request.getUser().getName());
        return details; 
    }

    public List<CreatedDare> assembleCreatedDares(List<Dare> dares) {
        List<CreatedDare> createdDares = new ArrayList(); 
        CreatedDare dare; 
        
        for(Dare d : dares){
            dare = new CreatedDare(d.getId(), d.getName(), d.getDescription(), d.isAccepted(), 
                d.isAnswered(), d.isDeclined(), new UserDescription(d.getChallengedUser().getId(), 
                        d.getChallengedUser().getName(), d.getChallengedUser().getUserSince()), 
            DareUtils.DATE_FORMAT.format(d.getCreationDate() ), d.getCategory().getName()); 
            createdDares.add(dare); 
        }
        return createdDares; 
    }

    public ActiveDare assembleActiveDare(Dare dare) {
        ActiveDare activeDare = new ActiveDare(); 
        activeDare.setId(dare.getId());
        activeDare.setName(dare.getName());
        activeDare.setDescription(dare.getDescription());
        activeDare.setTimer(dare.getEstimatedDareTime());
        activeDare.setAcceptedDate(dare.getAcceptedDate());
        activeDare.setChallenger(assembleUserDescription(dare.getChallengerUser()));
        return activeDare; 
    }
    
}
