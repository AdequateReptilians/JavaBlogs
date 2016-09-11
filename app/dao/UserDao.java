package dao;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.*;
import models.entities.LinkedAccount;
import models.entities.SecurityRole;
import models.entities.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.*;

/**
 * Created by mik on 10.09.2016.
 */
public class UserDao extends GenericDaoImpl<User, Serializable> {

    public UserDao(EntityManager em){
        super(em, User.class);
    }

    public boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
        final Criteria exp;
        if (identity instanceof UsernamePasswordAuthUser) {
            exp = getUsernamePasswordAuthUserFind((UsernamePasswordAuthUser) identity);
        } else {
            exp = getAuthUserFind(identity);
        }
        return exp.list().size() > 0;
    }

    private Criteria getAuthUserFind(final AuthUserIdentity identity) {
        return getSession().createCriteria(User.class)
                .add(Restrictions.eq("active", true))
                .createAlias("users.linked_account", "la")
                .add(Restrictions.eq("la.provider_key", identity.getProvider()))
                .add(Restrictions.eq("la.provider_user_id", identity.getId()));
    }

    public User findByAuthUserIdentity(final AuthUserIdentity identity) {
        if (identity == null) {
            return null;
        }
        if (identity instanceof UsernamePasswordAuthUser) {
            return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
        } else {
            return (User)getAuthUserFind(identity).list().get(0);
        }
    }

    public User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity) {
        return (User)getUsernamePasswordAuthUserFind(identity).setMaxResults(1).list().get(0);
    }

    private Criteria getUsernamePasswordAuthUserFind(final UsernamePasswordAuthUser identity) {
        return getEmailUserFind(identity.getEmail()).add(Restrictions.eq("la.provider_key", identity.getProvider()));
    }

    public User create(final AuthUser authUser, List<SecurityRole> roles) {
        final User user = new User();
        user.roles = roles;
        // user.permissions = new ArrayList<UserPermission>();
        // user.permissions.add(UserPermission.findByValue("printers.edit"));
        user.active = true;
        user.lastLogin = new Date();
        user.linkedAccounts = Collections.singletonList(models.entities.LinkedAccount.create(authUser));

        if (authUser instanceof EmailIdentity) {
            final EmailIdentity identity = (EmailIdentity) authUser;
            // Remember, even when getting them from FB & Co., emails should be
            // verified within the application as a security breach there might
            // break your security as well!
            user.email = identity.getEmail();
            user.emailValidated = false;
        }

        if (authUser instanceof NameIdentity) {
            final NameIdentity identity = (NameIdentity) authUser;
            final String name = identity.getName();
            if (name != null) {
                user.name = name;
            }
        }

        if (authUser instanceof FirstLastNameIdentity) {
            final FirstLastNameIdentity identity = (FirstLastNameIdentity) authUser;
            final String firstName = identity.getFirstName();
            final String lastName = identity.getLastName();
            if (firstName != null) {
                user.firstName = firstName;
            }
            if (lastName != null) {
                user.lastName = lastName;
            }
        }
        getSession().save(user);
        // Ebean.saveManyToManyAssociations(user, "roles");
        // Ebean.saveManyToManyAssociations(user, "permissions");
        return user;
    }

    public void merge(final AuthUser oldUser, final AuthUser newUser) {
        User otherUser = findByAuthUserIdentity(newUser);
        User toMerge = findByAuthUserIdentity(oldUser);
        for (final models.entities.LinkedAccount acc : otherUser.linkedAccounts) {
            toMerge.linkedAccounts.add(models.entities.LinkedAccount.create(acc));
        }
        // do all other merging stuff here - like resources, etc.

        // deactivate the merged user that got added to this one
        otherUser.active = false;
        Session session = (Session)JPA.em().getCriteriaBuilder();
        Arrays.asList(new User[] { otherUser, toMerge }).forEach(u -> session.save(u));
    }

    public void addLinkedAccount(final AuthUser oldUser, final AuthUser newUser) {
        final User u = findByAuthUserIdentity(oldUser);
        u.linkedAccounts.add(models.entities.LinkedAccount.create(newUser));
        getSession().save(u);
    }

    public void setLastLoginDate(final AuthUser knownUser) {
        final User u = findByAuthUserIdentity(knownUser);
        u.lastLogin = new Date();
        getSession().save(u);
    }

    public User findByEmail(final String email) {
        return (User)getEmailUserFind(email).list().get(0);
    }

    private Criteria getEmailUserFind(final String email) {
        Session session = (Session)JPA.em().getCriteriaBuilder();
        return session.createCriteria(User.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("email", email));
    }

    public void verify(final User unverified) {
        // You might want to wrap this into a transaction
        unverified.emailValidated = true;
        getSession().save(unverified);
    }

    public void changePassword(final UsernamePasswordAuthUser authUser, final boolean create, LinkedAccount linkedAccount) {
        User user = findByAuthUserIdentity(authUser);
        if (linkedAccount == null) {
            if (create) {
                linkedAccount = LinkedAccount.create(authUser);
                linkedAccount.user = user;
            } else {
                throw new RuntimeException(
                        "Account not enabled for password usage");
            }
        }
        linkedAccount.providerUserId = authUser.getHashedPassword();
        getSession().save(linkedAccount);
    }

    public void resetPassword(final User user, final UsernamePasswordAuthUser authUser, final boolean create, LinkedAccount linkedAccount) {
        // You might want to wrap this into a transaction
        changePassword(authUser, create, linkedAccount);
    }
}
