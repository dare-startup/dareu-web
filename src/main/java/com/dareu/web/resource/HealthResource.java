/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author MACARENA
 */
@Api(value = "health", authorizations = {
    @Authorization(value = "ALL")}, produces = "plain/text")
@Path("health")
public class HealthResource {

    @ApiOperation(value = "Get system health", httpMethod = "GET", response = String.class, 
            notes = "Check database connection and system functionallity", produces = "plain/text")
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "The operation was executed successfuly", response = String.class) 
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok("DareU version 1.0")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
