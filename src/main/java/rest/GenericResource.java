/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author dinuka
 */
@Path("/me")
@Api(value = "/me", description = "Me first swagger integration", produces = "text/plain")
@Produces({"application/json", "application/xml"})
public class GenericResource {

    @Context
    private UriInfo context;

    private static ConcurrentHashMap<String, String> userData = new ConcurrentHashMap<String, String>();

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {

        System.out.println("Initalizing REST API");
    }

    /**
     * Retrieves representation of an instance of me.first.rest.GenericResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/{id}")
    @Produces("text/plain")
    @Consumes("application/json")
    @ApiOperation(value = "XxXx", httpMethod = "GET", notes = "get a useful remark", response = Response.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found")
    })
    public Response getXml(@Context HttpServletRequest req, @ApiParam(value = "ID of me to fetch", required = true) @PathParam("id") String id) {
        //TODO return proper representation object
        //Monitor mon = MonitorFactory.start(id);

        //get the actual ip
        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }
        //If the server behind the LB or proxy this will take the proxy address
        String remoteHost = req.getRemoteHost();

        int remotePort = req.getRemotePort();
        String msg = remoteHost + " (" + ipAddress + ":" + remotePort + ")";

        userData.put(id, id);

        boolean bool = true;
        System.out.println("Initalizing REST" + id);
        String ss = "";
        if (!userData.isEmpty()) {
            for (String s : userData.keySet()) {
                ss = ss + " " + s;
            }
        }
        URI url = context.getBaseUri();
        UUID idOne = UUID.randomUUID();
        //UUID idTwo = UUID.fromString("9wXCEmkY-GcwA-Dofe-9uqc-A4ruMdG4bkYN");
        //mon.stop();

        return Response.ok().entity("getUserById are called by, ids test : " + ss + url + "/" + idOne + "/" + msg).build();
         
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
