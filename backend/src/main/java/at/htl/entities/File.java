package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File extends PanacheEntity {

    private String name;
    @Enumerated(value = EnumType.STRING)
    private FileType type;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] file;

    @ManyToOne
    @JoinColumn(name = "example")
    private Example example;


    public File() {
    }

    public File(String name, FileType type, byte[] file) {
        this.name = name;
        this.type = type;
        this.file = file;
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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
