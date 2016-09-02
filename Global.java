import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPA;

/**
 * The starting point of the service.
 */
public class Global extends GlobalSettings {

    public void onStart(Application app) {

//        JPA.withTransaction(new F.Callback0() {
//            @Override
//            public void invoke() throws Throwable {
//                play.Logger.debug("First JPA call");
//            }
//        });


    }

    public void onStop(Application app) {
    }

}