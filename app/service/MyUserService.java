package service;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import dao.GenericDao;
import dao.UserDao;
import models.entities.User;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class MyUserService extends AbstractUserService {
    @Inject
    public JPAApi jpaApi;

	@Inject
	public MyUserService(final PlayAuthenticate auth) { super(auth); }

	@Override
	public Object save(final AuthUser authUser) {
        UserDao userDao = new UserDao(jpaApi.em());
		final boolean isLinked = userDao.existsByAuthUserIdentity(authUser);
		if (!isLinked) {
			return User.create(authUser).id;
		} else {
			// we have this user already, so return null
			return null;
		}
	}

	@Override
	public Object getLocalIdentity(final AuthUserIdentity identity) {
		// For production: Caching might be a good idea here...
		// ...and dont forget to sync the cache when users get deactivated/deleted
		final User u = User.findByAuthUserIdentity(identity);
		if(u != null) {
			return u.id;
		} else {
			return null;
		}
	}

	@Override
	public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
	    UserDao userDao = new UserDao(jpaApi.em());
		if (!oldUser.equals(newUser)) {
            userDao.merge(oldUser, newUser);
		}
		return oldUser;
	}

	@Override
	public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
		User.addLinkedAccount(oldUser, newUser);
		return newUser;
	}
	
	@Override
	public AuthUser update(final AuthUser knownUser) {
		// User logged in again, bump last login date
		User.setLastLoginDate(knownUser);
		return knownUser;
	}

}
