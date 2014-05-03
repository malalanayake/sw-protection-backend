package com.sw.protection.backend.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.sw.protection.backend.service.CompanySWService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Rest web service for operate the company Software details
 * 
 * @author dinuka
 * 
 */
@Path("/" + APINames.COMPANY_SW)
@Api(value = "/" + APINames.COMPANY_SW, description = "Rest api for do operations on company software", produces = MediaType.APPLICATION_JSON)
@Produces({ MediaType.APPLICATION_JSON })
public class CompanySWAPI {

    private static final String ACCEPT_HEADERS = "accept";
    @Context
    private HttpHeaders headers;

    @GET
    @Path("/{userName}/{softwareName}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get specific company user", httpMethod = "GET", notes = "Fetch the company software details", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Given company software found"),
	    @ApiResponse(code = 404, message = "Given company software not found"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
    public Response getCompanySW(
	    @ApiParam(value = "user_name of company", required = true) @PathParam("userName") String userName,
	    @ApiParam(value = "software_name", required = true) @PathParam("softwareName") String softwareName) {
	CompanySWService companySWService = AppContext.getInstance().getBean(CompanySWService.class);

	try {
	    String companyUserData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companyUserData = companySWService.getCompanySW(EncoderDecoderType.JSON, userName, softwareName);
	    }
	    // TODO: Need to process the XML type requests

	    if (companyUserData != "") {
		return Response.ok().entity(companyUserData).build();
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
    @Path("/" + APINames.COMPANY_SW_LIST + "/{item_per_list}/{page_number}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get list of company software", httpMethod = "GET", notes = "Fetch the list of company software details", response = Response.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Company software list featched"),
	    @ApiResponse(code = 404, message = "There is no list found according to the given {item_per_list} and {page_number}"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to {item_per_list} or {page_number} is not grater than zero") })
    public Response getCompanySWList(
	    @ApiParam(value = "Max Company software to be featched", required = true) @PathParam("item_per_list") int itemPerList,
	    @ApiParam(value = "Page number of company software list", required = true) @PathParam("page_number") int pageNumber) {
	CompanySWService companySWService = AppContext.getInstance().getBean(CompanySWService.class);

	try {
	    String companySWData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySWData = companySWService.getAllCompanySW(EncoderDecoderType.JSON, pageNumber, itemPerList);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySWData != "") {
		return Response.ok().entity(companySWData).build();
	    } else {
		return Response.status(404).build();
	    }
	} catch (EncodingException e) {
	    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	} catch (RequiredDataNotFoundException e) {
	    return Response.status(Status.PRECONDITION_FAILED).build();
	}
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Save specific company software", httpMethod = "POST", notes = "Add new company software", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Save company software successful"),
	    @ApiResponse(code = 404, message = "Given company software not saved"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
	    @ApiResponse(code = 409, message = "Duplicate recode"),
	    @ApiResponse(code = 304, message = "Not modified due to operation rollback") })
    @ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanySW", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
    public Response saveCompanySW(String body) {

	CompanySWService companySWService = AppContext.getInstance().getBean(CompanySWService.class);

	try {
	    String companySWData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySWData = companySWService.saveCompanySW(EncoderDecoderType.JSON, body);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySWData != "") {
		return Response.ok().entity(companySWData).build();
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
    @ApiOperation(value = "Update specific company software", httpMethod = "PUT", notes = "update existing company software", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Company software successfuly updated"),
	    @ApiResponse(code = 404, message = "Given company software not updated"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
	    @ApiResponse(code = 304, message = "Not modified due to operation rollback"),
	    @ApiResponse(code = 206, message = "Partial content due to given recode is not the latest modification") })
    @ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanySW", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
    public Response updateCompanySW(String body) {

	CompanySWService companySwService = AppContext.getInstance().getBean(CompanySWService.class);

	try {
	    String companySwData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySwData = companySwService.updateCompanySW(EncoderDecoderType.JSON, body);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySwData != "") {
		return Response.ok().entity(companySwData).build();
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

    @DELETE
    @Path("/" + APINames.COMPANY_SW_DELETE)
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete specific company software", httpMethod = "DELETE", notes = "delete existing company software", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Company software successfuly deleted"),
	    @ApiResponse(code = 404, message = "Given company software not deleted"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
	    @ApiResponse(code = 304, message = "Not deleted due to operation rollback"),
	    @ApiResponse(code = 206, message = "Partial content due to given recode is not the latest modification") })
    @ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanySW", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
    public Response deleteCompanySW(String body) {

	CompanySWService companySwService = AppContext.getInstance().getBean(CompanySWService.class);

	try {
	    String companySWData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySWData = companySwService.deleteCompanySW(EncoderDecoderType.JSON, body);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySWData != "") {
		return Response.ok().entity(companySWData).build();
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
