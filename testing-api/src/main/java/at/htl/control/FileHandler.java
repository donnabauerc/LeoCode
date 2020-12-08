package at.htl.control;

import at.htl.Main;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileHandler {

    private final static String pathToProjectQueue = "../projects-in-queue/";
    private final static String projectUnderTest = "./project-under-test/";
    private final static String runTestsFile = "./run-tests.sh";
    private final static List<String> runTestsContent = Arrays.asList("cd ./project-under-test",
            "/opt/jenkinsfile-runner/bin/jenkinsfile-runner -w /opt/jenkins -p /opt/jenkins_home/plugins/ -f ./Jenkinsfile > log.txt",
            "mv ./log.txt ../");
    public static Map<String, String> currentFiles; //test, code or other

    private static final Logger log = Logger.getLogger(Main.class);

    private static String projectUnderTestZipPath;

    public static void setup() {
        try {
            log.info("setup test environment");
            projectUnderTestZipPath = pathToProjectQueue + "project-under-test-" + Main.submitionId + ".zip";
            currentFiles = new HashMap<>();
            File projectDirectory = new File(projectUnderTest);
            File runTestsShellscript = new File(runTestsFile);

            if (projectDirectory.exists()) {
                log.info("flushing " + projectDirectory.getPath());
                Arrays.asList(projectDirectory.listFiles()).forEach(File::delete);
            } else {
                log.info("creating " + projectDirectory.getPath());
                projectDirectory.mkdir();
            }

            if (!runTestsShellscript.exists()) {
                log.info("creating " + runTestsShellscript.getPath());
                runTestsShellscript.createNewFile();
                runTestsShellscript.setExecutable(true);
                Files.write(runTestsShellscript.toPath(), runTestsContent, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unzipProject() {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(projectUnderTestZipPath))) {
            log.info("unzipping " + projectUnderTestZipPath);
            File dest = new File(projectUnderTest);

            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(dest + "/" + zipEntry.toString());

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
