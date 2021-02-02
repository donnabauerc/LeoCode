package at.htl.repository;

import at.htl.entity.Submission;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class SubmissionRepository implements PanacheRepository<Submission> {
    @Transactional
    public void update(Submission s){
        Panache.getEntityManager().merge(s);
    }
}
