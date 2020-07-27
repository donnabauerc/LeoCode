package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "examples")
public class Example extends PanacheEntity {

    private String name;
    @OneToOne
    private File pom;
    @OneToOne
    private File instruction;
    @OneToMany(mappedBy = "example")
    private List<File> tests;
    @OneToMany(mappedBy = "example")
    private List<File> solutions;
    @OneToMany
    @JoinColumn(name = "fk_example")
    private List<Topic> topics;

    public Example() {
        this.tests = new LinkedList<>();
        this.solutions = new LinkedList<>();
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

    public File getPom() {
        return pom;
    }

    public void setPom(File pom) {
        this.pom = pom;
    }

    public void addTest(File file){
        this.tests.add(file);
    }

    public void addSolution(File file){
        this.solutions.add(file);
    }

    public List<File> getTests() {
        return tests;
    }

    public List<File> getSolutions() {
        return solutions;
    }

    public File getInstruction() {
        return instruction;
    }

    public void setInstruction(File instruction) {
        this.instruction = instruction;
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
