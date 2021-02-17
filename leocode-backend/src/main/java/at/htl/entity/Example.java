package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "LC_EXAMPLES")
public class Example extends PanacheEntity {
    public String name;
    public String description;
    public String author;
    @Enumerated(value = EnumType.STRING)
    public ExampleType type;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "LC_EXAMPLE_WHITELIST")
    public Set<String> whitelist;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "LC_EXAMPLE_BLACKLIST")
    public Set<String> blacklist;

    public Example() {
        this.whitelist = new HashSet<>();
        this.blacklist = new HashSet<>();
    }

    public Example(String name, String description, String author, ExampleType type, Set<String> whitelist, Set<String> blacklist) {
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
                ", whitelist=" + whitelist +
                ", blacklist=" + blacklist +
                '}';
    }
}
