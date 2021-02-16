package at.htl.dto;

import at.htl.entity.Submission;

import javax.ws.rs.PathParam;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class SubmissionDTO {
    public long id;
    public String status;
    public String result;
    public String lastTimeChanged;

    public SubmissionDTO(long id, String status, String result, String lastTimeChanged) {
        this.id = id;
        this.status = status;
        this.result = result;
        this.lastTimeChanged = lastTimeChanged;
    }

    public SubmissionDTO(Submission submission){
        this.id = submission.id;
        this.status = submission.getStatus().toString();
        this.result = submission.result;
        this.lastTimeChanged = submission.lastTimeChanged.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

}
