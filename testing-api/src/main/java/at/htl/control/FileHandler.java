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
    public HashMap<String, Path> currentFiles; //TODO: probably wrong, because of possible multiple same keys (multiple code files)

    @Inject
    Logger log;

    public String testProject(String projectPath, ExampleType type, Set<String> whitelist, Set<String> blacklist) {
        setup(projectPath);
        unzipProject();

        String resWhitelist;
        String resBlacklist;

        if((resWhitelist = checkWhitelist(whitelist)) != null) {
            return resWhitelist;
        } else if ((resBlacklist = checkBlacklist(blacklist)) != null) {
            return resBlacklist;
        } else {
            try {
                switch (type){
                    case MAVEN:
                        createMavenProjectStructure();
                        //runTests(); => uncomment SubmissionListener
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

            //Todo: delete result.txt file before run => only latest result or not existing
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

    public void createMavenProjectStructure() {
        log.info("create Maven Project Structure");

        currentFiles.forEach((k, v) -> {
            File file = v.toFile();
            StringBuilder fileDestination = new StringBuilder().append(PROJECT_UNDER_TEST_DIRECTORY);

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                fileDestination.append("/src/");

                switch (k) {
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
                String filename = v.toString().substring(v.toString().lastIndexOf("/"));

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

    public String checkWhitelist(Set<String> whitelist) {
        Iterator it = currentFiles.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            switch (pair.getKey().toString()){
                case "code":
                    for(String s : whitelist) {
                        try(BufferedReader br = new BufferedReader(new FileReader(currentFiles.get(pair.getKey()).toFile()))){
                            String line;
                            boolean wordWasUsed = false;
                            while((line = br.readLine())!= null) {
                                String[] words = line.split(" ");
                                for (String w: words) {
                                    if(wordWasUsed = w.equalsIgnoreCase(s)){
                                        break;
                                    }
                                }
                                if(wordWasUsed) {
                                    break;
                                }
                            }

                            if(!wordWasUsed){
                                log.info("Whitelist Error: " + s + " was not used!");
                                return "Whitelist Error: " + s + " was not used!";
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "Sorry, there has been an unknown error!";
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    public String checkBlacklist(Set<String> blacklist) {
        return null;
    }
}
