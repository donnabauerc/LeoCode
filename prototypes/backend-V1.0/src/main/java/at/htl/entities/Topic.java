package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "topics")
public class Topic extends PanacheEntity {

    private String name;
    private String description;
    @Enumerated(value = EnumType.STRING)
    private Curriculum curriculum;
    private Date year;

    @Transient
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

    public Topic() {
        this.year = new Date();
    }

    public Topic(String name, String description, Curriculum curriculum) {
        this();
        this.name = name;
        this.description = description;
        this.curriculum = curriculum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return formatter.format(year);
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", curriculum=" + curriculum +
                ", year=" + formatter.format(year)  +
                '}';
    }
}
