package at.htl.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/debug")
public class DebugEndpoint {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(String requestBody) throws Exception {
        System.out.println("Got a Request");
        System.out.println(requestBody);
        return requestBody;
    }
}
