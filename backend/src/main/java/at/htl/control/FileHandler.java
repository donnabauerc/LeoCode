package at.htl.control;

import at.htl.entities.File;
import at.htl.entities.FileType;
import at.htl.resources.UploadEndpoint;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.core.MultivaluedMap;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class FileHandler {

    private static final Logger log = Logger.getLogger(FileHandler.class.getSimpleName());

    public static String getFileName(MultivaluedMap<String, String> header) {
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

    public static void uploadFile(String fileType, List<InputPart> inputParts){
        if(fileType.equals("exampleName")){
            try {
                InputPart inputPart = inputParts.get(0);
                UploadEndpoint.example.setName(inputPart.getBodyAsString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (InputPart inputPart : inputParts) {
                try {
                    MultivaluedMap<String, String> header = inputPart.getHeaders();
                    String name = getFileName(header);

                    InputStream inputStream = inputPart.getBody(InputStream.class, null);
                    byte[] bytes = inputStream.readAllBytes();

                    at.htl.entities.File f = new File(name, FileType.valueOf(fileType.toUpperCase()), bytes);
                    f.setExample(UploadEndpoint.example);
                    f.persist();

                    log.info("Uploaded " + f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static java.io.File createFile(File file){
        String filename = file.getName();
        byte[] bytes = file.getFile();
        log.info("Extracting " + filename + " from DB");
        java.io.File newFile = new java.io.File("./"+filename);

        try {
            OutputStream os = new FileOutputStream(newFile);
            os.write(bytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFile;
    }

}
