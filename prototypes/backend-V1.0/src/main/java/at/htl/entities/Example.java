package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "examples")
public class Example extends PanacheEntity {

    private String name;
//    private File pom;
//    private File instruction;
//    private List<File> tests;
//    private List<File> solutions;


    public Example() {
    }

    public Example(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Example{" +
                "name='" + name + '\'' +
                '}';
    }
}
