package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "topics")
public class Topic extends PanacheEntity {

    private String name;
    private String description;
    private Date year;

    @Transient
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

    public Topic() {
        this.year = new Date();
    }

    public Topic(String name, String description) {
        this();
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", year=" + formatter.format(year) +
                '}';
    }
}
