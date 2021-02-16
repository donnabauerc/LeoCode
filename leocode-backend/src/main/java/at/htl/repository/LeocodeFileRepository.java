package at.htl.repository;

import at.htl.entity.Example;
import at.htl.entity.LeocodeFile;
import at.htl.entity.LeocodeFileType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@ApplicationScoped
public class LeocodeFileRepository implements PanacheRepository<LeocodeFile> {

    public final String zipLocation = "../../projects-in-queue/project-under-test";

    public String getMultipartFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");

                return finalFileName;
            }
        }
        return null;
    }

    public List<LeocodeFile> createFilesFromInputParts(String inputType, List<InputPart> inputParts, String author, Example example) {
        List<LeocodeFile> files = new LinkedList<>();
        for (InputPart inputPart : inputParts) {
            try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String name = getMultipartFileName(header);
                byte[] bytes = inputStream.readAllBytes();
                LeocodeFile f = new LeocodeFile(name, author, LeocodeFileType.valueOf(inputType.toUpperCase()), bytes, example);

                if (f.fileType.equals(LeocodeFileType.PROJECT)) {
                    files.addAll(extractFilesFromZip(f));
                } else {
                    files.add(f);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    public List<LeocodeFile> extractFilesFromZip(LeocodeFile file) {
        List<LeocodeFile> files = new LinkedList<>();
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file.content))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String filename = zipEntry.toString().substring(zipEntry.toString().lastIndexOf("/") + 1);
                LeocodeFileType type = null;
                if (filename.contains(".java")) {
                    if (zipEntry.toString().contains("main")) {
                        type = LeocodeFileType.SOLUTION;
                    } else if (zipEntry.toString().contains("test")) {
                        type = LeocodeFileType.TEST;
                    }
                }
                if (filename.contains("pom.xml")) {
                    type = LeocodeFileType.POM;
                }
                if (type != null) {
                    files.add(new LeocodeFile(filename
                            , "unknown"
                            , type
                            , zis.readAllBytes()
                            , null));
                }
                zipEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public String zipLeocodeFiles(Long prefix, List<LeocodeFile> files){
        final String destination = zipLocation + "-" + prefix.toString() + ".zip";
        try (FileOutputStream fos = new FileOutputStream(destination);
                ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            files.forEach(file -> {
                try {
                    if (file.fileType.equals(LeocodeFileType.TEST)) {
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

    @Transactional
    public List<LeocodeFile> getFilesRequiredForTesting(Example example) {
        return find("select f from LeocodeFile f where example = ?1 and filetype not in ('INSTRUCTION', 'SOLUTION', 'CODE') ", example).list();
    }

    @Transactional
    public List<LeocodeFile> findByExample(Example example){
        return find("select f from LeocodeFile f where example = ?1", example).list();
    }
}
