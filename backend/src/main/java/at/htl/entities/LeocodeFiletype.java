package at.htl.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

public enum LeocodeFiletype {
    POM,
    INSTRUCTION,
    TEST,
    SOLUTION,
    CODE,
    JENKINSFILE
}
