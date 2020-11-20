package at.htl.control;

import at.htl.entities.*;
import at.htl.entities.File;
import at.htl.resources.UploadEndpoint;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileHandler {
    
    private static Logger log = Logger.getLogger(FileHandler.class.getSimpleName());
    
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
                try (InputStream inputStream = inputPart.getBody(InputStream.class, null);) {
                    MultivaluedMap<String, String> header = inputPart.getHeaders();
                    String name = getFileName(header);

                    byte[] bytes = inputStream.readAllBytes();

                    at.htl.entities.File f = new File(name, FileType.valueOf(fileType.toUpperCase()), bytes);
                    f.setExample(UploadEndpoint.example);
                    f.persist();

                    log.info("Uploaded " + f + " to Database");
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

        try (OutputStream os = new FileOutputStream(newFile);) {
            os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFile;
    }

    public static void zipFiles(List<File> files){
        try (
                FileOutputStream fos = new FileOutputStream(UploadEndpoint.zip);
                ZipOutputStream zipOut = new ZipOutputStream(fos)
                ) {
            files.forEach(file -> {
                try {
                    if (file.getType().equals(FileType.TEST)) {
                        zipOut.putNextEntry(new ZipEntry("test/"+file.getName()));
                    } else {
                        zipOut.putNextEntry(new ZipEntry(file.getName()));
                    }

                    zipOut.write(file.getFile(), 0, file.getFile().length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
