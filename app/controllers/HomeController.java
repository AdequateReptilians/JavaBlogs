package controllers;

import models.entities.Person;
import play.mvc.*;

import views.html.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "defaultPersistenceUnit" );
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist( new Person("First person", 20) );
        entityManager.persist( new Person( "Second person", 12 ) );
        entityManager.getTransaction().commit();
        entityManager.close();
        return ok(index.render("xuz."));
    }
}
