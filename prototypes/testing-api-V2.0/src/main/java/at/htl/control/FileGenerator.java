package at.htl.control;

import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileGenerator {

    private static final Logger log = Logger.getLogger(FileGenerator.class.getSimpleName());

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

    public static String uploadFile(String uploadPath, List<InputPart> inputParts, String fileType) {
        String fileName = "";

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileName = getFileName(header);

                if (!fileName
                        .substring(fileName.lastIndexOf(".") + 1)
                        .toLowerCase()
                        .equals(fileType)) {
                    throw new IOException("Wrong file format!");
                }

                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = inputStream.readAllBytes();

                fileName = uploadPath + fileName;
                saveFile(bytes, fileName);

                log.info("Upload " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }
}
