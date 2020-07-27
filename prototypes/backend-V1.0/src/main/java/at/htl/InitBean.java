package at.htl;

import at.htl.entities.Curriculum;
import at.htl.entities.Student;
import at.htl.entities.Topic;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

@ApplicationScoped
public class InitBean {

    private static final Logger log = Logger.getLogger(InitBean.class.getSimpleName());

    public void init(@Observes StartupEvent e){
        insertStudents();
        insertTopics();
    }

    @Transactional
    public void insertStudents(){
        log.info("Inserting Students");
        new Student("it 160174", "Donnabauer", "Christian", "4AHITM").persist();
        new Student("it 160173", "Muratspahic", "Irfan", "4AHITM").persist();
        new Student("it 160172", "Schauflinger", "Hanna", "4AHITM").persist();
        new Student("it 160171", "Aigner", "Lara", "4AHITM").persist();
    }

    @Transactional
    public void insertTopics(){
        log.info("Inserting Topics");
        new Topic("Generics", "Learn the basics of Java Generics",  Curriculum.JAVA).persist();
        new Topic("Java FX", "Learn the basics of Java FX", Curriculum.JAVA).persist();
    }



}
