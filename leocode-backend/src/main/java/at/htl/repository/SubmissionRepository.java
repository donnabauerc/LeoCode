package at.htl.repository;

import at.htl.dto.SubmissionDTO;
import at.htl.entity.Submission;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class SubmissionRepository implements PanacheRepository<Submission> {

    @Transactional
    public void update(Submission s){
        Panache.getEntityManager().merge(s);
    }

    @Transactional
    public List<Submission> getFinishedByUsername(String username) {
        return find("select s from Submission s where author = ?1 and result is not NULL", username).list();
    }

    public List<SubmissionDTO> createSubmissionDTOs(List<Submission> submissionList) {
        List<SubmissionDTO> submissionDTOs = new LinkedList<>();
        submissionList.forEach(submission -> {
            submissionDTOs.add(new SubmissionDTO(submission));
        });
        return submissionDTOs;
    }
}
