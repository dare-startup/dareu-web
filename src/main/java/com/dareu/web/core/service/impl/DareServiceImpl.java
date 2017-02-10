package com.dareu.web.core.service.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.dareu.web.core.DareUtils;
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
import com.dareu.web.data.entity.DareResponse;
import com.dareu.web.data.repository.DareResponseRepository;
import com.dareu.web.dto.request.DareConfirmationRequest;
import com.dareu.web.dto.request.DareUploadRequest;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.response.entity.ActiveDare;
import com.dareu.web.dto.response.entity.CategoryDescription;
import com.dareu.web.dto.response.entity.CreatedDare;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.response.entity.UnacceptedDare;
import com.dareu.web.dto.response.entity.UserDescription;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;
import java.io.IOException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public class DareServiceImpl implements DareService {

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
    private Logger log;

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
                challengerDescription.setProfileImageAvailable(fileService.userHasProfileImage(challenger.getId())); 
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
        try{
            //get request object 
            DareUploadRequest request = multipartService.getDareUploadRequest(input); 
            //get user uplaoding video 
            DareUser user = dareUserRepository.findUserByToken(auth); 
            //validate
            if(request == null || request.getDareId() == null || request.getStream() == null)
                throw new InternalApplicationException("Invalid multipart request"); 
            //save file 
            fileService.saveFile(request.getStream(), FileService.FileType.DARE_VIDEO, user.getId().concat(".jpg")); 
            //create new Dare response 
            DareResponse dareResponse = new DareResponse(); 
            dareResponse.setViewsCount(0);
            dareResponse.setUser(user);
            dareResponse.setResponseDate(DareUtils.DETAILS_DATE_FORMAT.format(new Date()));
            //get dare 
            Dare dare = dareRepository.find(request.getDareId());
            if(dare == null)
                throw new InternalApplicationException("Dare id is not valid"); 
            dareResponse.setDare(dare);
            dareResponse.setLikes(0);
            dareResponseRepository.persist(dareResponse);
            //get fcm token from challenger user
            String challengerFcmToken = dareUserRepository.getUserFcmToken(dare.getChallengerUser().getId()); 
            //send notification
            messagingService.sendDareResponseUploaded(dareResponse, challengerFcmToken);
            //return response
            return Response.ok(new EntityRegistrationResponse("Dare response has been created", RegistrationType.DARE_RESPONSE, 
                                DareUtils.DETAILS_DATE_FORMAT.format(new Date()), dareResponse.getId()))
                    .build();
        }catch(DataAccessException ex){
            throw new InternalApplicationException(ex.getMessage()); 
        }catch(IOException ex){
            throw new InternalApplicationException(ex.getMessage()); 
        }
    }

}
