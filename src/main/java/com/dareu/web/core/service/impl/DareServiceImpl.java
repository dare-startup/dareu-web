package com.dareu.web.core.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.dareu.web.core.DareUtils;
import com.dareu.web.core.observable.DareEvent;
import com.dareu.web.core.observable.DareuEventHandler;
import com.dareu.web.core.service.*;
import com.dareu.web.data.entity.*;
import com.dareu.web.data.repository.CategoryRepository;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.dto.jms.FileUploadProperties;
import com.dareu.web.dto.jms.PayloadMessage;
import com.dareu.web.dto.jms.QueueMessage;
import com.dareu.web.dto.request.CreateCategoryRequest;
import com.dareu.web.dto.request.CreateDareRequest;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse.RegistrationType;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.dto.request.ClapRequest;
import com.dareu.web.dto.request.DareConfirmationRequest;
import com.dareu.web.dto.request.DareUploadRequest;
import com.dareu.web.dto.request.FlagDareRequest;
import com.dareu.web.dto.request.NewCommentRequest;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.response.entity.ActiveDare;
import com.dareu.web.dto.response.entity.AnchoredDescription;
import com.dareu.web.dto.response.entity.CategoryDescription;
import com.dareu.web.dto.response.entity.CommentDescription;
import com.dareu.web.dto.response.entity.CreatedDare;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.DareResponseDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.response.entity.UnacceptedDare;
import com.dareu.web.dto.response.entity.UserDescription;
import com.dareu.web.dto.response.message.*;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import java.io.IOException;
import javax.enterprise.event.Event;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public class DareServiceImpl implements DareService {

    @Inject
    private DareuEventHandler eventHandler;

    @Inject
    private DareRepository dareRepository;

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private DareUserRepository dareUserRepository;

    @Inject
    private DareResponseRepository dareResponseRepository;

    @Inject
    private DareuAssembler assembler;

    @Inject
    private MessagingService messagingService;

    @Inject
    private MultipartService multipartService;

    @Inject
    private FileService fileService;

    @Inject
    private Logger log;

    @Override
    public Response createNewDare(CreateDareRequest request, String authenticationToken)
            throws InvalidRequestException, InternalApplicationException {

        if (request == null) {
            throw new InvalidRequestException("Invalid dare request");
        }

        //validate 
        if (request.getCategoryId() == null) {
            throw new InvalidRequestException("Category field not provided");
        }
        if (request.getDescription() == null) {
            throw new InvalidRequestException("Description field not provided");
        }
        if (request.getFriendId() == null) {
            throw new InvalidRequestException("Friends ID's field not provided");
        }
        if (request.getName() == null) {
            throw new InvalidRequestException("Name field not provided");
        }
        if (request.getTimer() <= 0) {
            throw new InvalidRequestException("Time field is not valid");
        }
        log.info("Creating a new dare");
        //create a new dare 
        Dare dare = new Dare();
        dare.setAccepted(false);
        dare.setApproved(false);
        dare.setCreationDate(new Date());
        dare.setDescription(request.getDescription());
        dare.setEstimatedDareTime(request.getTimer()); //time is in hours
        dare.setName(request.getName());

        //get the challenged user 
        DareUser challengedUser = null;

        //get challenger user 
        DareUser challengerUser = null;

        try {
            log.info("Searching users");
            challengedUser = dareUserRepository.find(request.getFriendId());
            challengerUser = dareUserRepository.findUserByToken(authenticationToken);
            //get category
            log.info("Searching category");
            Category category = categoryRepository.find(request.getCategoryId());
            if (category == null) {
                throw new InvalidRequestException("Category not valid, try again");
            }

            dare.setCategory(category);
            //set users 
            dare.setChallengerUser(challengerUser);
            dare.setChallengedUser(challengedUser);

            String id = dareRepository.createDare(dare);

            String dareUserFcmToken = dareUserRepository.getUserFcmToken(challengedUser.getId());

            ActiveDare activeDare = dareRepository.getCurrentActiveDare(challengedUser.getId());

            Dare unacceptedDare = dareRepository.findUnacceptedDare(challengedUser.getId());
            //check if challenged user already has an active dare
            if (activeDare != null) {
                //user has an active dare
                log.info("User has an active dare, sending queued notification");
                //send a notification of another dare waiting for
                if (dareUserFcmToken != null && !dareUserFcmToken.isEmpty()) {
                    messagingService.sendPushNotificationMessage(new QueueMessage(dareUserFcmToken,
                            new PayloadMessage("active.dare",
                                    new QueuedDareMessage(MessageType.QUEUED_DARE_MESSAGE,
                                            dare.getId(), DareUtils.DETAILS_DATE_FORMAT.format(new Date()), QueuedDareMessage.ACTIVE))));
                }
            } else if (unacceptedDare != null && !unacceptedDare.getId().equals(dare.getId())) {
                //user has an unaccepted dare
                log.info("User has an unaccepted dare, sending queued notification");
                //send a notification for another dare waiting for
                if (dareUserFcmToken != null && !dareUserFcmToken.isEmpty()) {
                    messagingService.sendPushNotificationMessage(new QueueMessage(dareUserFcmToken,
                            new PayloadMessage("active.dare",
                                    new QueuedDareMessage(MessageType.QUEUED_DARE_MESSAGE,
                                            dare.getId(), DareUtils.DETAILS_DATE_FORMAT.format(new Date()), QueuedDareMessage.PENDING))));
                }
            } else {
                //user does not have any dare request
                //send push notification to the dared user 
                if (dareUserFcmToken != null && !dareUserFcmToken.isEmpty()) {
                    messagingService.sendPushNotificationMessage(new QueueMessage(dareUserFcmToken,
                            new PayloadMessage("new.dare", new NewDareMessage(dare.getId(), dare.getName(),
                                    dare.getDescription(), dare.getEstimatedDareTime(), dare.getChallengerUser().getName()))));
                }
            }
            //return response
            return Response
                    .ok(new EntityRegistrationResponse("Successfully created new dare",
                                    RegistrationType.DARE,
                                    DareUtils.DATE_FORMAT.format(new Date()), id))
                    .build();
        } catch (DataAccessException e) {
            throw new InternalApplicationException("Error creating new dare: " + e.getMessage());
        }
    }

    @Override
    public Response createNewCategory(CreateCategoryRequest request)
            throws InvalidRequestException, InternalApplicationException {

        //validate request
        if (request == null) {
            throw new InvalidRequestException("Invalid Category request");
        }

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new InvalidRequestException("Name field not provided");
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new InvalidRequestException("Description field not provided");
        }

        log.info("Creating new dare category");
        //create a category
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        try {
            String id = categoryRepository.persist(category);
            return Response
                    .ok(new EntityRegistrationResponse("Successfully created category",
                                    RegistrationType.CATEGORY,
                                    DareUtils.DATE_FORMAT.format(new Date()), id))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Could not add category: " + ex.getMessage());
        }
    }

    @Override
    public Response getCategories(int pageNumber) throws InternalApplicationException {
        //get all categories 
        try {
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            log.info("Getting categories");
            List<Category> categories = categoryRepository.findCategoriesByPage(pageNumber);
            Page<CategoryDescription> page = assembler.assembleCategories(categories, pageNumber);
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Could not get categories: " + ex.getMessage());
        }
    }

    @Override
    public Response findUnapprovedDares(int pageNumber) throws InternalApplicationException {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        try {
            log.info("Finding unapproved dares");
            List<Dare> dares = dareRepository.findUnapprovedDares(pageNumber);
            Page<DareDescription> dto = assembler.assembleDareDescriptions(dares, pageNumber);

            return Response.ok(dto)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException("Could not get dares: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Response findUnacceptedDare(String auth) throws InternalApplicationException {
        DareUser user;
        UnacceptedDare unacceptedDare;
        DareUser challenger;
        Dare dare;
        UserDescription challengerDescription;
        try {
            //find user by token
            user = dareUserRepository.findUserByToken(auth);
            //get id 
            String id = user.getId();
            //find dare
            dare = dareRepository.findUnacceptedDare(id);

            if (dare != null) {
                //created unaccepted dare 
                unacceptedDare = assembler.getUnacceptedDare(dare);
                challenger = dare.getChallengerUser();
                challengerDescription = assembler.assembleUserDescription(challenger);
                unacceptedDare.setChallenger(challengerDescription);
                return Response.ok(unacceptedDare)
                        .build();
            } else //return an empty response
            {
                return Response.status(Response.Status.NO_CONTENT)
                        .build();
            }

        } catch (DataAccessException ex) {
            throw new InternalApplicationException("");
        }
    }

    @Override
    public Response confirmDareRequest(DareConfirmationRequest request) throws InternalApplicationException, InvalidRequestException {
        //validate
        if (request == null) {
            throw new InvalidRequestException("No request provided");
        }
        if (request.getDareId() == null || request.getDareId().isEmpty()) {
            throw new InvalidRequestException("No dare id provided");
        }

        try {
            log.info("Confirming dare request");
            //update dare 
            dareRepository.confirmDareRequest(request.getDareId(), request.isAccepted());
            return Response.ok(new UpdatedEntityResponse("Updated dare", true, "dare"))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }

    }

    @Override
    public Response discoverDares(int pageNumber, String authToken) throws InternalApplicationException {
        DareUser user;
        try {
            user = dareUserRepository.findUserByToken(authToken);

            Page<DareDescription> descs = dareRepository.discoverDares(pageNumber, user.getId());

            return Response.ok(descs)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response findDareDescription(String dareId) throws InternalApplicationException, InvalidRequestException {
        if (dareId == null || dareId.isEmpty()) {
            throw new InvalidRequestException("dareId must be provided");
        }

        try {
            Dare dare = dareRepository.find(dareId);

            DareDescription desc = assembler.assembleDareDescription(dare);

            return Response.ok(desc)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    @Override
    public Response findCreatedDares(String auth, int pageNumber) throws InternalApplicationException, InvalidRequestException {
        DareUser user;
        try {
            user = dareUserRepository.findUserByToken(auth);
            //get all created dares 

            Page<CreatedDare> createdDaresPage = dareRepository.findCreatedDares(user.getId(), pageNumber);

            //return response
            return Response.ok(createdDaresPage)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    @Override
    public Response getCurrentActiveDare(String auth) throws InternalApplicationException {
        try {
            DareUser user = dareUserRepository.findUserByToken(auth);
            String id = user.getId();

            log.info("Getting current active dare for " + user.getEmail());
            ActiveDare description = dareRepository.getCurrentActiveDare(id);
            if(description == null){
                log.info("No active dare found");
                return Response.status(Response.Status.NO_CONTENT)
                        .build();
            }
            return Response.ok(description)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    @Override
    public Response uploadDareResponse(MultipartFormDataInput input, String auth) throws InternalApplicationException {
        try {
            //get request object 
            DareUploadRequest request = multipartService.getDareUploadRequest(input);

            //get user uplaoding video 
            DareUser user = dareUserRepository.findUserByToken(auth);
            //validate
            if (request == null || request.getDareId() == null || request.getStream() == null) {
                throw new InternalApplicationException("Invalid multipart request");
            }
            //create new Dare response 
            DareResponse dareResponse = new DareResponse();
            //save tmp file
            String videoPath = fileService.saveTemporalfile(request.getStream(), dareResponse.getId(), FileService.FileType.DARE_VIDEO);
            String thumbPath = fileService.saveTemporalfile(request.getThumb(), dareResponse.getId(), FileService.FileType.VIDEO_THUMBNAIL);

            //send video upload message
            messagingService.sendAwsFileUpload(new QueueMessage(user.getGCM(),
                    new PayloadMessage("dareu.upload.pending",
                            new FileUploadProperties(videoPath, FileUploadProperties.DareuFileType.RESPONSE))));

            //send thumb upload message
            messagingService.sendAwsFileUpload(new QueueMessage(user.getGCM(),
                    new PayloadMessage("dareu.upload.pending",
                            new FileUploadProperties(thumbPath, FileUploadProperties.DareuFileType.RESPONSE_THUMB))));

            //populate response
            dareResponse.setVideoUrl("");
            dareResponse.setThumbUrl("");
            dareResponse.setComment(request.getComment());
            dareResponse.setViewsCount(0);
            dareResponse.setUser(user);
            dareResponse.setLastUpdate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
            dareResponse.setResponseDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
            //get dare 
            Dare dare = dareRepository.find(request.getDareId());
            if (dare == null) {
                throw new InternalApplicationException("Dare id is not valid");
            }
            dareResponse.setDare(dare);
            dareResponseRepository.persist(dareResponse);
            //get fcm token from challenger user
            String challengerFcmToken = dareUserRepository.getUserFcmToken(dare.getChallengerUser().getId());
            //send notification
            if (challengerFcmToken != null && !challengerFcmToken.isEmpty()) {
                messagingService.sendPushNotificationMessage(new QueueMessage(challengerFcmToken,
                        new PayloadMessage("dare.response", new UploadedResponseMessage(MessageType.DARE_RESPONSE_UPLOADED,
                                dareResponse.getId(), "Dare response has been uploaded!"))));
            }

            //set dare as complete
            log.info("Setting dare as completed");
            dareRepository.setDareCompleted(dare.getId());
            //return response
            return Response.ok(new EntityRegistrationResponse("Dare response has been created", RegistrationType.DARE_RESPONSE,
                    DareUtils.DETAILS_DATE_FORMAT.format(new Date()), dareResponse.getId()))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        } catch (IOException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    @Override
    public Response flagDare(FlagDareRequest request, String auth) throws InternalApplicationException, InvalidRequestException {
        //valdiate 
        if (request == null) {
            throw new InvalidRequestException("No request body provided");
        }
        if (request.getDareId() == null || request.getDareId().isEmpty()) {
            throw new InvalidRequestException("No dare id provided");
        }

        //check if dare is already flagged
        try {
            //get dare 
            Dare dare = dareRepository.find(request.getDareId());
            if (dare == null) {
                throw new InvalidRequestException("Provided dare id is not valid");
            }

            if (dare.getFlag() != null) {
                return Response.ok(new EntityRegistrationResponse("This dare is already flagged",
                        RegistrationType.DARE_FLAG, DareUtils.DETAILS_DATE_FORMAT.format(new Date()), "N/A"))
                        .build();
            }

            //create a flag 
            DareFlag flag = new DareFlag();
            flag.setComment(request.getComment());
            flag.setFlagDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));

            //persist
            dareRepository.flagDare(flag);

            return Response.ok(new EntityRegistrationResponse("Success", RegistrationType.DARE_FLAG,
                    DareUtils.DETAILS_DATE_FORMAT.format(new Date()), flag.getId()))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response findResponses(int pageNumber, String auth) throws InternalApplicationException, InvalidRequestException {
        try {
            DareUser user = dareUserRepository.findUserByToken(auth);
            Page<DareResponseDescription> page = dareResponseRepository.getResponses(user.getId(), pageNumber);
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response setDareExpired(String dareId, String auth) throws InternalApplicationException, InvalidRequestException {
        try {
            //get user 
            DareUser requestingUser = dareUserRepository.findUserByToken(auth);
            //get dare 
            Dare dare = dareRepository.find(dareId);
            if (dare == null) {
                throw new InvalidRequestException("Provided dare id is not valid");
            }
            //get user from dare 
            DareUser dareUser = dare.getChallengedUser();

            //check id's 
            if (!dareUser.getId().equals(requestingUser.getId())) {
                throw new InvalidRequestException("Only challenged user can expire this dare");
            }

            dareRepository.setDareExpiration(dareId);

            return Response.ok(new UpdatedEntityResponse("Success", true, "dare"))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }
    }

    @Override
    public Response hotResponses(int pageNumber) throws InternalApplicationException {
        return null;
    }

    @Override
    public Response channelResponses(int pageNumber, String token) throws InternalApplicationException {
        try {
            DareUser user = dareUserRepository.findUserByToken(token);
            Page<DareResponseDescription> page = dareResponseRepository.getChannelPage(pageNumber, user.getId());
            for (DareResponseDescription desc : page.getItems()) {
                desc.setComments(dareResponseRepository.getResponseCommentsCount(desc.getId()));
            }
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }

    }

    @Override
    public Response findResponseDescription(String responseId, String token) throws InternalApplicationException, InvalidRequestException {
        if (responseId == null || responseId.isEmpty())
            throw new InvalidRequestException("No response id provided");
        try {
            log.info("Finding user");
            DareUser user = dareUserRepository.findUserByToken(token);

            log.info("Finding response");
            DareResponse resp = dareResponseRepository.find(responseId);

            log.info("Getting number of comments");
            int commentsCount = dareResponseRepository.getResponseCommentsCount(responseId);
            if (resp == null)
                throw new InvalidRequestException("Invalid id");
            log.info("Creating response");
            DareResponseDescription desc = assembler.assembleDareResponseDescription(resp, user.getId());
            //TODO: LOOK FOR ANCHORED RESPONSE FLAG HERE
            desc.setComments(commentsCount);
            return Response.ok(desc)
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response createResponseComment(NewCommentRequest request, String token) throws InternalApplicationException, InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("No request body provided");
        }

        if (request.getComment() == null || request.getComment().isEmpty()) {
            throw new InvalidRequestException("No comment body provided");
        }
        if (request.getResponseId() == null || request.getResponseId().isEmpty()) {
            throw new InvalidRequestException("No response id provided");
        }

        try {
            //get response 
            DareResponse dareResponse = dareResponseRepository.find(request.getResponseId());
            if (dareResponse == null) {
                throw new InvalidRequestException("Invalid response id");
            }

            //get user
            DareUser user = dareUserRepository.findUserByToken(token);

            //creates a new comment
            Comment comment = new Comment();
            comment.setCommentDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
            comment.setComment(request.getComment());
            comment.setResponse(dareResponse);
            comment.setUser(user);

            //persist
            dareResponseRepository.createResponseComment(comment);

            if (!comment.getUser().getId().equals(dareResponse.getDare().getChallengerUser().getId())) {
                //send notification to the dare creator
                String dareCreatorId = dareUserRepository
                        .getUserFcmToken(dareResponse.getDare().getChallengerUser().getId());

                if (dareCreatorId != null && !dareCreatorId.isEmpty()) {
                    messagingService.sendPushNotificationMessage(new QueueMessage(dareCreatorId,
                            new PayloadMessage("dare.response.comment",
                                    new NewCommentMessage(comment.getId(), dareResponse.getId(), comment.getComment()))));
                }
            }

            if (!comment.getUser().getId().equals(dareResponse.getUser().getId())) {
                String videoCreatorId = dareUserRepository
                        .getUserFcmToken(dareResponse.getUser().getId());

                //response creator 
                if (videoCreatorId != null && !videoCreatorId.isEmpty()) {
                    messagingService.sendPushNotificationMessage(new QueueMessage(videoCreatorId,
                            new PayloadMessage("dare.response.comment",
                                    new NewCommentMessage(comment.getId(), dareResponse.getId(), comment.getComment()))));
                }
            }
            return Response.ok(
                    new EntityRegistrationResponse("Success", RegistrationType.COMMENT,
                            DareUtils.DETAILS_DATE_FORMAT.format(new Date()), comment.getId()))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response findResponseComments(int pageNumber, String responseId, String token) throws InternalApplicationException, InvalidRequestException {
        if (responseId == null || responseId.isEmpty()) {
            throw new InvalidRequestException("No response id provided");
        }
        try {
            DareUser user = dareUserRepository.findUserByToken(token);
            DareResponse response = dareResponseRepository.find(responseId);
            if (response == null) {
                throw new InvalidRequestException("Invalid response id");
            }
            Page<CommentDescription> page = dareResponseRepository.findResponseComments(pageNumber, responseId, user.getId());

            //return response
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response viewedResponse(String responseId) throws InternalApplicationException, InvalidRequestException {
        if (responseId == null || responseId.isEmpty()) {
            throw new InvalidRequestException("No response id provided");
        }

        try {
            DareResponse response = dareResponseRepository.find(responseId);
            if (response == null) {
                throw new InvalidRequestException("Invalid response id");
            }

            dareResponseRepository.viewedResponse(response.getId());
            return Response.ok(new UpdatedEntityResponse("Success", true, "dare_response"))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    public Response clapResponse(ClapRequest request, String token) throws InternalApplicationException, InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("No clap request body provided");
        }

        if (request.getResponseId() == null || request.getResponseId().isEmpty()) {
            throw new InvalidRequestException("No response id provided");
        }

        try {
            log.info("Searching dare response");
            DareResponse response = dareResponseRepository.find(request.getResponseId());
            if (response == null)
                throw new InvalidRequestException("Response id not valid");

            DareUser user = dareUserRepository.findUserByToken(token);

            if (request.isClapped()) {
                //creates a new clap entity
                //create a new clap
                log.info("Clapping dare response");
                ResponseClap clap = new ResponseClap();
                clap.setResponse(response);
                clap.setUser(user);
                //persists
                log.info("Saving");
                dareResponseRepository.clapResponse(clap);

                String fcmToken = dareUserRepository.getUserFcmToken(user.getId());
                if (fcmToken != null && !fcmToken.isEmpty()) {
                    log.info("Sending notification to response creator");
                    //send a notification to dare response creator
                    messagingService.sendPushNotificationMessage(new QueueMessage(fcmToken,
                            new PayloadMessage("dare.response.clap",
                                    new ClappedResponseMessage(response.getId()))));
                }
                return Response.ok(new UpdatedEntityResponse("Clap has been created", true, "dare_response_clap"))
                        .build();
            } else {
                log.info("Un-clapping dare response");
                //delete current clap 
                dareResponseRepository.unclapResponse(response.getId(), user.getId());

                return Response.ok(new UpdatedEntityResponse("Clap has been removed", true, "dare_response_clap"))
                        .build();
            }
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response findResponseComment(String commentId, String token) throws InternalApplicationException, InvalidRequestException {
        if (commentId == null || commentId.isEmpty()) {
            throw new InvalidRequestException("Comment is not provided");
        }

        try {
            DareUser user = dareUserRepository.findUserByToken(token);
            Comment comment = dareResponseRepository.findComment(commentId);
            if (comment == null) {
                throw new InvalidRequestException("Invalid comment id");
            }

            CommentDescription descr = assembler.assembleCommentDescription(comment, user.getId());

            return Response.ok(descr)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response anchorContent(String responseId, String token) throws InternalApplicationException, InvalidRequestException {
        if (responseId == null || responseId.isEmpty()) {
            throw new InvalidRequestException("No response id provided");
        }

        try {
            log.info("Finding dare response");
            if(dareResponseRepository.find(responseId) == null)
                throw new InvalidRequestException("Invalid response id");

            log.info("Finding previous anchored content");
            if(dareResponseRepository.findAnchoredContent(responseId, token) != null)
                throw new InvalidRequestException("This response has already been anchored");

            log.info("Finding user");
            DareUser user = dareUserRepository.findUserByToken(token);
            AnchoredContent content;
            //anchor
            log.info("Anchoring response to " + user.getEmail());
            content = new AnchoredContent();
            content.setCreationDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
            content.setResponse(dareResponseRepository.find(responseId));
            content.setUser(user);

            log.info("Saving");
            dareResponseRepository.anchorContent(content);
            return Response.ok(new EntityRegistrationResponse("Anchored",
                    RegistrationType.ANCHOR, content.getCreationDate(), content.getId()))
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response getAnchoredContent(int pageNumber, String token) throws InternalApplicationException, InvalidRequestException {
        try {
            DareUser user = dareUserRepository.findUserByToken(token);
            log.info("Searching " + user.getEmail() + " anchored content");
            Page<AnchoredDescription> page = dareResponseRepository.getAnchoredContent(pageNumber, user.getId());
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response clapResponseComment(String commentId, String token) throws InternalApplicationException, InvalidRequestException {
        if(commentId == null || commentId.isEmpty())
            throw new InvalidRequestException("No comment id was provided");

        try{
            Comment comment = dareResponseRepository.findComment(commentId);
            if(comment == null)
                throw new InvalidRequestException("Invalid comment id");

            DareUser user = dareUserRepository.findUserByToken(token);

            //check if this comment is already clapped by this user

            if(dareResponseRepository.isCommentClapped(user.getId(), comment.getId())){
                log.info("Removing comment clap");
                dareResponseRepository.unClapComment(comment.getId(), user.getId()); // un clap it
                return Response.ok(new EntityRegistrationResponse("Un-clapped", RegistrationType.COMMENT_CLAP,
                        DareUtils.DETAILS_DATE_FORMAT.format(new Date()), "N/A"))
                        .build();
            } else{
                log.info("Clapping a response comment");
                CommentClap clap = new CommentClap();
                clap.setUser(user);
                clap.setClapDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
                clap.setComment(comment);

                //persists
                dareResponseRepository.clapResponseComment(clap);

                return Response.ok(new EntityRegistrationResponse("Clapped", RegistrationType.COMMENT_CLAP,
                        DareUtils.DETAILS_DATE_FORMAT.format(new Date()), clap.getId()))
                        .build();
            }



        } catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response unpinAnchoredContent(String anchoredContentId, String token) throws InternalApplicationException, InvalidRequestException {
        try{
            log.info("Finding anchored content");
            //search anchored content
            AnchoredContent content = dareResponseRepository.findAnchoredContent(anchoredContentId);
            if(content == null)
                throw new InvalidRequestException("Anchored content not valid");

            log.info("Finding user by token");
            DareUser user = dareUserRepository.findUserByToken(token);

            //validate if user owns this anchor
            if(content.getUser().getId().equals(user.getId())){
                log.info("Deleting anchored content");
                //delete anchored content
                dareResponseRepository.unpinContent(anchoredContentId);

                log.info("Anchored content has been removed");
                return Response.ok(new UpdatedEntityResponse("Success", true, "anchoredContent"))
                        .build();
            }else{
                log.error("User doe3s not owns anchored content, invalid request");
                throw new InvalidRequestException("User does not own this anchored content");
            }

        }catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

}
