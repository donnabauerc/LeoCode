package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exercises")
public class Exercise extends PanacheEntity {

    @ManyToOne
    private User user;
    @ManyToOne
    private Example example;
    private String result;
    @Temporal(TemporalType.DATE)
    private Date date;

    public Exercise() {
    }

    public Exercise(User user, Example example, String result, Date date) {
        this.user = user;
        this.example = example;
        this.result = result;
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Example getExample() {
        return example;
    }

    public void setExample(Example example) {
        this.example = example;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "user=" + user +
                ", example=" + example +
                ", result='" + result + '\'' +
                ", date=" + date +
                '}';
    }
}
