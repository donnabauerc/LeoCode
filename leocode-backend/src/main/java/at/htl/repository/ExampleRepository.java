package at.htl.repository;

import at.htl.entity.Example;
import at.htl.entity.ExampleType;
import at.htl.entity.LeocodeFile;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ExampleRepository implements PanacheRepository<Example> {

    @Inject
    Logger log;

    @Inject
    LeocodeFileRepository leocodeFileRepository;

    @Inject
    LeocodeKeywordRepository leocodeKeywordRepository;

    @Transactional
    public Example createExampleFromMultipart(MultipartFormDataInput input) {
        Map<String, List<InputPart>> inputForm = input.getFormDataMap();
        Example example = new Example();
        List<LeocodeFile> files = new LinkedList<>();

        inputForm.forEach((inputType, inputParts) -> {
            try {
                switch (inputType) {
                    case "exampleName":
                        example.name = inputParts.get(0).getBodyAsString();
                        break;
                    case "description":
                        example.description = inputParts.get(0).getBodyAsString();
                        break;
                    case "username":
                        example.author = inputParts.get(0).getBodyAsString();
                        break;
                    case "exampleType":
                        example.type = ExampleType.valueOf(inputParts.get(0).getBodyAsString().toUpperCase());
                        break;
                    case "blacklist":
                        example.blacklist = leocodeKeywordRepository.filterStringInput(inputParts);
                        break;
                    case "whitelist":
                        example.whitelist = leocodeKeywordRepository.filterStringInput(inputParts);
                        break;
                    default: //files
                        files.addAll(leocodeFileRepository.createFilesFromInputParts(inputType, inputParts, "unknown", example));
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (!example.isValid()) {
            return null;
        }
        if (files.size() < 4) {
            return null;
        }

        //because the order of the inputParts is unknown => username could be null when creating a file
        files.forEach(f -> {
            f.author = example.author;
            f.example = example;
            leocodeFileRepository.persist(f);
        });

        example.persist();
        return example;
    }
}
