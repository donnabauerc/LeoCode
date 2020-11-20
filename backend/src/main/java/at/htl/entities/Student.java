package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student extends PanacheEntityBase {
    @Id
    private String username;
    private String lastName;
    private String firstName;
    private String schoolClass;

    @OneToMany(mappedBy = "student")
    private final List<Exercise> exercises;

    public Student() {
        this.username = "NAN";
        this.exercises = new LinkedList<>();
    }

    public Student(String username, String lastName, String firstName, String schoolClass) {
        this();
        this.username = username;
        this.lastName = lastName;
        this.firstName = firstName;
        this.schoolClass = schoolClass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", schoolClass='" + schoolClass + '\'' +
                '}';
    }
}
