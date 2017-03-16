package com.dareu.web.core.service.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.dareu.web.core.DareUtils;
import com.dareu.web.core.codec.ThumbManager;
import com.dareu.web.core.observable.DareEvent;
import com.dareu.web.core.observable.DareuEventHandler;
import com.dareu.web.core.service.DareService;
import com.dareu.web.data.entity.Category;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.CategoryRepository;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.dto.request.CreateCategoryRequest;
import com.dareu.web.dto.request.CreateDareRequest;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse.RegistrationType;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.core.service.DareuMessagingService;
import com.dareu.web.core.service.FileService;
import com.dareu.web.core.service.MultipartService;
import com.dareu.web.data.entity.AnchoredContent;
import com.dareu.web.data.entity.Comment;
import com.dareu.web.data.entity.DareFlag;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.entity.ResponseClap;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.dto.request.AnchorContentRequest;
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
import com.dareu.web.dto.response.message.QueuedDareMessage;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import java.io.IOException;
import javax.enterprise.event.Event;
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
    private DareuMessagingService messagingService;

    @Inject
    private MultipartService multipartService;

    @Inject
    private FileService fileService;

    @Inject
    private ThumbManager thumbManager;

    @Inject
    private Logger log;

    private Event<DareEvent> expiredDaresEvent;

    public DareServiceImpl() {

    }

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
            challengedUser = dareUserRepository.find(request.getFriendId());
            challengerUser = dareUserRepository.findUserByToken(authenticationToken);
            //get category
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
                    messagingService.sendQueuedDareNotification(dare.getId(), dareUserFcmToken,
                            QueuedDareMessage.ACTIVE);
                }
            } else if (unacceptedDare != null && !unacceptedDare.getId().equals(dare.getId())) {
                //user has an unaccepted dare
                log.info("User has an unaccepted dare, sending queued notification");
                //send a notification for another dare waiting for
                if (dareUserFcmToken != null && !dareUserFcmToken.isEmpty()) {
                    messagingService.sendQueuedDareNotification(dare.getId(), dareUserFcmToken,
                            QueuedDareMessage.PENDING);
                }
            } else {
                //user does not have any dare request
                //send push notification to the dared user 
                if (dareUserFcmToken != null && !dareUserFcmToken.isEmpty()) {
                    messagingService.sendNewDareNotification(dare, dareUserFcmToken);
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
            log.info("Saving temporal file " + dareResponse.getId() + ".mp4");
            String videoPath = fileService.saveTemporalfile(request.getStream(), dareResponse.getId(), FileService.FileType.DARE_VIDEO);

            //save video file to where it belongs
            String videoUrl = fileService.saveFile(videoPath, FileService.FileType.DARE_VIDEO, dareResponse.getId() + ".mp4");
            //String videoUrl = fileService.saveFile(request.getStream(), FileService.FileType.DARE_VIDEO, dareResponse.getId().concat(".mp4"));

            log.info("Saving temporal file " + dareResponse.getId() + ".jpg");
            String thumbPath = fileService.saveTemporalfile(request.getThumb(), dareResponse.getId(), FileService.FileType.VIDEO_THUMBNAIL);

            String thumbUrl = fileService.saveFile(thumbPath, FileService.FileType.VIDEO_THUMBNAIL, dareResponse.getId().concat(".jpg"));

            //delete both file 
            fileService.deleteTemporalFile(videoPath);
            fileService.deleteTemporalFile(thumbPath);

            //populate response
            dareResponse.setVideoUrl(videoUrl);
            dareResponse.setThumbUrl(thumbUrl);
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
                messagingService.sendDareResponseUploaded(dareResponse, challengerFcmToken);
            }

            //set dare as complete 
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
    public Response channelResponses(int pageNumber) throws InternalApplicationException {
        try {
            Page<DareResponseDescription> page = dareResponseRepository.getChannelPage(pageNumber);
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
    public Response findResponseDescription(String responseId) throws InternalApplicationException, InvalidRequestException {
        if (responseId == null || responseId.isEmpty()) {
            throw new InvalidRequestException("No response id provided");
        }

        try {
            DareResponse resp = dareResponseRepository.find(responseId);
            int commentsCount = dareResponseRepository.getResponseCommentsCount(responseId);
            if (resp == null) {
                throw new InvalidRequestException("Invalid id");
            }
            DareResponseDescription desc = assembler.assembleDareResponseDescription(resp);
            desc.setComments(commentsCount);
            return Response.ok(desc)
                    .build();
        } catch (Exception ex) {
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
            comment.setLikes(0);
            comment.setResponse(dareResponse);
            comment.setUser(user);

            //persist
            dareResponseRepository.createResponseComment(comment);

            if (!comment.getUser().getId().equals(dareResponse.getDare().getChallengerUser().getId())) {
                //send notification to the dare creator
                String dareCreatorId = dareUserRepository
                        .getUserFcmToken(dareResponse.getDare().getChallengerUser().getId());

                if (dareCreatorId != null && !dareCreatorId.isEmpty()) {
                    messagingService.sendNewCommentNotification(dareCreatorId, comment.getId(),
                            dareResponse.getId(), comment.getComment());
                }
            }

            if (!comment.getUser().getId().equals(dareResponse.getUser().getId())) {
                String videoCreatorId = dareUserRepository
                        .getUserFcmToken(dareResponse.getUser().getId());

                //response creator 
                if (videoCreatorId != null && !videoCreatorId.isEmpty()) {
                    messagingService.sendNewCommentNotification(videoCreatorId,
                            comment.getId(), dareResponse.getId(), comment.getComment());
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
    public Response findResponseComments(int pageNumber, String responseId) throws InternalApplicationException, InvalidRequestException {
        if (responseId == null || responseId.isEmpty()) {
            throw new InvalidRequestException("No response id provided");
        }
        try {
            DareResponse response = dareResponseRepository.find(responseId);
            if (response == null) {
                throw new InvalidRequestException("Invalid response id");
            }
            Page<CommentDescription> page = dareResponseRepository.findResponseComments(pageNumber, responseId);

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

            response.setViewsCount(response.getViewsCount() + 1);

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
            DareResponse response = dareResponseRepository.find(request.getResponseId());
            if (response == null) {
                throw new InvalidRequestException("Response id not valid");
            }

            DareUser user = dareUserRepository.findUserByToken(token);

            if (request.isClapped()) {
                //creates a new clap entity
                //create a new clap 
                ResponseClap clap = new ResponseClap();
                clap.setResponse(response);
                clap.setUser(user);
                //persists
                dareResponseRepository.clapResponse(clap);

                String fcmToken = dareUserRepository.getUserFcmToken(user.getId());
                if (fcmToken != null && !fcmToken.isEmpty()) {
                    //send a notification to dare response creator 
                    messagingService.sendClappedResponse(response.getId(), fcmToken);
                }
                return Response.ok(new UpdatedEntityResponse("Clap has been created", true, "dare_response_clap"))
                        .build();
            } else {
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
    public Response findResponseComment(String commentId) throws InternalApplicationException, InvalidRequestException {
        if (commentId == null || commentId.isEmpty()) {
            throw new InvalidRequestException("Comment is not provided");
        }

        try {
            Comment comment = dareResponseRepository.findComment(commentId);
            if (comment == null) {
                throw new InvalidRequestException("Invalid comment id");
            }

            CommentDescription descr = assembler.assembleCommentDescription(comment);

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
            if(dareResponseRepository.find(responseId) == null)
                throw new InvalidRequestException("Invalid");
            if(dareResponseRepository.findAnchoredContent(responseId, token) != null)
                throw new InvalidRequestException("This response has already been anchored");
            AnchoredContent content;
            //anchor 
            content = new AnchoredContent();
            content.setCreationDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
            content.setResponse(dareResponseRepository.find(responseId));
            content.setUser(dareUserRepository.findUserByToken(token));
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
            Page<AnchoredDescription> page = dareResponseRepository.getAnchoredContent(pageNumber, token);
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response unpinAnchoredContent(String responseId, String token) throws InternalApplicationException, InvalidRequestException {
        try{
            //search anchored content
            AnchoredContent content = dareResponseRepository.findAnchoredContent(responseId, token);
            if(content == null)
                throw new InvalidRequestException("Anchored content not valid");

            //delete anchored content
            dareResponseRepository.unpinContent(responseId, token);

            return Response.ok(new UpdatedEntityResponse("Success", true, "anchoredContent"))
                    .build();
        }catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

}
