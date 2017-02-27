/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.dareu.web.core.DareUtils;
import com.dareu.web.core.security.DareuPrincipal;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.FileService;
import com.dareu.web.core.service.FileService.FileType;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.data.repository.FriendshipRepository;
import com.dareu.web.dto.request.SigninRequest;
import com.dareu.web.dto.request.SignupRequest;
import com.dareu.web.dto.response.AuthenticationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse.RegistrationType;
import com.dareu.web.dto.response.ResourceAvailableResponse;
import com.dareu.web.data.exception.AuthenticationException;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.core.service.DareuMessagingService;
import com.dareu.web.core.service.MultipartService;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.dto.request.ChangeEmailAddressRequest;
import com.dareu.web.dto.security.PasswordEncryptor;
import com.dareu.web.dto.response.BadRequestResponse;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.response.entity.AccountProfile;
import com.dareu.web.dto.response.entity.ConnectionDetails;
import com.dareu.web.dto.response.entity.DiscoverUserAccount;
import com.dareu.web.dto.response.entity.FriendSearchDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.response.entity.UserAccount;
import com.dareu.web.exception.EntityRegistrationException;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author MACARENA
 */
public class AccountServiceImpl extends AbstractService implements AccountService {

    @Inject
    private DareUserRepository dareUserRepository;

    @Inject
    private FriendshipRepository friendshipRepository;

    @Inject
    private FileService fileService;

    @Inject
    private DareUtils utils;

    @Inject
    private PasswordEncryptor encryptor;

    @Inject
    private DareRepository dareRepository;

    @Inject
    private DareResponseRepository dareResponseRepository;

    @Inject
    private Logger log;

    @Inject
    private DareuAssembler assembler;

    @Inject
    private DareuMessagingService messagingService;

    @Inject
    private MultipartService multipartService;

    @Override
    public Response registerDareUser(SignupRequest request)
            throws EntityRegistrationException, InternalApplicationException {
        Response response = null;

        //check entity 
        if (request == null) {
            throw new EntityRegistrationException("No entity found on request");
        }

        //validate email for duplicates 
        if (!dareUserRepository.isEmailAvailable(request.getEmail())) {
            throw new EntityRegistrationException("An account with the email " + request.getEmail() + " already exists");
        }

        //create an entity 
        DareUser user = new DareUser();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encryptor.encryptPassword(request.getPassword()));
        user.setUserSince(DareUtils.DATE_FORMAT.format(new Date()));
        user.setCoins(0);
        user.setRole(SecurityRole.USER);
        user.setuScore(0);
        user.setBirthday(request.getBirthday());
        //save the entity 
        try {
            dareUserRepository.registerDareUser(user);
        } catch (DataAccessException ex) {
            log.severe("Could not register new dare user: " + ex.getMessage());
            throw new EntityRegistrationException("Could not register dare user, try again");
        }

        log.info("Generating new security token");
        //generate a new token for this user 
        String token = utils.getNextSessionToken();

        //save the token here
        user.setSecurityToken(token);

        log.info("Updating security token");
        //update token
        dareUserRepository.updateSecurityToken(token, user.getId());

        //create response 
        response = Response.ok(new AuthenticationResponse(token, DareUtils.DATE_FORMAT.format(new Date()), "Welcome"))
                .build();
        return response;
    }

    @Override
    public Response authenticate(SigninRequest request)
            throws AuthenticationException {
        if (request == null) {
            throw new AuthenticationException("No signin body provided");
        }
        //try to authenticate
        DareUser user = dareUserRepository.login(request.getUser(),
                encryptor.encryptPassword(request.getPassword()));
        if (user == null) {
            throw new AuthenticationException("Username and/or password are incorrect");
        } else {
            //generate a new token 
            String token = utils.getNextSessionToken();

            //update token 
            dareUserRepository.updateSecurityToken(token, user.getId());
            return Response.ok(new AuthenticationResponse(token, DareUtils.DATE_FORMAT.format(new Date()), "Welcome"))
                    .build();
        }
    }

    @Override
    public Response isEmailAvailable(String email)
            throws InternalApplicationException {

        if (dareUserRepository.isEmailAvailable(email)) {
            return Response.ok(new ResourceAvailableResponse(true, "The email " + email + " is available", DareUtils.DATE_FORMAT.format(new Date())))
                    .build();
        } else {
            return Response.ok(new ResourceAvailableResponse(false, "The email " + email + " is not available", DareUtils.DATE_FORMAT.format(new Date())))
                    .build();
        }
    }

    @Override
    public Response isNicknameAvailable(String nickname)
            throws InternalApplicationException {
        if (dareUserRepository.isNicknameAvailable(nickname)) {
            return Response.ok(new ResourceAvailableResponse(true, "The nickname " + nickname + " is available", DareUtils.DATE_FORMAT.format(new Date())))
                    .build();
        } else {
            return Response.ok(new ResourceAvailableResponse(false, "The nickname " + nickname + " is not available", DareUtils.DATE_FORMAT.format(new Date())))
                    .build();
        }
    }

    @Override
    public Response requestFriendship(String requestedUserId)
            throws InvalidRequestException, InternalApplicationException {
        //validate 
        if (requestedUserId == null || requestedUserId.isEmpty()) {
            throw new InvalidRequestException("Invalid requestedUserId field");
        }

        EntityRegistrationResponse response = null;
        //create a friendship if not exists 
        FriendshipRequest friendship = new FriendshipRequest();
        friendship.setAccepted(false);
        friendship.setRequestDate(DareUtils.DATE_FORMAT.format(new Date()));

        //get requested user 
        DareUser requestedUser = null, user = null;
        try {
            //check if a friendship with this ids exists 
            FriendshipRequest request = friendshipRepository.findFriendship(getPrincipal().getId(), requestedUserId);
            if (request != null) {
                //the friendship already exists 
                if (request.isAccepted()) {
                    response = new EntityRegistrationResponse("You already are friend with " + requestedUser.getName(),
                            RegistrationType.FRIENDSHIP_REQUEST, DareUtils.DATE_FORMAT.format(new Date()),
                            "N/A");
                    return Response.ok(response)
                            .build();
                } else {
                    response = new EntityRegistrationResponse("You already sent a request to " + requestedUser.getName(),
                            RegistrationType.FRIENDSHIP_REQUEST, DareUtils.DATE_FORMAT.format(new Date()),
                            "N/A");
                    return Response.ok(response)
                            .build();
                }
            }
            requestedUser = dareUserRepository.find(requestedUserId);
            user = dareUserRepository.find(getPrincipal().getId());

            if (requestedUser != null && user != null) {
                //set users 
                friendship.setRequestedUser(requestedUser);
                friendship.setUser(user);
                //try to persist
                String id = friendshipRepository.persist(friendship);

                String fcmToken = friendship.getRequestedUser().getGCM();

                if (fcmToken != null && !fcmToken.isEmpty()) {
                    messagingService.sendConnectionRequestedNotification(friendship, fcmToken);
                }

                return Response
                        .ok(new EntityRegistrationResponse("Friendship request sent to " + requestedUser.getName(),
                                        RegistrationType.FRIENDSHIP_REQUEST,
                                        DareUtils.DATE_FORMAT.format(new Date()), id))
                        .build();
            } else //return bad response
            {
                throw new InvalidRequestException("Users identificators are not valid, try again");
            }

        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Error creating a friendship request: " + ex.getMessage());
        }
    }

    @Override
    public Response friendshipResponse(String userId, Boolean accepted)
            throws InvalidRequestException, InternalApplicationException {
        if (userId == null) {
            throw new InvalidRequestException("Invalid friendship response body");
        } else if (accepted == null) {
            throw new InvalidRequestException("Invalid friendship id provided");
        }

        //get the friendhip 
        FriendshipRequest f = null;
        try {
            String value = accepted ? "Accepting " : " Declining"; 
            log.info(value + "friendship request " + userId);
            f = friendshipRepository.findFriendship(userId, getPrincipal().getId());
            
            if (f == null) {
                log.info("Friendship request is null"); 
                throw new InvalidRequestException("Friendship id not valid");
            }
            friendshipRepository.updateFriendhip(accepted, f.getId());

            if (accepted) {
                //get fcm token from user id 
                String userFcmToken = dareUserRepository.getUserFcmToken(f.getUser().getGCM());
                if (userFcmToken != null && !userFcmToken.isEmpty()) //check if user updated token
                {
                    messagingService.sendConnectionAcceptedNotification(userFcmToken, f);
                }
            }

            //send notification to requested user 
            //
            return Response
                    .ok(new EntityRegistrationResponse("You are now friends with " + f.getUser().getName(),
                                    RegistrationType.FRIENDSHIP_RESPONSE,
                                    DareUtils.DATE_FORMAT.format(new Date()),
                                    f.getId()))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Could process friendhip: " + ex.getMessage());
        }
    }

    @Override
    public Response updateRegId(String regId, String auth)
            throws InvalidRequestException,
            InternalApplicationException {
        if (regId == null || regId.isEmpty()) {
            throw new InvalidRequestException();
        }
        try {
            //try to update
            dareUserRepository.updateFcmRegId(regId, auth);

            //return response 
            return Response.ok(new UpdatedEntityResponse("Registration ID successfully updated", true, "user"))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Could not update FCM: " + ex.getMessage());
        }
    }

    @Override
    public Response getAccountImage(String userId, String auth) throws InvalidRequestException, InternalApplicationException {
        //get file 
        InputStream stream = null;
        String id;
        DareUser user;

        try {
            if (userId == null || userId.isEmpty()) {
                log.info("Searching user by token");
                //get image from token
                user = dareUserRepository.findUserByToken(auth);
            } else {
                log.info("Searching user by userId");
                user = dareUserRepository.find(userId);
            }
            stream = fileService.getFileStream(user.getId() + ".jpg", FileType.PROFILE_IMAGE);
            log.info("Image is null: " + (stream == null));
            BufferedImage image = ImageIO.read(stream);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ImageIO.write(image, "jpg", out);

            return Response.ok(out.toByteArray())
                    .build();
        } catch (FileNotFoundException ex) {
            log.info(ex.getMessage());
            return Response.ok(null)
                    .build();
        } catch (IOException ex) {
            log.info(ex.getMessage());
            throw new InternalApplicationException("Could not get account profile image: " + ex.getMessage());
        } catch (DataAccessException ex) {
            log.info(ex.getMessage());
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    @Override
    public Response findUserById(String email) throws InvalidRequestException, InternalApplicationException {
        //validate 
        if (email == null || email.isEmpty()) {
            throw new InvalidRequestException("No email provided");
        }

        DareUser user = null;
        UserAccount account = null;
        try {
            user = dareUserRepository.findUserByEmail(email);
            if (user != null) {
                //create dto 
                account = assembler.assembleUserAccount(user);
                return Response.ok(account)
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new BadRequestResponse("No user was found with email " + email,
                                        DareUtils.DATE_FORMAT.format(new Date()), 404))
                        .build();
            }
        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Could not get user by email: " + ex.getMessage());
        }
    }

    public Response findUsersByPage(int pageNumber) throws InternalApplicationException {
        List<DareUser> users = null;
        try {
            users = dareUserRepository.findUsersByPage(pageNumber, true, null);
            //creates a page 
            Page<UserAccount> page = assembler.assembleUserAccountPage(users, pageNumber);
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Could not get users", ex);
        }
    }

    @Override
    public Response discoverUsers(int pageNumber) throws InternalApplicationException {
        Page<DiscoverUserAccount> accounts = null;
        try {
            DareUser user = dareUserRepository.find(getPrincipal().getId());

            //testing purposes
            Page<DareUser> discoverableUsers = dareUserRepository.discoverUsers(pageNumber, user.getId());
            List<DiscoverUserAccount> discovers = new ArrayList();
            DiscoverUserAccount acc = null;
            int dares = 0;
            int responses = 0;
            for (DareUser another : discoverableUsers.getItems()) {
                //if the two users are not friends, add it to discover list
                acc = assembler.assembleDiscoverUserAccount(another);
                if (friendshipRepository.isRequestSent(getPrincipal().getId(), another.getId())) {
                    //user sent request to another user
                    acc.setRequestSent(true);
                } else if (friendshipRepository.isRequestReceived(getPrincipal().getId(), another.getId())) {
                    //request has been received by another user and not accepted
                    acc.setRequestReceived(true);
                }
                dares = dareRepository.daresCount(acc.getId());
                responses = dareResponseRepository.responsesCount(acc.getId());
                acc.setDares(dares);
                acc.setResponses(responses);
                discovers.add(acc);
            }
            accounts = assembler.assembleDiscoverUserAccounts(discovers, discoverableUsers);
            return Response.ok(accounts)
                    .build();
        } catch (DataAccessException ex) {
            log.severe("Could not fetch discoverable users: " + ex.getMessage());
            throw new InternalApplicationException("Could not fetch discoverable users: " + ex.getMessage());
        }
    }

    public Response findFriends(int pageNumber, String query) throws InternalApplicationException {
        Page<FriendSearchDescription> page = null;
        try {
            if (query == null || query.isEmpty()) {
                page = friendshipRepository.findFriendDescriptions(getPrincipal().getId(), pageNumber);
            } else {
                page = friendshipRepository.findFriendDescriptions(getPrincipal().getId(), pageNumber, query);
            }
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Could not find friends: " + ex.getMessage());
        }
    }

    public Response findFriendshipDetails(String friendshipId, String auth) throws InternalApplicationException, InvalidRequestException {
        ConnectionDetails details;

        if (friendshipId == null || friendshipId.isEmpty()) {
            throw new InvalidRequestException("Friendship ID must be provided");
        }

        try {
            FriendshipRequest request = friendshipRepository.find(friendshipId);
            if (request == null) {
                throw new InvalidRequestException("This connection does not exists");
            }

            details = assembler.assembleConnectionDetails(request);
            return Response.ok(details)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    public Response updateProfileImage(MultipartFormDataInput input, String auth) throws InternalApplicationException {
        try {
            InputStream stream = multipartService.getImageProfile(input);
            //get user id 
            DareUser dareUser = dareUserRepository.findUserByToken(auth);
            fileService.saveFile(stream, FileType.PROFILE_IMAGE, dareUser.getId() + ".jpg");
            //update file path 
            dareUserRepository.updateImageUrl(dareUser.getId());

            return Response.ok(new UpdatedEntityResponse("Image has been updated", true, "user"))
                    .build();
        } catch (IOException ex) {
            throw new InternalApplicationException(ex.getMessage());
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    public Response me() throws InternalApplicationException {
        try{
            String id = getPrincipal().getId(); 
            //get account profile 
            AccountProfile accountProfile = dareUserRepository.getAccountProfile(id); 
            return Response.ok(accountProfile)
                    .build(); 
        }catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    public Response changeEmailAddress(ChangeEmailAddressRequest request, String token) throws InvalidRequestException, InternalApplicationException {
        UpdatedEntityResponse response = null; 
        try{
            if(request == null)
                throw new InvalidRequestException("Request body not valid"); 
            if(request.getNewEmail() == null || request.getNewEmail().isEmpty())
                throw new InvalidRequestException("Email is not valid");
            
            dareUserRepository.changeEmailAddress(request.getNewEmail(), token); 
            response = new UpdatedEntityResponse("Email has been updated", true, "user"); 
            return Response.ok(response)
                    .build(); 
        }catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage());
        }
    }
}
