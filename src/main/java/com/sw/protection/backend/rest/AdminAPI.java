package com.sw.protection.backend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.service.AdminService;
import com.sw.protection.backend.service.impl.AdminServiceImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Rest web service for operate the admin details
 * 
 * @author dinuka
 * 
 */
@Path("/" + APINames.ADMIN)
@Api(value = "/" + APINames.ADMIN, description = "Rest api for do operations on admin", produces = MediaType.APPLICATION_JSON)
@Produces({ MediaType.APPLICATION_JSON })
public class AdminAPI {
    @Context
    private UriInfo context;
    private static final String ACCEPT_HEADERS = "accept";
    @Context
    private HttpHeaders headders;

    @GET
    @Path("/{userName}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get specific admin", httpMethod = "GET", notes = "Fetch the admin user details", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Given admin user found"),
	    @ApiResponse(code = 404, message = "Given admin user not found"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
    public Response getAdmin(
	    @ApiParam(value = "user_name of admin", required = true) @PathParam("userName") String userName) {
	AdminService adminService = AdminServiceImpl.getInstance();
	try {
	    String adminData = "";
	    // process the JSON type request
	    if (headders.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		adminData = adminService.getAdmin(EncoderDecoderType.JSON, "{user_name:\"" + userName + "\"}");
	    }
	    // TODO: Need to process the XML type requests

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
	    return Response.status(Status.PRECONDITION_FAILED).build();
	}
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
	headders = httpHeaders;
    }
}
