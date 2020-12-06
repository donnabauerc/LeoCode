package at.htl.repositories;

import at.htl.entities.Example;
import at.htl.entities.LeocodeFile;
import at.htl.entities.LeocodeFileType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class LeocodeFileRepository implements PanacheRepository<LeocodeFile> {

    @Inject
    Logger log;

    @Transactional
    public List<LeocodeFile> persistFilesFromMultipart(String fileType, String username, List<InputPart> inputParts, Example example) {
        List<LeocodeFile> files = new LinkedList<>();//TODO: set username before returning list
        try {
            switch (fileType) {
                case "exampleName":
                    example.name = inputParts.get(0).getBodyAsString();
                    break;
                case "description":
                    example.description = inputParts.get(0).getBodyAsString();
                    break;
                case "username":
                    break;
                default: //files
                    for (InputPart inputPart : inputParts) {
                        try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {
                            MultivaluedMap<String, String> header = inputPart.getHeaders();
                            String name = getMultipartFileName(header);
                            byte[] bytes = inputStream.readAllBytes();

                            LeocodeFile f = new LeocodeFile(name, username, LeocodeFileType.valueOf(fileType.toUpperCase()), bytes, example);
                            persist(f);
                            files.add(f);
                            log.info("Uploaded " + f.toString() + " to Database");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            files.forEach(leocodeFile -> {
                leocodeFile.author = username;
            });
            return files;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMultipartFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");

                return finalFileName;
            }
        }
        //ToDO: throw Name Exception
        return "unknown";
    }

    @Transactional
    public List<LeocodeFile> getFilesRequiredForTesting(Example example) {
        return find("select f from LeocodeFile f where example = ?1 and filetype not in ('INSTRUCTION', 'SOLUTION', 'CODE') ", example).list();
    }
}
