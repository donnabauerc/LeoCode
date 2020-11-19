package at.htl.entities;

import java.util.List;

public class ExampleResponse {
    public Example example;
    public List<File> files;

    public ExampleResponse(Example example, List<File> files) {
        this.example = example;
        this.files = files;
    }
}
