package service;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import dao.UserDao;
import models.entities.User;
//import org.jetbrains.annotations.Nullable;
import play.db.jpa.JPAApi;
import play.mvc.Http.Session;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Service layer for User DB entity
 */
public class UserProvider {
    private final PlayAuthenticate auth;
    private EntityManager entityManager;

    @Inject
    public UserProvider(final PlayAuthenticate auth) {
        this.auth = auth;
    }

    private EntityManager getEntityManager(){
        if(this.entityManager == null){
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
            this.entityManager = entityManagerFactory.createEntityManager();
        }
        return this.entityManager;
    }

//    @Nullable
    public User getUser(Session session) {
        final AuthUser currentAuthUser = this.auth.getUser(session);
        final UserDao userDao = new UserDao(getEntityManager());
        return userDao.findByAuthUserIdentity(currentAuthUser);
    }
}
