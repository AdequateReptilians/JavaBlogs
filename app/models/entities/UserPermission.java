package models.entities;

import be.objectify.deadbolt.java.models.Permission;

import javax.persistence.*;

/**
 * Initial version based on work by Steve Chaloner (steve@objectify.be) for
 * Deadbolt2
 */
@Entity
public class UserPermission implements Permission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;

	public String value;

	public String getValue() {
		return value;
	}
}
