package com.dareu.web.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dareu.web.core.service.AccountService;
import com.dareu.web.dto.request.SigninRequest;
import com.dareu.web.data.exception.AuthenticationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "security")
@Path("security/")
public class SecurityResource {

    @Inject
    private AccountService accountService;

    /**
     * Signin
     *
     * @param request
     * @return
     * @throws com.dareu.web.data.exception.AuthenticationException
     */
    @Path("authenticate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "authenticate", consumes = "application/json", httpMethod = "POST", 
            produces = "application/json", response = SigninRequest.class, notes = "Authenticates against Dareu services")
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Authentication has been executed successfully"),
        @ApiResponse(code = 401, message = "Authentication failed")})
    public Response signin(@ApiParam(required = true, name = "signinRequest", allowableValues = "SigninRequest class")
            SigninRequest request) throws AuthenticationException {
        return accountService.authenticate(request);
    }
}
