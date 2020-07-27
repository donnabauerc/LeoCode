package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File extends PanacheEntity {

    private String name;
    @Enumerated(value = EnumType.STRING)
    private FileType type;
    private java.io.File file;

    @ManyToOne
    @JoinColumn(name = "example", nullable = false)
    private Example example;


    public File() {
    }

    public File(String name, FileType type, java.io.File file, Example example) {
        this.name = name;
        this.type = type;
        this.file = file;
        this.example = example;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public Example getExample() {
        return example;
    }

    public void setExample(Example example) {
        this.example = example;
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
