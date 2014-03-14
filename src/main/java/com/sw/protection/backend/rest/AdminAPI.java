package com.sw.protection.backend.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.service.AdminService;
import com.sw.protection.backend.service.impl.AdminServiceImpl;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Rest web service for operate the admin details
 * 
 * @author dinuka
 * 
 */
@Path("/" + APINames.ADMIN)
public class AdminAPI {
    @Context
    private UriInfo context;

    @GET
    @Path("/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getXml(@Context HttpServletRequest req, @PathParam("userName") String userName) {
	AdminService adminService = AdminServiceImpl.getInstance();
	try {
	    String adminData = adminService.getAdmin(EncoderDecoderType.JSON, "{user_name:\"" + userName + "\"}");
	    if (adminData != "") {
		return Response.ok().entity(adminData).build();
	    } else {
		return Response.status(404).build();
	    }
	} catch (EncodingException e) {
	    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	} catch (DecodingException e) {
	    return Response.status(Status.BAD_REQUEST).build();
	} catch (RequiredDataNotFoundException e) {
	    return Response.status(Status.NOT_ACCEPTABLE).build();
	}
    }
}
