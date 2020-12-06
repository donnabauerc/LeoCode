package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "examples")
public class Example extends PanacheEntity {
    public String name;
    public String description;

    public Example() {
    }

    public Example(String name, String description) {
        this.name = name;
        this.description = description;
    }

    //    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "fk_example")
//    private List<Topic> topics;


    @Override
    public String toString() {
        return "Example{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
