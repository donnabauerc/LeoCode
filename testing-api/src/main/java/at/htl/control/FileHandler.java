package at.htl.control;

import at.htl.resources.UploadEndpoint;
import org.jboss.logmanager.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileHandler {

    private static final Logger log = Logger.getLogger(FileHandler.class.getSimpleName()); //Because of static methods ...
    private static final List<String> executeTests = Arrays.asList("cd ../project-under-test",
            "/opt/jenkinsfile-runner/bin/jenkinsfile-runner -w /opt/jenkins -p /opt/jenkins_home/plugins/ -f ./Jenkinsfile > log.txt",
            "mv ./log.txt ../");
    public static Map<String, String> currentFiles; //test, code or other

    public static void moveToRequiredDirectory() {
        log.info("Moving files");

        currentFiles.forEach((k, v) -> {
            File file = new File(v);
            String fileDestination = v;

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String packages = br.readLine();
                String pathBeforePackages = UploadEndpoint.FILE_SEPARATOR + "src" + UploadEndpoint.FILE_SEPARATOR;

                switch (k) {
                    case "test":
                        pathBeforePackages += "test";
                        fileDestination = UploadEndpoint.pathToProject + file.getName(); //because of test directory
                        break;
                    case "code":
                        pathBeforePackages += "main";
                        break;
                    case "other":
                        return;
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

            } catch (IOException e) { //delete test directory
                e.printStackTrace();
            }
        });

        new File(UploadEndpoint.pathToProject + "test").delete();
    }

    public static String fetchResult() {
        Path file = Path.of(".." + UploadEndpoint.FILE_SEPARATOR + "log.txt");
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    public static void setup() {
        try {
            File projectDirectory = new File(UploadEndpoint.pathToProject);

            if (!projectDirectory.exists()) {
                projectDirectory.mkdir();
            }

            new File(UploadEndpoint.pathToProject + "log.txt").createNewFile();

            File shellScript = new File("../run-tests.sh");
            shellScript.createNewFile();
            shellScript.setExecutable(true);
            Files.write(shellScript.toPath(), executeTests, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unzipProject(MultipartBody mb) {
        File dest = new File(UploadEndpoint.pathToProject);

        try (ZipInputStream zis = new ZipInputStream(mb.file)) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(dest + UploadEndpoint.FILE_SEPARATOR + zipEntry.toString());

                if (zipEntry.toString().contains("/")) {
                    if (!newFile.getParentFile().exists()) {
                        newFile.getParentFile().mkdirs();
                    }
                    newFile.createNewFile();
                    currentFiles.put("test", newFile.getPath());
                } else if (newFile.getPath().contains(".java")) {
                    newFile.createNewFile();
                    currentFiles.put("code", newFile.getPath());
                } else {
                    newFile.createNewFile();
                    currentFiles.put("other", newFile.getPath());
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(zis.readAllBytes());
                fos.close();

                zipEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
