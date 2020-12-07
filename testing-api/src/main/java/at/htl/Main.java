package at.htl;

import org.jboss.logging.Logger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main( String[] args ) {
        //Todo: Will be triggered by Kafka Message
        log.info("Hello World!");
    }
}
