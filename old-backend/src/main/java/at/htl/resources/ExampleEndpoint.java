package at.htl.resources;

import at.htl.entities.Example;
import at.htl.entities.LeocodeFile;
import at.htl.repositories.ExampleRepository;
import at.htl.repositories.LeocodeFileRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/example")
public class ExampleEndpoint {

    @Inject
    Logger log;

    @Inject
    ExampleRepository exampleRepository;

    @Inject
    LeocodeFileRepository fileRepository;

    @GET
    @Path("/list")
    public Response listAll(){
        log.info("ListAll()");
        return Response.ok(exampleRepository.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id")long id){
        log.info("GetById("+id+")");
        Example example = exampleRepository.findById(id);
        List<LeocodeFile> files = fileRepository.getFilesByExample(example);

        JsonArrayBuilder fileArray = Json.createArrayBuilder();

        files.forEach(leocodeFile -> {
            fileArray.add(Json.createObjectBuilder()
                    .add("name", leocodeFile.name)
                    .add("fileType", leocodeFile.filetype.toString())
                    .add("content", new String(leocodeFile.content))
                    .build());
        });

        JsonObject obj = Json.createObjectBuilder()
                .add("id", example.id)
                .add("name", example.name)
                .add("description", example.description)
                .add("author", example.author)
                .add("type", example.type.toString())
                .add("files", fileArray.build())
                .build();

        return Response.ok(obj).build();
    }
}
