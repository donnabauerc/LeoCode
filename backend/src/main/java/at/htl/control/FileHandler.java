package at.htl.control;

import at.htl.entities.Example;
import at.htl.entities.LeocodeFile;
import at.htl.entities.LeocodeFiletype;
import at.htl.repositories.LeocodeFileRepository;
import at.htl.resources.UploadEndpoint;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@ApplicationScoped
public class FileHandler {

    @Inject
    LeocodeFileRepository fileRepository;

    public final String zipLocation = "../project-under-test.zip";

//    @Transactional should be able with fileRepository
//    public List<LeocodeFile> createFile(List<InputPart> inputParts, Example example) {
//        List<LeocodeFile> files = new LinkedList<>();
//
//        for (InputPart inputPart : inputParts) {
//            try {
//                MultivaluedMap<String, String> header = inputPart.getHeaders();
//                String name = fileRepository.getMultipartFileName(header);
//
//                try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {
//                    byte[] bytes = inputStream.readAllBytes();
//                    LeocodeFile file = new LeocodeFile(name, LeocodeFiletype.CODE, bytes, example);
//                    fileRepository.persist(file);
//                    files.add(file);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return files;
//    }

    public String zipLeocodeFiles(List<LeocodeFile> files){
        try (
                FileOutputStream fos = new FileOutputStream(zipLocation);
                ZipOutputStream zipOut = new ZipOutputStream(fos)
        ) {
            files.forEach(file -> {
                try {
                    if (file.filetype.equals(LeocodeFiletype.TEST)) {
                        zipOut.putNextEntry(new ZipEntry("test/" + file.name));
                    } else {
                        zipOut.putNextEntry(new ZipEntry(file.name));
                    }
                    zipOut.write(file.content, 0, file.content.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return zipLocation;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
