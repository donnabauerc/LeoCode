package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class LeocodeFile extends PanacheEntity {
    public String name;
    public String author;

    @Enumerated(value = EnumType.STRING)
    public LeocodeFileType filetype;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    public byte[] content;

    @ManyToOne
    @JoinColumn(name = "example")
    public Example example;

    public LeocodeFile() {
    }

    public LeocodeFile(String name, String author, LeocodeFileType filetype, byte[] content, Example example) {
        this.name = name;
        this.author = author;
        this.filetype = filetype;
        this.content = content;
        this.example = example;
    }

    @Override
    public String toString() {
        return "LeocodeFile{" +
                "name='" + name + '\'' +
                ", filetype=" + filetype +
                ", id=" + id +
                '}';
    }
}
