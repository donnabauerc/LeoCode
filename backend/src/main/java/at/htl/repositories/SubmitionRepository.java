package at.htl.repositories;

import at.htl.entities.Submition;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SubmitionRepository implements PanacheRepository<Submition> {
}
