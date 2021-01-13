package at.htl.entities;

import java.time.LocalDateTime;

public class Submission {

    public long id;
    public String pathToZip;
    private LeocodeStatus status;  //private so the timestamp gets updated, whenever its set
    public String author;
    public String result;
    public Example example;
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