package com.dareu.web.resource.open;

import com.dareu.web.core.service.AccountService;
import com.dareu.web.dto.request.SignupRequest;
import com.dareu.web.exception.InternalApplicationException;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jose.rubalcaba
 */
@Path("open")
public class OpenResource {
    
    @Inject
    private AccountService accountService; 
    
    
    /**
     * check if a nickname is available
     * @return 
     */
    @Path("nicknameAvailable/{nickname}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response nicknameAvailable(@PathParam("nickname")String nickname)throws InternalApplicationException{
        return accountService.isNicknameAvailable(nickname); 
    }
    
    /**
     * check if a nickname is available
     * @param email
     * @return 
     * @throws com.dareu.web.exception.InternalApplicationException 
     */
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
    @Path("registerUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response registerUser(SignupRequest input)throws Exception{
    	//SignupRequest request = multipartService.getSignupRequest(input); 
        return  accountService.registerDareUser(input); 
    }
    
}
