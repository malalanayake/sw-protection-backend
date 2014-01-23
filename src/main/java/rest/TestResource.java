/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

/**
 * REST Web Service
 *
 * @author Simon
 */
@Path("test")
@Api(value = "/test", description = "does fascinating stuff", produces = "text/plain")
public class TestResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TestResource
     */
    public TestResource() {
    }

    /**
     * Retrieves representation of an instance of rest.TestResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "XxXx", httpMethod = "GET", notes = "get a useful remark", response = String.class)
    public String getXml() {
        return "this works...";
    }
}
