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
import com.sw.protection.backend.service.CompanyUserScopeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Rest web service for operate the company user scope details
 * 
 * @author dinuka
 * 
 */
@Path("/" + APINames.COMPANY_USER_SCOPE)
@Api(value = "/" + APINames.COMPANY_USER_SCOPE, description = "Rest api for do operations on company user scopes", produces = MediaType.APPLICATION_JSON)
@Produces({ MediaType.APPLICATION_JSON })
public class CompanyUserScopeAPI {
	private static final String ACCEPT_HEADERS = "accept";
	@Context
	private HttpHeaders headers;

	@GET
	@Path("/{userName}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Get company user scope list", httpMethod = "GET", notes = "Fetch the company user scope details", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Given company user scope found"),
			@ApiResponse(code = 404, message = "Given company user scope list not found"),
			@ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
			@ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
	public Response getCompanyUserScopes(
			@ApiParam(value = "user_name of company user", required = true) @PathParam("userName") String userName) {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);

		try {
			String companyUserScopData = "";
			// process the JSON type request
			if (headers.getRequestHeaders().get(ACCEPT_HEADERS)
					.contains(MediaType.APPLICATION_JSON)) {
				companyUserScopData = companyUserScopeService.getCompanyUserScopes(
						EncoderDecoderType.JSON, userName);
			}
			// TODO: Need to process the XML type requests

			if (companyUserScopData != "") {
				return Response.ok().entity(companyUserScopData).build();
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
	@ApiOperation(value = "Get specific company user scope", httpMethod = "GET", notes = "Fetch the company user scope details", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Given company user scope found"),
			@ApiResponse(code = 404, message = "Given company user scope is not found"),
			@ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
			@ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
	public Response getCompanyUserScope(
			@ApiParam(value = "user_name of company user", required = true) @PathParam("userName") String userName,
			@ApiParam(value = "API name of company user", required = true) @PathParam("apiName") String apiName) {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);

		try {
			String companyUserScopeData = "";
			// process the JSON type request
			if (headers.getRequestHeaders().get(ACCEPT_HEADERS)
					.contains(MediaType.APPLICATION_JSON)) {
				companyUserScopeData = companyUserScopeService.getCompanyUserScope(
						EncoderDecoderType.JSON, userName, apiName);
			}
			// TODO: Need to process the XML type requests

			if (companyUserScopeData != "") {
				return Response.ok().entity(companyUserScopeData).build();
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
	@ApiOperation(value = "Check whether the specific company user scope", httpMethod = "GET", notes = "Check the access of given company user for given operation", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Given company user scope found"),
			@ApiResponse(code = 404, message = "Given company user scope is not found"),
			@ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
			@ApiResponse(code = 412, message = "Pre condition failed due to required data not found") })
	public Response isAccessGrantForCompanyUser(
			@ApiParam(value = "user_name of company user", required = true) @PathParam("userName") String userName,
			@ApiParam(value = "API name of company user", required = true) @PathParam("apiName") String apiName,
			@ApiParam(value = "API operation of company user", required = true) @PathParam("apiOpertaion") String apiOpertaion) {
		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);

		try {
			String companyUserScopeData = "";
			// process the JSON type request
			if (headers.getRequestHeaders().get(ACCEPT_HEADERS)
					.contains(MediaType.APPLICATION_JSON)) {
				companyUserScopeData = companyUserScopeService.isAccessGrantedFor(userName,
						apiName, apiOpertaion);
			}
			// TODO: Need to process the XML type requests

			if (companyUserScopeData != "") {
				return Response.ok().entity(companyUserScopeData).build();
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
	@ApiOperation(value = "Save specific company user scope", httpMethod = "POST", notes = "Add new company user scope data", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Save company user scope successful"),
			@ApiResponse(code = 404, message = "Given company user scope not saved"),
			@ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
			@ApiResponse(code = 400, message = "Bad request due to decoding the data"),
			@ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
			@ApiResponse(code = 409, message = "Duplicate recode"),
			@ApiResponse(code = 304, message = "Not modified due to operation rollback") })
	@ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanyUserScope", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
	public Response saveCompanyUserScope(String body) {

		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);

		try {
			String companyUserScopeData = "";
			// process the JSON type request
			if (headers.getRequestHeaders().get(ACCEPT_HEADERS)
					.contains(MediaType.APPLICATION_JSON)) {
				companyUserScopeData = companyUserScopeService.saveCompanyUserScope(
						EncoderDecoderType.JSON, body);
			}
			// TODO: Need to process the XML type requests

			if (companyUserScopeData != "") {
				return Response.ok().entity(companyUserScopeData).build();
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
	@ApiOperation(value = "Update specific company user scope", httpMethod = "PUT", notes = "update existing company user scope", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Company user scope successfuly updated"),
			@ApiResponse(code = 404, message = "Given company user scope not updated"),
			@ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
			@ApiResponse(code = 400, message = "Bad request due to decoding the data"),
			@ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
			@ApiResponse(code = 304, message = "Not modified due to operation rollback"),
			@ApiResponse(code = 206, message = "Partial content due to given recode is not the latest modification") })
	@ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanyUserScope", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
	public Response updateCompanyUserScope(String body) {

		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);

		try {
			String companyUserScopeData = "";
			// process the JSON type request
			if (headers.getRequestHeaders().get(ACCEPT_HEADERS)
					.contains(MediaType.APPLICATION_JSON)) {
				companyUserScopeData = companyUserScopeService.updateCompanyUserScope(
						EncoderDecoderType.JSON, body);
			}
			// TODO: Need to process the XML type requests

			if (companyUserScopeData != "") {
				return Response.ok().entity(companyUserScopeData).build();
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
	@Path("/" + APINames.COMPANY_USER_SCOPE_DELETE)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Delete specific company user scope", httpMethod = "DELETE", notes = "delete existing company user scope", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Company user scope successfuly deleted"),
			@ApiResponse(code = 404, message = "Given company user scope not deleted"),
			@ApiResponse(code = 500, message = "Internal server error due to encoding the data"),
			@ApiResponse(code = 400, message = "Bad request due to decoding the data"),
			@ApiResponse(code = 412, message = "Pre condition failed due to required data not found"),
			@ApiResponse(code = 304, message = "Not deleted due to operation rollback"),
			@ApiResponse(code = 206, message = "Partial content due to given recode is not the latest modification") })
	@ApiImplicitParams({ @ApiImplicitParam(required = true, dataType = "CompanyUserScope", paramType = "body", allowableValues = MediaType.APPLICATION_JSON) })
	public Response deleteCompany(String body) {

		CompanyUserScopeService companyUserScopeService = AppContext.getInstance().getBean(
				CompanyUserScopeService.class);

		try {
			String companyUserScopeData = "";
			// process the JSON type request
			if (headers.getRequestHeaders().get(ACCEPT_HEADERS)
					.contains(MediaType.APPLICATION_JSON)) {
				companyUserScopeData = companyUserScopeService.deleteCompanyUserScope(
						EncoderDecoderType.JSON, body);
			}
			// TODO: Need to process the XML type requests

			if (companyUserScopeData != "") {
				return Response.ok().entity(companyUserScopeData).build();
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
