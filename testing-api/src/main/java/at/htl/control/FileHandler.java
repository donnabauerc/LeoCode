package at.htl.control;

import at.htl.entities.LeocodeStatus;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@ApplicationScoped
public class FileHandler {

    private final Path PROJECT_UNDER_TEST_DIRECTORY = Paths.get("../project-under-test/");
    private final Path RUN_TEST_SCRIPT = Paths.get("../run-tests.sh");
    private final List<String> SHELL_SCRIPT_CONTENT = Arrays.asList("cd ../project-under-test",
            "/opt/jenkinsfile-runner/bin/jenkinsfile-runner -w /opt/jenkins -p /opt/jenkins_home/plugins/ -f ./Jenkinsfile > log.txt",
            "tail -n 1 log.txt > ../result.txt");


    public Path pathToProject;
    public HashMap<String, Path> currentFiles;

    @Inject
    Logger log;

    public void testProject(String projectPath) {
        setup(projectPath);
        unzipProject();
        createJavaProjectStructure();
        runTests();
    }

    public void setup(String projectPath) {
        try {
            log.info("setup test environment");
            pathToProject = Paths.get(projectPath);
            currentFiles = new HashMap<String, Path>();

            File projectDirectory = PROJECT_UNDER_TEST_DIRECTORY.toFile();
            File runTestsShellscript = RUN_TEST_SCRIPT.toFile();

            if (projectDirectory.exists()) {
                log.info("flushing " + projectDirectory.getPath());
                FileUtils.cleanDirectory(projectDirectory);
            } else {
                log.info("creating " + projectDirectory.getPath());
                projectDirectory.mkdir();
            }

            if (!runTestsShellscript.exists()) {
                log.info("creating " + runTestsShellscript.getPath());
                runTestsShellscript.createNewFile();
                runTestsShellscript.setExecutable(true);
                Files.write(runTestsShellscript.toPath(), SHELL_SCRIPT_CONTENT, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unzipProject() {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(pathToProject))) {
            log.info("unzipping " + pathToProject);
            File dest = PROJECT_UNDER_TEST_DIRECTORY.toFile();

            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(dest + "/" + zipEntry.toString());

                if (zipEntry.toString().contains("/")) {
                    if (!newFile.getParentFile().exists()) {
                        newFile.getParentFile().mkdirs();
                    }
                    currentFiles.put("test", newFile.toPath());
                } else if (newFile.getPath().contains(".java")) {
                    currentFiles.put("code", newFile.toPath());
                } else {
                    currentFiles.put("other", newFile.toPath());
                }

                newFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(zis.readAllBytes());
                fos.close();

                zipEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createJavaProjectStructure() {
        log.info("create Java Project Structure");

        currentFiles.forEach((k, v) -> {
            File file = v.toFile();
            String fileDestination = v.toString();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String packages = br.readLine();
                String pathBeforePackages = "/src/";

                switch (k) {
                    case "test":
                        pathBeforePackages += "test";
                        break;
                    case "code":
                        pathBeforePackages += "main";
                        break;
                    case "other":
                        return;
                }

                packages = "/java/"
                        + packages
                        .substring(
                                packages.lastIndexOf(" ") + 1,
                                packages.lastIndexOf(";"))
                        .replace(".", "/");

                fileDestination = PROJECT_UNDER_TEST_DIRECTORY.toString()
                        + pathBeforePackages
                        + packages
                        + fileDestination.substring(fileDestination.lastIndexOf("/"));

                //move file
                File directories = new File(fileDestination
                        .substring(0, fileDestination.lastIndexOf("/"))
                        + "/");

                if (!directories.exists()) {
                    directories.mkdirs();
                }

                file.renameTo(new File(fileDestination));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Paths.get(PROJECT_UNDER_TEST_DIRECTORY.toString() + "/test").toFile().delete();
    }

    public void runTests() {
        log.info("running tests");
        try {
            ProcessBuilder builder = new ProcessBuilder("../run-tests.sh");
            Process process = builder.start();
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getResult(){
        log.info("getResult");
        try (BufferedReader br = new BufferedReader(new FileReader("../result.txt"))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "Something Went Wrong";
        }
    }

    public LeocodeStatus evaluateStatus(String result){
        log.info("evaluateStatus");

        result = result.substring(result.lastIndexOf(" ") + 1);

        LeocodeStatus status;
        switch (result){
            case "FAILURE":
                status = LeocodeStatus.FAIL;
                break;
            case "SUCCESS":
                status = LeocodeStatus.SUCCESS;
                break;
            default:
                status = LeocodeStatus.ERROR;
                break;
        }
        return status;
    }
}
