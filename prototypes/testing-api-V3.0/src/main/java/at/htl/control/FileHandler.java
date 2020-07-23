package at.htl.control;

import at.htl.resources.UploadEndpoint;
import org.apache.commons.io.FileUtils;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void saveFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();
    }

    public static void uploadFile(String uploadPath, List<InputPart> inputParts) {
        String fileDestination = "unknown";

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileDestination = getFileName(header);

                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = inputStream.readAllBytes();

                fileDestination = uploadPath + fileDestination;
                saveFile(bytes, fileDestination);

            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadEndpoint.files.add(fileDestination);
        }

        log.info("Uploaded Files");
    }

    public static void clearDirectory(String path){
        log.info("Deleting " + path);
        for (File f: new File(path).listFiles()) {
            f.delete();
        }
    }

    public static void moveToRequiredDirectory(List<String> files){
        log.info("Moving files");

        for (String fileDestination: files) {
            if(fileDestination.endsWith("xml")){
                return;
            }
            try {
                File file = new File(fileDestination);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String packages = br.readLine();
                String path = UploadEndpoint.FILE_SEPARATOR + "src" + UploadEndpoint.FILE_SEPARATOR;

                if(UploadEndpoint.filesAreForTesting){
                    path += "test";
                }else{
                    path += "main";
                }

                packages = UploadEndpoint.FILE_SEPARATOR + "java" + UploadEndpoint.FILE_SEPARATOR
                        + packages
                            .substring(
                                packages.lastIndexOf(" ") + 1,
                                packages.lastIndexOf(";"))
                            .replace(".", UploadEndpoint.FILE_SEPARATOR);

                fileDestination = fileDestination.substring(0, fileDestination.lastIndexOf(UploadEndpoint.FILE_SEPARATOR))
                        + path
                        + packages
                        + fileDestination.substring(fileDestination.lastIndexOf(UploadEndpoint.FILE_SEPARATOR));

                //move file
                File directories = new File(fileDestination
                        .substring(0, fileDestination.lastIndexOf(UploadEndpoint.FILE_SEPARATOR))
                        + UploadEndpoint.FILE_SEPARATOR);

                if(!directories.exists()){
                    directories.mkdirs();
                }

                file.renameTo(new File(fileDestination));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
