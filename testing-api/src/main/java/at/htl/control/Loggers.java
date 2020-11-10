package at.htl.control;

import org.jboss.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class Loggers {

    @Produces
    Logger getLogger(InjectionPoint injectionPoint) {
        return Logger
                .getLogger(injectionPoint
                        .getMember()
                        .getDeclaringClass()
                        .getSimpleName()
                );
    }
}
