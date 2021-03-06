package com.dareu.web.core.service.impl;

import com.dareu.web.core.DareUtils;
import com.dareu.web.data.entity.*;
import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.core.service.FileService;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.dto.request.ContactReplyRequest;
import com.dareu.web.dto.request.ContactRequest;
import com.dareu.web.dto.request.GoogleSignupRequest;
import com.dareu.web.dto.response.entity.*;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.exception.application.InternalApplicationException;
import com.messaging.dto.email.ContactEmailRequest;
import com.messaging.dto.email.EmailRequest;
import com.messaging.dto.email.WelcomeEmailPayload;
import com.messaging.dto.email.ContactReplyEmailPayload;
import org.apache.log4j.Logger;

import java.util.*;
import javax.inject.Inject;

/**
 *
 * @author jose.rubalcaba
 */
public class DareuAssemblerImpl implements DareuAssembler {

    @Inject
    private FileService fileService;

    @Inject
    private DareResponseRepository dareResponseRepository;

    private final Logger log =  Logger.getLogger(getClass());

    private static final String APPLICATION_ID = "com.dareu.web";

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
    public List<DareResponseDescription> assembleDareResponseDescriptions(List<DareResponse> responses, String userId) {
        List<DareResponseDescription> list = new ArrayList();
        DareResponseDescription desc;
        for (DareResponse response : responses) {
            desc = assembleDareResponseDescription(response, userId);

            list.add(desc);
        }
        return list;
    }

    @Override
    public List<DareResponseDescription> getResponseDescriptions(List<DareResponse> list, String userId) {
        List<DareResponseDescription> descs = new ArrayList();
        DareResponseDescription desc;
        for (DareResponse r : list) {
            desc = assembleDareResponseDescription(r, userId);
            descs.add(desc);
        }
        return descs;
    }

    @Override
    public DareResponseDescription assembleDareResponseDescription(DareResponse resp, String userId) {
        DareResponseDescription desc = new DareResponseDescription();
        desc.setClaps(resp.getClaps().size());
        desc.setDare(assembleDareDescription(resp.getDare()));
        desc.setId(resp.getId());
        desc.setLastUpdate(resp.getLastUpdate());
        desc.setThumbUrl(resp.getThumbUrl());
        desc.setUploadDate(resp.getResponseDate());
        desc.setUser(assembleUserDescription(resp.getUser()));
        desc.setVideoUrl(resp.getVideoUrl());
        desc.setViews(resp.getViewsCount());
        //check if current user has already clapped this response
        try{
            desc.setClapped(dareResponseRepository.isResponseClapped(userId, resp.getId()));
            desc.setAnchored(dareResponseRepository.isResponseAnchored(userId, resp.getId()));
        }catch(DataAccessException ex){
            log.error(ex.getMessage());
        }
        return desc;
    }

    @Override
    public List<CommentDescription> assembleCommentDescriptions(List<Comment> comments, String userId) {
        List<CommentDescription> list = new ArrayList();
        for (Comment comment : comments) {
            list.add(assembleCommentDescription(comment, userId));
        }
        return list;
    }

    @Override
    public CommentDescription assembleCommentDescription(Comment comment, String userId) {
        CommentDescription desc = new CommentDescription();
        desc.setComment(comment.getComment());
        desc.setCommentDate(comment.getCommentDate());
        desc.setId(comment.getId());
        try{
            desc.setClapped(dareResponseRepository.isCommentClapped(userId, comment.getId()));
        }catch(DataAccessException ex){
            log.error(ex.getMessage());
        }
        desc.setResponse(assembleDareResponseDescription(comment.getResponse(), userId));
        desc.setUser(assembleUserDescription(comment.getUser()));
        return desc;
    }

    @Override
    public AccountProfile getAccountProfile(DareUser user, Page<CreatedDare> createdDares, Page<DareResponseDescription> responses, Page<FriendSearchDescription> contacts) {
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
        profile.setContacts(contacts);
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

    @Override
    public List<ConnectionRequest> assembleConnectionRequests(List<FriendshipRequest> list, boolean sent) {
        List<ConnectionRequest> conns = new ArrayList();
        ConnectionRequest conn; 
        for(FriendshipRequest req : list){
            conn = new ConnectionRequest();
            conn.setConnectionId(req.getId());
            if(sent){
                conn.setType(ConnectionRequest.ConnectioRequestType.SENT);
                conn.setUser(assembleUserDescription(req.getRequestedUser()));
            }
            else{
                conn.setUser(assembleUserDescription(req.getUser())); 
                conn.setType(ConnectionRequest.ConnectioRequestType.RECEIVED);
            }
            conns.add(conn);
        }
        return conns; 
    }

    @Override
    public List<AnchoredDescription> assembleAnchoredContent(List<AnchoredContent> list, String userId) {
        List<AnchoredDescription> descs = new ArrayList();
        for(AnchoredContent c : list){
            AnchoredDescription desc = new AnchoredDescription(c.getCreationDate(), assembleDareResponseDescription(c.getResponse(), userId));
            desc.setAnchorId(c.getId());
            descs.add(desc);
        }

        return descs;
    }

    @Override
    public DareUser getDareUser(GoogleSignupRequest request) {
        DareUser user = new DareUser();
        user.setAccountType(DareUser.AccountType.G_PLUS);
        user.setBirthday(request.getBirthdate());
        user.setCoins(0);
        user.setEmail(request.getEmail());
        user.setGCM(request.getFcm());
        user.setGoogleId(request.getGoogleId());
        user.setImageUrl(request.getImageUrl());
        user.setName(request.getName());
        user.setPassword("N/A");
        user.setRole(SecurityRole.USER);
        user.setuScore(0);
        user.setVerified(false);
        user.setUserSince(DareUtils.DATE_FORMAT.format(new Date()));
        return user;
    }

    @Override
    public List<ContactMessageDescription> assembleContactMessageDescriptions(List<ContactMessage> messages) {
        List<ContactMessageDescription> descs = new ArrayList<>();
        messages.stream().forEach((cm) -> {
            descs.add(new ContactMessageDescription(cm.getId(), cm.getName(), cm.getEmail(), cm.getComment(), cm.getStatus().toString(), cm.getDatetime()));
        });
        return descs;
    }

    @Override
    public EmailRequest assembleWelcomeEmailRequest(DareUser user) {
        final EmailRequest<WelcomeEmailPayload> emailRequest = new EmailRequest();
        emailRequest.setDate(DareUtils.DATE_FORMAT.format(new Date()));
        emailRequest.setApplicationId(APPLICATION_ID);
        emailRequest.setBody(new WelcomeEmailPayload(user.getId(), user.getName(), user.getEmail()));
        return emailRequest;
    }

    @Override
    public EmailRequest<ContactReplyEmailPayload> assembleContactMessageEmailReply(ContactReplyRequest request, String email, String recipientName) {
        final EmailRequest<ContactReplyEmailPayload> emailRequest = new EmailRequest<>();
        ContactReplyEmailPayload payload = new ContactReplyEmailPayload();
        payload.setBody(request.getBody());
        //payload.setRecipientName(); TODO: create replyContact operation to continue with this...
        List<String> recipients = new ArrayList();
        recipients.add(email);
        emailRequest.setRecipients(recipients);
        emailRequest.setDate(DareUtils.DATE_FORMAT.format(new Date()));
        emailRequest.setApplicationId(APPLICATION_ID);
        emailRequest.setBody(payload);
        return emailRequest;
    }

    @Override
    public EmailRequest assembleErrorEmailRequest(InternalApplicationException ex) {
        final EmailRequest emailRequest = new EmailRequest<>();
        //get stack trace string
        emailRequest.setRecipients(null);
        emailRequest.setBody(DareUtils.getStackTraceString(ex));
        emailRequest.setApplicationId(APPLICATION_ID);
        emailRequest.setDate(DareUtils.DATE_FORMAT.format(new Date()));
        return emailRequest;
    }

    @Override
    public EmailRequest assembleRequestedFriendshipEmailRequest(String name, String email) {
        return null;
    }

    @Override
    public EmailRequest assembleContactEmailRequest(ContactRequest contactRequest) {
        EmailRequest request = new EmailRequest();
        List<String> recipients = new ArrayList<>();
        recipients.add(contactRequest.getEmail());
        request.setRecipients(recipients);
        request.setDate(DareUtils.DATE_FORMAT.format(new Date()));
        request.setApplicationId(APPLICATION_ID);
        ContactEmailRequest contactEmailRequest = new ContactEmailRequest();
        contactEmailRequest.setDate(DareUtils.DATE_FORMAT.format(new Date()));
        contactEmailRequest.setBody(contactRequest.getComment());
        contactEmailRequest.setName(contactRequest.getName());
        return request;
    }

}
