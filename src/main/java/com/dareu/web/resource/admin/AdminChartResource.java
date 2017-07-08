package com.dareu.web.resource.admin;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.dto.request.DateRangesRequest;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.exception.application.InternalApplicationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin/metrics")
@AllowedUsers(securityRoles = { SecurityRole.ADMIN })
public class AdminChartResource {

    @ApiOperation(value = "Get the number of users registered per day in the last month", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "ADMIN")
            },
            notes = "You can send the two dates (from and to) to get the number of users registered per day between those dates. If no body is received, the dates will br taken from last month to actual date. ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfully",
                    response = EntityRegistrationResponse.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("users")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getAverageRegisteredUsersPerDay(DateRangesRequest dateRangesRequest)
            throws InternalApplicationException {
        return null;
    }

    //TODO
    public void getAverageCreatedDaresPerDay(DateRangesRequest dateRangesRequest)throws InternalApplicationException{

    }

    //TODO
    public void getNumberOfPendingDares()throws InternalApplicationException{

    }
}
