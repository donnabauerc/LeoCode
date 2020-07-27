package at.htl.control;

import at.htl.entities.Example;
import at.htl.entities.File;
import at.htl.entities.FileType;
import at.htl.resources.UploadEndpoint;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
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

    @Transactional
    public static void uploadFile(List<InputPart> inputParts){
        log.info("Uploading File...");

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String name = getFileName(header);
                String fileType;

                if(UploadEndpoint.filesAreForTesting){
                    if(name.substring(name.lastIndexOf(".")+1).contains("java")){
                        fileType = "test";
                    }else{
                        fileType = "pom";
                    }
                }else{
                    fileType = "solution";
                }

                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = inputStream.readAllBytes();

                File f = new File(name, FileType.valueOf(fileType.toUpperCase()), bytes);
                f.persist();

                log.info("Uploaded " + f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
