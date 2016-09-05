package controllers;

import play.mvc.*;

import views.html.home.*; // Idea тупая, эта строчка нужна
import play.db.jpa.Transactional;
import play.db.jpa.JPAApi;
import play.data.FormFactory;
import javax.inject.Inject;
import models.entities.*;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private final JPAApi jpaApi;
    private final FormFactory formFactory;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    @Inject
    public HomeController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    @Transactional
    public Result index() {
        List<Post> posts = jpaApi.em()
                .createQuery("select p from Post p", Post.class)
                .getResultList();
        return ok(index.render(posts));
    }
}
