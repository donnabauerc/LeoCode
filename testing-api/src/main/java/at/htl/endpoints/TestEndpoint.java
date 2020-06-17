package at.htl.endpoints;

import io.quarkus.launcher.shaded.org.apache.commons.io.IOUtils;
import io.quarkus.launcher.shaded.org.slf4j.Logger;
import io.quarkus.launcher.shaded.org.slf4j.LoggerFactory;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Path("/upload")
public class TestEndpoint {
    private final String UPLOADED_FILE_PATH = "../src/test/java/at/htl/examples/";
    private final Logger log = LoggerFactory.getLogger(TestEndpoint.class);

    @POST
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {

        String fileName = "";

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        // depends on form eg. name="uploadedFile"
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();

                fileName = getFileName(header);

                if (!fileName
                        .substring(fileName.lastIndexOf(".") + 1)
                        .toLowerCase()
                        .equals("java")){
                    throw new IOException("Wrong file format!");
                }

                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,null);

                byte [] bytes = IOUtils.toByteArray(inputStream);

                //constructs upload file path
                fileName = UPLOADED_FILE_PATH + fileName;

                saveFile(bytes,fileName);

                log.info("Uploaded "+fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Response.ok(fileName).build();
    }

    /*
     * header sample
     * {
     * 	Content-Type=[image/png],
     * 	Content-Disposition=[form-data; name="file"; filename="filename.extension"]
     * }
     */

    //get uploaded filename
    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");

                return finalFileName;
            }
        }
        return "unknown";
    }

    private void saveFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();
    }
}
