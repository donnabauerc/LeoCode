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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Path("/upload")
public class UploadEndpoint {

    @ConfigProperty(name = "project-under-test-folder")
    String projectUnderTestFolder;

    private final Logger log = Logger.getLogger(UploadEndpoint.class.getSimpleName());
    public String projectUnderTestName;

    //Later should be dynamic - must be the same as test anyway -> read first line
    public String projectUnderTestPackages;

    @POST
    @Path("/project")
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) throws IOException {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        // depends on form eg. name="uploadedFile"
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        String fileName = FileGenerator.uploadFile(projectUnderTestFolder + System.getProperty("file.separator"),
                inputParts, "zip");
        log.info("Uploaded " + fileName);

        setProjectName(fileName);

        try {
            log.info("Unzip " + fileName);
            ZipFile zipFile = new ZipFile(fileName);
            zipFile.extractAll(projectUnderTestFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Delete " + fileName);
        new File(fileName).delete();

        return Response.ok("Uploaded "+fileName).build();
    }

    @POST
    @Path("/test")
    @Consumes("multipart/form-data")
    public Response uploadTests(MultipartFormDataInput input) throws IOException {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        // depends on form eg. name="uploadedFile"
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        String fileName = FileGenerator.uploadFile(projectUnderTestFolder + System.getProperty("file.separator"),
                inputParts, "java");
        log.info("Uploaded " + fileName);

        setProjectUnderTestPackages(fileName);

        if(projectUnderTestName != null){//filename
            String filenameWithoutPath = fileName
                    .substring(
                            (fileName.lastIndexOf("/")+1),
                            fileName.length());

            projectUnderTestPackages = "../project-under-test/"
                    + projectUnderTestName
                    + "/src/test/java"
                    + projectUnderTestPackages
                    + "/" + filenameWithoutPath;

            log.info("Filename "+fileName);
            log.info("Filepath "+projectUnderTestPackages);

            File f = new File(fileName);
            f.renameTo(new File(projectUnderTestPackages));

        }else {
            throw new IOException("Please upload a Project first!");
        }

        return Response.ok("Uploaded "+fileName).build();
    }

    public void setProjectName(String path){
        projectUnderTestName = path.substring(path.lastIndexOf("/")+1,
                path.lastIndexOf("."));
    }

    public void setProjectUnderTestPackages(String path){
        try {
            log.info("Read Packages");
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            projectUnderTestPackages = br.readLine();

            projectUnderTestPackages = "/" + projectUnderTestPackages
                    .substring(
                            projectUnderTestPackages.lastIndexOf(" ")+1,
                            projectUnderTestPackages.lastIndexOf(";"))
                    .replace(".", "/");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
