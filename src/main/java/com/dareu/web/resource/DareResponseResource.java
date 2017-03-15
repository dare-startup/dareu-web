package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.DareService;
import com.dareu.web.dto.request.ClapRequest;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Path("dare/response")
@AllowedUsers(securityRoles = {SecurityRole.SPONSOR, SecurityRole.USER, SecurityRole.ADMIN})
public class DareResponseResource {
    
    @Inject
    private DareService dareService;

    @Inject
    private Logger log;
    
    
    @ApiOperation(value = "Creates a dare response", produces = "application/json",
            consumes = "multipart/form-data", 
            notes = "Uplaods a dare response video")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response uploadDareResponse(@ApiParam(allowableValues = "dareId, comment, thumb, file", name = "input",
            required = true) MultipartFormDataInput input, @HeaderParam("Authorization") String auth) throws InternalApplicationException {
        return dareService.uploadDareResponse(input, auth);
    }
    
    @ApiOperation(value = "Get all dare responses created by a user", produces = "application/json",
            response = Page.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findResponses(@HeaderParam("Authorization")String auth, 
                @DefaultValue("1") @QueryParam("pageNumber")int pageNumber)
                            throws InternalApplicationException, InvalidRequestException{
        return dareService.findResponses(pageNumber, auth);
    }
    
    @ApiOperation(value = "set a dare as expired", produces = "application/json", 
            notes = "Set a dare as expired")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("hot")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response hotResponses(@DefaultValue("1") @QueryParam("pageNumber")int pageNumber)throws InternalApplicationException{
        return dareService.hotResponses(pageNumber); 
    }
    
    @ApiOperation(value = "Get a page of channel responses", produces = "application/json", 
            notes = "Get a page of channel responses")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("channel")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response channelResponses(@DefaultValue("1") @QueryParam("pageNumber")int pageNumber)throws InternalApplicationException{
        return dareService.channelResponses(pageNumber); 
    }
    
    @ApiOperation(value = "Get a response description", produces = "application/json", 
            notes = "Find an exisiting response description")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("find")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findResponseDescription(@QueryParam("id")String responseId) throws InternalApplicationException, InvalidRequestException {
        return dareService.findResponseDescription(responseId); 
    }
    
    @ApiOperation(value = "set a new view to a dare response", 
            produces = "application/json", 
            notes = "After a video is viewed, the counter goes up +1")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("view")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findResponseComments(@QueryParam("responseId")String responseId) throws InternalApplicationException, InvalidRequestException {
        return dareService.viewedResponse(responseId); 
    }
    
    @ApiOperation(value = "Clap or un-clap a dare response", 
            produces = "application/json", 
            notes = "Clap a response")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("clap")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findResponseComments(ClapRequest request, 
                                         @HeaderParam("Authorization")String token) 
                                throws InternalApplicationException, InvalidRequestException {
        return dareService.clapResponse(request, token); 
    }
}
