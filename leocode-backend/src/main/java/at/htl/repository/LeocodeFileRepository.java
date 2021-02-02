package at.htl.repository;

import at.htl.entity.LeocodeFile;
import at.htl.entity.LeocodeFileType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class LeocodeFileRepository implements PanacheRepository<LeocodeFile> {
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

    @Transactional
    public List<LeocodeFile> persistFromInputParts(String inputType, List<InputPart> inputParts){
        List<LeocodeFile> files = new LinkedList<>();
        for (InputPart inputPart : inputParts) {
            try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String name = getMultipartFileName(header);
                byte[] bytes = inputStream.readAllBytes();

                LeocodeFile f = new LeocodeFile(name, "unknown", LeocodeFileType.valueOf(inputType.toUpperCase()), bytes, null);
                persist(f);
                files.add(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }
}
