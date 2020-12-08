package at.htl;

import at.htl.control.FileHandler;
import org.jboss.logging.Logger;

public class Main {

    public static String submitionId;

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main( String[] args ) {
        // Todo: Will be triggered by Kafka Message
        // Currently the id for the project which should be tested (e.g. Submition Id)
        // gets passed by the Build Arguments -> simply "8"
        submitionId = args[0];

        FileHandler.setup();
        FileHandler.unzipProject();
        FileHandler.createJavaProjectStructure();
        FileHandler.runTests();
    }
}
