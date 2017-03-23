package com.dareu.web.resource.open;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.dto.request.ChangeEmailAddressRequest;
import com.dareu.web.dto.request.ContactRequest;
import com.dareu.web.dto.request.GoogleSignupRequest;
import com.dareu.web.dto.request.SignupRequest;
import com.dareu.web.dto.response.*;
import com.dareu.web.dto.response.entity.ActiveDare;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jose.rubalcaba
 */
@Api(produces = "application/json", value = "open")
@Path("open")
public class OpenResource {
    
    @Inject
    private AccountService accountService; 

    
    /**
     * check if a nickname is available
     * @param email
     * @return 
     * @throws com.dareu.web.exception.application.InternalApplicationException 
     */
    @ApiOperation(value = "Check if an email is available", produces = "application/json", 
            authorizations = { @Authorization(value = "ALL")}, 
            notes = "Check if a email is available")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfully",
                response = ResourceAvailableResponse.class), 
        
    })
    @Path("emailAvailable/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response emailAvailable(@PathParam("email")String email)throws InternalApplicationException{
        return accountService.isEmailAvailable(email);
    }    
    
    /**
     * Register a new user
     * @param input
     * @return 
     * @throws java.lang.Exception 
     */
    @ApiOperation(value = "Register a new user to dareu", produces = "application/json", 
            authorizations = { @Authorization(value = "ALL")}, 
            notes = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfully",
                response = AuthenticationResponse.class), 
        
    })
    @Path("registerUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response registerUser(SignupRequest input)throws Exception{
    	//SignupRequest request = multipartService.getSignupRequest(input); 
        return  accountService.registerDareUser(input); 
    }


    
    @ApiOperation(value = "Creates a new contact message to be shown on admin console", produces = "application/json", 
            authorizations = { @Authorization(value = "ALL")}, 
            notes = "Send a new contact message")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfully",
                response = EntityRegistrationResponse.class)
    })
    @Path("contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response contactMessage(ContactRequest message)throws InternalApplicationException, InvalidRequestException{
    	//SignupRequest request = multipartService.getSignupRequest(input); 
        return  accountService.contactMessage(message); 
    }

    @ApiOperation(value = "Sign up a user using a google id ", produces = "application/json",
            authorizations = { @Authorization(value = "ALL")},
            notes = "Creates a new user using a google id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfully",
                    response = EntityRegistrationResponse.class)
    })
    @Path("registerUserGoogle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response signupGoogle(GoogleSignupRequest request)throws InternalApplicationException, InvalidRequestException{
        return  accountService.signupGoogle(request);
    }

    @ApiOperation(value = "process a forgot password request", produces = "application/json",
            notes = "Send an email to change a password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfuly",
                    response = UpdatedEntityResponse.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("forgotPassword")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeEmailAddress(@QueryParam(value = "email") String email)throws InternalApplicationException, InvalidRequestException{
        return accountService.forgotPassword(email);
    }
}
