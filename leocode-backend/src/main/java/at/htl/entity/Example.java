package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "LC_EXAMPLES")
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

    @JsonbTransient
    public boolean isValid(){
        if (this.name == null) {
            return false;
        }
        if (this.description == null) {
            return false;
        }
        if (this.author == null) {
            return false;
        }
        if (this.type == null) {
            return false;
        }
        return true;
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
