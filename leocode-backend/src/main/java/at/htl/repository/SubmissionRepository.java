package at.htl.repository;

import at.htl.entity.Submission;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SubmissionRepository implements PanacheRepository<Submission> {
}
