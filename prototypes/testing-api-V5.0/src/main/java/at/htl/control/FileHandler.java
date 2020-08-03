package at.htl.control;

import at.htl.resources.UploadEndpoint;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileHandler {

    private static final Logger log = Logger.getLogger(FileHandler.class.getSimpleName());
    private static List<String> testFiles = new LinkedList<>();
    private static List<String> codeFiles = new LinkedList<>();
    public static List<String> currentlyUploadedFiles = new LinkedList<>();


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

    public static void uploadFile(MultipartBody multipartBody) {// test, pom, code
        String fileDestination = "unknown";

        log.info("Uploading Files");

        try {
            fileDestination = multipartBody.fileName;

            InputStream inputStream = multipartBody.file;
            byte[] bytes = inputStream.readAllBytes();

            fileDestination = UploadEndpoint.pathToProject + fileDestination;

            if(multipartBody.fileType.toLowerCase().equals("test")){
                testFiles.add(fileDestination);
            }else if(multipartBody.fileType.toLowerCase().equals("code")){
                codeFiles.add(fileDestination);
            }

            saveFile(bytes, fileDestination);

            } catch (IOException e) {
                e.printStackTrace();
            }
            currentlyUploadedFiles.add(fileDestination);
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

    public static void moveToRequiredDirectory() {
        log.info("Moving files");

        System.out.println("CurrentlyUploadedFiles: " + currentlyUploadedFiles.toString());
        System.out.println("TestFiles: "+ testFiles.toString());
        System.out.println("CodeFiles: "+ codeFiles.toString());
        System.out.println(UploadEndpoint.FILE_SEPARATOR);


        for (String fileDestination : currentlyUploadedFiles) {
            System.out.println("fileDestination" + fileDestination);
            if (!fileDestination.endsWith("xml")) {
                try {
                    File file = new File(fileDestination);
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String packages = br.readLine();
                    String pathBeforePackages = UploadEndpoint.FILE_SEPARATOR + "src" + UploadEndpoint.FILE_SEPARATOR;

                    if (testFiles.contains(fileDestination)) {
                        pathBeforePackages += "test";
                    } else {
                        pathBeforePackages += "main";
                    }

                    System.out.println(packages);
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
}
