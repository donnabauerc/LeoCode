package at.htl.control;

import at.htl.entities.LeocodeFile;
import at.htl.entities.LeocodeFileType;

import javax.enterprise.context.ApplicationScoped;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@ApplicationScoped
public class FileHandler {

    public final String zipLocation = "../../projects/project-under-test";

    public String zipLeocodeFiles(Long submitionId, List<LeocodeFile> files) {
        final String destination = zipLocation + "-" + submitionId.toString() + ".zip";
        try (
                FileOutputStream fos = new FileOutputStream(destination);
                ZipOutputStream zipOut = new ZipOutputStream(fos)
        ) {
            files.forEach(file -> {
                try {
                    if (file.filetype.equals(LeocodeFileType.TEST)) {
                        zipOut.putNextEntry(new ZipEntry("test/" + file.name));
                    } else {
                        zipOut.putNextEntry(new ZipEntry(file.name));
                    }
                    zipOut.write(file.content, 0, file.content.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return destination;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
