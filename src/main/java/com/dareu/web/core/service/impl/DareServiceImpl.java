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
import com.dareu.web.data.entity.Comment;
import com.dareu.web.data.entity.DareFlag;
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.dto.request.DareConfirmationRequest;
import com.dareu.web.dto.request.DareUploadRequest;
import com.dareu.web.dto.request.FlagDareRequest;
import com.dareu.web.dto.request.NewCommentRequest;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.response.entity.ActiveDare;
import com.dareu.web.dto.response.entity.CategoryDescription;
import com.dareu.web.dto.response.entity.CommentDescription;
import com.dareu.web.dto.response.entity.CreatedDare;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.DareResponseDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.response.entity.UnacceptedDare;
import com.dareu.web.dto.response.entity.UserDescription;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ejb.Asynchronous;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.imageio.ImageIO;
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
            log.info("Successfully created new dare with id: " + id);

            //send push notification to the dared user 
            String dareUserFcmToken = dareUserRepository.getUserFcmToken(challengedUser.getId());
            if (dareUserFcmToken != null && !dareUserFcmToken.isEmpty()) {
                messagingService.sendNewDareNotification(dare, dareUserFcmToken);
                log.info("Sending FCM message to " + challengedUser.getName());
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
                unacceptedDare = new UnacceptedDare();
                unacceptedDare.setId(dare.getId());
                unacceptedDare.setName(dare.getName());
                unacceptedDare.setDescription(dare.getDescription());
                unacceptedDare.setCreationDate(DareUtils.DETAILS_DATE_FORMAT.format(dare.getCreationDate()));
                challenger = dare.getChallengerUser();
                challengerDescription = new UserDescription();
                challengerDescription.setId(challenger.getId());
                challengerDescription.setName(challenger.getName());
                challengerDescription.setUserSinceDate(challenger.getUserSince());
                challengerDescription.setProfileImageAvailable(fileService.fileExists(FileService.FileType.PROFILE_IMAGE, challenger.getId()));
                unacceptedDare.setTimer(dare.getEstimatedDareTime());
                unacceptedDare.setChallenger(challengerDescription);
                return Response.ok(unacceptedDare)
                        .build();
            } else //return an empty response
            {
                return Response.ok()
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

            ActiveDare description = dareRepository.getCurrentActiveDare(id);
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
            //save file 
            fileService.saveFile(request.getStream(), FileService.FileType.DARE_VIDEO, dareResponse.getId().concat(".mp4"));
            if (request.getThumb() == null) {
                //create new thumb
                thumbManager.createThumb(fileService.getFile(dareResponse.getId(), FileService.FileType.DARE_VIDEO), dareResponse.getId());
            } else {
                //save attached thumb file 
                fileService.saveFile(request.getThumb(), FileService.FileType.VIDEO_THUMBNAIL, dareResponse.getId().concat(".jpg"));
            }
            //populate response
            dareResponse.setComment(request.getComment());
            dareResponse.setViewsCount(0);
            dareResponse.setUser(user);
            dareResponse.setResponseDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
            //get dare 
            Dare dare = dareRepository.find(request.getDareId());
            if (dare == null) {
                throw new InternalApplicationException("Dare id is not valid");
            }
            dareResponse.setDare(dare);
            dareResponse.setLikes(0);
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
        return null;
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
            return Response.ok(page)
                    .build();
        } catch (DataAccessException ex) {
            throw new InternalApplicationException(ex.getMessage());
        }

    }

    @Override
    public Response getThumbImage(String responseId) throws InternalApplicationException, InvalidRequestException {
        if(responseId == null && responseId.isEmpty())
            throw new InvalidRequestException("No id provided");
        
        try{
            DareResponse response = dareResponseRepository.find(responseId); 
            if(response == null)
                throw new InvalidRequestException("Invalid id");
            
            //get image 
            InputStream stream = fileService.getFileStream(responseId, FileService.FileType.VIDEO_THUMBNAIL); 
            /**BufferedImage image = ImageIO.read(stream);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ImageIO.write(image, "jpg", out);**/
            return Response.ok(stream)
                    .build(); 
        }catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage()); 
        }catch(IOException ex){
            throw new InternalApplicationException(ex.getMessage()); 
        }
    }

    @Override
    public Response findResponseDescription(String responseId) throws InternalApplicationException, InvalidRequestException {
        if(responseId == null || responseId.isEmpty())
            throw new InvalidRequestException("No response id provided"); 
        
        try{
            DareResponse resp = dareResponseRepository.find(responseId); 
            if(resp == null)
                throw new InvalidRequestException("Invalid id"); 
            
            DareResponseDescription desc = assembler.assembleDareResponseDescription(resp);
            return Response.ok(desc)
                    .build(); 
        }catch(Exception ex){
            throw new InternalApplicationException(ex.getMessage(), ex); 
        }
    }

    @Override
    public Response createResponseComment(NewCommentRequest request, String token) throws InternalApplicationException, InvalidRequestException {
        if(request == null)
            throw new InvalidRequestException("No request body provided");
        
        if(request.getComment() == null || request.getComment().isEmpty())
            throw new InvalidRequestException("No comment body provided");
        if(request.getResponseId() == null || request.getResponseId().isEmpty())
            throw new InvalidRequestException("No response id provided");
        
        try{
            //get response 
            DareResponse dareResponse = dareResponseRepository.find(request.getResponseId());
            if(dareResponse == null)
                throw new InvalidRequestException("Invalid response id");
            
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
            
            //send notification to response creator and dare creator 
            String videoCreatorId = dareUserRepository
                    .getUserFcmToken(dareResponse.getUser().getId());
            
            String dareCreatorId = dareUserRepository
                    .getUserFcmToken(dareResponse.getDare().getChallengerUser().getId());
            
            //response creator 
            if(videoCreatorId != null && ! videoCreatorId.isEmpty())
                messagingService.sendNewCommentNotification(videoCreatorId, 
                        comment.getId(), dareResponse.getId(), comment.getComment());
            
            if(dareCreatorId != null && ! dareCreatorId.isEmpty())
                messagingService.sendNewCommentNotification(dareCreatorId, comment.getId(), 
                        dareResponse.getId(), comment.getComment());
            
            return Response.ok(
                    new EntityRegistrationResponse("Success", RegistrationType.COMMENT, 
                            DareUtils.DETAILS_DATE_FORMAT.format(new Date()), comment.getId()))
                    .build();
        } catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

    @Override
    public Response findResponseComments(int pageNumber, String responseId) throws InternalApplicationException, InvalidRequestException {
        if(responseId == null || responseId.isEmpty())
            throw new InvalidRequestException("No response id provided");
        try{
            DareResponse response = dareResponseRepository.find(responseId); 
            if(response == null)
                throw new InvalidRequestException("Invalid response id");
            Page<CommentDescription> page = dareResponseRepository.findResponseComments(pageNumber, responseId);
            
            //return response
            return Response.ok(page)
                    .build();
        }catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage(), ex);
        }
    }

}
