package at.htl.control;

import at.htl.Main;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class FileHandler {

    private final static String pathToProjectQueue = "../projects-in-queue/";
    private final static String projectUnderTest = "./project-under-test/";
    private final static String runTestsFile = "./run-tests.sh";
    private final static List<String> runTestsContent = Arrays.asList("cd ./project-under-test",
            "/opt/jenkinsfile-runner/bin/jenkinsfile-runner -w /opt/jenkins -p /opt/jenkins_home/plugins/ -f ./Jenkinsfile > log.txt",
            "mv ./log.txt ../");

    private static final Logger log = Logger.getLogger(Main.class);

    private static String projectUnderTestZipPath;

    public static void setup() {
        try {
            log.info("setup test environment");
            projectUnderTestZipPath = pathToProjectQueue + "project-under-test-" + Main.submitionId + ".zip";
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

    }
}
