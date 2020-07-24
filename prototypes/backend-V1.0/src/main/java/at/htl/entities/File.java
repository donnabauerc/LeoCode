package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "files")
public class File extends PanacheEntity {

    private String name;
    @Enumerated
    private FileType type;
    private java.io.File file;


    public File() {
    }

    public File(String name, FileType type, java.io.File file) {
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

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
