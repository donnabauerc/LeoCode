package at.htl.control;

import at.htl.entities.ExampleType;
import at.htl.entities.SubmissionStatus;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@ApplicationScoped
public class FileHandler {

    private final Path PROJECT_UNDER_TEST_DIRECTORY = Paths.get("../project-under-test/");
    private final Path BUILD_RESULT = Paths.get("../result.txt");
    private final Path RUN_TEST_SCRIPT = Paths.get("../run-tests.sh");
    private final List<String> SHELL_SCRIPT_CONTENT = Arrays.asList("cd " + PROJECT_UNDER_TEST_DIRECTORY.toString(),
            "/opt/jenkinsfile-runner/bin/jenkinsfile-runner -w /opt/jenkins -p /opt/jenkins_home/plugins/ -f ./Jenkinsfile > log.txt",
            "tail -n 1 log.txt > " + BUILD_RESULT.toString());


    public Path pathToProject;
    public HashMap<Path, String> currentFiles;

    @Inject
    Logger log;

    public String testProject(String projectPath, ExampleType type, Set<String> whitelist, Set<String> blacklist) {
        setup(projectPath);
        unzipProject();

        String resWhitelist;
        String resBlacklist;

        if((resWhitelist = checkWhiteOrBlacklist("whitelist", whitelist)) != null) {
            return resWhitelist;
        } else if ((resBlacklist = checkWhiteOrBlacklist("blacklist", blacklist)) != null) {
            return resBlacklist;
        } else {
            try {
                switch (type){
                    case MAVEN:
                        createMavenProjectStructure();
                        //runTests(); => TODO: uncomment SubmissionListener
                    break;
                    default:
                        throw new Exception("Project Type not supported yet!");
                }
                return getResult();
            } catch (Exception e) {
                e.printStackTrace();
                return "Oops, something went wrong!";
            }
        }

    }

    public void setup(String projectPath) {
        try {
            log.info("setup test environment");
            pathToProject = Paths.get(projectPath);
            currentFiles = new HashMap<Path, String>();

            File projectDirectory = PROJECT_UNDER_TEST_DIRECTORY.toFile();
            File runTestsShellscript = RUN_TEST_SCRIPT.toFile();

            if (projectDirectory.exists()) {
                log.info("flushing " + projectDirectory.getPath());
                FileUtils.cleanDirectory(projectDirectory);
            } else {
                log.info("creating " + projectDirectory.getPath());
                projectDirectory.mkdir();
            }

            if (BUILD_RESULT.toFile().exists()) {
                Files.delete(BUILD_RESULT);
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
                    currentFiles.put(newFile.toPath() ,"test");
                } else if (newFile.getPath().contains(".java")) {
                    currentFiles.put(newFile.toPath(), "code");
                } else {
                    currentFiles.put(newFile.toPath(), "other");
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

    public void createMavenProjectStructure() {
        log.info("create Maven Project Structure");

        currentFiles.forEach((k, v) -> {
            File file = k.toFile();
            StringBuilder fileDestination = new StringBuilder().append(PROJECT_UNDER_TEST_DIRECTORY);

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                fileDestination.append("/src/");

                switch (v) {
                    case "test":
                        fileDestination.append("test");
                        break;
                    case "code":
                        fileDestination.append("main");
                        break;
                    case "other": //Files already exist at the correct path (../project-under-test/)
                        return;
                }

                fileDestination.append("/java/");

                //evaluate packages & filename
                String packages = br.readLine();
                packages = packages
                        .substring(
                                packages.lastIndexOf(" ") + 1,
                                packages.lastIndexOf(";"))
                        .replace(".", "/");
                String filename = k.toString().substring(k.toString().lastIndexOf("/"));

                fileDestination.append(packages).append(filename);

                //create Directories
                String directoriesOnly = fileDestination.substring(0, fileDestination.lastIndexOf("/") + 1);
                File directories = new File(directoriesOnly);
                if (!directories.exists()) {
                    directories.mkdirs();
                }

                //move actual File
                file.renameTo(new File(fileDestination.toString()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Delete temporary directory
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

    public SubmissionStatus evaluateStatus(String result){
        log.info("evaluateStatus");

        result = result.substring(result.lastIndexOf(" ") + 1);

        SubmissionStatus status;
        switch (result){
            case "FAILURE":
                status = SubmissionStatus.FAIL;
                break;
            case "SUCCESS":
                status = SubmissionStatus.SUCCESS;
                break;
            default:
                status = SubmissionStatus.ERROR;
                break;
        }
        return status;
    }

    public String checkWhiteOrBlacklist(String type, Set<String> list){
        List<Map.Entry<Path, String>> currentCodeFiles = currentFiles.entrySet().stream()
                .filter(pathStringEntry -> pathStringEntry.getValue().equals("code"))
                .collect(Collectors.toList());

        for(Map.Entry<Path, String> e: currentCodeFiles) {
            for(String s : list) {
                try{
                    switch (type.toLowerCase()){
                        case "blacklist":
                            log.info("checking Blacklist");
                            int c = checkForUsage(s, e.getKey().toFile());
                            if(c >= 0){
                                log.info("Blacklist Error: " + s + " has been used at line "+ c + "!");
                                return "Blacklist Error: " + s + " has been used at line "+ c + "!";
                            }
                            break;
                        case "whitelist":
                            log.info("checking Whitelist");
                            if(checkForUsage(s, e.getKey().toFile()) < 0){
                                log.info("Whitelist Error: " + s + " has not been used!");
                                return "Whitelist Error: " + s + " has not been used!";
                            }
                            break;
                        default:
                            throw new IOException();
                    }

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    return "Sorry, there has been an unknown error!";
                }
            }
        }
        return null;
    }

    public int checkForUsage(String needle, File haystack) throws IOException {
        int lineNumber = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(haystack))) {
            String line;
            while((line = br.readLine())!= null) {
                lineNumber++;
                String[] words = line.split(" ");
                for (String w: words) {
                    if(w.equalsIgnoreCase(needle)){
                        return lineNumber;
                    }
                }
            }
        }
        return -1;
    }
}
