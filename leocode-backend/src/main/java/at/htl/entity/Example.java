package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "LC_EXAMPLES")
public class Example extends PanacheEntity {
    public String name;
    public String description;
    public String author;
    @Enumerated(value = EnumType.STRING)
    public ExampleType type;
    @ElementCollection
    public List<String> whitelist;
    @ElementCollection
    public List<String> blacklist;

    public Example() {
        this.whitelist = new LinkedList<>();
        this.blacklist = new LinkedList<>();
    }

    public Example(String name, String description, String author, ExampleType type, List<String> whitelist, List<String> blacklist) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.type = type;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
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
