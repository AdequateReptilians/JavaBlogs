package models.entities;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import javax.persistence.*;
import java.util.*;

/**
 * Initial version based on work by Steve Chaloner (steve@objectify.be) for
 * Deadbolt2
 */
// TODO: Add beans setters and getters
@Entity
@Table(name = "users")
public class User implements Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;

	public String email;

	public String name;

	public String firstName;

	public String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    public Date lastLogin;

	public boolean active;

	public boolean emailValidated;

	@ManyToMany
	public List<SecurityRole> roles;

	@OneToMany(mappedBy= "user", cascade = CascadeType.ALL, orphanRemoval = true)
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

	public Set<String> getProviders() {
		final Set<String> providerKeys = new HashSet<String>(
				this.linkedAccounts.size());
		for (final models.entities.LinkedAccount acc : this.linkedAccounts) {
			providerKeys.add(acc.providerKey);
		}
		return providerKeys;
	}
}
