package at.htl.control;

import at.htl.resources.UploadEndpoint;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

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

    public static void uploadFile(String fileType, List<InputPart> inputParts) {// test, pom, code
        String fileDestination = "unknown";

        log.info("Uploading Files");

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileDestination = getFileName(header);

                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = inputStream.readAllBytes();

                fileDestination = UploadEndpoint.pathToProject + fileDestination;

                if(fileType.equals("test")){
                    UploadEndpoint.testFiles.add(fileDestination);
                }else if(fileType.equals("code")){
                    UploadEndpoint.codeFiles.add(fileDestination);
                }

                saveFile(bytes, fileDestination);

            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadEndpoint.currentlyUploadedFiles.add(fileDestination);
        }
    }

    public static void clearDirectory(String path) {
        try {
            ProcessBuilder builder = new ProcessBuilder();

            if(!UploadEndpoint.OS.contains("win")){
                builder.command("bash", "-c", "rm -rf ../project-under-test/*");
            }else{
                builder.command("cmd", "/c", "del ..\\project-under-test\\*");
            }

            Process process = builder.start();

            log.info("Deleting " + path);

            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void moveToRequiredDirectory(List<String> files) {
        log.info("Moving files");

        for (String fileDestination : files) {
            if (fileDestination.endsWith("xml")) {
                return;
            }
            try {
                File file = new File(fileDestination);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String packages = br.readLine();
                String pathBeforePackages = UploadEndpoint.FILE_SEPARATOR + "src" + UploadEndpoint.FILE_SEPARATOR;

                if (UploadEndpoint.testFiles.contains(fileDestination)) {
                    pathBeforePackages += "test";
                } else {
                    pathBeforePackages += "main";
                }

                packages = UploadEndpoint.FILE_SEPARATOR + "java" + UploadEndpoint.FILE_SEPARATOR
                        + packages
                        .substring(
                                packages.lastIndexOf(" ") + 1,
                                packages.lastIndexOf(";"))
                        .replace(".", UploadEndpoint.FILE_SEPARATOR);

                fileDestination = fileDestination.substring(0, fileDestination.lastIndexOf(UploadEndpoint.FILE_SEPARATOR))
                        + pathBeforePackages
                        + packages
                        + fileDestination.substring(fileDestination.lastIndexOf(UploadEndpoint.FILE_SEPARATOR));

                //move file
                File directories = new File(fileDestination
                        .substring(0, fileDestination.lastIndexOf(UploadEndpoint.FILE_SEPARATOR))
                        + UploadEndpoint.FILE_SEPARATOR);

                if (!directories.exists()) {
                    directories.mkdirs();
                }

                file.renameTo(new File(fileDestination));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
