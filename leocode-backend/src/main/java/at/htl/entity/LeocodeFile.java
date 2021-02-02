package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "LC_FILES")
public class LeocodeFile extends PanacheEntity {
    public String name;
    public String author;
    @Enumerated(value = EnumType.STRING)
    public LeocodeFileType fileType;
    @Lob
    public byte[] content;
    @ManyToOne
    @JoinColumn(name = "example")
    public Example example;

    public LeocodeFile() {
    }

    public LeocodeFile(String name, String author, LeocodeFileType fileType, byte[] content, Example example) {
        this.name = name;
        this.author = author;
        this.fileType = fileType;
        this.content = content;
        this.example = example;
    }

    @Override
    public String toString() {
        return "LeocodeFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", fileType=" + fileType +
                ", example=" + example.id +
                '}';
    }
}
