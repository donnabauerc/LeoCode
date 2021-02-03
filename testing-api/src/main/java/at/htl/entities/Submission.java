package at.htl.entities;

import java.time.LocalDateTime;

public class Submission {
    public long id;
    public String pathToProject;
    public String author;
    public String result;
    private SubmissionStatus status; //private so the timestamp gets updated, whenever it is set => see setter
    public LocalDateTime lastTimeChanged;
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