package at.htl.repositories;

import at.htl.entities.Submition;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class SubmitionRepository implements PanacheRepository<Submition> {

    @Inject
    Logger log;

    @Transactional
    public void update(Submition s) {
        Panache.getEntityManager().merge(s);
    }
}
