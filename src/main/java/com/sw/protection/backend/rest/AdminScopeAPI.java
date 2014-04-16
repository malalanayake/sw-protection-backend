package com.sw.protection.backend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.common.exception.RequiredDataNotFoundException;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.service.AdminScopeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
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
@Path("/" + APINames.ADMIN_SCOPE)
@Api(value = "/" + APINames.ADMIN_SCOPE, description = "Rest api for do operations on admin scopes", produces = MediaType.APPLICATION_JSON)
@Produces({ MediaType.APPLICATION_JSON })
public class AdminScopeAPI {
    private static final String ACCEPT_HEADERS = "accept";
    @Context
    private HttpHeaders headers;

    @GET
    @Path("/{userName}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get admin scope list", httpMethod = "GET", notes = "Fetch the admin user scope details", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Given admin user found"),
	    @ApiResponse(code = 404, message = "Given admin scope list not found"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
    public Response getAdminScopes(
	    @ApiParam(value = "user_name of admin", required = true) @PathParam("userName") String userName) {
	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);

	try {
	    String adminData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		adminData = adminScopeService.getAdminScopes(EncoderDecoderType.JSON, userName);
	    }
	    // TODO: Need to process the XML type requests

	    if (adminData != "") {
		return Response.ok().entity(adminData).build();
	    } else {
		return Response.status(404).build();
	    }
	} catch (EncodingException e) {
	    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	} catch (RequiredDataNotFoundException e) {
	    return Response.status(Status.PRECONDITION_FAILED).build();
	}
    }

    @GET
    @Path("/{userName}/{apiName}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get specific admin scope", httpMethod = "GET", notes = "Fetch the admin user scope details", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Given admin scope found"),
	    @ApiResponse(code = 404, message = "Given admin scope is not found"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
    public Response getAdminScope(
	    @ApiParam(value = "user_name of admin", required = true) @PathParam("userName") String userName,
	    @ApiParam(value = "API name of admin", required = true) @PathParam("apiName") String apiName) {
	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);

	try {
	    String adminData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		adminData = adminScopeService.getAdminScope(EncoderDecoderType.JSON, userName, apiName);
	    }
	    // TODO: Need to process the XML type requests

	    if (adminData != "") {
		return Response.ok().entity(adminData).build();
	    } else {
		return Response.status(404).build();
	    }
	} catch (EncodingException e) {
	    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	} catch (RequiredDataNotFoundException e) {
	    return Response.status(Status.PRECONDITION_FAILED).build();
	}
    }

    @GET
    @Path("/{userName}/{apiName}/{apiOpertaion}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Check whether the specific admin scope", httpMethod = "GET", notes = "Check the access of given admin user for given operation", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Given admin scope found"),
	    @ApiResponse(code = 404, message = "Given admin scope is not found"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
    public Response isAccessGrantForAdmin(
	    @ApiParam(value = "user_name of admin", required = true) @PathParam("userName") String userName,
	    @ApiParam(value = "API name of admin", required = true) @PathParam("apiName") String apiName,
	    @ApiParam(value = "API operation of admin", required = true) @PathParam("apiOpertaion") String apiOpertaion) {
	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);

	try {
	    String adminData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		adminData = adminScopeService.isAccessGrantedFor(userName, apiName, apiOpertaion);
	    }
	    // TODO: Need to process the XML type requests

	    if (adminData != "") {
		return Response.ok().entity(adminData).build();
	    } else {
		return Response.status(404).build();
	    }
	} catch (RequiredDataNotFoundException e) {
	    return Response.status(Status.PRECONDITION_FAILED).build();
	}
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Save specific admin scope", httpMethod = "POST", notes = "Add new admin scope data", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Save admin scope successful"),
	    @ApiResponse(code = 404, message = "Given admin scope not saved"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
	    @ApiResponse(code = 409, message = "Duplicate recode"),
	    @ApiResponse(code = 304, message = "Not modified due to operation rollback") })
    @ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "AdminScope", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
    public Response saveAdminScope(String body) {

	AdminScopeService adminScopeService = AppContext.getInstance().getBean(AdminScopeService.class);

	try {
	    String adminData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		adminData = adminScopeService.saveAdminScope(EncoderDecoderType.JSON, body);
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
	} catch (DuplicateRecordException e) {
	    return Response.status(Status.CONFLICT).build();
	} catch (OperationRollBackException e) {
	    return Response.status(Status.NOT_MODIFIED).build();
	}
    }

    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Update specific admin scope", httpMethod = "PUT", notes = "update existing admin user scope", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Admin user scope successfuly updated"),
	    @ApiResponse(code = 404, message = "Given admin scope not updated"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
	    @ApiResponse(code = 304, message = "Not modified due to operation rollback"),
	    @ApiResponse(code = 206, message = "Partial content due to given recode is not the latest modification") })
    @ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "Admin", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
    public Response updateAdminScope(String body) {

	AdminScopeService adminService = AppContext.getInstance().getBean(AdminScopeService.class);

	try {
	    String adminData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		adminData = adminService.updateAdminScope(EncoderDecoderType.JSON, body);
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
	} catch (OperationRollBackException e) {
	    return Response.status(Status.NOT_MODIFIED).build();
	} catch (RecordAlreadyModifiedException e) {
	    return Response.status(206).build();
	}
    }
}
