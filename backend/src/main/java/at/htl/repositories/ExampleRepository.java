package at.htl.repositories;

import at.htl.entities.Example;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ExampleRepository implements PanacheRepository<Example> {

    @Inject
    LeocodeFileRepository fileRepository;
    @Inject
    Logger log;

    @Transactional
    public Example createExampleFromMultipartFiles(MultipartFormDataInput input) {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        Example example = new Example(); //todo: add author to example
        String username;

        try {
            username = uploadForm.get("username").get(0).getBodyAsString();
            uploadForm.forEach((inputType, inputParts) -> {
                fileRepository.persistFilesFromMultipart(inputType, username, inputParts, example);
            });
            persist(example);
            log.info("Created Example: " + example.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return example;
    }
}
