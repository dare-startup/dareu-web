/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.resource;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author MACARENA
 */
@Path("health")
public class HealthResource {
    public Response health(){
        return Response.ok("DareU version 1.0")
                .type(MediaType.TEXT_PLAIN)
                .build(); 
    }
}
