package controllers;

import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.mvc.Controller;

import javax.inject.Inject;

public class PostController extends Controller {
    private final JPAApi jpaApi;
    private final FormFactory formFactory;


    @Inject
    public PostController(FormFactory formFactory, JPAApi jpaApi) {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }
}
