package at.htl.repositories;

import at.htl.entities.Submission;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class SubmissionRepository implements PanacheRepository<Submission> {

    @Inject
    Logger log;

    @Transactional
    public void update(Submission s) {
        Panache.getEntityManager().merge(s);
    }

    @Transactional
    public List<Submission> getFinished(String username) {
        return find("select s from Submission s where author = ?1 and result is not NULL", username).list();
    }
}
