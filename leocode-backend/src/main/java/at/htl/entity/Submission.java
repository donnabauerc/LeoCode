package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LC_SUBMISSIONS")
public class Submission extends PanacheEntity {
    public String pathToProject;
    public String author;
    public String result;
    @Enumerated(value = EnumType.STRING)
    private SubmissionStatus status; //private so the timestamp gets updated, whenever it is set => see setter
    @Column(columnDefinition = "TIMESTAMP")
    public LocalDateTime lastTimeChanged;
    @ManyToOne
    @JoinColumn(name = "example")
    public Example example;

    public Submission() {
        setStatus(SubmissionStatus.CREATED);
    }

    public Submission(String pathToProject, String author, Example example) {
        this();
        this.pathToProject = pathToProject;
        this.author = author;
        this.example = example;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
        this.lastTimeChanged = LocalDateTime.now();
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", pathToProject='" + pathToProject + '\'' +
                ", author='" + author + '\'' +
                ", result='" + result + '\'' +
                ", status=" + status +
                ", lastTimeChanged=" + lastTimeChanged +
                ", example=" + example.id +
                '}';
    }
}
