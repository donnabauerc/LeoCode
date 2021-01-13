package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

import at.htl.entities.LeocodeStatus;

import java.time.LocalDateTime;

@Entity
public class Submission extends PanacheEntity {

    public String pathToZip;
    @Enumerated(value = EnumType.STRING) //private so the timestamp gets updated, whenever its set
    private LeocodeStatus status;
    public String author;
    public String result;
    @OneToOne
    public Example example;
    @Column(columnDefinition = "TIMESTAMP")
    public LocalDateTime lastTimeChanged;


    public Submission() {
        setStatus(LeocodeStatus.CREATED);
    }

    public Submission(String pathToZip, LeocodeStatus status, String author) {
        this.pathToZip = pathToZip;
        this.status = status;
        this.author = author;
    }

    public void setStatus(LeocodeStatus status) {
        this.status = status;
        this.lastTimeChanged = LocalDateTime.now();
    }

    public LeocodeStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", pathToZip='" + pathToZip + '\'' +
                ", status=" + status +
                ", author='" + author + '\'' +
                ", result='" + result + '\'' +
                ", example=" + example +
                ", lastTimeChanged=" + lastTimeChanged +
                '}';
    }
}
