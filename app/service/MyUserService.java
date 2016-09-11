package service;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import dao.SecurityRoleDao;
import dao.UserDao;
import models.entities.SecurityRole;
import models.entities.User;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Base64;
import java.util.Collections;
import java.util.List;


@Singleton
public class MyUserService extends AbstractUserService {
    private EntityManager entityManager;
	@Inject
	public MyUserService(final PlayAuthenticate auth) { super(auth); }

	private EntityManager getEntityManager(){
	    if(this.entityManager == null){
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
            this.entityManager = entityManagerFactory.createEntityManager();
        }
        return this.entityManager;
    }

	@Override
    @Transactional
	public Object save(final AuthUser authUser) {
        final UserDao userDao = new UserDao(getEntityManager());
		final boolean isLinked = userDao.existsByAuthUserIdentity(authUser);
		if (!isLinked) {
		    final SecurityRoleDao securityRoleDao = new SecurityRoleDao(getEntityManager());
            List<SecurityRole> roles = Collections.singletonList(securityRoleDao.findByRoleName(controllers.Application.USER_ROLE));
			return userDao.create(authUser, roles).id;
		} else {
			// we have this user already, so return null
			return null;
		}
	}

	@Override
	public Object getLocalIdentity(final AuthUserIdentity identity) {
        final UserDao userDao = new UserDao(getEntityManager());
		// For production: Caching might be a good idea here...
		// ...and dont forget to sync the cache when users get deactivated/deleted
		final User u = userDao.findByAuthUserIdentity(identity);
		if(u != null) {
			return u.id;
		} else {
			return null;
		}
	}

	@Override
	public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
	    UserDao userDao = new UserDao(getEntityManager());
		if (!oldUser.equals(newUser)) {
            userDao.merge(oldUser, newUser);
		}
		return oldUser;
	}

	@Override
    @Transactional
	public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        final UserDao userDao = new UserDao(getEntityManager());
        userDao.addLinkedAccount(oldUser, newUser);
		return newUser;
	}
	
	@Override
    @Transactional
	public AuthUser update(final AuthUser knownUser) {
        final UserDao userDao = new UserDao(getEntityManager());
		// User logged in again, bump last login date
        userDao.setLastLoginDate(knownUser);
		return knownUser;
	}

}
