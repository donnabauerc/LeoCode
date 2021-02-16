package at.htl.dto;

import at.htl.entity.Example;
import at.htl.entity.LeocodeFile;

import java.util.LinkedList;
import java.util.List;

public class ExampleDTO {
    public long id;
    public String name;
    public String author;
    public String description;
    public String type;

    public LinkedList<LeocodeFileDTO> files;

    public ExampleDTO(long id, String name, String author, String description, String type, LinkedList<LeocodeFileDTO> files) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.type = type;
        this.files = files;
    }

    public ExampleDTO(Example example, List<LeocodeFile> files){
        this.id = example.id;
        this.name = example.name;
        this.author = example.author;
        this.description = example.description;
        this.type = example.type.toString();

        this.files = new LinkedList<>();
        files.forEach(file -> {
            this.files.add(new LeocodeFileDTO(file));
        });
    }
}
