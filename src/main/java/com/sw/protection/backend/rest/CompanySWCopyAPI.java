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
import com.sw.protection.backend.service.CompanySWCopyService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Rest web service for operate the company Software Copy details
 * 
 * @author dinuka
 * 
 */
@Path("/" + APINames.COMPANY_SW_COPY)
@Api(value = "/" + APINames.COMPANY_SW_COPY, description = "Rest api for do operations on company software copy", produces = MediaType.APPLICATION_JSON)
@Produces({ MediaType.APPLICATION_JSON })
public class CompanySWCopyAPI {
    private static final String ACCEPT_HEADERS = "accept";
    @Context
    private HttpHeaders headers;

    @GET
    @Path("/{clientUserName}/{softwareName}/{mb}/{hd}/{mac}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get specific company software copy", httpMethod = "GET", notes = "Fetch the company software copy details", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Given company software copy found"),
	    @ApiResponse(code = 404, message = "Given company software copy not found"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
    public Response getCompanySWCopy(
	    @ApiParam(value = "user_name of company client", required = true) @PathParam("clientUserName") String clientUserName,
	    @ApiParam(value = "software_name", required = true) @PathParam("softwareName") String softwareName,
	    @ApiParam(value = "mother_board name", required = true) @PathParam("mb") String mb,
	    @ApiParam(value = "hard drive", required = true) @PathParam("hd") String hd,
	    @ApiParam(value = "mac address", required = true) @PathParam("mac") String mac) {
	CompanySWCopyService companySWCopyService = AppContext.getInstance().getBean(CompanySWCopyService.class);

	try {
	    String companySWCopyData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySWCopyData = companySWCopyService.getCompanySWCopy(EncoderDecoderType.JSON, clientUserName,
			softwareName, mb, hd, mac);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySWCopyData != "") {
		return Response.ok().entity(companySWCopyData).build();
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
    @Path("/" + APINames.COMPANY_SW_COPY_LIST + "/{item_per_list}/{page_number}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get list of company software copies", httpMethod = "GET", notes = "Fetch the list of company software copy details", response = Response.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Company software copy list featched"),
	    @ApiResponse(code = 404, message = "There is no list found according to the given {item_per_list} and {page_number}"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to {item_per_list} or {page_number} is not grater than zero") })
    public Response getCompanySWCopyList(
	    @ApiParam(value = "Max Company software copy to be featched", required = true) @PathParam("item_per_list") int itemPerList,
	    @ApiParam(value = "Page number of company software copy list", required = true) @PathParam("page_number") int pageNumber) {
	CompanySWCopyService companySWCopyService = AppContext.getInstance().getBean(CompanySWCopyService.class);

	try {
	    String companySWCopyData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySWCopyData = companySWCopyService.getAllCompanySWCopy(EncoderDecoderType.JSON, pageNumber,
			itemPerList);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySWCopyData != "") {
		return Response.ok().entity(companySWCopyData).build();
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
    @ApiOperation(value = "Save specific company software copy", httpMethod = "POST", notes = "Add new company software copy", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Save company software copy successful"),
	    @ApiResponse(code = 404, message = "Given company software copy not saved"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
	    @ApiResponse(code = 409, message = "Duplicate recode"),
	    @ApiResponse(code = 304, message = "Not modified due to operation rollback") })
    @ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanySWCopy", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
    public Response saveCompanySWCopy(String body) {

	CompanySWCopyService companySWCopyService = AppContext.getInstance().getBean(CompanySWCopyService.class);

	try {
	    String companySWCopyData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySWCopyData = companySWCopyService.saveCompanySWCopy(EncoderDecoderType.JSON, body);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySWCopyData != "") {
		return Response.ok().entity(companySWCopyData).build();
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
    @ApiOperation(value = "Update specific company software copy", httpMethod = "PUT", notes = "update existing company software copy", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Company software copy successfuly updated"),
	    @ApiResponse(code = 404, message = "Given company software copy not updated"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
	    @ApiResponse(code = 304, message = "Not modified due to operation rollback"),
	    @ApiResponse(code = 206, message = "Partial content due to given recode is not the latest modification") })
    @ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanySWCopy", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
    public Response updateCompanySWCopy(String body) {

	CompanySWCopyService companySwCopyService = AppContext.getInstance().getBean(CompanySWCopyService.class);

	try {
	    String companySwCopyData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySwCopyData = companySwCopyService.updateCompanySWCopy(EncoderDecoderType.JSON, body);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySwCopyData != "") {
		return Response.ok().entity(companySwCopyData).build();
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
    @Path("/" + APINames.COMPANY_SW_COPY_DELETE)
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete specific company software copy", httpMethod = "DELETE", notes = "delete existing company software copy", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Company software copy successfuly deleted"),
	    @ApiResponse(code = 404, message = "Given company software copy not deleted"),
	    @ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
	    @ApiResponse(code = 400, message = "Bad request due to decoding the data"),
	    @ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
	    @ApiResponse(code = 304, message = "Not deleted due to operation rollback"),
	    @ApiResponse(code = 206, message = "Partial content due to given recode is not the latest modification") })
    @ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanySWCopy", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
    public Response deleteCompanySWCopy(String body) {

	CompanySWCopyService companySwCopyService = AppContext.getInstance().getBean(CompanySWCopyService.class);

	try {
	    String companySWCopyData = "";
	    // process the JSON type request
	    if (headers.getRequestHeaders().get(ACCEPT_HEADERS).contains(MediaType.APPLICATION_JSON)) {
		companySWCopyData = companySwCopyService.deleteCompanySWCopy(EncoderDecoderType.JSON, body);
	    }
	    // TODO: Need to process the XML type requests

	    if (companySWCopyData != "") {
		return Response.ok().entity(companySWCopyData).build();
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
