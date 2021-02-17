package at.htl.dto;

import at.htl.entity.Example;
import at.htl.entity.LeocodeFile;

import javax.persistence.ElementCollection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExampleDTO {
    public long id;
    public String name;
    public String author;
    public String description;
    public String type;

    public Set<String> whitelist;
    public Set<String> blacklist;
    public List<LeocodeFileDTO> files;

    public ExampleDTO(long id, String name, String author, String description, String type, LinkedList<LeocodeFileDTO> files, Set<String> whitelist, Set<String> blacklist) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.type = type;
        this.files = files;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public ExampleDTO(Example example, List<LeocodeFile> files){
        this.id = example.id;
        this.name = example.name;
        this.author = example.author;
        this.description = example.description;
        this.type = example.type.toString();
        this.whitelist = example.whitelist;
        this.blacklist = example.blacklist;

        this.files = new LinkedList<>();
        files.forEach(file -> {
            this.files.add(new LeocodeFileDTO(file));
        });
    }
}
