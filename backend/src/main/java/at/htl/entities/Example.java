package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "examples")
public class Example extends PanacheEntity {

    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_example")
    private List<Topic> topics;

    public Example() {
        this.topics = new LinkedList<>();
    }

    public Example(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }

    @Override
    public String toString() {
        return "Example{" +
                "name='" + name + '\'' +
                '}';
    }
}
