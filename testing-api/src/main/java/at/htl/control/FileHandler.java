package at.htl.control;

import at.htl.resources.UploadEndpoint;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    private static final Logger log = Logger.getLogger(FileHandler.class.getSimpleName());
    public static List<String> testFiles = new LinkedList<>();
    public static List<String> codeFiles = new LinkedList<>();
    public static List<String> currentlyUploadedFiles = new LinkedList<>();
    private static final List<String> executeTests = Arrays.asList("cd ../project-under-test",
            "mvn test", "cat ./target/surefire-reports/*.txt > log.txt");


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

            builder.command("bash", "-c", "rm -rf " + path + "/*");

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

        for (String fileDestination : currentlyUploadedFiles) {
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

    public static String fetchResult(){
        Path file = Path.of(UploadEndpoint.pathToProject + UploadEndpoint.FILE_SEPARATOR + "log.txt");
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    public static void createDir(){
        File dir = new File(UploadEndpoint.pathToProject);

        if(!dir.exists()){
            dir.mkdir();
        }

        try {

            File shellScript = new File("../run-tests.sh");

            if(!shellScript.exists()){
                shellScript.createNewFile();
                shellScript.setExecutable(true);
                Files.write(shellScript.toPath(), executeTests, StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
