package models.entities;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.*;
import models.*;
import models.LinkedAccount;
import models.TokenAction.Type;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * Initial version based on work by Steve Chaloner (steve@objectify.be) for
 * Deadbolt2
 */
@Entity
@Table(name = "users")
public class User implements Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;

	@Column
	@org.hibernate.annotations.Type(type = "text")
	public String email;

	@Column
	@org.hibernate.annotations.Type(type = "text")
	public String name;

	@Column
	@org.hibernate.annotations.Type(type = "text")
	public String firstName;

    @Column
    @org.hibernate.annotations.Type(type = "text")
	public String lastName;

	//@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    @org.hibernate.annotations.Type(type="timestamp")
    public Date lastLogin;

    @Column
    @org.hibernate.annotations.Type(type = "boolean")
	public boolean active;

    @Column
    @org.hibernate.annotations.Type(type = "boolean")
	public boolean emailValidated;


	@ManyToMany
	public List<SecurityRole> roles;

	@OneToMany(cascade = CascadeType.ALL)
	public List<models.entities.LinkedAccount> linkedAccounts;

	@ManyToMany
	public List<UserPermission> permissions;

	@Override
	public String getIdentifier()
	{
		return Long.toString(this.id);
	}

	@Override
	public List<? extends Role> getRoles() {
		return this.roles;
	}

	@Override
	public List<? extends Permission> getPermissions() {
		return this.permissions;
	}

	public static boolean existsByAuthUserIdentity(
			final AuthUserIdentity identity) {
		final Criteria exp;
		if (identity instanceof UsernamePasswordAuthUser) {
			exp = getUsernamePasswordAuthUserFind((UsernamePasswordAuthUser) identity);
		} else {
			exp = getAuthUserFind(identity);
		}
		return exp.list().size() > 0;
	}

	private static Criteria getAuthUserFind(
			final AuthUserIdentity identity) {
        //String sql_query = String.format("select * from users join linked_accounts where active = true and linked_accounts.provider_key = %s and linked_accounts.provider_user_id", identity.getProvider(), identity.getId());

        Session session = (Session)JPA.em().getCriteriaBuilder();
        return session.createCriteria(User.class)
                .add(Restrictions.eq("active", true))
                .createAlias("users.linked_account", "la")
                .add(Restrictions.eq("la.provider_key", identity.getProvider()))
                .add(Restrictions.eq("la.provider_user_id", identity.getId()));
	}

	public static User findByAuthUserIdentity(final AuthUserIdentity identity) {
		if (identity == null) {
			return null;
		}
		if (identity instanceof UsernamePasswordAuthUser) {
			return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
		} else {
			return (User)getAuthUserFind(identity).list().get(0);
		}
	}

	public static User findByUsernamePasswordIdentity(
			final UsernamePasswordAuthUser identity) {
		return (User)getUsernamePasswordAuthUserFind(identity).setMaxResults(1).list().get(0);
	}

	private static Criteria getUsernamePasswordAuthUserFind(
			final UsernamePasswordAuthUser identity) {

		return getEmailUserFind(identity.getEmail()).add(Restrictions.eq("la.provider_key", identity.getProvider()));
	}

	public void merge(final User otherUser) {
		for (final models.entities.LinkedAccount acc : otherUser.linkedAccounts) {
			this.linkedAccounts.add(models.LinkedAccount.create(acc));
		}
		// do all other merging stuff here - like resources, etc.

		// deactivate the merged user that got added to this one
		otherUser.active = false;
		Arrays.asList(new User[] { otherUser, this }).forEach(u -> u.save());
	}

	public static User create(final AuthUser authUser) {
		final User user = new User();
		user.roles = Collections.singletonList(SecurityRole
				.findByRoleName(controllers.Application.USER_ROLE));
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

		user.save();
		// Ebean.saveManyToManyAssociations(user, "roles");
		// Ebean.saveManyToManyAssociations(user, "permissions");
		return user;
	}

	public static void merge(final AuthUser oldUser, final AuthUser newUser) {
		User.findByAuthUserIdentity(oldUser).merge(
				User.findByAuthUserIdentity(newUser));
	}

	public Set<String> getProviders() {
		final Set<String> providerKeys = new HashSet<String>(
				this.linkedAccounts.size());
		for (final models.LinkedAccount acc : this.linkedAccounts) {
			providerKeys.add(acc.providerKey);
		}
		return providerKeys;
	}

	public static void addLinkedAccount(final AuthUser oldUser,
			final AuthUser newUser) {
		final User u = User.findByAuthUserIdentity(oldUser);
		u.linkedAccounts.add(models.LinkedAccount.create(newUser));
		u.save();
	}

	public static void setLastLoginDate(final AuthUser knownUser) {
		final User u = User.findByAuthUserIdentity(knownUser);
		u.lastLogin = new Date();
		u.save();
	}

	public static User findByEmail(final String email) {
	    return (User)getEmailUserFind(email).list().get(0);
	}

	private static Criteria getEmailUserFind(final String email) {
        Session session = (Session)JPA.em().getCriteriaBuilder();
		return session.createCriteria(User.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("email", email));
	}

	public models.LinkedAccount getAccountByProvider(final String providerKey) {
		return models.LinkedAccount.findByProviderKey(this, providerKey);
	}

	public static void verify(final User unverified) {
		// You might want to wrap this into a transaction
		unverified.emailValidated = true;
		unverified.save();
		TokenAction.deleteByUser(unverified, Type.EMAIL_VERIFICATION);
	}

	public void changePassword(final UsernamePasswordAuthUser authUser,
			final boolean create) {
		models.LinkedAccount a = this.getAccountByProvider(authUser.getProvider());
		if (a == null) {
			if (create) {
				a = LinkedAccount.create(authUser);
				a.user = this;
			} else {
				throw new RuntimeException(
						"Account not enabled for password usage");
			}
		}
		a.providerUserId = authUser.getHashedPassword();
		a.save();
	}

	public void resetPassword(final UsernamePasswordAuthUser authUser,
			final boolean create) {
		// You might want to wrap this into a transaction
		this.changePassword(authUser, create);
		TokenAction.deleteByUser(this, Type.PASSWORD_RESET);
	}
}
