package com.dareu.web.core.service.impl;

import com.dareu.web.core.DareUtils;
import com.dareu.web.data.entity.Category;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.core.service.FileService;
import com.dareu.web.data.entity.Comment;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.dto.response.entity.AccountProfile;
import com.dareu.web.dto.response.entity.ActiveDare;
import com.dareu.web.dto.response.entity.CategoryDescription;
import com.dareu.web.dto.response.entity.CommentDescription;
import com.dareu.web.dto.response.entity.ConnectionDetails;
import com.dareu.web.dto.response.entity.CreatedDare;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.DareResponseDescription;
import com.dareu.web.dto.response.entity.DiscoverUserAccount;
import com.dareu.web.dto.response.entity.FriendSearchDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.response.entity.UnacceptedDare;
import com.dareu.web.dto.response.entity.UserAccount;
import com.dareu.web.dto.response.entity.UserDescription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 *
 * @author jose.rubalcaba
 */
public class DareuAssemblerImpl implements DareuAssembler {

    @Inject
    private FileService fileService;

    public DareuAssemblerImpl() {
    }

    @Override
    public Page<CategoryDescription> assembleCategories(List<Category> categories, int pageNumber) {
        Page<CategoryDescription> cats = new Page<CategoryDescription>();
        List<CategoryDescription> list = new ArrayList();
        for (Category category : categories) {
            list.add(new CategoryDescription(category.getId(), category.getName(), category.getDescription()));
        }
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
        for (DareUser user : users) {
            accounts.add(assembleUserAccount(user));
        }

        return new Page<UserAccount>(accounts, pageNumber, 20);
    }

    @Override
    public Page<DareDescription> assembleDareDescriptions(List<Dare> dares, int pageNumber) {
        Page<DareDescription> page = new Page<DareDescription>();

        List<DareDescription> list = new ArrayList();
        DareDescription desc;
        for (Dare dare : dares) {
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
    public DareDescription assembleDareDescription(Dare dare) {
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
        DiscoverUserAccount acc = new DiscoverUserAccount(user.getId(), user.getName(),
                user.getCoins(), user.getuScore());
        acc.setImageUrl(user.getImageUrl());
        return acc;
    }

    @Override
    public List<FriendSearchDescription> transformFriendRequests(List<DareUser> users, Long count) {
        List<FriendSearchDescription> list = new ArrayList();
        FriendSearchDescription desc;
        for (DareUser user : users) {
            desc = new FriendSearchDescription(user.getId(), user.getName());
            desc.setImageUrl(user.getImageUrl());
            list.add(desc);
        }

        return list;
    }

    @Override
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
        UserDescription desc = new UserDescription(user.getId(), user.getName(), user.getUserSince());
        desc.setImageUrl(user.getImageUrl());
        return desc;
    }

    @Override
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

    @Override
    public List<CreatedDare> assembleCreatedDares(List<Dare> dares) {
        List<CreatedDare> createdDares = new ArrayList();
        CreatedDare dare;

        for (Dare d : dares) {
            dare = new CreatedDare(d.getId(), d.getName(), d.getDescription(), d.isAccepted(),
                    d.isAnswered(), d.isDeclined(), assembleUserDescription(d.getChallengedUser()),
                    DareUtils.DATE_FORMAT.format(d.getCreationDate()), d.getCategory().getName());
            createdDares.add(dare);
        }
        return createdDares;
    }

    @Override
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

    @Override
    public List<DareResponseDescription> assembleDareResponseDescriptions(List<DareResponse> responses) {
        List<DareResponseDescription> list = new ArrayList();
        DareResponseDescription desc;
        for (DareResponse response : responses) {
            desc = assembleDareResponseDescription(response);
            list.add(desc);
        }
        return list;
    }

    @Override
    public List<DareResponseDescription> getResponseDescriptions(List<DareResponse> list) {
        List<DareResponseDescription> descs = new ArrayList();
        DareResponseDescription desc;
        for (DareResponse r : list) {
            desc = assembleDareResponseDescription(r);
            descs.add(desc);
        }
        return descs;
    }

    @Override
    public DareResponseDescription assembleDareResponseDescription(DareResponse resp) {
        DareResponseDescription desc = new DareResponseDescription();
        desc.setClaps(resp.getLikes());
        desc.setDare(assembleDareDescription(resp.getDare()));
        desc.setId(resp.getId());
        desc.setLastUpdate(resp.getLastUpdate());
        desc.setThumbUrl(resp.getThumbUrl());
        desc.setUploadDate(resp.getResponseDate());
        desc.setUser(assembleUserDescription(resp.getUser()));
        desc.setVideoUrl(resp.getVideoUrl());
        desc.setViews(resp.getViewsCount());
        return desc;
    }

    @Override
    public List<CommentDescription> assembleCommentDescriptions(List<Comment> comments) {
        List<CommentDescription> list = new ArrayList();
        for (Comment comment : comments) {
            list.add(assembleCommentDescription(comment));
        }
        return list;
    }

    @Override
    public CommentDescription assembleCommentDescription(Comment comment) {
        CommentDescription desc = new CommentDescription();
        desc.setComment(comment.getComment());
        desc.setCommentDate(comment.getCommentDate());
        desc.setId(comment.getId());
        desc.setLikes(comment.getLikes());
        desc.setResponse(assembleDareResponseDescription(comment.getResponse()));
        desc.setUser(assembleUserDescription(comment.getUser()));
        return desc;
    }

    @Override
    public AccountProfile getAccountProfile(DareUser user, Page<CreatedDare> createdDares, Page<DareResponseDescription> responses) {
        AccountProfile profile = new AccountProfile();
        profile.setCoins(user.getCoins());
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setUscore(user.getuScore());
        profile.setUserSinceDate(user.getUserSince());
        profile.setCreatedDares(createdDares);
        profile.setCreatedResponses(responses);
        profile.setEmail(user.getEmail());
        profile.setImageUrl(user.getImageUrl());
        return profile;
    }

    @Override
    public UnacceptedDare getUnacceptedDare(Dare dare) {
        UnacceptedDare unacceptedDare = new UnacceptedDare();
        unacceptedDare.setId(dare.getId());
        unacceptedDare.setName(dare.getName());
        unacceptedDare.setDescription(dare.getDescription());
        unacceptedDare.setCreationDate(DareUtils.DETAILS_DATE_FORMAT.format(dare.getCreationDate()));
        unacceptedDare.setTimer(dare.getEstimatedDareTime());
        return unacceptedDare;
    }
}
