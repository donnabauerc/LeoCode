package at.htl.kafka;

import at.htl.entity.Submission;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class SubmissionDeserializer extends ObjectMapperDeserializer<Submission> {
    public SubmissionDeserializer() {
        super(Submission.class);
    }
}
