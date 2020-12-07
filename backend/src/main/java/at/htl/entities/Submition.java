package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Submition extends PanacheEntity {
    public String pathToZip;
    @Enumerated(value = EnumType.STRING)
    public LeocodeStatus status;
    public String author;

    public Submition() {
        this.status = LeocodeStatus.CREATED;
    }

    public Submition(String pathToZip, LeocodeStatus status, String author) {
        this.pathToZip = pathToZip;
        this.status = status;
        this.author = author;
    }
}