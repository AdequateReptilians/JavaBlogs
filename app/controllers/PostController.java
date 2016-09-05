package controllers;

import models.entities.Post;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class PostController extends Controller {
    private final JPAApi jpaApi;
    private final FormFactory formFactory;


    @Inject
    public PostController(FormFactory formFactory, JPAApi jpaApi) {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional
    public Result create() {
        Form<Post> postForm = formFactory.form(Post.class).bindFromRequest();
        jpaApi.em().persist(postForm.get());
        return(redirect("/"));
    }
}
