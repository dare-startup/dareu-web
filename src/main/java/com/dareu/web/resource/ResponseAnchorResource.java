package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.DareService;
import com.dareu.web.dto.request.AnchorContentRequest;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Path("dare/response/anchor")
@AllowedUsers(securityRoles = {SecurityRole.SPONSOR, SecurityRole.USER, SecurityRole.ADMIN})
public class ResponseAnchorResource {

    @Inject
    private DareService dareService;
    
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response anchorContent(@QueryParam("responseId")String responseId,
                                @HeaderParam("Authorization")String token)throws InternalApplicationException, 
                                                            InvalidRequestException{
        return dareService.anchorContent(responseId, token);
    }
    
    @Path("unpin")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response unpinAnchoredContent(@QueryParam("responseId")String responseId,
                                @HeaderParam("Authorization")String token)throws InternalApplicationException, 
                                                            InvalidRequestException{
        return dareService.unpinAnchoredContent(responseId, token);
    }
    
    
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Secured
    public Response getAnchoredContent(@QueryParam("pageNumber")int pageNumber, 
                                    @HeaderParam("Authorization")String token)throws InternalApplicationException, 
                                                            InvalidRequestException{
        return dareService.getAnchoredContent(pageNumber, token);
    }
}
