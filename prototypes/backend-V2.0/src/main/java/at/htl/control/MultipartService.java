package at.htl.control;

import at.htl.entities.MultipartBody;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@RegisterRestClient
public interface MultipartService {

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    String sendMultipartData(@MultipartForm MultipartBody data);

    @GET
    @Path("/run")
    @Produces(MediaType.TEXT_PLAIN)
    String runTests();
}
