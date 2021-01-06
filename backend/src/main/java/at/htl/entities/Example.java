package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "examples")
public class Example extends PanacheEntity {

    public String name;
    public String description;
    public String author;

    @Enumerated(value = EnumType.STRING)
    public ExampleType type;

    public Example() {
    }

    public Example(String name, String description, ExampleType type) {
        this.name = name;
        this.description = description;
        this.type = type;
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
