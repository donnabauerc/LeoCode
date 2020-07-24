package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "exercises")
public class Exercise extends PanacheEntity {

    //private User user;
    //private Example example;
    private String result;
    private Date date;

    public Exercise() {
    }

    public Exercise(String result, Date date) {
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

    @Override
    public String toString() {
        return "Exercise{" +
                "result='" + result + '\'' +
                ", date=" + date +
                '}';
    }
}
