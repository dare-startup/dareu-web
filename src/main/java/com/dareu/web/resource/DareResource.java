package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.DareService;
import com.dareu.web.dto.request.CreateCategoryRequest;
import com.dareu.web.dto.request.CreateDareRequest;
import com.dareu.web.dto.request.DareConfirmationRequest;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

import io.swagger.annotations.Api;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;

@Api(value = "dare")
@Path("dare/")
@AllowedUsers(securityRoles = {SecurityRole.SPONSOR, SecurityRole.USER, SecurityRole.ADMIN})
public class DareResource {

    @Inject
    private DareService dareService;

    @Inject
    private AccountService accountService;

    @Inject
    private Logger log;

    @Path("create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response createNewDare(CreateDareRequest request, @HeaderParam("Authorization") String auth) throws InternalApplicationException,
            InvalidRequestException {
        return dareService.createNewDare(request, auth);
    }
    
    @Path("confirm")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response confirmDareRequest(DareConfirmationRequest request)throws InternalApplicationException, InvalidRequestException{
        return dareService.confirmDareRequest(request);
    }
    
    @Path("unaccepted")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findUnacceptedDare(@HeaderParam("Authorization")String auth) throws InternalApplicationException{
        return dareService.findUnacceptedDare(auth);
    }

    

    @Path("category")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getCategories(@QueryParam("pageNumber")int pageNumber) throws InternalApplicationException {
        return dareService.getCategories(pageNumber);
    }
}
