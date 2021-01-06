package at.htl.entities;

public class Submission {

    public Long id;
    public String pathToZip;
    public LeocodeStatus status;
    public String author;
    public String result;
    public Example example;

    public Submission() {
        this.status = LeocodeStatus.CREATED;
    }

    public Submission(String pathToZip, String author) {
        this();
        this.pathToZip = pathToZip;
        this.author = author;
    }

    public Submission(String pathToZip, LeocodeStatus status, String author) {
        this.pathToZip = pathToZip;
        this.status = status;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", pathToZip='" + pathToZip + '\'' +
                ", status=" + status +
                ", author='" + author + '\'' +
                '}';
    }
}
