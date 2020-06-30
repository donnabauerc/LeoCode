package at.htl.resources;

import at.htl.control.FileGenerator;
import net.lingala.zip4j.ZipFile;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("/upload")
public class UploadEndpoint {

    private final Logger log = Logger.getLogger(UploadEndpoint.class.getSimpleName());

    @ConfigProperty(name = "project-under-test")
    private String projectUnderTest;

    private String path = projectUnderTest + "/";

    public String projectName;
    public String projectPackages;

    @POST
    @Path("/project")
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        // depends on form eg. name="uploadedFile"
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        path = FileGenerator
                .uploadFile(path, inputParts, "zip");

        projectName = path.substring(
                path.lastIndexOf("/") + 1,
                path.lastIndexOf(".")
        );

        try {
            ZipFile zipFile = new ZipFile(path);
            zipFile.extractAll(projectUnderTest);
            log.info("Unzip " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new File(path).delete();
        log.info("Delete " + path);

        return Response.ok("Uploaded " + path).build();
    }

    @POST
    @Path("/test")
    @Consumes("multipart/form-data")
    public Response uploadTests(MultipartFormDataInput input) throws IOException {
        path = projectUnderTest + "/";

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        path = FileGenerator
                .uploadFile(path, inputParts, "java");

        getProjectPackages();

        if (projectName != null) {
            String fileName = path
                    .substring(
                            (path.lastIndexOf("/") + 1),
                            path.length());

            path =  projectUnderTest + "/" + projectName + "/src/test/java"
                    + projectPackages + "/" + fileName;

            File f = new File(fileName);
            f.renameTo(new File(path));

            log.info("Move File " + path);
        } else {
            throw new IOException("Please upload a Project first!");
        }

        return Response.ok("Uploaded Test" ).build();
    }

    public void getProjectPackages() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            projectPackages = br.readLine();

            projectPackages = "/" + projectPackages
                    .substring(
                            projectPackages.lastIndexOf(" ") + 1,
                            projectPackages.lastIndexOf(";"))
                    .replace(".", "/");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
