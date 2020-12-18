package at.htl.control;

import at.htl.entities.Submition;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class SubmitionDeserializer extends ObjectMapperDeserializer<Submition> {
    public SubmitionDeserializer() {
        super(Submition.class);
    }
}
