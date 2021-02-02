package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "EXAMPLES")
public class Example extends PanacheEntity {
    public String name;
    public String description;
    public String author;
    @Enumerated(value = EnumType.STRING)
    public ExampleType type;

    public Example() {
    }

    public Example(String name, String description, String author, ExampleType type) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Example{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", type=" + type +
                '}';
    }
}
